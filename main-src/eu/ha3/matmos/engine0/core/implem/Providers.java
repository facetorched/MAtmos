package eu.ha3.matmos.engine0.core.implem;

import eu.ha3.matmos.engine0.core.implem.abstractions.ProviderCollection;
import eu.ha3.matmos.engine0.core.interfaces.Provider;
import eu.ha3.matmos.engine0.core.interfaces.ReferenceTime;
import eu.ha3.matmos.engine0.core.interfaces.SheetCommander;
import eu.ha3.matmos.engine0.core.interfaces.SoundRelay;

/*
--filenotes-placeholder
*/

public class Providers implements ProviderCollection
{
	private final ReferenceTime time;
	private final SoundRelay soundRelay;
	private final SheetCommander commander;
	private final Provider<Condition> conditionProvider;
	private final Provider<Junction> junctionProvider;
	private final Provider<Machine> machineProvider;
	private final Provider<Event> eventProvider;
	private final Provider<Dynamic> dynamicProvider;
	
	public Providers(
		ReferenceTime time, SoundRelay soundRelay, SheetCommander commander, Provider<Condition> conditionProvider,
		Provider<Junction> junctionProvider, Provider<Machine> machineProvider, Provider<Event> eventProvider,
		Provider<Dynamic> dynamicProvider)
	{
		this.time = time;
		this.soundRelay = soundRelay;
		this.commander = commander;
		
		this.conditionProvider = conditionProvider;
		this.junctionProvider = junctionProvider;
		this.machineProvider = machineProvider;
		this.eventProvider = eventProvider;
		this.dynamicProvider = dynamicProvider;
	}
	
	@Override
	public ReferenceTime getReferenceTime()
	{
		return this.time;
	}
	
	@Override
	public SoundRelay getSoundRelay()
	{
		return this.soundRelay;
	}
	
	@Override
	public SheetCommander getSheetCommander()
	{
		return this.commander;
	}
	
	@Override
	public Provider<? extends Condition> getCondition()
	{
		return this.conditionProvider;
	}
	
	@Override
	public Provider<? extends Junction> getJunction()
	{
		return this.junctionProvider;
	}
	
	@Override
	public Provider<? extends Machine> getMachine()
	{
		return this.machineProvider;
	}
	
	@Override
	public Provider<? extends Event> getEvent()
	{
		return this.eventProvider;
	}
	
	@Override
	public Provider<? extends Dynamic> getDynamic()
	{
		return this.dynamicProvider;
	}
	
}
