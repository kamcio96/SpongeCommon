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
package org.spongepowered.common.data.manipulator.mutable.entity;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.entity.ImmutableFlyingAbilityData;
import org.spongepowered.api.data.manipulator.mutable.entity.FlyingAbilityData;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.common.data.manipulator.immutable.entity.ImmutableSpongeFlyingAbilityData;
import org.spongepowered.common.data.manipulator.mutable.common.AbstractBooleanData;
import org.spongepowered.common.data.value.mutable.SpongeValue;

public class SpongeFlyingAbilityData extends AbstractBooleanData<FlyingAbilityData, ImmutableFlyingAbilityData> implements FlyingAbilityData {

    public SpongeFlyingAbilityData(boolean value) {
        super(FlyingAbilityData.class, value, Keys.CAN_FLY, ImmutableSpongeFlyingAbilityData.class);
    }

    public SpongeFlyingAbilityData() {
        this(false);
    }

    @Override
    protected Value<?> getValueGetter() {
        return canFly();
    }

    @Override
    public Value<Boolean> canFly() {
        return new SpongeValue<>(Keys.CAN_FLY, false, this.getValue());
    }

}