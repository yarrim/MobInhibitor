package com.korazail.mobinhibitor.util;

import com.korazail.mobinhibitor.MobInhibitor;
import com.korazail.mobinhibitor.inhibitor.ItemMobInhibitor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.List;
/* Only thing in here currently is the code that changes the hotbar color when holding an inhibitor */
public class ClientEventHandler {
    public ClientEventHandler(){

    }

    @SubscribeEvent(receiveCanceled=true)
    public void onEvent(RenderGameOverlayEvent.Pre event) {
        if (event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR) {return;} // stop if not trying to render the hotbar

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (player == null || player.getHeldItemMainhand() == null) { return;} //stop if null player or nothing in hand

        ItemStack heldItem = player.getHeldItemMainhand().getItem() instanceof ItemMobInhibitor ? (player.getHeldItemMainhand()):null;
        if (heldItem ==null){ return;} //stop if current main hand item is not an inhibitor

        boolean underTheInfluence = false;
        List<MobInhibitorReference> reflist = MobInhibitor.InhibitorRegistryLookup.get(EnumTypes.byMetadata(heldItem.getMetadata()));
        for (MobInhibitorReference ref : reflist){
            if (ref.TestRange(player.posX, player.posY, player.posZ, player.dimension)){ underTheInfluence = true; break;}
        }
        //We're holding an inhibitor and the protectedness of our current position is (boolean)underTheInfluence. Let's apply a color filter to the hotbar:
        if (underTheInfluence) {
            GL11.glColor3f(0,.8f,0); // tint bar green
        } else {
            GL11.glColor3f(.8f,0,0); // tint bar red
        }

    }
}
