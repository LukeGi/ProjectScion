package com.github.projectscion.common.features.duping;

import com.github.projectscion.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

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

    public static class TileEntityTransformingBlock extends TileEntity implements ITickable {
        public ItemStack drop;
        int ticksAlive = 0;

        @Override
        public void update() {
            ticksAlive++;
            if (ticksAlive > 300 && ticksAlive < 11955) {
                for (int i = 0; i < 16; i++)
                    getWorld().spawnParticle(EnumParticleTypes.PORTAL, pos.getX() + worldObj.rand.nextFloat(), pos.up().getY(), pos.getZ() + worldObj.rand.nextFloat(), 0, 1f / 16, 0);
            }
            if (!worldObj.isRemote) {
                if (ticksAlive > 12000) {
                    if (drop != null)
                        worldObj.spawnEntityInWorld(new EntityItem(getWorld(), pos.getX() + 0.5f, pos.up().getY(), pos.getZ() + 0.5f, drop));
                    worldObj.destroyBlock(pos, false);
                    worldObj.removeTileEntity(pos);
                    worldObj.setBlockToAir(pos);
                    worldObj.addWeatherEffect(new EntityLightningBolt(getWorld(), pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, true));
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
