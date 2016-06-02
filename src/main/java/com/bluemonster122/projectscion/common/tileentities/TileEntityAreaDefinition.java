package com.bluemonster122.projectscion.common.tileentities;

import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityFarm;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class TileEntityAreaDefinition extends TileEntityBase {

    private TileEntityAreaDefinition master = this;
    private boolean isMaster = false;
    private BlockPos center = pos;
    private TileEntityFarm farm;
    private AxisAlignedBB aabb;

    public void save(NBTTagCompound nbt) {

        nbt.setBoolean("isMaster", isMaster());
        nbt.setBoolean("masterPos", getMaster() != null);
        if (getMaster() != null) {
            nbt.setInteger("masterX", getMaster().getPos().getX());
            nbt.setInteger("masterY", getMaster().getPos().getY());
            nbt.setInteger("masterZ", getMaster().getPos().getZ());
        }
        nbt.setBoolean("farmPos", getFarm() != null);
        if (getFarm() != null) {
            nbt.setInteger("farmX", getFarm().getPos().getX());
            nbt.setInteger("farmY", getFarm().getPos().getY());
            nbt.setInteger("farmZ", getFarm().getPos().getZ());
        }
        nbt.setBoolean("aabbPos", getAABB() != null);
        if (getAABB() != null) {
            nbt.setDouble("aabbMinX", getAABB().minX);
            nbt.setDouble("aabbMinY", getAABB().minY);
            nbt.setDouble("aabbMinZ", getAABB().minZ);
            nbt.setDouble("aabbMaxX", getAABB().minX);
            nbt.setDouble("aabbMaxY", getAABB().minY);
            nbt.setDouble("aabbMaxZ", getAABB().minZ);
        }
    }

    public void load(NBTTagCompound nbt) {

        setMaster(nbt.getBoolean("isMaster"));
        if (nbt.getBoolean("masterPos")) {
            setMaster((TileEntityAreaDefinition) getWorld().getTileEntity(new BlockPos(nbt.getInteger("masterX"), nbt.getInteger("masterY"), nbt.getInteger("masterZ"))));
        }
        if (nbt.getBoolean("farmPos")) {
            setFarm((TileEntityFarm) getWorld().getTileEntity(new BlockPos(nbt.getInteger("farmX"), nbt.getInteger("farmY"), nbt.getInteger("farmZ"))));
        }
        if (nbt.getBoolean("aabbPos")) {
            setAABB(new AxisAlignedBB(nbt.getDouble("aabbMinX"), nbt.getDouble("aabbMinY"), nbt.getDouble("aabbMinZ"), nbt.getDouble("aabbMaxX"), nbt.getDouble("aabbMaxY"), nbt.getDouble("aabbMaxZ")));
        }
    }

    public List<TileEntityAreaDefinition> getMultiblockTiles(Block match, BlockPos start) {

        List<BlockPos> poses = getMultiblock(match, start);
        List<TileEntityAreaDefinition> returner = new ArrayList<>(poses.size());
        returner.addAll(poses.stream().map(pos -> (TileEntityAreaDefinition) getWorld().getTileEntity(pos)).collect(Collectors.toList()));
        return returner;
    }

    public List<BlockPos> getMultiblock(Block match, BlockPos start) {

        List<BlockPos> matches = new ArrayList<>();
        List<BlockPos> visited = new ArrayList<>();
        Stack<BlockPos> toScan = new Stack<>();
        toScan.push(start);
        visited.add(start);
        while (!toScan.isEmpty()) {
            BlockPos current = toScan.pop();
            if (worldObj.getBlockState(current) == match) {
                matches.add(current);
                for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                    BlockPos element = current.offset(facing);
                    if (!visited.contains(element)) {
                        visited.add(element);
                        toScan.push(element);
                    }
                }
            }
        }
        return matches;
    }

    public AxisAlignedBB getAABB() {

        return aabb;
    }

    public void setAABB(AxisAlignedBB aabb) {

        this.aabb = aabb;
    }

    @Override
    public void initMachineData() {

        List<TileEntityAreaDefinition> others = getMultiblockTiles(ModBlocks.AREA_DEFINITION.getBlock(), getPos());
        TileEntityFarm farm = null;
        for (TileEntityAreaDefinition tile : others) {
            tile.setMaster(tile == this);
            tile.setMaster(this);
            tile.setCenter(null);
            tile.setAABB(null);
            tile.setFarm(null);
        }
        int xplus = pos.getX();
        int zplus = pos.getZ();
        int xminus = pos.getX();
        int zminus = pos.getZ();
        for (TileEntityAreaDefinition tile : others) {
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
        setAABB(new AxisAlignedBB(xplus + 10, pos.getY(), zplus + 10, xminus - 10, pos.getY() + 5, zminus - 10));
        setFarm(farm);
    }

    private void setupProperties(List<BlockPos> poses) {

        List<TileEntityAreaDefinition> multiblock = new ArrayList<>();
        poses.forEach(pos -> multiblock.add((TileEntityAreaDefinition) worldObj.getTileEntity(pos)));
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
    }

    public List<BlockPos> getInside() {

        AxisAlignedBB aabb = getAABB();
        int xlow = (int) aabb.minX;
        int xhigh = (int) aabb.maxX;
        int zlow = (int) aabb.minZ;
        int zhigh = (int) aabb.maxZ;
        List<BlockPos> inside = new ArrayList<>();
        int y = pos.getY() + 1;
        for (int x = xlow; x <= xhigh; x++) {
            for (int z = zlow; z <= zhigh; z++) {
                inside.add(new BlockPos(x, y, z));
            }
        }
        return inside;
    }

    @Override
    public void onChunkLoad() {

        initMachineData();
        super.onChunkLoad();
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (this.isMaster()) {
            setMaster(false);
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                TileEntity genericTile = getWorld().getTileEntity(pos.offset(facing));
                if (genericTile instanceof TileEntityAreaDefinition) {
                    ((TileEntityAreaDefinition) genericTile).initMachineData();
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

    public void setCenter(BlockPos center) {

        this.center = center;
    }

    public boolean isMaster() {

        return isMaster;
    }

    public void setMaster(TileEntityAreaDefinition master) {

        this.master = master;
    }

    public void setMaster(boolean isMaster) {

        this.isMaster = isMaster;
    }

    public TileEntityFarm getFarm() {

        return farm;
    }

    public void setFarm(TileEntityFarm farm) {

        this.farm = farm;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {

        super.writeToNBT(nbtTagCompound);
        save(nbtTagCompound);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {

        super.readFromNBT(nbtTagCompound);
        load(nbtTagCompound);
    }
}
