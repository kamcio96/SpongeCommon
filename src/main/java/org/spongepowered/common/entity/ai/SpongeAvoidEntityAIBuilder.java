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
package org.spongepowered.common.entity.ai;

import com.google.common.base.Preconditions;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.ai.task.builtin.creature.AvoidEntityAITask;
import org.spongepowered.api.entity.living.Creature;
import org.spongepowered.common.util.GuavaJavaUtils;

import java.util.function.Predicate;

public final class SpongeAvoidEntityAIBuilder implements AvoidEntityAITask.Builder {
    Predicate<? extends Entity> targetSelector;
    float searchDistance;
    double closeRangeSpeed, farRangeSpeed;

    @Override
    public AvoidEntityAITask.Builder targetSelector(Predicate<Entity> predicate) {
        this.targetSelector = predicate;
        return this;
    }

    @Override
    public AvoidEntityAITask.Builder searchDistance(float distance) {
        this.searchDistance = distance;
        return this;
    }

    @Override
    public AvoidEntityAITask.Builder closeRangeSpeed(double speed) {
        this.closeRangeSpeed = speed;
        return this;
    }

    @Override
    public AvoidEntityAITask.Builder farRangeSpeed(double speed) {
        this.farRangeSpeed = speed;
        return this;
    }

    @Override
    public AvoidEntityAITask.Builder reset() {
        this.searchDistance = 1;
        this.closeRangeSpeed = 1;
        this.farRangeSpeed = 1;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AvoidEntityAITask build(Creature owner) {
        Preconditions.checkNotNull(owner);
        Preconditions.checkNotNull(targetSelector);
        return (AvoidEntityAITask) new EntityAIAvoidEntity((EntityCreature) owner, Entity.class,
                GuavaJavaUtils.asGuavaPredicate((Predicate) targetSelector),
                searchDistance, closeRangeSpeed, farRangeSpeed);
    }

}
