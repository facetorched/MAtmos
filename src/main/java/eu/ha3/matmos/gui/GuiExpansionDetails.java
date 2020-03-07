package eu.ha3.matmos.gui;

import java.io.File;

import eu.ha3.matmos.Matmos;
import eu.ha3.matmos.core.expansion.Expansion;
import eu.ha3.matmos.core.expansion.ExpansionDebugUnit;
import eu.ha3.matmos.core.expansion.FolderExpansionDebugUnit;
import eu.ha3.matmos.core.expansion.JsonExpansionDebugUnit;
import eu.ha3.matmos.debug.PluggableIntoMAtmos;
import eu.ha3.matmos.debug.SoundsJsonGenerator;
import eu.ha3.matmos.game.user.VisualExpansionDebugging;
import eu.ha3.matmos.util.IDontKnowHowToCode;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

public class GuiExpansionDetails extends GuiScreen {
    private final GuiMatMenu parentScreen;
    private final Matmos mod;
    private final Expansion expansion;
    private final VisualExpansionDebugging debug;

    public GuiExpansionDetails(GuiMatMenu menu, Matmos mod, Expansion expansion) {
        parentScreen = menu;
        this.mod = mod;
        this.expansion = expansion;
        debug = new VisualExpansionDebugging(this.mod, expansion.getName());
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        drawGradientRect(0, 0, width, height, 0xF0000000, 0x90000000);

        drawCenteredString(fontRenderer, I18n.format("mat.mode.dev", expansion.getFriendlyName(), expansion.getName()), width / 2, 4, 0xffffff);

        debug.onFrame(0f);

        super.drawScreen(par1, par2, par3);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void initGui() {
        int h = new ScaledResolution(mc).getScaledHeight() - 22;

        buttonList.add(new GuiButton(200, 2, h, 70, 20, I18n.format("mat.options.close")));
        buttonList.add(new GuiButton(201, 4 + 70, h, 70, 20, I18n.format("mat.options.osd")));
        buttonList.add(new GuiButton(202, 6 + 70 * 2, h, 70, 20, I18n.format("mat.options.reload")));

        String editor = I18n.format(mod.isEditorAvailable() ? "mat.options.editor" : "mat.options.editor.disabled");

        buttonList.add(new GuiButton(203, 8 + 70 * 3, h, 110, 20, editor));
        buttonList.add(new GuiButton(204, 10 + 70 * 3 + 110, h, 96, 20, I18n.format("mat.options.sounds")));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 200) {
            // This triggers onGuiClosed
            mc.displayGuiScreen(parentScreen);
        } else if (button.id == 201) {
            mod.getVisualDebugger().debugModeExpansion(debug);
            mc.displayGuiScreen(null);
        } else if (button.id == 202) {
            expansion.refreshKnowledge();
        } else if (button.id == 203 && mod.isEditorAvailable()) {
            final ExpansionDebugUnit debugUnit = expansion.obtainDebugUnit();
            if (debugUnit != null) {
                PluggableIntoMAtmos plug = new PluggableIntoMAtmos(mod, expansion);

                Runnable editor = mod.instantiateRunnableEditor(plug);
                if (editor != null) {
                    new Thread(editor, "EditorWindow_for_" + expansion.getName()).start();

                    if (debugUnit instanceof JsonExpansionDebugUnit) {
                        // XXX Read only mode
                        mod.getChatter().printChat(TextFormatting.RED, I18n.format("mat.zip.unsupported"));
                        mod.getChatter().printChatShort(TextFormatting.RED, I18n.format("mat.zip.unzip"));
                    }
                } else {
                    mod.getChatter().printChat(TextFormatting.RED, "Could not start editor for an unknown reason.");
                }
            }
        } else if (button.id == 204) {
            final ExpansionDebugUnit debugUnit = expansion.obtainDebugUnit();

            if (debugUnit instanceof FolderExpansionDebugUnit) {

                File expFolder = ((FolderExpansionDebugUnit)debugUnit).getExpansionFolder();
                File minecraftFolder = new File(expFolder, "assets/minecraft/");

                if (minecraftFolder.exists()) {

                    File soundsFolder = new File(minecraftFolder, "sounds/");
                    File jsonFile = new File(minecraftFolder, "sounds.json");

                    if (soundsFolder.exists()) {
                        try {
                            new SoundsJsonGenerator(soundsFolder, jsonFile).run();

                            mod.getChatter().printChat(I18n.format("mat.generator.done", jsonFile.getAbsolutePath()));
                            mod.getChatter().printChatShort(I18n.format("mat.generator.plswait"));
                        } catch (Exception e) {
                            e.printStackTrace(System.out);
                            IDontKnowHowToCode.whoops__printExceptionToChat(mod.getChatter(), e, this);
                        }
                    } else {
                        mod.getChatter().printChat(TextFormatting.RED, I18n.format("mat.folders.sounds"));
                    }
                } else {
                    mod.getChatter().printChat(TextFormatting.RED, I18n.format("mat.folders.mc"));
                }
            }
        }
    }
}
