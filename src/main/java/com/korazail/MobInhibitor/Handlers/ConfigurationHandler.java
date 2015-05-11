package com.korazail.MobInhibitor.Handlers;

import com.korazail.MobInhibitor.reference.Reference;
import com.korazail.MobInhibitor.utility.LogHelper;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigurationHandler {

    public static Configuration configuration;
    public static int EffectRadius=32;
    public static int intMethod=0;
    public static boolean InhibitAmbient=true;

    public static void init(File configFile){
        if (configuration == null){
            configuration = new Configuration(configFile);
            readConfig();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)){
            readConfig();
        }
    }

    private static void readConfig() {
        EffectRadius = configuration.getInt("EffectRadius", Configuration.CATEGORY_GENERAL, 32, 0, 1024, "Radius around block in which mob spawns are inhibited");
        String[] Methods = {"sphere","cube","cylinder","column"};
        String MethodComment =
            "Method to determine whether an event is in range.\n"+
            "sphere: any point inside a sphere of radius EffectRadius centered on the inhibitor\n"+
            "cube: any point inside a cube with sides EffectRadius*2+1 centered on the inhibitor\n"+
            "cylinder: any point inside a cylinder defined by the circle of radius EffectRadius and height EffectRadius*2+1 centered on the inhibitor\n"+
            "column: any point inside the circle of radius EffectRadius centered on the inhibitor regardless of y-position";
        String Method = configuration.getString("BlockMethod",Configuration.CATEGORY_GENERAL,"sphere",MethodComment,Methods);
        boolean InhibitAmbient = configuration.getBoolean("InhibitAmbient",Configuration.CATEGORY_GENERAL,true,"Should Passive MobInhibitors block ambient creatures like bats?");
        for (int i=0;i<4;i++){
            if (Methods[i].equals(Method)){
                intMethod = i;
            }
        }
        if (configuration.hasChanged()){
            configuration.save();
        }
    }

}
