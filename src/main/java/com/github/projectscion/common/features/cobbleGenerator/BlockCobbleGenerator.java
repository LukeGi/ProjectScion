package com.github.projectscion.common.features.cobbleGenerator;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.core.multiblock.BlockMultiblock;
import com.github.projectscion.common.util.Platform;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockCobbleGenerator extends BlockMultiblock {

 public BlockCobbleGenerator() {
  super(Material.ROCK);
  setRegistryName(ModInfo.MOD_ID, "cobble_generator");
  setUnlocalizedName(getRegistryName().toString());
  setCreativeTab(CreativeTabs.REDSTONE);
 }

 @Override
 public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
  if (Platform.isServer() && playerIn.isSneaking()) {
   TileEntity tile = worldIn.getTileEntity(pos);
   if (tile != null && tile instanceof TileEntityCobbleGenerator) {
    TileEntityCobbleGenerator cobbleGenerator = (TileEntityCobbleGenerator) tile;
    ItemStack cobble = cobbleGenerator.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN).extractItem(0, 64, false);
    if (cobble != null && cobble.stackSize > 0) {
     EntityItem cobbleItem = new EntityItem(playerIn.worldObj, playerIn.posX, playerIn.posY, playerIn.posZ, cobble);
     cobbleItem.setVelocity(0, 0, 0);
     cobbleItem.setPickupDelay(0);
     worldIn.spawnEntityInWorld(cobbleItem);
    }
   }
  }
  return super.onBlockActivated(worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ);
 }

 @Override
 public TileEntity createNewTileEntity(World worldIn, int meta) {
  return new TileEntityCobbleGenerator();
 }
}
