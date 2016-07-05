package com.korazail.mobinhibitor.inhibitor;

import com.korazail.mobinhibitor.util.EnumTypes;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
/* this class handles the item representation of the inhibitor. This exists almost entirely to provide
 * metadata support. If I weren't using metadata, the auto-generated ItemBlock that many tutorials describe
 * would have been sufficient. */
public class ItemMobInhibitor extends ItemBlock {
    public ItemMobInhibitor(Block block) {
        super(block);
        this.setMaxDamage(0);
        this.hasSubtypes=true;
    }

    @Override
    public int getMetadata(int meta){
        return meta;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        String type = EnumTypes.byMetadata(stack.getMetadata()).getName();
        return super.getUnlocalizedName()+"."+type;
    }

    // adds 'tooltip' text
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        String type = EnumTypes.byMetadata(stack.getMetadata()).toString();
        tooltip.add("Place to prevent "+type+" mobs");
        tooltip.add("from spawning. Hold to see if");
        tooltip.add("another inhibitor is protecting");
        tooltip.add("your current location.");
    }
}
