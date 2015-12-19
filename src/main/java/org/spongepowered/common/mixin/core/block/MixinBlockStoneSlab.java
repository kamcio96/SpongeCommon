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
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutablePortionData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableSeamlessData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableSlabData;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.SlabType;
import org.spongepowered.api.data.type.SlabTypes;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongePortionData;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeSlabData;

import java.util.Optional;

@Mixin(value = {BlockStoneSlabNew.class, BlockStoneSlab.class})
public abstract class MixinBlockStoneSlab extends MixinBlock {

    @Override
    public ImmutableList<ImmutableDataManipulator<?, ?>> getManipulators(IBlockState blockState) {
        return ImmutableList.<ImmutableDataManipulator<?, ?>>of(getSlabTypeFor(blockState), getPortionTypeFor(blockState));
    }

    @Override
    public boolean supports(Class<? extends ImmutableDataManipulator<?, ?>> immutable) {
        return ImmutableSlabData.class.isAssignableFrom(immutable) || ImmutablePortionData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> getStateWithData(IBlockState blockState, ImmutableDataManipulator<?, ?> manipulator) {
        if (manipulator instanceof ImmutableSlabData) {
            SlabType type = ((ImmutableSlabData) manipulator).type().get();
            if (blockState.getBlock() instanceof BlockStoneSlab) {
                if (!type.equals(SlabTypes.RED_SAND)) {
                    final BlockStoneSlab.EnumType slabType = (BlockStoneSlab.EnumType) (Object) type;
                    return Optional.of((BlockState) blockState.withProperty(BlockStoneSlab.VARIANT, slabType));
                }
            } else if (blockState.getBlock() instanceof BlockStoneSlabNew) {
                if (type.equals(SlabTypes.RED_SAND)) {
                    final BlockStoneSlabNew.EnumType slabType = (BlockStoneSlabNew.EnumType) (Object) type;
                    return Optional.of((BlockState) blockState.withProperty(BlockStoneSlabNew.VARIANT, slabType));
                }
            }
            return Optional.empty();
        } else if (manipulator instanceof ImmutablePortionData) {
            final PortionType portionType = ((ImmutablePortionData) manipulator).type().get();
            return Optional.of((BlockState) blockState.withProperty(BlockSlab.HALF, (BlockSlab.EnumBlockHalf) (Object) portionType));
        }
        if (manipulator instanceof ImmutableSeamlessData) {
            final boolean seamless = ((ImmutableSeamlessData) manipulator).seamless().get();
            if (blockState.getBlock() instanceof BlockStoneSlab) {
                return Optional.of((BlockState) blockState.withProperty(BlockStoneSlab.SEAMLESS, seamless));
            }
            if (blockState.getBlock() instanceof BlockStoneSlabNew) {
                return Optional.of((BlockState) blockState.withProperty(BlockStoneSlabNew.SEAMLESS, seamless));
            }
        }
        return super.getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> getStateWithValue(IBlockState blockState, Key<? extends BaseValue<E>> key, E value) {
        if (key.equals(Keys.SLAB_TYPE)) {
            SlabType type = (SlabType) value;
            if (blockState.getBlock() instanceof BlockStoneSlab) {
                if (!type.equals(SlabTypes.RED_SAND)) {
                    final BlockStoneSlab.EnumType slabType = (BlockStoneSlab.EnumType) value;
                    return Optional.of((BlockState) blockState.withProperty(BlockStoneSlab.VARIANT, slabType));
                }
            } else if (blockState.getBlock() instanceof BlockStoneSlabNew) {
                if (type.equals(SlabTypes.RED_SAND)) {
                    final BlockStoneSlabNew.EnumType slabType = (BlockStoneSlabNew.EnumType) value;
                    return Optional.of((BlockState) blockState.withProperty(BlockStoneSlabNew.VARIANT, slabType));
                }
            }
            return Optional.empty();
        } else if (key.equals(Keys.PORTION_TYPE)) {
            return Optional.of((BlockState) blockState.withProperty(BlockSlab.HALF, (BlockSlab.EnumBlockHalf) value));
        }
        if (key.equals(Keys.SEAMLESS)) {
            final boolean seamless = (Boolean) value;
            if (blockState.getBlock() instanceof BlockStoneSlab) {
                return Optional.of((BlockState) blockState.withProperty(BlockStoneSlab.SEAMLESS, seamless));
            }
            if (blockState.getBlock() instanceof BlockStoneSlabNew) {
                return Optional.of((BlockState) blockState.withProperty(BlockStoneSlabNew.SEAMLESS, seamless));
            }
        }
        return super.getStateWithValue(blockState, key, value);
    }

    private ImmutableSlabData getSlabTypeFor(IBlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeSlabData.class,
                blockState.getBlock() instanceof BlockStoneSlab
                        ? (SlabType) (Object) blockState.getValue(BlockStoneSlab.VARIANT)
                        : blockState.getBlock() instanceof BlockStoneSlabNew
                                ? (SlabType) (Object) blockState.getValue(BlockStoneSlabNew.VARIANT)
                                : SlabTypes.COBBLESTONE);
    }

    private ImmutablePortionData getPortionTypeFor(IBlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongePortionData.class, blockState.getValue(BlockSlab.HALF));
    }
}
