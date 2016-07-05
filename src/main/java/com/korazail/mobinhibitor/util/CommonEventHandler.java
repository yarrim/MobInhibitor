package com.korazail.mobinhibitor.util;

import com.korazail.mobinhibitor.MobInhibitor;
import com.korazail.mobinhibitor.config.ConfigHandler;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/* This class handles events that both client and/or the server need. The server watches for spawn and teleport events
 * and breaks them if an appropriate inhibitor is in range */

public class CommonEventHandler {
    public CommonEventHandler(){

    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event){
        if (event.getModID().equalsIgnoreCase(MobInhibitor.MODID)){
            ConfigHandler.readConfig();
        }
    }

    @SubscribeEvent
    public void BlockSpawnEvent(LivingSpawnEvent.CheckSpawn event){
        List<MobInhibitorReference> RefList;
        EnumTypes type = null;
        if (event.getResult() != Event.Result.DEFAULT){ return;} // If the event is already forced Allow or Deny, let it through
        if (event.getEntity().isCreatureType(EnumCreatureType.MONSTER,false)){ //decide which list to use.
            RefList = MobInhibitor.InhibitorRegistryLookup.get(EnumTypes.HOSTILE);
            type = EnumTypes.HOSTILE;
        } else if (event.getEntity().isCreatureType(EnumCreatureType.WATER_CREATURE,false)){
            RefList = MobInhibitor.InhibitorRegistryLookup.get(EnumTypes.WATER);
            type = EnumTypes.WATER;
        } else {
            if (!ConfigHandler.InhibitAmbient && event.getEntity().isCreatureType(EnumCreatureType.AMBIENT,false)){
                return; //If the InhibitAmbient config is not set, and the creature type is ambient, don't do anything.
            }
            RefList = MobInhibitor.InhibitorRegistryLookup.get(EnumTypes.PASSIVE);
            type = EnumTypes.PASSIVE;
        }
        for (MobInhibitorReference Ref : RefList){
            if (Ref.TestRange(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, event.getWorld().provider.getDimension())) {
                event.setResult(Event.Result.DENY);
                break;
            }
        }
    }

    @SubscribeEvent
    public void BlockMobTeleport(EnderTeleportEvent event){
        if (!event.isCancelable()){return;}
        for (MobInhibitorReference Ref : MobInhibitor.InhibitorRegistryLookup.get(EnumTypes.HOSTILE)){
            if (Ref.TestRange(event.getTargetX(), event.getTargetY(), event.getTargetZ(), event.getEntity().worldObj.provider.getDimension())) {
                event.setCanceled(true);
                break;
            }
        }

    }
}
