package com.korazail.MobInhibitor.tile;

public class MobInhibitorReference {
    public int i,j,k;
    public MobInhibitorReference(int i, int j, int k){
        this.i = i;
        this.j = j;
        this.k = k;
    }

    @Override
    public boolean equals(Object o){ //Used by List to enable .contains(Ref)
        boolean ret = false;
        if (o!=null&&o instanceof MobInhibitorReference){
            MobInhibitorReference ref = (MobInhibitorReference) o;
            ret = (ref.i == i && ref.j ==j && ref.k==k);
        }
        return ret;
    }
}
