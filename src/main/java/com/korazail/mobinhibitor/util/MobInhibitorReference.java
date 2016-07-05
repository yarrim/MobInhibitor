package com.korazail.mobinhibitor.util;

import com.korazail.mobinhibitor.config.ConfigHandler;
import net.minecraft.util.math.BlockPos;

/* This is a helper class that encapsulates the x, y, z coordinates for an inhibitor along with the
 * dimension it resides in. I cull potential matches by dimension ID first to prevent doing the more
 * complicated math on inhibitors that should not affect the spawn event. Having all the relevant
 * inhibitor details in an object allows me to implement a .equals() and use the .contains() method
 * in a List<>. This makes other code easier to read.*/
public class MobInhibitorReference {
    public BlockPos BlockPos;
    public int dim;
    public MobInhibitorReference(BlockPos BlockPos, int dim){
        this.BlockPos = BlockPos;
        this.dim = dim;
    }

    @Override
    public boolean equals(Object o){ //Used by List to enable .contains(Ref)
        boolean ret = false;
        if (o!=null&&o instanceof MobInhibitorReference){
            MobInhibitorReference ref = (MobInhibitorReference) o;
            ret = (ref.BlockPos.equals(BlockPos) && ref.dim==dim);
        }
        return ret;
    }

    @Override
    public String toString() {
        return ("("+this.BlockPos.toString()+"@"+this.dim+")");
    }

    public boolean TestRange(double x, double y, double z, int dim){
        if (this.dim != dim) { return false;} // Ignore any inhibitors in other dimensions
        BlockPos me = this.BlockPos;
        int i = (int)(x-.5);
        int j = (int)(y); // Spawns reference the middle of the block in the x and z axis, but not y.
        int k = (int)(z-.5);

        //if config dictates the spawn event to be above, then check to see if the event is below.
        boolean isBelow = ConfigHandler.EffectUp && (j<me.getY());

        //if config dictates we count in chunks, then div all the coordinates by 16
        if (ConfigHandler.CountChunks){
            me = new BlockPos(me.getX()/16,me.getY()/16,me.getZ()/16);
            i=i/16;
            j=j/16;
            k=k/16;
        }

        int rad = ConfigHandler.EffectRadius;
        switch (ConfigHandler.intMethod) {
            case 0: // sphere
                return (me.getDistance(i,j,k) <= rad) && !isBelow;
            case 1: // cube - x,y,z all between Ref + or - radius
                return (Math.abs(me.getX()-i)<=rad && Math.abs(me.getY()-j)<=rad && Math.abs(me.getZ()-k)<=rad) & !isBelow;
            case 2: // cylinder - same as sphere, but ignore y-axis
                return (me.getDistance(i,me.getY(),k)<=rad) && !isBelow;
            case 3: // Zone - Sphere's Column to the cube. X and Z within +/- radius, ignore Y
                return (Math.abs(me.getX()-i)<=rad && Math.abs(me.getZ()-k)<=rad) && !isBelow;
        }
        return false;
    }
}
