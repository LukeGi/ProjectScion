package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import com.bluemonster122.projectscion.common.util.InventoryHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemIronChainsaw extends ItemBase implements IProvideEvent{

    public ItemIronChainsaw() {

        super("ironchainsaw");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("ironchainsaw");
        setMaxStackSize(1);
        setMaxDamage(875);
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
        return material != Material.wood && material != Material.leaves ? super.getStrVsBlock(stack, state) : 8.0F;
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return material == Material.leaves || material == Material.wood;
    }


    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void startBreak(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        if (event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() == ModItems.IRON_CHAINSAW.getItem()) {
            if (event.getEntityPlayer().worldObj.getBlockState(event.getPos()).getBlock().isWood(event.getEntityPlayer().worldObj, event.getPos())) {
                if (event.getEntityPlayer().isSneaking()) {
                    event.setNewSpeed(event.getOriginalSpeed() * 0.2F);
                }
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void breakBlock(BlockEvent.BreakEvent event) {

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        IBlockState state = event.getState();
        if (event.getPlayer().getHeldItemMainhand() != null && event.getPlayer().getHeldItemMainhand().getItem() == ModItems.IRON_CHAINSAW.getItem()) {
            if (state.getBlock().isWood(world, pos)) { // ALL CONDITIONS MET BY HERE
                List<BlockPos> wood = new ArrayList<>();
                Stack<BlockPos> toVisit = new Stack<>();
                toVisit.push(pos);
                while (!toVisit.isEmpty()) {
                    BlockPos current = toVisit.pop();
                    wood.add(current);
                    for (int i = -4; i < 5; i++) {
                        for (int j = -4; j < 5; j++) {
                            for (int k = -4; k < 5; k++) {
                                BlockPos element = current.add(i, j, k);
                                IBlockState elementState = world.getBlockState(element);
                                if (!wood.contains(element) && !toVisit.contains(element) && elementState.getBlock().isWood(world, element)) {
                                    toVisit.push(element);
                                }
                            }
                        }
                    }
                }
                wood.sort((a, b) -> {
                    if (a.distanceSq(pos) < b.distanceSq(pos)) {
                        return 1;
                    } else if (a.distanceSq(pos) == b.distanceSq(pos)) {
                        return 0;
                    } else if (a.distanceSq(pos) > b.distanceSq(pos))
                        return -1;
                    return 0;
                });
                if (event.getPlayer().isSneaking()) {
                    for (int i = 0; i < 10 && !wood.isEmpty(); i++) {
                        BlockPos currentPos = wood.get(0);
                        wood.remove(0);
                        InventoryHelper.breakBlockIntoPlayerInv(world, currentPos, event.getPlayer().getHeldItemMainhand(), event.getPlayer());
                        event.getPlayer().getHeldItemMainhand().damageItem(1, event.getPlayer());
                    }
                    event.setCanceled(true);
                } else {
                    BlockPos currentPos = wood.get(0);
                    wood.remove(0);
                    InventoryHelper.breakBlockIntoPlayerInv(world, currentPos, event.getPlayer().getHeldItemMainhand(), event.getPlayer());
                    event.getPlayer().getHeldItemMainhand().damageItem(1, event.getPlayer());
                    event.setCanceled(true);
                }
            }
        }
    }
}
