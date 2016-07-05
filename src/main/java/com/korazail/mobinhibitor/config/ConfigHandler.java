package com.korazail.mobinhibitor.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;
/* Relatively clear code to handle config settings. The most interesting part is the readConfig converting a set of Strings to an int
 * This is done because I want to switch on this value later, and switch doesn't take String*/
public class ConfigHandler {
    public static Configuration configuration;
    public static int EffectRadius=32;
    public static boolean CountChunks=false;
    public static boolean EffectUp=false;
    public static int intMethod=0;
    public static boolean InhibitAmbient=true;

    public static void init(File configFile){
        if (configuration == null){
            configuration = new Configuration(configFile);
            readConfig();
        }
    }

    public static void readConfig() {
        //todo: someday, these strings should be set via localization.
        EffectRadius = configuration.getInt("EffectRadius", Configuration.CATEGORY_GENERAL, 32, 0, 1024,
            "Radius around inhibitor in which mob spawns are inhibited");
        CountChunks = configuration.getBoolean("CountChunks",Configuration.CATEGORY_GENERAL,false,
            "Is EffectRadius counted in chunks?\n"+
            "Works best with Zone method, a small radius and a base design that is chunk-border-aware.");
        EffectUp = configuration.getBoolean("EffectUp",Configuration.CATEGORY_GENERAL,false,
            "Restrict effect to the inhibitor and above only?\n"+
            "Enable this if you're afraid of cheating by dropping an inhibitor on the surface and preventing spawns in "+
            "caves below. To havesave caving, you'd need to put the inhibitor at bedrock.");
        String[] Methods = {"sphere","cube","cylinder","zone"};
        String MethodComment =
                "Method to determine whether an event is in range.\n"+
                        "  sphere: any point inside a sphere of radius EffectRadius centered on the inhibitor\n"+
                        "  cube: any point inside a cube with sides EffectRadius*2+1 centered on the inhibitor\n"+
                        "  cylinder: any point inside a cylinder defined by the circle of radius EffectRadius centered on the inhibitor regardless of height\n"+
                        "  zone: any point inside a square with sides EffectRadius*2+1 centered on the inhibitor regardless of height";
        String Method = configuration.getString("BlockMethod",Configuration.CATEGORY_GENERAL,"sphere",MethodComment,Methods);
        for (int i=0;i<4;i++){
            if (Methods[i].equals(Method)){
                intMethod = i;
            }
        }
        InhibitAmbient = configuration.getBoolean("InhibitAmbient",Configuration.CATEGORY_GENERAL,true,
            "Should Passive Mob Inhibitors inhibitor ambient creatures like bats?");
        if (configuration.hasChanged()){
            configuration.save();
        }
    }
}
