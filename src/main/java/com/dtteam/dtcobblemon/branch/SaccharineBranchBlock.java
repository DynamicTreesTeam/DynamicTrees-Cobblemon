package com.dtteam.dtcobblemon.branch;

import com.bedrockk.molang.runtime.value.DoubleValue;
import com.cobblemon.mod.common.entity.MoLangScriptingEntity;
import com.dtteam.dtcobblemon.tree.SaccharineFamily;
import com.dtteam.dynamictrees.block.branch.BasicBranchBlock;
import com.dtteam.dynamictrees.block.branch.BranchBlock;
import com.dtteam.dynamictrees.tree.TreeHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class SaccharineBranchBlock extends BasicBranchBlock {

    public SaccharineBranchBlock(ResourceLocation name, Properties blockProperties) {
        super(name, blockProperties);
    }

    public static void changeLogType(LevelAccessor level, BlockPos pos, BranchBlock branch, int radius, Direction honeyDirection) {
        changeLogType(level, pos, branch, radius, honeyDirection, null, null);
    }

    public static void changeLogType(LevelAccessor level, BlockPos pos, BranchBlock branch, int radius, Direction honeyDirection, @Nullable Player player, @Nullable ItemStack itemStack) {
        if (branch instanceof SlatheredSaccharineBranchBlock slatheredBranch){
            slatheredBranch.setRadius(level, pos, radius, 3, honeyDirection);
        } else {
            branch.setRadius(level, pos, radius, null, 3);
        }

        if (player == null || player.hasInfiniteMaterials() || itemStack == null) return;

        itemStack.consume(1, player);
        ItemStack glassBottle = new ItemStack(Items.GLASS_BOTTLE);
        if (!player.addItem(glassBottle)) player.drop(glassBottle, false);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        ItemStack itemStack = player.getItemInHand(hand);
        Direction blockFace = hitResult.getDirection();

        if (level.isClientSide()
                || blockFace == Direction.UP || blockFace == Direction.DOWN
                || !itemStack.is(Items.HONEY_BOTTLE)
                || level.getFluidState(pos.relative(blockFace)).getAmount() > 3
                || state.getValue(WATERLOGGED)
                || TreeHelper.isBranch(level.getBlockState(pos.relative(blockFace)))
                || !(getFamily() instanceof SaccharineFamily saccharineFamily))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        int rad = TreeHelper.getRadius(level, pos);
        Optional<BranchBlock> branchOpt = saccharineFamily.getSlatheredBranch();
        if (branchOpt.isEmpty()
                || !(branchOpt.get() instanceof SlatheredSaccharineBranchBlock branch)
                || rad <= 0 || rad < saccharineFamily.getMinimumRadiusForSlathering())
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);

        changeLogType(level, pos, branch, rad, blockFace, player, itemStack);
        level.playSound(null, pos, SoundEvents.HONEY_BLOCK_PLACE, SoundSource.BLOCKS);

        return ItemInteractionResult.SUCCESS;
    }

    @Override @NotNull
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context instanceof EntityCollisionContext eContext && TreeHelper.getRadius(level, pos) < 8){
            Entity entity = eContext.getEntity();
            if (entity instanceof MoLangScriptingEntity moEntity && moEntity.getConfig().getMap().getOrDefault("can_path_through_sacc_leaves", DoubleValue.ZERO).asDouble() == 1.0){
                return Shapes.empty();
            }
        }
        return super.getCollisionShape(state, level, pos, context);
    }

}

//https://gitlab.com/cable-mc/cobblemon/-/blob/main/common/src/main/kotlin/com/cobblemon/mod/common/block/SaccharineLogBlock.kt