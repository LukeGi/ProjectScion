package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.IProvideEvent;
import com.github.projectscion.common.util.LogHelper;
import com.github.projectscion.common.util.Platform;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blue on 26/06/16.
 */
public class ItemRythmicPickaxe extends ItemPickaxe implements IProvideEvent {
    public ToolMaterial Rythmicness = EnumHelper.addToolMaterial("rythmic", 3, 1561, 8.0F, 3.0F, 10).setRepairItem(new ItemStack(Items.RECORD_MALL, 1));

    public ItemRythmicPickaxe() {
        super(ToolMaterial.DIAMOND);
        setRegistryName(ModInfo.MOD_ID, "rythmic_pickaxe");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.TOOLS);
    }

    @SubscribeEvent
    public void handler(PlayerEvent.BreakSpeed event) {
        if (event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem().equals(FeatureTool.RYTHMIC_PICKAXE)) {
            World world = event.getEntityPlayer().getEntityWorld();
            IBlockState state = world.getBlockState(event.getPos());
            if (world.getTotalWorldTime() % 2 == 1 && event.getEntityPlayer().getHeldItemMainhand().getItem().canHarvestBlock(state)) {
                event.setNewSpeed(state.getBlockHardness(world, event.getPos()) * 10F);
            }
        }
    }

    @SubscribeEvent
    public void recipe(PlayerInteractEvent.RightClickBlock event) {
        ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();
        if (Platform.isServer() && heldStack != null && heldStack.getItem() == Items.STICK && heldStack.stackSize == 20) {
            World world = event.getWorld();
            List<BlockPos> toair = new ArrayList<>();
            IBlockState comp;
            EnumFacing i1 = event.getFace();
            if (i1 == EnumFacing.NORTH || i1 == EnumFacing.SOUTH) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i == 0 && j == 0) {
                            comp = Blocks.JUKEBOX.getDefaultState();
                        } else {
                            comp = Blocks.OBSIDIAN.getDefaultState();
                        }
                        if (!world.getBlockState(event.getPos().add(i, j, 0)).equals(comp)) {
                            LogHelper.info("not " + comp.toString() + " at " + event.getPos().add(i, j, 0).toString());
                            return;
                        } else {
                            toair.add(event.getPos().add(i, j, 0));
                        }
                    }
                }
            } else if (i1 == EnumFacing.WEST || i1 == EnumFacing.EAST) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i == 0 && j == 0) {
                            comp = Blocks.JUKEBOX.getDefaultState();
                        } else {
                            comp = Blocks.OBSIDIAN.getDefaultState();
                        }
                        if (!world.getBlockState(event.getPos().add(0, i, j)).equals(comp)) {
                            LogHelper.info("not " + comp.toString() + " at " + event.getPos().add(i, j, 0).toString());
                            return;
                        } else {
                            toair.add(event.getPos().add(0, i, j));
                        }
                    }
                }
            } else if (i1 == EnumFacing.UP || i1 == EnumFacing.DOWN) {
                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        if (i == 0 && j == 0) {
                            comp = Blocks.JUKEBOX.getDefaultState();
                        } else {
                            comp = Blocks.OBSIDIAN.getDefaultState();
                        }
                        if (!world.getBlockState(event.getPos().add(j, 0, i)).equals(comp)) {
                            LogHelper.info("not " + comp.toString() + " at " + event.getPos().add(i, j, 0).toString());
                            return;
                        } else {
                            toair.add(event.getPos().add(j, 0, i));
                        }
                    }
                }
            }
            for (BlockPos pos : toair) {
                world.setBlockToAir(pos);
                world.addWeatherEffect(new EntityLightningBolt(world, pos.getX() + 0.5f, pos.getY(), pos.getZ() + 0.5f, world.rand.nextBoolean()));
            }
            event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, new ItemStack(FeatureTool.RYTHMIC_PICKAXE, 1));
        }
    }
}

