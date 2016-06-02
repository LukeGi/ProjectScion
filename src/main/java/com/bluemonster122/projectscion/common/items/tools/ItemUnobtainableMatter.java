package com.bluemonster122.projectscion.common.items.tools;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.blocks.ModBlocks;
import com.bluemonster122.projectscion.common.items.ItemBase;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import com.bluemonster122.projectscion.common.util.IProvideRecipe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class ItemUnobtainableMatter extends ItemBase implements IProvideEvent, IProvideRecipe {

    public ItemUnobtainableMatter() {

        super("unobtainable_matter");
        setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        setInternalName("unobtainable_matter");
    }

    @SubscribeEvent
    public void onCraftHandle(PlayerInteractEvent.RightClickBlock event) {

        EntityPlayer player = event.getEntityPlayer();
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        if (player.getHeldItemMainhand() != null && player.getHeldItemMainhand().getItem() == Items.dragon_breath && player.getHeldItemMainhand().stackSize == 1 && world.getBlockState(pos).getBlock() == Blocks.beacon) {
            BlockPos[] corners = new BlockPos[]{pos.north().west(), pos.north().east(), pos.south().west(), pos.south().east(),};
            BlockPos[] sides = new BlockPos[]{pos.north(), pos.west(), pos.east(), pos.south()};
            int numberOfPRIS = 0, numberOfNB = 0;
            for (BlockPos corner : corners) {
                IBlockState state = world.getBlockState(corner);
                if (state.getBlock() == Blocks.prismarine && Blocks.prismarine.getMetaFromState(state) == 2) {
                    numberOfPRIS++;
                }
                if (state.getBlock() == Blocks.nether_brick) {
                    numberOfNB++;
                }
            }
            if (numberOfNB != 2 || numberOfPRIS != 2) {
                return;
            }
            for (BlockPos side : sides) {
                IBlockState state = world.getBlockState(side);
                if (state.getBlock() != Blocks.diamond_block) {
                    return;
                }
            }
            for (BlockPos p : sides) {
                world.setBlockToAir(p);
            }
            for (BlockPos p : corners) {
                world.setBlockToAir(p);
            }
            world.setBlockToAir(pos);
            for (int j = 0; j < 19; j++) {
                world.weatherEffects.add(new EntityLightningBolt(world, pos.getX(), pos.getY(), pos.getZ(), true));
            }
            event.getEntityPlayer().setHeldItem(EnumHand.MAIN_HAND, ModItems.UNOBTAINABLE_MATTER.getStack());
        }
    }

    @Override
    public void RegisterRecipes() {

        GameRegistry.addShapelessRecipe(ModItems.UNOBTAINABLE_MATTER.getStack(9), ModBlocks.UNOBTAINABLE_MATTER_BLOCK.getStack(1));
    }
}
