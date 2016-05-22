package com.bluemonster122.projectscion.common.tileentities;

import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityFarm;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
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
    private BlockPos[] inside = null;
    private TileEntityFarm farm;
    private AxisAlignedBB aabb;

    public AxisAlignedBB getAABB() {

        return aabb;
    }

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
        getMaster().setCenter(new BlockPos((xminus + xplus) / 2, pos.getY(), (zminus + zplus) / 2));
        getMaster().setAABB(new AxisAlignedBB(xplus + 10, pos.getY(), zplus + 10, xminus - 10, pos.getY() + 5, zminus - 10));
        List<BlockPos> insiders = new ArrayList<>();
        int y = pos.getY() + 1;
        for (int x = xminus; x <= xplus; x++) {
            for (int z = zminus; z <= zplus; z++) {
                insiders.add(new BlockPos(x, y, z));
            }
        }
        BlockPos[] surrounding = new BlockPos[insiders.size()];
        for (int i = insiders.size() - 1; i >= 0; i--) {
            surrounding[i] = insiders.get(i);
        }
        getMaster().setInside(surrounding);
    }

    public BlockPos[] getInside() {

        return inside;
    }

    @Override
    public void onChunkLoad() {

        initMachineData();
        super.onChunkLoad();
    }

    public void setInside(BlockPos[] inside) {

        if (isMaster()) {
            this.inside = inside;
        } else {
            getMaster().setInside(inside);
        }
    }

    public void setAABB(AxisAlignedBB aabb) {

        if (isMaster()) {
            this.aabb = aabb;
        } else {
            getMaster().setAABB(aabb);
        }
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (this.isMaster()) {
            this.isMaster = false;
            for (EnumFacing facing : EnumFacing.HORIZONTALS)
                if (worldObj.getTileEntity(pos.offset(facing)) != null && worldObj.getTileEntity(pos.offset(facing)) instanceof TileEntityAreaDefinition) {
                    ((TileEntityAreaDefinition) worldObj.getTileEntity(pos.offset(facing))).setMaster();
                    getMaster().setInside(inside);
                    getMaster().setCenter(center);
                    getMaster().setFarm(farm);
                    getMaster().setAABB(aabb);
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

    public TileEntityFarm getFarm() {

        return isMaster() ? farm : getMaster().getFarm();
    }
}
