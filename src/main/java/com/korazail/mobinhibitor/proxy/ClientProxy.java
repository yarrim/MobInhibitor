package com.korazail.mobinhibitor.proxy;

import com.korazail.mobinhibitor.util.ClientEventHandler;
import com.korazail.mobinhibitor.util.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/* Clients only get rendering code and the UI modifying code that lives in the client event handler */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit(FMLPreInitializationEvent event){
        super.preInit(event);
        Reference.initRender();
    }

    @Override
    public void Init(FMLInitializationEvent event){
        super.Init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event){
        super.postInit(event);
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
    }

}
