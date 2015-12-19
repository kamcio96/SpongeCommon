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
import net.minecraft.block.BlockWall;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableConnectedDirectionData;
import org.spongepowered.api.data.manipulator.immutable.block.ImmutableWallData;
import org.spongepowered.api.data.type.WallType;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.util.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.common.data.ImmutableDataCachingUtil;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeConnectedDirectionData;
import org.spongepowered.common.data.manipulator.immutable.block.ImmutableSpongeWallData;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Mixin(BlockWall.class)
public abstract class MixinBlockWall extends MixinBlock {

    @Override
    public ImmutableList<ImmutableDataManipulator<?, ?>> getManipulators(IBlockState blockState) {
        return ImmutableList.<ImmutableDataManipulator<?, ?>>of(getWallTypeFor(blockState), getConnectedDirectionData(blockState));
    }

    @Override
    public boolean supports(Class<? extends ImmutableDataManipulator<?, ?>> immutable) {
        return ImmutableWallData.class.isAssignableFrom(immutable) || ImmutableConnectedDirectionData.class.isAssignableFrom(immutable);
    }

    @Override
    public Optional<BlockState> getStateWithData(IBlockState blockState, ImmutableDataManipulator<?, ?> manipulator) {
        if (manipulator instanceof ImmutableWallData) {
            final BlockWall.EnumType wallType = (BlockWall.EnumType) (Object) ((ImmutableWallData) manipulator).type().get();
            return Optional.of((BlockState) blockState.withProperty(BlockWall.VARIANT, wallType));
        }
        if (manipulator instanceof ImmutableConnectedDirectionData) {
            return Optional.of((BlockState) blockState);
        }
        return super.getStateWithData(blockState, manipulator);
    }

    @Override
    public <E> Optional<BlockState> getStateWithValue(IBlockState blockState, Key<? extends BaseValue<E>> key, E value) {
        if (key.equals(Keys.WALL_TYPE)) {
            final BlockWall.EnumType wallType = (BlockWall.EnumType) value;
            return Optional.of((BlockState) blockState.withProperty(BlockWall.VARIANT, wallType));
        }
        if (key.equals(Keys.CONNECTED_DIRECTIONS) || key.equals(Keys.CONNECTED_EAST) || key.equals(Keys.CONNECTED_NORTH)
                || key.equals(Keys.CONNECTED_SOUTH) || key.equals(Keys.CONNECTED_WEST)) {
            return Optional.of((BlockState) blockState);
        }
        return super.getStateWithValue(blockState, key, value);
    }

    private ImmutableWallData getWallTypeFor(IBlockState blockState) {
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeWallData.class, (WallType) (Object) blockState.getValue(BlockWall.VARIANT));
    }

    @Override
    public boolean forceUpdateBlockState() {
        return true;
    }

    private ImmutableConnectedDirectionData getConnectedDirectionData(IBlockState blockState) {
        final Set<Direction> directions = new HashSet<>();
        final Boolean north = (Boolean) blockState.getValue(BlockWall.NORTH);
        final Boolean east = (Boolean) blockState.getValue(BlockWall.EAST);
        final Boolean west = (Boolean) blockState.getValue(BlockWall.WEST);
        final Boolean south = (Boolean) blockState.getValue(BlockWall.SOUTH);
        final Boolean up = (Boolean) blockState.getValue(BlockWall.UP);
        if (north) {
            directions.add(Direction.NORTH);
        }
        if (south) {
            directions.add(Direction.SOUTH);
        }
        if (west) {
            directions.add(Direction.WEST);
        }
        if (east) {
            directions.add(Direction.EAST);
        }
        if (up) {
            directions.add(Direction.UP);
        }
        return ImmutableDataCachingUtil.getManipulator(ImmutableSpongeConnectedDirectionData.class, directions);
    }
}
