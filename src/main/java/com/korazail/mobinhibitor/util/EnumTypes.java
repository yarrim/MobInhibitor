package com.korazail.mobinhibitor.util;

import net.minecraft.util.IStringSerializable;
/* This Enum exists only to make code implementing the type of inhibitor easier to read.
 * I could have just used an int everywhere instead, but then you'd have to guess or
 * look up to determine which type "2" was. The Enum allows me to easily get an int from a name
 * The most complicated part here is the lookup, which allows me to get the enum back if all
 * I know is the int it represents.*/
public enum EnumTypes implements IStringSerializable {
    HOSTILE (0, "hostile"),
    WATER   (1, "water"),
    PASSIVE (2, "passive");

    private int metadata;
    private String name;
    private static final EnumTypes[] META_LOOKUP = new EnumTypes[values().length];

    EnumTypes(int metadataIn, String nameIn) {
        this.metadata = metadataIn;
        this.name = nameIn;
    }

    public int getMetadata() {
        return this.metadata;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static EnumTypes byMetadata(int meta) {
        if (meta < 0 || meta >= META_LOOKUP.length) {
            meta = 0;
        }
        return META_LOOKUP[meta];
    }

    static {
        for (EnumTypes type : values()) {
            META_LOOKUP[type.getMetadata()] = type;
        }
    }
}
