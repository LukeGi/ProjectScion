package com.github.projectscion.common.features.duping;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.Platform;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticlePortal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

import java.awt.*;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 * Created by blue on 02/07/16.
 */
public class BlockTransformingBlock extends Block implements ITileEntityProvider {
 public BlockTransformingBlock() {
  super(Material.AIR);
  setHardness(5f);
  setResistance(100f);
  setRegistryName(ModInfo.MOD_ID, "transforming_block");
  setUnlocalizedName(getRegistryName().getResourcePath());
  setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
 }

 @Override
 public TileEntity createNewTileEntity(World worldIn, int meta) {
  return new TileEntityTransformingBlock();
 }

 @Override
 public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
  tooltip.clear();
  tooltip.add(new BigInteger(130, new Random(player.worldObj.getTotalWorldTime() / 4)).toString(32).toUpperCase());
  super.addInformation(stack, player, tooltip, advanced);
 }

 public static class TileEntityTransformingBlock extends TileEntity implements ITickable {
  public ItemStack drop;
  int ticksAlive = 0;

  @Override
  public void update() {
   ticksAlive++;
   if (Platform.isServer()) {
    if (ticksAlive > 12000) {
     if (drop != null)
      worldObj.spawnEntityInWorld(new EntityItem(getWorld(), pos.getX() + 0.5f, pos.up().getY(), pos.getZ() + 0.5f, drop));
     worldObj.destroyBlock(pos, false);
     worldObj.removeTileEntity(pos);
     worldObj.setBlockToAir(pos);
     worldObj.addWeatherEffect(new EntityLightningBolt(getWorld(), pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, true));
    }
   }
   markDirty();
   if (Platform.isClient()) {
    if (ticksAlive > 300 && ticksAlive < 11955) {
     IParticleFactory particleFactory = new ParticlePortal.Factory();
     for (int i = 0; i < 4; i++) {
      Particle p = particleFactory.getEntityFX(0, worldObj, pos.getX() + worldObj.rand.nextFloat(), pos.up().getY(), pos.getZ() + worldObj.rand.nextFloat(), worldObj.rand.nextGaussian(), worldObj.rand.nextGaussian(), worldObj.rand.nextGaussian());
      Color c = new Color(0X16906F);
      p.setRBGColorF(c.getRed() / 255F, c.getGreen() / 255F, c.getBlue() / 255F);
      Minecraft.getMinecraft().effectRenderer.addEffect(p);
     }
    }
   }
  }

  public ItemStack getDrop() {
   return drop;
  }

  public void setDrop(ItemStack drop) {
   this.drop = drop;
  }

  @Override
  public NBTTagCompound writeToNBT(NBTTagCompound compound) {
   drop.writeToNBT(compound);
   compound.setInteger("ticksAlive", ticksAlive);
   return super.writeToNBT(compound);
  }

  @Override
  public void readFromNBT(NBTTagCompound compound) {
   drop.readFromNBT(compound);
   ticksAlive = compound.getInteger("ticksAlive");
   super.readFromNBT(compound);
  }
 }
}
