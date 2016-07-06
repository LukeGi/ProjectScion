package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.IProvideEvent;
import com.github.projectscion.common.util.InventoryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.function.BiFunction;

public class ItemChainsaw extends Item implements IProvideEvent {

    public ItemChainsaw(String type) {

        super();
        setCreativeTab(CreativeTabs.TOOLS);
        setRegistryName(new ResourceLocation(ModInfo.MOD_ID, "chainsaw_" + type));
        setUnlocalizedName(getRegistryName().toString());
        setMaxDamage(875);
        setMaxStackSize(1);
    }

    public static boolean breakWood(World world, EntityPlayer player, BlockPos pos, IBlockState state) {

        if (state.getBlock().isWood(world, pos)) {
            List<BlockPos> wood = getSortedList(pos, world, (pos1, world1) -> world1.getBlockState(pos1).getBlock().isWood(world1, pos1));
            if (!wood.isEmpty()) {
                if (player.isSneaking()) {
                    for (int i = 0; i < 10 && !wood.isEmpty(); i++) {
                        BlockPos currentPos = wood.get(0);
                        wood.remove(0);
                        InventoryHelper.breakBlockIntoPlayerInv(world, currentPos, player.getHeldItemMainhand(), player);
                        player.getHeldItemMainhand().damageItem(1, player);
                    }
                    return true;
                } else {
                    BlockPos currentPos = wood.get(0);
                    wood.remove(0);
                    InventoryHelper.breakBlockIntoPlayerInv(world, currentPos, player.getHeldItemMainhand(), player);
                    player.getHeldItemMainhand().damageItem(1, player);
                    return true;
                }
            }
        }
        return false;
    }

    public static List<BlockPos> getSortedList(BlockPos start, World world, BiFunction<BlockPos, World, Boolean> valid) {
        if (valid.apply(start, world)) {
            List<BlockPos> returner = new ArrayList<>();
            Stack<BlockPos> toVisit = new Stack<>();
            toVisit.push(start);
            while (!toVisit.isEmpty()) {
                BlockPos current = toVisit.pop();
                returner.add(current);
                for (int i = -4; i < 5; i++) {
                    for (int j = -4; j < 5; j++) {
                        for (int k = -4; k < 5; k++) {
                            BlockPos element = current.add(i, j, k);
                            if (!returner.contains(element) && !toVisit.contains(element) && valid.apply(element, world)) {
                                toVisit.push(element);
                            }
                        }
                    }
                }
                if (returner.size() > 512) { //TODO: make this number configurable
                    return null;
                }
            }
            returner.sort((a, b) -> {
                if (a.distanceSq(start) < b.distanceSq(start)) {
                    return 1;
                } else if (a.distanceSq(start) == b.distanceSq(start)) {
                    return 0;
                } else if (a.distanceSq(start) > b.distanceSq(start))
                    return -1;
                return 0;
            });
            return returner;
        }
        return null;
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {

        if (player.worldObj.getBlockState(pos).getBlock().isWood(player.worldObj, pos)) {
            return true;
        } else {
            return super.onBlockStartBreak(itemstack, pos, player);
        }
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.LEAVES ? super.getStrVsBlock(stack, state) : 8.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return material == Material.LEAVES || material == Material.WOOD;
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void startBreak(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        if (event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() == FeatureTool.CHAINSAW) {
            if (event.getEntityPlayer().worldObj.getBlockState(event.getPos()).getBlock().isWood(event.getEntityPlayer().worldObj, event.getPos())) {
                if (event.getEntityPlayer().isSneaking()) {
                    event.setNewSpeed(event.getOriginalSpeed() * 0.2F);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakBlock(BlockEvent.BreakEvent event) {

        if (event.getPlayer().getHeldItemMainhand() != null && event.getPlayer().getHeldItemMainhand().getItem() == FeatureTool.CHAINSAW) {
            event.setCanceled(breakWood(event.getWorld(), event.getPlayer(), event.getPos(), event.getState()));
        }
    }
}
