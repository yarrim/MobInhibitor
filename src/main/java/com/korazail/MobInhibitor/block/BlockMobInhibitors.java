package com.korazail.MobInhibitor.block;


import com.korazail.MobInhibitor.reference.Reference;
import com.korazail.MobInhibitor.tile.TileMobInhibitor;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class BlockMobInhibitors extends Block implements ITileEntityProvider{
    private IIcon[] icons=new IIcon[3];
    public BlockMobInhibitors(){
        super(Material.wood);
        this.setBlockName("MobInhibitors");
        this.setCreativeTab(CreativeTabs.tabBlock);
        this.setHardness(.5F);
        this.setStepSound(soundTypeWood);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileMobInhibitor(this, meta);
    }

    @Override
    public boolean isFlammable(IBlockAccess world, int x, int y, int z, ForgeDirection face) {
        return false; //wood motif for the block, but don't catch fire
    }

    @Override
    public boolean hasTileEntity(int metadata){
        return true;
    }

    /*
     * getUnlocalizedName, registerBlockIcons, damageDropped, and getSubBlocks
     * are all used to enable meta data on this block
     *
     * the TileEntity will consume the metadata to influence it's behavior.
     * getIcon also uses the metadata to pick the correct default texture.
     */
    @Override
    public String getUnlocalizedName(){
        //this nonsense is needed to insert my MOD_ID into the block's unlocalizedName
        return String.format("tile.%s:%s", Reference.MOD_ID, super.getUnlocalizedName().substring(super.getUnlocalizedName().indexOf(".") + 1));
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister){
        String blockName = this.getUnlocalizedName().substring(this.getUnlocalizedName().indexOf(':')+1);
        this.icons[0] = iconRegister.registerIcon(String.format("%s:%s",Reference.MOD_ID,blockName));
        this.icons[1] = iconRegister.registerIcon(String.format("%s:Passive%s",Reference.MOD_ID,blockName));
        this.icons[2] = iconRegister.registerIcon(String.format("%s:Aqua%s",Reference.MOD_ID,blockName));
    }

    @Override
    public int damageDropped(int meta){
        return meta;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list){
        for (int i=0;i<3;i++){
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta){
        if (meta > 2) {meta =0;}
        return this.icons[meta];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        TileMobInhibitor TE = (TileMobInhibitor)world.getTileEntity(x,y,z);
        if (TE!=null){
            return TE.ImpersonatingIcon(side);
        } else {
            return this.icons[world.getBlockMetadata(x,y,z)];
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz) {
        TileMobInhibitor TE = (TileMobInhibitor) world.getTileEntity(x, y, z);
        if (world.isRemote) { return true;}
        ItemStack held = player.getHeldItem();
        if (held != null && !player.isSneaking()) {
            Block block = getBlockFromItem(held.getItem());
            return TE.setImpersonation(block, held.getItemDamage());
        }
        if (player.isSneaking() && (held == null || held.getUnlocalizedName().matches("(wrench|tool|hammer|screwdriver)"))) {
            return TE.clearImpersonation();
        }
        return super.onBlockActivated(world,x,y,z,player,side,px,py,pz);
    }
}
