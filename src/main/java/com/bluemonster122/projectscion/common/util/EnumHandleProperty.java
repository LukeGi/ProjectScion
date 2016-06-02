package com.bluemonster122.projectscion.common.util;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public enum EnumHandleProperty implements IStringSerializable {
    BASIC,
    ADVANCED;

    @Override
    public String getName() {

        return this.name();
    }
}
