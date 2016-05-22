package com.bluemonster122.projectscion.common.blocks;

import com.bluemonster122.projectscion.ProjectScionCreativeTabs;
import com.bluemonster122.projectscion.common.items.ModItems;
import com.bluemonster122.projectscion.common.items.tools.ItemToolHandle;
import com.bluemonster122.projectscion.common.tileentities.TileEntityAreaDefinition;
import com.bluemonster122.projectscion.common.util.IProvideEvent;
import com.bluemonster122.projectscion.common.util.LogHelper;
import com.bluemonster122.projectscion.common.util.TileHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by Blue <boo122333@gmail.com>.
 */
public class BlockAreaDefinition extends BlockTileBase implements IProvideEvent {

    public BlockAreaDefinition() {

        super(Material.cloth, "area_definition");
        this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setTileEntity(TileEntityAreaDefinition.class);
        this.setCreativeTab(ProjectScionCreativeTabs.tabGeneral);
        this.setInternalName("area_definition");
    }

    @Override
    protected BlockStateContainer createBlockState() {

        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {

        return this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {

        return 0;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {

        TileEntityAreaDefinition tileEntity = TileHelper.getTileEntity(worldIn, pos, TileEntityAreaDefinition.class);
        if (tileEntity != null) {
            return state.withProperty(FACING, tileEntity.getForward());
        }
        return state.withProperty(FACING, EnumFacing.NORTH);
    }

    @SubscribeEvent
    public void onCraftHandle(PlayerInteractEvent.RightClickBlock event) {

        ItemStack heldStack = event.getEntityPlayer().getHeldItemMainhand();
        if (heldStack != null && heldStack.stackSize == 1) {
            if (heldStack.getItem() == Items.iron_ingot) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.stone) {
                    ItemToolHandle.changeItemTo(event.getWorld(), event.getPos(), heldStack, ModBlocks.AREA_DEFINITION.getStack(1), 0);
                    event.setCanceled(true);
                }
            }
            if (heldStack.getItem() == new ItemStack(Blocks.iron_block).getItem()) {
                if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.obsidian) {
                    ItemToolHandle.changeItemTo(event.getWorld(), event.getPos(), heldStack, ModBlocks.AREA_DEFINITION.getStack(16), 0);
                    event.setCanceled(true);
                }
            }
        }
    }
}
