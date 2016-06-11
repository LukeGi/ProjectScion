package com.github.projectscion.common.core.multiblock;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public abstract class BlockMultiblock extends Block implements ITileEntityProvider{
    public BlockMultiblock(Material materialIn) {
        super(materialIn);
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile!= null && tile instanceof TileEntityMultiblock) {
            TileEntityMultiblock multiblock = (TileEntityMultiblock)tile;
            if (multiblock.hasMaster()) {
                if (multiblock.isMaster()) {
                    if (!multiblock.checkMuliblockForm())
                        multiblock.resetStructure();
                } else {
                    if (!multiblock.checkForMaster()) {
                        multiblock.reset();
                        multiblock.markDirty();
                    }
                }
            }
        }
        super.onNeighborChange(world, pos, neighbor);
    }
}
