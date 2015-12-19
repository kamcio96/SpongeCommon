/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.common.mixin.core.block;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableLogAxisData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableTreeData;
import org.spongepowered.api.data.type.LogAxis;
import org.spongepowered.api.data.type.TreeType;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeLogAxisData;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeTreeData;
import org.spongepowered.common.data.util.TreeTypeResolver;

import java.util.List;
import java.util.Optional;

@NonnullByDefault
@Mixin(BlockLog.class)
public abstract class MixinBlockLog extends MixinBlock {

    protected ImmutableTreeData getTreeData(IBlockState blockState) {
        BlockPlanks.EnumType type;
        if(blockState.getBlock() instanceof BlockOldLog) {
            type = blockState.getValue(BlockOldLog.VARIANT);
        } else if(blockState.getBlock() instanceof BlockNewLog) {
            type = blockState.getValue(BlockNewLog.VARIANT);
        } else {
            type = BlockPlanks.EnumType.OAK;
        }

        final TreeType treeType = TreeTypeResolver.getFor(type);

        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeTreeData.class, treeType);
    }

    protected ImmutableLogAxisData getLogAxisData(IBlockState blockState) {
        final LogAxis logAxis = (LogAxis) (Object) blockState.getValue(BlockLog.LOG_AXIS);
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeLogAxisData.class, logAxis);
    }


    @Override
    public boolean supports(Class<? extends ImmutableDataManipulator<?, ?>> immutable) {
        return ImmutableTreeData.class.isAssignableFrom(immutable) || ImmutableLogAxisData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> getStateWithData(IBlockState blockState, ImmutableDataManipulator<?, ?> manipulator) {
        if (manipulator instanceof ImmutableTreeData) {
            final TreeType treeType = ((ImmutableTreeData) manipulator).type().get();
            final BlockPlanks.EnumType type = TreeTypeResolver.getFor(treeType);
            if (blockState.getBlock() instanceof BlockOldLog) {
                if (treeType.equals(TreeTypes.OAK) ||
                    treeType.equals(TreeTypes.BIRCH) ||
                    treeType.equals(TreeTypes.SPRUCE) ||
                    treeType.equals(TreeTypes.JUNGLE)) {
                    return Optional.of((BlockState) blockState.withProperty(BlockOldLog.VARIANT, type));
                }
            } else if (blockState.getBlock() instanceof BlockNewLog) {
                if (treeType.equals(TreeTypes.ACACIA) || treeType.equals(TreeTypes.DARK_OAK)) {
                    return Optional.of((BlockState) blockState.withProperty(BlockNewLog.VARIANT, type));
                }
            }
            return Optional.empty();
        } else if (manipulator instanceof ImmutableLogAxisData) {
            final LogAxis logAxis = ((ImmutableLogAxisData) manipulator).type().get();
            return Optional.of((BlockState) blockState.withProperty(BlockLog.LOG_AXIS, (BlockLog.EnumAxis) (Object) logAxis));
        }
        return super.getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> getStateWithValue(IBlockState blockState, Key<? extends BaseValue<E>> key, E value) {
        if (key.equals(Keys.TREE_TYPE)) {
            final TreeType treeType = (TreeType) value;
            final BlockPlanks.EnumType type = TreeTypeResolver.getFor(treeType);
            if (blockState.getBlock() instanceof BlockOldLog) {
                if (treeType.equals(TreeTypes.OAK) ||
                    treeType.equals(TreeTypes.BIRCH) ||
                    treeType.equals(TreeTypes.SPRUCE) ||
                    treeType.equals(TreeTypes.JUNGLE)) {
                    return Optional.of((BlockState) blockState.withProperty(BlockOldLog.VARIANT, type));
                }
            } else if (blockState.getBlock() instanceof BlockNewLog) {
                if (treeType.equals(TreeTypes.ACACIA) || treeType.equals(TreeTypes.DARK_OAK)) {
                    return Optional.of((BlockState) blockState.withProperty(BlockNewLog.VARIANT, type));
                }
            }
            return Optional.empty();
        } else if (key.equals(Keys.LOG_AXIS)) {
            return Optional.of((BlockState) blockState.withProperty(BlockLog.LOG_AXIS, (BlockLog.EnumAxis) value));
        }
        return super.getStateWithValue(blockState, key, value);
    }

    @Override
    public List<ImmutableDataManipulator<?, ?>> getManipulators(IBlockState blockState) {
        return ImmutableList.<ImmutableDataManipulator<?, ?>>of(getTreeData(blockState),
                                                                getLogAxisData(blockState));
    }
}
