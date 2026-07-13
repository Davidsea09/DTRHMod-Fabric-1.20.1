package net.emanueljdf09.dtrhmod.block.custom.mirror;

import net.minecraft.util.StringIdentifiable;

public enum MirrorType implements StringIdentifiable {
    normal("normal"),
    structure("structure"),
    wonderland("wonderland");

    private final String name;

    MirrorType(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name();
    }
}
