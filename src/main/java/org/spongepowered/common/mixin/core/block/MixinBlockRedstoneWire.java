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
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableRedstonePoweredData;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeConnectedDirectionData;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeRedstonePoweredData;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(BlockRedstoneWire.class)
public abstract class MixinBlockRedstoneWire extends MixinBlock {

    @Override
    public ImmutableList<ImmutableDataManipulator<?, ?>> getManipulators(IBlockState blockState) {
        return ImmutableList.<ImmutableDataManipulator<?, ?>>of(getPowerFor(blockState), getConnectedDirectionData(blockState));
    }

    @Override
    public boolean supports(Class<? extends ImmutableDataManipulator<?, ?>> immutable) {
        return ImmutableRedstonePoweredData.class.isAssignableFrom(immutable) || ImmutableConnectedDirectionData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> getStateWithData(IBlockState blockState, ImmutableDataManipulator<?, ?> manipulator) {
        if (manipulator instanceof ImmutableRedstonePoweredData) {
            return Optional.of((BlockState) blockState);
        }
        if (manipulator instanceof ImmutableConnectedDirectionData) {
            return Optional.of((BlockState) blockState);
        }
        return super.getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> getStateWithValue(IBlockState blockState, Key<? extends BaseValue<E>> key, E value) {
        if (key.equals(Keys.POWER)) {
            return Optional.of((BlockState) blockState);
        }
        if (key.equals(Keys.CONNECTED_DIRECTIONS) || key.equals(Keys.CONNECTED_EAST) || key.equals(Keys.CONNECTED_NORTH)
                || key.equals(Keys.CONNECTED_SOUTH) || key.equals(Keys.CONNECTED_WEST)) {
            return Optional.of((BlockState) blockState);
        }
        return super.getStateWithValue(blockState, key, value);
    }

    @Override
    public boolean forceUpdateBlockState() {
        return true;
    }

    private ImmutableRedstonePoweredData getPowerFor(IBlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeRedstonePoweredData.class,
                (Integer) blockState.getValue(BlockRedstoneWire.POWER));
    }

    private ImmutableConnectedDirectionData getConnectedDirectionData(IBlockState blockState) {
        final Set<Direction> directions = new HashSet<>();
        final IStringSerializable north = blockState.getValue(BlockRedstoneWire.NORTH);
        final IStringSerializable east = blockState.getValue(BlockRedstoneWire.EAST);
        final IStringSerializable west = blockState.getValue(BlockRedstoneWire.WEST);
        final IStringSerializable south = blockState.getValue(BlockRedstoneWire.SOUTH);
        if (!north.getName().matches("none")) {
            directions.add(Direction.NORTH);
        }
        if (!east.getName().matches("none")) {
            directions.add(Direction.EAST);
        }
        if (!west.getName().matches("none")) {
            directions.add(Direction.WEST);
        }
        if (!south.getName().matches("none")) {
            directions.add(Direction.SOUTH);
        }
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeConnectedDirectionData.class, directions);
    }
}
