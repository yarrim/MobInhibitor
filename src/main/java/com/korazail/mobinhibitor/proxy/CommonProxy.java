package com.korazail.mobinhibitor.proxy;

import com.korazail.mobinhibitor.config.ConfigHandler;
import com.korazail.mobinhibitor.util.CommonEventHandler;
import com.korazail.mobinhibitor.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/* Servers and Clients need to know about:
 * the block and item definitions [ Reference.init() / Reference.initRecipes() ]
 * Configuration Options
 * and events that either FML or Forge send.*/
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event){
        Reference.init();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new CommonEventHandler());
        MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
    }
    public void Init(FMLInitializationEvent event){
        Reference.initRecipes();
    }
    public void postInit(FMLPostInitializationEvent event){}
}
