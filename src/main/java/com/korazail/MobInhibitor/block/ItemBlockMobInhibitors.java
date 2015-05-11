package com.korazail.MobInhibitor.block;


import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockMobInhibitors extends ItemBlockWithMetadata {
    public ItemBlockMobInhibitors(Block block){
        super(block,block);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack){
        return String.format("%s_%d",this.getUnlocalizedName(),stack.getItemDamage());
    }
}
