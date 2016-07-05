package com.korazail.mobinhibitor.inhibitor;

import com.korazail.mobinhibitor.util.EnumTypes;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
/* This class handles the block representation of a Mob inhibitor. About the only things the code below does
 * is to enable metadata to support the three types of inhibitor and link the block to a Tile Entity */
public class BlockMobInhibitor extends Block implements ITileEntityProvider {
    private static final PropertyEnum<EnumTypes> TYPE = PropertyEnum.create("type",EnumTypes.class);

    public BlockMobInhibitor () {
        super(Material.WOOD);
        this.setUnlocalizedName("mobinhibitor:mobinhibitor");
        this.setRegistryName("mobinhibitor");
        this.setCreativeTab(CreativeTabs.MISC);
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(TYPE, EnumTypes.HOSTILE));
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TEMobInhibitor(EnumTypes.byMetadata(meta));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).getMetadata();
    }

    @Override
    public BlockStateContainer createBlockState () {
        return new BlockStateContainer(this, TYPE);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).getMetadata();
    }

    @Override
    public IBlockState getStateFromMeta(int meta){
        return this.getDefaultState().withProperty(TYPE,EnumTypes.byMetadata(meta));
    }

    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        for (EnumTypes type : EnumTypes.values()) {
            list.add(new ItemStack(this,1,type.getMetadata()));
        }
    }

}
