package com.korazail.MobInhibitor.tile;

import com.korazail.MobInhibitor.MobInhibitor;
import com.korazail.MobInhibitor.utility.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.util.List;


public class TileMobInhibitor extends TileEntity {
    private int meta;
    private Block skin;
    private int skinMeta;

    /*
     * Constructors.
     */
    public TileMobInhibitor(Block skin, int meta) {
        this.skin = skin;
        this.skinMeta = meta;
        this.meta = meta;
    }

    public TileMobInhibitor(){
        //only called by loader when block already exists. Values will be populated by the subsequent NBT read.
    }

    /*
     * register- and unregister-Inhibitors add the coordinates of this inhibitor to the appropriate global list object
     * The server-side spawn checking in ServerEventHandler will look through those lists to determine if a spawn event
     * is too close to an inhibitor.
     */

    private void registerInhibitor(){
        if (!this.worldObj.isRemote) {
            List<MobInhibitorReference> RefList;
            switch (this.meta){
                case 2: RefList = MobInhibitor.AquaInhibitors;break;
                case 1: RefList = MobInhibitor.PassiveInhibitors;break;
                case 0: default: RefList = MobInhibitor.HostileInhibitors;break;
            }
            MobInhibitorReference Ref = new MobInhibitorReference(this.xCoord, this.yCoord, this.zCoord);
            if (!RefList.contains(Ref)){
                if (!RefList.add(Ref)){
                    LogHelper.warn(String.format("Failed to register Inhibitor %s", Ref.toString()));
                }
            }
            else {
                LogHelper.warn("Inhibitor already registered");
            }
        }
    }

    private void unregisterInhibitor(){
        if (!this.worldObj.isRemote) {
            List<MobInhibitorReference> RefList;
            switch (this.meta){
                case 2: RefList = MobInhibitor.AquaInhibitors;break;
                case 1: RefList = MobInhibitor.PassiveInhibitors;break;
                case 0: default: RefList = MobInhibitor.HostileInhibitors;break;
            }
            MobInhibitorReference Ref = new MobInhibitorReference(this.xCoord, this.yCoord, this.zCoord);
            if (RefList.contains(Ref)){
                if (!RefList.remove(Ref)){
                    LogHelper.warn(String.format("Failed to unregister Inhibitor %s", Ref.toString()));
                }
            }
            else {
                LogHelper.warn("Unloaded a non-registered Inhibitor");
            }
        }
    }

    /*
     * validate, invalidate and onChunkUnload are good places to hook into the Tile Entity
     * lifecycle for registering and deregistering the inhibitors.
     */

    @Override
    public void validate (){
        //This suffices as an onLoad();
        super.validate();
        this.registerInhibitor();
    }

    @Override
    public void invalidate(){
        //this suffices as an OnUnload();
        super.invalidate();
        this.unregisterInhibitor();
    }

    @Override
    public void onChunkUnload(){
        //this is also an Unload
        this.unregisterInhibitor();
        super.onChunkUnload();
    }

    /*
     * getDescriptionPacket and onDataPacket sync NBTData between the server and client
     * readfromNBT imports data from the NBT Tags
     * writetoNBT writes data to the NBT Tags
     * read and write are called automatically by the server to preserve and recreate entity states at start and exit
     */

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
        readFromNBT(packet.func_148857_g());
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt){
        super.writeToNBT(nbt);
        nbt.setInteger("meta", this.meta);
        nbt.setInteger("skinBlockID", Block.getIdFromBlock(this.skin));
        nbt.setInteger("skinMeta", this.skinMeta);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        this.meta = nbt.getInteger("meta");
        this.skin = Block.getBlockById(nbt.getInteger("skinBlockID"));
        this.skinMeta = nbt.getInteger("skinMeta");
        if (!this.skin.isNormalCube()){ // If the returned block isn't a "normal cube", default back to MobInhibitors
            this.skin = worldObj.getBlock(this.xCoord,this.yCoord,this.zCoord);
            this.skinMeta = worldObj.getBlockMetadata(this.xCoord,this.yCoord,this.zCoord);
        }
    }

    /*
     * Set the skin block and return icons based on that block. See the other side of this in the BlockMobInhibitors definition
     */

    public boolean setImpersonation(Block block, int blockMeta){
        if(block.isNormalCube()){
            this.skin = block;
            this.skinMeta = blockMeta;
            worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            return true;
        }
        return false;
    }

    public boolean clearImpersonation(){
        this.skin = worldObj.getBlock(this.xCoord, this.yCoord, this.zCoord);
        this.skinMeta = worldObj.getBlockMetadata(this.xCoord,this.yCoord,this.zCoord);
        worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        return true;
    }

    public IIcon ImpersonatingIcon(int side){
        return this.skin.getIcon(side,this.skinMeta);
    }

}
