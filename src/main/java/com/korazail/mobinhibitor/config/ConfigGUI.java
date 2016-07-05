package com.korazail.mobinhibitor.config;

import com.korazail.mobinhibitor.MobInhibitor;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
/* Generic code to implement a GUI*/
public class ConfigGUI extends GuiConfig {
    public ConfigGUI(GuiScreen guiScreen) {
        super(guiScreen,
                new ConfigElement(ConfigHandler.configuration.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), MobInhibitor.MODID,
                false,
                false,
                GuiConfig.getAbridgedConfigPath(ConfigHandler.configuration.toString()));
    }
}