package com.korazail.mobinhibitor.util;


import com.korazail.mobinhibitor.inhibitor.BlockMobInhibitor;
import com.korazail.mobinhibitor.inhibitor.ItemMobInhibitor;
import com.korazail.mobinhibitor.inhibitor.TEMobInhibitor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
/* this class does all the registration:
 * It instantiates the block and registers it and the item to the game in init() - this is called in CommonProxy.preInit
 * It registers model resources for rendering in initRender() - called in ClientProxy.preInit()
 * It registers recipes in initRecipes() - called in CommonProxy.init();
 *  */
public class Reference {

    public static BlockMobInhibitor blockMobInhibitor;
    public static void init() {
        blockMobInhibitor = new BlockMobInhibitor();
        GameRegistry.registerBlock(blockMobInhibitor, ItemMobInhibitor.class, "mobinhibitor:mobinhibitor");
        GameRegistry.registerTileEntity(TEMobInhibitor.class,"TEMobInhibitor");
    }

    @SideOnly(Side.CLIENT)
    public static void initRender() {
        Item ItemBlockMI = GameRegistry.findItem("mobinhibitor","mobinhibitor");
        for (EnumTypes type : EnumTypes.values()){
            ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation("mobinhibitor:mobinhibitor_"+type.getName(),"inventory");
            ModelLoader.setCustomModelResourceLocation(ItemBlockMI, type.getMetadata(), itemModelResourceLocation);
        }
    }

    public static void initRecipes() {
        ItemStack squidInk = new ItemStack(Items.DYE,1,0);
        GameRegistry.addRecipe(new ItemStack(blockMobInhibitor,1,0),
                "PFP",
                "BPE",
                "PTP",
                'B',Items.BONE,
                'E',Items.SPIDER_EYE,
                'F',Items.ROTTEN_FLESH,
                'P', Blocks.PLANKS,
                'T',Blocks.TORCH
        );
        GameRegistry.addRecipe(new ItemStack(blockMobInhibitor,1,1),
                "PIP",
                "IPI",
                "PTP",
                'I', squidInk,
                'P', Blocks.PLANKS,
                'T', Blocks.TORCH
        );
        GameRegistry.addRecipe(new ItemStack(blockMobInhibitor,1,2),
                "PWP",
                "WPW",
                "PTP",
                'W',Blocks.WOOL,
                'P',Blocks.PLANKS,
                'T',Blocks.TORCH
        );
    }
}
