package com.korazail.mobinhibitor;

import com.google.common.collect.Maps;
import com.korazail.mobinhibitor.proxy.CommonProxy;
import com.korazail.mobinhibitor.util.EnumTypes;
import com.korazail.mobinhibitor.util.MobInhibitorReference;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Mod(modid = MobInhibitor.MODID, version = MobInhibitor.VERSION,guiFactory = "com.korazail.mobinhibitor.config.GUIFactory")
public class MobInhibitor
{

    public static final String MODID = "mobinhibitor";
    public static final String VERSION = "1.9.4-2.1";


    public static Map<EnumTypes,List<MobInhibitorReference>> InhibitorRegistryLookup = Maps.<EnumTypes,List<MobInhibitorReference>>newHashMap();



    @SidedProxy(clientSide = "com.korazail.mobinhibitor.proxy.ClientProxy", serverSide = "com.korazail.mobinhibitor.proxy.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event){
        this.proxy.preInit(event);
        for (EnumTypes type : EnumTypes.values()){
            InhibitorRegistryLookup.put(type, new ArrayList<MobInhibitorReference>());
        }
    }

    @EventHandler
    public void Init(FMLInitializationEvent event){ this.proxy.Init(event); }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event){
        this.proxy.postInit(event);
    }

}
