package com.korazail.MobInhibitor;

import com.korazail.MobInhibitor.Handlers.ConfigurationHandler;
import com.korazail.MobInhibitor.Handlers.ServerEventHandler;
import com.korazail.MobInhibitor.block.BlockMobInhibitors;
import com.korazail.MobInhibitor.block.ItemBlockMobInhibitors;
import com.korazail.MobInhibitor.reference.Reference;
import com.korazail.MobInhibitor.tile.MobInhibitorReference;
import com.korazail.MobInhibitor.tile.TileMobInhibitor;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = Reference.MOD_ID, name= Reference.MOD_NAME, version = Reference.MOD_VERSION,guiFactory = Reference.GUI_FACTORY_CLASS)
public class MobInhibitor {
    //These are basically lists of coordinates in handy packaging. Look in the ServerEventHandler for how they are used.
    public static List<MobInhibitorReference> HostileInhibitors = new ArrayList<MobInhibitorReference>();
    public static List<MobInhibitorReference> PassiveInhibitors = new ArrayList<MobInhibitorReference>();
    public static List<MobInhibitorReference> AquaInhibitors = new ArrayList<MobInhibitorReference>();

    //For registering and recipies
    public static final BlockMobInhibitors BlockMobInhibitors = new BlockMobInhibitors();

    @Mod.Instance(Reference.MOD_ID)
    public static MobInhibitor instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event){
        //Read configurations
        ConfigurationHandler.init(event.getSuggestedConfigurationFile());
        FMLCommonHandler.instance().bus().register(new ConfigurationHandler());

        //Register Block and Tile Entity
        GameRegistry.registerBlock(BlockMobInhibitors, ItemBlockMobInhibitors.class, "MobInhibitors");
        GameRegistry.registerTileEntity(TileMobInhibitor.class, "TileMobInhibitor");
    }

    @Mod.EventHandler
    public void Init(FMLInitializationEvent event){
        //Start Listening to spawn events
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        ItemStack squidInk = new ItemStack(Items.dye,1,0);

        //Register recipes
        GameRegistry.addRecipe(new ItemStack(BlockMobInhibitors,1,0),
                "PFP",
                "BPE",
                "PTP",
                'B',Items.bone,
                'E',Items.spider_eye,
                'F',Items.rotten_flesh,
                'P', Blocks.planks,
                'T',Blocks.torch
        );
        GameRegistry.addRecipe(new ItemStack(BlockMobInhibitors,1,1),
                "PWP",
                "WPW",
                "PTP",
                'W',Blocks.wool,
                'P',Blocks.planks,
                'T',Blocks.torch
        );
        GameRegistry.addRecipe(new ItemStack(BlockMobInhibitors, 1, 2),
                "PIP",
                "IPI",
                "PTP",
                'I', squidInk,
                'P', Blocks.planks,
                'T', Blocks.torch
        );
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        //Nothing, yet...
    }
}
