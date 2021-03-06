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
package org.spongepowered.common.data.manipulator.mutable;

import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.immutable.ImmutablePotionEffectData;
import org.spongepowered.api.data.manipulator.mutable.PotionEffectData;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.common.data.manipulator.immutable.ImmutableSpongePotionEffectData;
import org.spongepowered.common.data.manipulator.mutable.common.collection.AbstractSingleListData;

import java.util.List;

public class SpongePotionEffectData extends AbstractSingleListData<PotionEffect, PotionEffectData, ImmutablePotionEffectData> implements PotionEffectData {

    public SpongePotionEffectData(List<PotionEffect> effects) {
        super(PotionEffectData.class, effects, Keys.POTION_EFFECTS, ImmutableSpongePotionEffectData.class);
    }

    @Override
    public ListValue<PotionEffect> effects() {
        return getValueGetter();
    }

    @Override
    public int compareTo(PotionEffectData o) {
        return 0; //TODO
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer()
            .set(Keys.POTION_EFFECTS.getQuery(), this.getValue());
    }
}
