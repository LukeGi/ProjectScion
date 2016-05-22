package com.bluemonster122.projectscion.common.tileentities.farm;

import com.bluemonster122.projectscion.common.tileentities.TileEntityAreaDefinition;
import com.bluemonster122.projectscion.common.tileentities.TileEntityInventoryBase;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public abstract class TileEntityFarm extends TileEntityInventoryBase {

    private TileEntityAreaDefinition area;

    public void setArea(BlockPos area) {

        this.area = (TileEntityAreaDefinition) worldObj.getTileEntity(area);
        this.area.setFarm(this);
    }

    public BlockPos getArea() {

        return area.getPos();
    }
}
