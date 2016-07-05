package com.korazail.mobinhibitor.inhibitor;

import com.korazail.mobinhibitor.MobInhibitor;
import com.korazail.mobinhibitor.util.EnumTypes;
import com.korazail.mobinhibitor.util.MobInhibitorReference;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import java.util.List;
/* This class does fun stuff: handle keeping track of all the loaded inhibitors.
 * I haven't done any real analysis, but I believe the overhead of keeping my own
 * list of Tile entities to reference is faster than scanning all loaded Tile entities
 * when a spawn event triggers. for reference, that would look something like
 * for ( TileEntity e : worldObj.loadedTileEntityList ) { if (e instanceof TEMobInhibitor) ...}
 *
 * Often, Tile Entities in tutorial will have datapacket code. In my case, the client doesn't need
 * to know anything about the TE, so I'm not telling it. Neener*/
public class TEMobInhibitor extends TileEntity {
    private EnumTypes type;

    public TEMobInhibitor() { }

    public TEMobInhibitor(EnumTypes type){ this.type = type; }

    public EnumTypes getInhibitorType(){
        return this.type;
    }

    private void RegisterInhibitor(){
        if (this.worldObj.isRemote ){ return;}
        if (this.type == null) { return;}
        List<MobInhibitorReference> reflist = MobInhibitor.InhibitorRegistryLookup.get(this.type);
        MobInhibitorReference Ref = new MobInhibitorReference(this.pos, this.worldObj.provider.getDimension());
        if (!reflist.contains(Ref)){
            reflist.add(Ref);
        }
    }

    private void DeregisterInhibitor(){
        if (this.worldObj.isRemote){ return;}
        if (this.type == null) { return;}
        List<MobInhibitorReference> reflist = MobInhibitor.InhibitorRegistryLookup.get(this.type);
        MobInhibitorReference Ref = new MobInhibitorReference(this.pos, this.worldObj.provider.getDimension());
        int refIndex = reflist.indexOf(Ref);
        if (refIndex>=0){
            reflist.remove(refIndex);
        }
    }

    @Override
    public void validate (){
        super.validate();
        this.RegisterInhibitor();
    }

    @Override
    public void invalidate(){
        super.invalidate();
        this.DeregisterInhibitor();
    }

    @Override
    public void onChunkUnload(){
        this.DeregisterInhibitor();
        super.onChunkUnload();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt){
        super.readFromNBT(nbt);
        int meta = nbt.getInteger("meta");
        if (this.type == null){
            this.type = EnumTypes.byMetadata(meta);
        } else if (this.type.getMetadata() != meta) {
        }
    }
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt){
        if (this.type == null) {
            return super.writeToNBT(nbt);
        }
        int meta = this.type.getMetadata();
        nbt.setInteger("meta",meta);
        return super.writeToNBT(nbt);
    }
}
