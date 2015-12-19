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

import com.flowpowered.math.vector.Vector3i;
import com.google.common.base.Objects;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenDesertWells;
import net.minecraft.world.gen.feature.WorldGenerator;
import org.spongepowered.api.world.Chunk;
import org.spongepowered.api.world.gen.PopulatorObject;
import org.spongepowered.api.world.gen.populator.DesertWell;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(WorldGenDesertWells.class)
public abstract class MixinWorldGenDesertWells extends WorldGenerator implements DesertWell, PopulatorObject {

    @Shadow private static BlockStateHelper field_175913_a;

    private double spawnProbability;
    private PopulatorObject obj;

    @Inject(method = "<init>()V", at = @At("RETURN") )
    public void onConstructed(CallbackInfo ci) {
        this.spawnProbability = 0.001;
        this.obj = this;
    }

    @Override
    public void populate(Chunk chunk, Random random) {
        World world = (World) chunk.getWorld();
        Vector3i min = chunk.getBlockMin();
        BlockPos chunkPos = new BlockPos(min.getX(), min.getY(), min.getZ());

        if (random.nextDouble() < this.spawnProbability) {
            int x = random.nextInt(16) + 8;
            int z = random.nextInt(16) + 8;
            BlockPos pos = world.getTopSolidOrLiquidBlock(chunkPos.add(x, 0, z)).up();
            if (this.obj.canPlaceAt((org.spongepowered.api.world.World) world, pos.getX(), pos.getY(), pos.getZ())) {
                this.obj.placeObject((org.spongepowered.api.world.World) world, random, pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    @Override
    public double getSpawnProbability() {
        return this.spawnProbability;
    }

    @Override
    public void setSpawnProbability(double p) {
        this.spawnProbability = p;
    }

    @Override
    public PopulatorObject getWellObject() {
        return this.obj;
    }

    @Override
    public void setWellObject(PopulatorObject obj) {
        this.obj = obj;
    }

    @Override
    public boolean canPlaceAt(org.spongepowered.api.world.World world, int x, int y, int z) {
        System.out.println("Checking " + x + " " + y + " " + z);
        World worldIn = (World) world;
        BlockPos position = new BlockPos(x, y, z);
        while (worldIn.isAirBlock(position) && position.getY() > 2)
        {
            position = position.down();
        }
        if (!field_175913_a.apply(worldIn.getBlockState(position))) {
            System.out.println("Check failed not sand");
            return false;
        }
        int i;
        int j;
        for (i = -2; i <= 2; ++i) {
            for (j = -2; j <= 2; ++j) {
                if (worldIn.isAirBlock(position.add(i, -1, j)) && worldIn.isAirBlock(position.add(i, -2, j))) {
                    System.out.println("Check failed too much air");
                    return false;
                }
            }
        }
        System.out.println("Check passes");
        return true;
    }

    @Override
    public void placeObject(org.spongepowered.api.world.World world, Random random, int x, int y, int z) {
        generate((World) world, random, new BlockPos(x, y, z));
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("Type", "DesertWell")
                .add("Chance", this.spawnProbability)
                .add("Object", this.obj)
                .toString();
    }

}
