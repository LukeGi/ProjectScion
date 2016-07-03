package com.github.projectscion.common.features.magic;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.LogHelper;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by blue on 03/07/16.
 */
public class BlockMagicDoor extends BlockDoor implements ITileEntityProvider {
 public BlockMagicDoor() {
  super(Material.WOOD);
  setRegistryName(ModInfo.MOD_ID, "magic_door");
  setUnlocalizedName(getRegistryName().getResourcePath());
  setCreativeTab(CreativeTabs.DECORATIONS);
 }

 public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
  if (playerIn.getUniqueID().equals(((TileEntityMagicDoor) worldIn.getTileEntity(pos)).uuid)) {
   return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
  }
  return false;
 }

 @Override
 public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
  return new ItemStack(this, 1);
 }

 @Override
 public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
  if (!worldIn.isRemote) {
   ((TileEntityMagicDoor) worldIn.getTileEntity(pos)).setUUID(placer.getUniqueID());
   LogHelper.info(((TileEntityMagicDoor) worldIn.getTileEntity(pos)).uuid.toString());
  }
  super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
 }

 @Override
 public TileEntity createNewTileEntity(World worldIn, int meta) {
  return new TileEntityMagicDoor();
 }

 public static class TileEntityMagicDoor extends TileEntity {
  public UUID uuid;

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
   compound.setString("UUIDofPlayer", uuid.toString());
   return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
   uuid = UUID.fromString(compound.getString("UUIDofPlayer"));
   super.readFromNBT(compound);
  }

  public void setUUID(UUID UUID) {
   this.uuid = UUID;
  }
 }
}
