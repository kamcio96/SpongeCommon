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
package org.spongepowered.common.mixin.core.world.gen.populators;

import com.google.common.base.Objects;
import com.google.common.base.Predicate;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.util.weighted.VariableAmount;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.populator.Ore;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WorldGenMinable.class)
public abstract class MixinWorldGenMinable extends WorldGenerator implements Ore {

    @Shadow private IBlockState oreBlock;
    @Shadow private int numberOfBlocks;
    @Shadow private Predicate<?> predicate;

    private VariableAmount size;
    private VariableAmount count;
    private VariableAmount height;

    @Inject(method = "<init>(Lnet/minecraft/block/state/IBlockState;ILcom/google/common/base/Predicate;)V", at = @At("RETURN") )
    public void onConstructed(IBlockState ore, int count, Predicate<IBlockState> condition, CallbackInfo ci) {
        this.size = VariableAmount.fixed(count);
        this.count = VariableAmount.fixed(16);
        this.height = VariableAmount.baseWithRandomAddition(0, 64);
    }

    @Override
    public void populate(Chunk chunk, Random random) {
        int n = this.count.getFlooredAmount(random);
        World world = (World) chunk.getWorld();
        BlockPos position = new BlockPos(chunk.getBlockMin().getX(), chunk.getBlockMin().getY(), chunk.getBlockMin().getZ());
        for (int i = 0; i < n; i++) {
            BlockPos pos = position.add(random.nextInt(16), this.height.getFlooredAmount(random), random.nextInt(16));
            this.numberOfBlocks = this.size.getFlooredAmount(random);
            generate(world, random, pos);
        }
    }

    @Override
    public BlockState getOreBlock() {
        return (BlockState) this.oreBlock;
    }

    @Override
    public void setOreBlock(BlockState block) {
        this.oreBlock = (IBlockState) block;
    }

    @Override
    public VariableAmount getDepositSize() {
        return this.size;
    }

    @Override
    public void setDepositSize(VariableAmount size) {
        this.size = size;
    }

    @Override
    public VariableAmount getDepositsPerChunk() {
        return this.count;
    }

    @Override
    public void setDepositsPerChunk(VariableAmount count) {
        this.count = count;
    }

    @Override
    public VariableAmount getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(VariableAmount height) {
        this.height = height;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Predicate<BlockState> getPlacementCondition() {
        return (Predicate<BlockState>) this.predicate;
    }

    @Override
    public void setPlacementCondition(Predicate<BlockState> condition) {
        this.predicate = condition;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Type", "Ore")
                .add("OreBlock", this.oreBlock)
                .add("Size", this.size)
                .add("PerChunk", this.count)
                .add("Height", this.height)
                .toString();
    }

}
