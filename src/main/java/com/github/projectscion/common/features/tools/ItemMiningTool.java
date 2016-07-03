package com.github.projectscion.common.features.tools;

import com.github.projectscion.ModInfo;
import com.github.projectscion.common.util.IProvideEvent;
import com.github.projectscion.common.util.InventoryHelper;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketBlockBreakAnim;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ItemMiningTool extends Item implements IProvideEvent {
    public ItemMiningTool(String type) {
        super();
        setCreativeTab(CreativeTabs.TOOLS);
        setRegistryName(new ResourceLocation(ModInfo.MOD_ID, "mining_tool_" + type));
        setUnlocalizedName(getRegistryName().toString());
        setMaxDamage((type == "iron" ? 128 : type == "diamond" ? 512 : 0));
        setMaxStackSize(1);
    }

    public static EnumActionResult placeTorch(World world, BlockPos pos, EnumFacing facing, EntityPlayer player) {
        boolean flag = false;
        int slot = -1;
        if (player.inventory.hasItemStack(new ItemStack(Blocks.TORCH))) {
            IItemHandler itemHandler = player.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
                    EnumFacing.DOWN);
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                ItemStack stack = player.inventory.getStackInSlot(i);
                if (stack == null) {
                    continue;
                }
                if (stack.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
                    ItemStack torch = itemHandler.extractItem(i, 1, false);
                    if (torch.getItem() == Item.getItemFromBlock(Blocks.TORCH)) {
                        flag = true;
                    }
                    break;
                }
            }
            if (flag && Blocks.TORCH.canPlaceBlockAt(world, pos.offset(facing))) {
                world.setBlockState(pos.offset(facing),
                        Blocks.TORCH.getDefaultState().withProperty(BlockTorch.FACING, facing));
                return EnumActionResult.SUCCESS;
            } else if (flag && slot != -1) {
                itemHandler.insertItem(slot, new ItemStack(Blocks.TORCH, 1), false);
                return EnumActionResult.FAIL;
            }
        }
        return EnumActionResult.PASS;
    }

    public static boolean handleBlockDestroyed(ItemStack stack, World worldIn, BlockPos pos,
                                               EntityLivingBase entityLiving) {

        if (!(entityLiving instanceof EntityPlayer)) {
            return false;
        }
        if (entityLiving.isSneaking()) {
            stack.damageItem(1, entityLiving);
            return !InventoryHelper.breakBlockIntoPlayerInv(worldIn, pos, stack, (EntityPlayer) entityLiving);
        }
        BlockPos[] toBreak = null;
        EnumFacing facing = entityLiving.rayTrace(10, 1F).sideHit;
        if (facing.getAxis().equals(EnumFacing.Axis.Z)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.east(), pos.west(), pos.up().west(),
                    pos.up().east(), pos.down().west(), pos.down().east()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.X)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.north(), pos.south(), pos.up().north(),
                    pos.up().south(), pos.down().north(), pos.down().south()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.Y)) {
            toBreak = new BlockPos[]{pos, pos.north(), pos.east(), pos.south(), pos.west(), pos.north().east(),
                    pos.south().east(), pos.south().west(), pos.north().west()};
        }
        if (toBreak == null) {
            stack.damageItem(1, entityLiving);
            return !InventoryHelper.breakBlockIntoPlayerInv(worldIn, pos, stack, (EntityPlayer) entityLiving);
        }
        boolean flag = false;
        for (BlockPos blockPos : toBreak) {
            flag |= InventoryHelper.breakBlockIntoPlayerInv(worldIn, blockPos, stack, (EntityPlayer) entityLiving);
        }
        if (flag) {
            stack.attemptDamageItem(1, worldIn.rand);
            if (stack.getItemDamage() == stack.getMaxDamage()) {
                entityLiving.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(FeatureTool.TOOL_HANDLE, 1));
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos,
                                    EntityLivingBase entityLiving) {
        return handleBlockDestroyed(stack, worldIn, pos, entityLiving);
    }

    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        // get blocks, send packet saying breaking them.
        BlockPos[] toBreak = null;
        EnumFacing facing = player.rayTrace(10, 1F).sideHit;
        if (facing.getAxis().equals(EnumFacing.Axis.Z)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.east(), pos.west(), pos.up().west(),
                    pos.up().east(), pos.down().west(), pos.down().east()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.X)) {
            toBreak = new BlockPos[]{pos, pos.up(), pos.down(), pos.north(), pos.south(), pos.up().north(),
                    pos.up().south(), pos.down().north(), pos.down().south()};
        } else if (facing.getAxis().equals(EnumFacing.Axis.Y)) {
            toBreak = new BlockPos[]{pos, pos.north(), pos.east(), pos.south(), pos.west(), pos.north().east(),
                    pos.south().east(), pos.south().west(), pos.north().west()};
        }
        if (!player.worldObj.isRemote) {
            for (BlockPos npos : toBreak) {
                SPacketBlockBreakAnim packet = new SPacketBlockBreakAnim(player.getEntityId(), npos, 15);
                ((EntityPlayerMP) player).connection.sendPacket(packet);
            }
        }
        return super.onBlockStartBreak(itemstack, pos, player);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
                                      EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return facing == EnumFacing.DOWN ? EnumActionResult.PASS : placeTorch(worldIn, pos, facing, playerIn);
    }

    public float getStrVsBlock(ItemStack stack, IBlockState state) {

        Material material = state.getMaterial();
        if (stack.getItem().getUnlocalizedName().contains("iron")) {
            return state.getBlock() instanceof BlockObsidian
                    || (material != Material.GRASS && material != Material.GROUND && material != Material.IRON
                    && material != Material.ANVIL && material != Material.ROCK) ? 0F : 8.0F / 5.0F;
        } else {
            return material != Material.GRASS && material != Material.GROUND && material != Material.IRON
                    && material != Material.ANVIL && material != Material.ROCK ? 0F : 8.0F / 4.0F;
        }
    }

    @Override
    public boolean canHarvestBlock(IBlockState blockIn) {

        Material material = blockIn.getMaterial();
        return material == Material.GROUND || material == Material.GRASS || material == Material.ROCK
                || (material == Material.IRON || material == Material.ANVIL);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass) {

        return stack.getItem().getUnlocalizedName().contains("iron") ? 2
                : stack.getItem().getUnlocalizedName().contains("diamond") ? 3 : -1;
    }

    @SubscribeEvent
    public void startbreak(PlayerEvent.BreakSpeed event) {
        ItemStack item = event.getEntityPlayer().getHeldItemMainhand();
        if (item != null && item.getItem() != null && item.getItem() instanceof ItemMiningTool) {
            List<BlockPos> blockPoses = new ArrayList<>();
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        blockPoses.add(event.getPos().add(i, j, k));
                    }
                }
            }
            int average = 0;
            World world = event.getEntityPlayer().getEntityWorld();
            for (BlockPos pos : blockPoses) {
                average += world.getBlockState(pos).getBlockHardness(world, pos) * 5;
            }
            average /= blockPoses.size();
            event.setNewSpeed(average);
        }
    }
}
