package com.korazail.MobInhibitor.utility;

import com.korazail.MobInhibitor.reference.Reference;
import cpw.mods.fml.common.FMLLog;
import org.apache.logging.log4j.Level;

public class LogHelper{
    public static void log(Level level, Object object){
        if (level.lessOrEqual(Reference.LOGGING_LEVEL)) {
            FMLLog.log(Reference.MOD_NAME, level, String.valueOf(object));
        }
    }
    public static void all(Object object){log(Level.ALL,object);}
    public static void info (Object object){log(Level.INFO,object);}
    public static void debug (Object object){log(Level.DEBUG,object);}
    public static void warn (Object object){log(Level.WARN,object);}
    public static void error (Object object){log(Level.ERROR,object);}
    public static void fatal (Object object){log(Level.FATAL,object);}
}