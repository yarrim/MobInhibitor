package com.korazail.MobInhibitor.tile;

/*
This class is used solely to allow List<MobInhibitorReference> objects to
use .contains, to make reading easier when adding or removing an inhibitor
from the active inhibitors lists.
 */
public class MobInhibitorReference {
    public int i,j,k,dim;
    public MobInhibitorReference(int i, int j, int k, int dim){
        this.i = i;
        this.j = j;
        this.k = k;
        this.dim = dim;
    }

    @Override
    public boolean equals(Object o){ //Used by List to enable .contains(Ref)
        boolean ret = false;
        if (o!=null&&o instanceof MobInhibitorReference){
            MobInhibitorReference ref = (MobInhibitorReference) o;
            ret = (ref.i == i && ref.j ==j && ref.k==k & ref.dim==dim);
        }
        return ret;
    }

    @Override
    public String toString() {
        return ("("+this.i+", "+this.j+", "+this.k+"@"+this.dim+")");
    }
}
