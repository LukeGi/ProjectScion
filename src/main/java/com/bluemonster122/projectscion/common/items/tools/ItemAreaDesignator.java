package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.tileentities.TileEntityAreaDefinition;
import com.bluemonster122.projectscion.common.tileentities.farm.TileEntityTreeFarm;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemAreaDesignator extends ItemBase {

    public ItemAreaDesignator() {

        super("area_designator");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("area_designator");
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

        TileEntity genericTile = worldIn.getTileEntity(pos);
        if (genericTile != null) {
            if (genericTile instanceof TileEntityAreaDefinition) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt == null) {
                    nbt = new NBTTagCompound();
                }
                BlockPos masterPos = ((TileEntityAreaDefinition) genericTile).getMaster().getPos();
                nbt.setInteger("inx", masterPos.getX());
                nbt.setInteger("iny", masterPos.getY());
                nbt.setInteger("inz", masterPos.getZ());
                stack.setTagCompound(nbt);
            }
            if (genericTile instanceof TileEntityTreeFarm) {
                NBTTagCompound nbt = stack.getTagCompound();
                if (nbt != null && nbt.hasKey("inx") && nbt.hasKey("iny") && nbt.hasKey("inz")) {
                    BlockPos areaPos = new BlockPos(nbt.getInteger("inx"), nbt.getInteger("iny"), nbt.getInteger("inz"));
                    ((TileEntityTreeFarm) genericTile).setArea(areaPos);
                    stack.setTagCompound(null);
                }
            }
        }
        return EnumActionResult.SUCCESS;
    }
}
