package com.korazail.MobInhibitor.utility;

import com.korazail.MobInhibitor.reference.Reference;
import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.world.WorldServer;
import org.apache.logging.log4j.Level;

import java.util.List;
import java.util.Objects;

public class LogHelper{

    public static void log(Level level, Object object){
        if (level.lessOrEqual(Reference.LOGGING_LEVEL)) {
            FMLLog.log(Reference.MOD_NAME, level, String.valueOf(object));
        }
    }

    public static void chat(Level level, EnumChatFormatting color, Object object){
        if (level.lessOrEqual(Reference.LOGGING_LEVEL)) {
            IChatComponent message = new ChatComponentText(String.valueOf(object));
            message.setChatStyle(message.getChatStyle().setColor(color));
            MinecraftServer.getServer().getConfigurationManager().sendChatMsg(message);
        }
    }

    public static void info (Object object){log(Level.INFO,object);}
    public static void debug (Object object){log(Level.DEBUG,object);}
    public static void warn (Object object){log(Level.WARN,object);}
    public static void error (Object object){log(Level.ERROR,object);}
    public static void fatal (Object object){log(Level.FATAL,object);}

    public static void chatInfo(Object object){ chat(Level.INFO, EnumChatFormatting.DARK_GRAY, "[Info]" + String.valueOf(object));}
    public static void chatDebug(Object object){ chat(Level.DEBUG, EnumChatFormatting.AQUA, "[Debug]"+String.valueOf(object));}
    public static void chatWarn(Object object){ chat(Level.DEBUG, EnumChatFormatting.YELLOW, "[Warning]"+String.valueOf(object));}
    public static void chatError(Object object){ chat(Level.DEBUG, EnumChatFormatting.RED, "[Error]"+String.valueOf(object));}
    //No fatal, because a fatal error would not leave a chat visible. Just Log those, instead.
}