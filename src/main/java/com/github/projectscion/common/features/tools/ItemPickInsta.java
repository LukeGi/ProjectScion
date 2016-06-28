package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.IProvideEvent;
import com.github.projectscion.common.util.LogHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.List;

/**
 * Created by blue on 26/06/16.
 */
public class ItemPickInsta extends ItemPickaxe implements IProvideEvent {
    public ItemPickInsta() {
        super(ToolMaterial.DIAMOND);
        setRegistryName(ModInfo.MOD_ID, "instant_pick");
        setUnlocalizedName(getRegistryName().getResourcePath());
        setCreativeTab(CreativeTabs.FOOD);
    }

    @SubscribeEvent
    public void handler(PlayerEvent.HarvestCheck event) {
        LogHelper.info("boom");
        if (event.getEntityPlayer().getHeldItemMainhand() != null && event.getEntityPlayer().getHeldItemMainhand().getItem() != null && event.getEntityPlayer().getHeldItemMainhand().equals(FeatureTool.instant_pickaxe) && event.canHarvest()) {
            IBlockState state = event.getTargetBlock();
            World world = event.getEntityPlayer().getEntityWorld();
            BlockPos pos = event.getEntity().rayTrace(7, 0).getBlockPos();
            List<ItemStack> drops = state.getBlock().getDrops(world, pos, world.getBlockState(pos), 0);
            drops.forEach(stack -> ItemHandlerHelper.giveItemToPlayer(event.getEntityPlayer(), stack));
            world.destroyBlock(pos, false);
            event.getEntityPlayer().getHeldItemMainhand().damageItem(1, event.getEntityLiving());
            event.setCanceled(true);
        }
    }
}

