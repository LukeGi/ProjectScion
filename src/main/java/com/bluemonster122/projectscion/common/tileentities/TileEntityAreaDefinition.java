package com.bluemonster122.projectscion.common.tileentities;

import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityFarm;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityAreaDefinition extends TileEntityBase {

    private TileEntityAreaDefinition master = this;
    private boolean isMaster = false;
    private BlockPos center = pos;
    private TileEntityFarm farm;

    @Override
    public void initMachineData() {

        List<TileEntityAreaDefinition> multiblock = new ArrayList<>();
        Stack<BlockPos> toScan = new Stack<>();
        toScan.push(pos);
        while (!toScan.isEmpty()) {
            BlockPos current = toScan.pop();
            TileEntity genericTile = worldObj.getTileEntity(current);
            if (genericTile != null && genericTile instanceof TileEntityAreaDefinition) {
                TileEntityAreaDefinition tile = (TileEntityAreaDefinition) genericTile;
                if (!multiblock.contains(tile)) {
                    multiblock.add(tile);
                    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                        toScan.push(current.offset(facing));
                    }
                }
            }
        }
        multiblock.stream().filter(TileEntityAreaDefinition::isMaster).forEach(tile -> this.master = tile);
        if (this.getMaster().equals(this)) {
            isMaster = true;
        }
        int xplus = pos.getX();
        int zplus = pos.getZ();
        int xminus = pos.getX();
        int zminus = pos.getZ();
        for (TileEntityAreaDefinition tile : multiblock) {
            BlockPos tilePos = tile.getPos();
            if (tilePos.getX() > xplus) {
                xplus = tilePos.getX();
            }
            if (tilePos.getX() < xminus) {
                xminus = tilePos.getX();
            }
            if (tilePos.getZ() > zplus) {
                zplus = tilePos.getZ();
            }
            if (tilePos.getZ() < zminus) {
                zminus = tilePos.getZ();
            }
        }
        setCenter(new BlockPos((xminus + xplus) / 2, pos.getY(), (zminus + zplus) / 2));
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (this.isMaster()) {
            this.isMaster = false;
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
                if (worldObj.getTileEntity(pos.offset(facing)) != null && worldObj.getTileEntity(pos.offset(facing)) instanceof TileEntityAreaDefinition) {
                    ((TileEntityAreaDefinition) worldObj.getTileEntity(pos.offset(facing))).setMaster();
                    break;
                }
        }
    }

    private void setMaster() {

        isMaster = true;
        if (farm != null) {
            farm.setArea(this.pos);
        }
        List<TileEntityAreaDefinition> multiblock = new ArrayList<>();
        Stack<BlockPos> toScan = new Stack<>();
        toScan.push(pos);
        while (!toScan.isEmpty()) {
            BlockPos current = toScan.pop();
            TileEntity genericTile = worldObj.getTileEntity(current);
            if (genericTile != null && genericTile instanceof TileEntityAreaDefinition) {
                TileEntityAreaDefinition tile = (TileEntityAreaDefinition) genericTile;
                if (!multiblock.contains(tile)) {
                    multiblock.add(tile);
                    tile.initMachineData();
                    for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                        toScan.push(current.offset(facing));
                    }
                }
            }
        }
    }

    public TileEntityAreaDefinition getMaster() {

        return master;
    }

    public BlockPos getCenter() {

        return isMaster() ? center : getMaster().getCenter();
    }

    public boolean isMaster() {

        return isMaster;
    }

    public void setCenter(BlockPos center) {

        if (isMaster()) {
            this.center = center;
        } else {
            getMaster().setCenter(center);
        }
    }

    public void setFarm(TileEntityFarm farm) {

        this.farm = farm;
    }
}
