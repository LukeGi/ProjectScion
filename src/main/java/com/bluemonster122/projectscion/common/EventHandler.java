package com.bluemonster122.projectscion.common;

import com.bluemonster122.projectscion.common.items.Items;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class EventHandler {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void startBreak(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {

        if (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.IRON_CHAINSAW.getItem()) {
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
        if (event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.IRON_CHAINSAW.getItem()) {
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
                        world.destroyBlock(wood.get(0), true);
                        event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).damageItem(1, event.getPlayer());
                        wood.remove(0);
                    }
                    event.setCanceled(true);
                } else {
                    world.destroyBlock(wood.get(0), true);
                    event.getPlayer().getHeldItem(EnumHand.MAIN_HAND).damageItem(1, event.getPlayer());
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void login(PlayerEvent.PlayerLoggedInEvent event) {

        event.player.addChatComponentMessage(new TextComponentString(TextFormatting.DARK_AQUA + "Welcome to the world!"));
    }
}
