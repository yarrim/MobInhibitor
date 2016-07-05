package com.korazail.mobinhibitor.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.util.Set;
/* generic code to hook into the forge for GUI support */
public class GUIFactory implements IModGuiFactory {
        @Override
        public void initialize(Minecraft minecraftInstance) {

        }

        @Override
        public Class<? extends GuiScreen> mainConfigGuiClass() {
            return ConfigGUI.class;
        }

        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }

        @Override
        public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
            return null;
        }
}
