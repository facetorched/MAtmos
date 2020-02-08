package eu.ha3.matmos.game.data.abstractions.scanner;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import eu.ha3.matmos.engine.core.interfaces.Data;
import eu.ha3.matmos.game.data.abstractions.module.BlockCountModule;
import eu.ha3.matmos.game.data.abstractions.module.ExternalStringCountModule;
import eu.ha3.matmos.game.data.abstractions.module.PassOnceModule;
import eu.ha3.matmos.game.data.abstractions.module.ThousandStringCountModule;
import eu.ha3.matmos.game.data.abstractions.module.VirtualModuleProcessor;
import eu.ha3.matmos.game.system.MAtmosUtility;
import eu.ha3.matmos.log.MAtLog;

/*
--filenotes-placeholder
*/

public class ScannerModule implements PassOnceModule, ScanOperations, Progress
{
	public static final String THOUSAND_SUFFIX = "_p1k";
	
	private static final int WORLD_LOADING_DURATION = 100;
	
	private final String passOnceName;
	private final boolean requireThousand;
	private final int movement;
	private final int pulse;
	
	private final int xS;
	private final int yS;
	private final int zS;
	private final int blocksPerCall;
	
	private final BlockCountModule base;
	private final VirtualModuleProcessor thousand;
	
	private final Set<String> subModules = new HashSet<String>();
	
	//
	
	private int ticksSinceBoot;
	private boolean firstScan;
	private boolean workInProgress;
	
	private int dimension = Integer.MIN_VALUE;
	private int xx = Integer.MIN_VALUE;
	private int yy = Integer.MIN_VALUE;
	private int zz = Integer.MIN_VALUE;
	
	//
	
	private final Scan scanner;
	
	/**
	 * Movement: Requires the player to move to another block to trigger a new
	 * scan. If movement is zero, no scan until the player moves. If movement is
	 * negative, always scan even if the player hasn't moved.
	 * 
	 * @param data
	 * @param passOnceName
	 * @param baseName
	 * @param requireThousand
	 * @param movement
	 * @param pulse
	 * @param xS
	 * @param yS
	 * @param zS
	 * @param blocksPerCall
	 */
	public ScannerModule(
		Class scannerClass, Data data, String passOnceName, String baseName, boolean requireThousand, int movement, int pulse, int xS,
		int yS, int zS, int blocksPerCall)
	{
		this.passOnceName = passOnceName;
		this.requireThousand = requireThousand;
		this.movement = movement;
		this.pulse = pulse;
		
		this.xS = xS;
		this.yS = yS;
		this.zS = zS;
		this.blocksPerCall = blocksPerCall;
		
		//
		
		
		if (requireThousand)
		{
			String thousandName = baseName + THOUSAND_SUFFIX;
			this.thousand = new VirtualModuleProcessor(data, thousandName, true);
			this.subModules.add(thousandName);
			data.getSheet(thousandName).setDefaultValue("0");
		}
		else
		{
			this.thousand = null;
		}
		
		this.base = new BlockCountModule(data, baseName, true, thousand);
		this.subModules.add(baseName);
		data.getSheet(baseName).setDefaultValue("0");
		
		// 
		
		Scan theScanner = null;
		
		try
		{
			theScanner = (Scan)scannerClass.newInstance();
		} catch (InstantiationException e)
		{
			e.printStackTrace();
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		this.scanner = theScanner;
		
		this.scanner.setPipeline(this);
		
		this.ticksSinceBoot = 0;
		this.firstScan = true;
	}
	
	@Override
	public String getModuleName()
	{
		return this.passOnceName;
	}
	
	@Override
	public Set<String> getSubModules()
	{
		return this.subModules;
	}
	
	@Override
	public void process()
	{
		Minecraft.getMinecraft().mcProfiler.startSection("scanner_module_process");
		if (tryToReboot())
		{
			MAtLog.info("Detected large movement or teleportation. Rebooted module " + getModuleName());
			return;
		}
		
		if (this.ticksSinceBoot < WORLD_LOADING_DURATION)
		{
			this.ticksSinceBoot = this.ticksSinceBoot + 1;
			return;
		}
		
		tryToBoot();
		
		if (this.workInProgress)
		{
			this.scanner.routine();
		}
		this.ticksSinceBoot = this.ticksSinceBoot + 1;
		
		Minecraft.getMinecraft().mcProfiler.endSection();
	}
	
	private boolean tryToReboot()
	{
		int x = MAtmosUtility.getPlayerX();
		int y = MAtmosUtility.clampToBounds(MAtmosUtility.getPlayerY());
		int z = MAtmosUtility.getPlayerZ();
		
		if (Minecraft.getMinecraft().thePlayer.dimension != this.dimension)
		{
			reboot();
			return true;
		}
		
		int max = Math.max(Math.abs(this.xx - x), Math.abs(this.yy - y));
		max = Math.max(max, Math.abs(this.zz - z));
		
		if (max > 128)
		{
			reboot();
			return true;
		}
		
		return false;
	}
	
	private void reboot()
	{
		this.scanner.stopScan();
		this.workInProgress = false;
		
		this.ticksSinceBoot = 0;
		this.firstScan = true;
		
		this.dimension = Minecraft.getMinecraft().thePlayer.dimension;
		this.xx = MAtmosUtility.getPlayerX();
		this.yy = MAtmosUtility.clampToBounds(MAtmosUtility.getPlayerY());
		this.zz = MAtmosUtility.getPlayerZ();
	}
	
	private void tryToBoot()
	{
		if (this.workInProgress)
			return;
		
		if (this.ticksSinceBoot % this.pulse == 0)
		{
			boolean go = false;
			
			if (this.firstScan)
			{
				this.firstScan = false;
				
				go = true;
			}
			else if (this.movement >= 0)
			{
				int x = MAtmosUtility.getPlayerX();
				int y = MAtmosUtility.clampToBounds(MAtmosUtility.getPlayerY());
				int z = MAtmosUtility.getPlayerZ();
				
				int max = Math.max(Math.abs(this.xx - x), Math.abs(this.yy - y));
				max = Math.max(max, Math.abs(this.zz - z));
				
				go = max > this.movement;
			}
			else
			{
				go = true;
			}
			
			if (go)
			{
				this.workInProgress = true;
				
				this.xx = MAtmosUtility.getPlayerX();
				this.yy = MAtmosUtility.clampToBounds(MAtmosUtility.getPlayerY());
				this.zz = MAtmosUtility.getPlayerZ();
				
				this.scanner.startScan(this.xx, this.yy, this.zz, this.xS, this.yS, this.zS, this.blocksPerCall);
			}
		}
	}
	
	@Override
	public void input(int x, int y, int z)
	{
		Block block = MAtmosUtility.getBlockAt(x, y, z);
		int meta = MAtmosUtility.getMetaAt(x, y, z, -1);
		base.increment(block, meta);
	}
	
	@Override
	public void begin()
	{
	}
	
	@Override
	public void finish()
	{
		base.apply();	
		
		this.workInProgress = false;
	}
	
	@Override
	public int getProgress_Current()
	{
		return this.scanner.getProgress_Current();
	}
	
	@Override
	public int getProgress_Total()
	{
		return this.scanner.getProgress_Total();
	}
}
