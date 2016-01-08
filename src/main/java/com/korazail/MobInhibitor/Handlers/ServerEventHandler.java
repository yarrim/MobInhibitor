package com.korazail.MobInhibitor.Handlers;

import com.korazail.MobInhibitor.MobInhibitor;
import com.korazail.MobInhibitor.tile.MobInhibitorReference;
import com.korazail.MobInhibitor.utility.LogHelper;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.EnumCreatureType;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;

import java.util.List;


public class ServerEventHandler {

    private boolean TestRange(MobInhibitorReference ref, double x, double y, double z){
        //cast (double) coordinates for an entity spawn, which will be in the middle of the block, do an (int) for more accurate range calculation.
        // could have gone the other way and cast everything to (double) and added .5 to each MobInhibitorReference coordinate, but I figured
        // integer math would be slightly faster than double math.
        int i = (int)(x-.5);
        int j = (int)(y-.5);
        int k = (int)(z-.5);
        int method = ConfigurationHandler.intMethod;
        int rad = ConfigurationHandler.EffectRadius;
        switch (method) {
            case 0: // sphere - dx^2+dy^2+dz^2 <= rad^2
                return (((ref.i- i)*(ref.i- i))   +   ((ref.j- j)*(ref.j- j))   +   ((ref.k- k)*(ref.k- k))   <=   (rad*rad));
            case 1: // cube - x,y,z all between Ref + or - radius
                return (((i >=ref.i-rad) && (i <=ref.i+rad))   &&   ((j >=ref.j-rad) && (j <=ref.j+rad))   &&   ((k >=ref.k-rad) && (k <=ref.k+rad)));
            case 2: // cylinder - dx^2+dz^2 <= rad^2 && y between Ref + or - radius
                return ((((ref.i- i)*(ref.i- i))  +  ((ref.k- k)*(ref.k- k))  <=  (rad*rad))  &&  ((j >=ref.j-rad) && (j <=ref.j+rad)));
            case 3: // column - same as Cylinder, but no height requirement.
                return (((ref.i- i)*(ref.i- i))  +  ((ref.k- k)*(ref.k- k))<=(rad*rad));
        }
        return false;
    }

    @SubscribeEvent
    public void BlockSpawnEvent(LivingSpawnEvent.CheckSpawn event){
        List<MobInhibitorReference> RefList;
        if (event.getResult() == Event.Result.ALLOW){ return;} // If the event is already forced, let it through
        if (event.entity.isCreatureType(EnumCreatureType.monster,false)){ //decide which list to use.
            RefList = MobInhibitor.HostileInhibitors;
        } else if (event.entity.isCreatureType(EnumCreatureType.waterCreature,false)){
            RefList = MobInhibitor.AquaInhibitors;
        } else { // this will also catch ambient creatures like bats.
            if (!ConfigurationHandler.InhibitAmbient && event.entity.isCreatureType(EnumCreatureType.ambient,false)){
                return; //If the InhibitAmbient config is not set, and the creature type is ambient, don't do anything.
                //If the config is set, fall through. The remamining category is Creature, which includes passives.
            }
            RefList = MobInhibitor.PassiveInhibitors;
        }
        for (MobInhibitorReference Ref : RefList){
            if (TestRange(Ref, event.entity.posX, event.entity.posY, event.entity.posZ)) {
                event.setResult(Event.Result.DENY);
                LogHelper.debug("Blocked a spawn based on inhibitor at:"+Ref.i+", "+Ref.j+", "+Ref.k);
                return;
            }
        }
    }

    @SubscribeEvent
    public void BlockMobTeleport(EnderTeleportEvent event){
        if (!event.isCancelable()){return;}
        for (MobInhibitorReference Ref : MobInhibitor.HostileInhibitors){
            if (TestRange(Ref, event.targetX, event.targetY, event.targetZ)){
                event.setCanceled(true);
                LogHelper.debug("Blocked an Enderman teleport based on inhibitor at:"+Ref.i+", "+Ref.j+", "+Ref.k);
                break;
            }
        }

    }

    @SubscribeEvent
    public void ClearInhibitorsOnWorldUnload(net.minecraftforge.event.world.WorldEvent.Unload event){
        //Clear the Inhibitors lists when the world is unloaded.
        //When it is reloaded, the inhibitors will all validate() and add themselves back.
        MobInhibitor.HostileInhibitors.clear();
        MobInhibitor.PassiveInhibitors.clear();
        MobInhibitor.AquaInhibitors.clear();
    }
}
