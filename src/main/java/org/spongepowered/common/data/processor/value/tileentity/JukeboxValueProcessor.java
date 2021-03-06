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
package org.spongepowered.common.data.processor.value.tileentity;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockJukebox.TileEntityJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemRecord;
import org.spongepowered.api.block.tileentity.Jukebox;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.data.processor.common.AbstractSpongeValueProcessor;
import org.spongepowered.common.data.value.immutable.ImmutableSpongeValue;
import org.spongepowered.common.data.value.mutable.SpongeValue;

import java.util.Optional;

public class JukeboxValueProcessor extends AbstractSpongeValueProcessor<BlockJukebox.TileEntityJukebox, ItemStackSnapshot, Value<ItemStackSnapshot>> {

    public JukeboxValueProcessor() {
        super(BlockJukebox.TileEntityJukebox.class, Keys.REPRESENTED_ITEM);
    }

    @Override
    protected Value<ItemStackSnapshot> constructValue(ItemStackSnapshot value) {
        return new SpongeValue<>(Keys.REPRESENTED_ITEM, ItemStackSnapshot.NONE, value);
    }

    @Override
    protected boolean set(BlockJukebox.TileEntityJukebox jukebox, ItemStackSnapshot value) {
        IBlockState block = jukebox.getWorld().getBlockState(jukebox.getPos());
        if (value == ItemStackSnapshot.NONE) {
            if (jukebox.getRecord() == null) {
                return true;
            }
            ((Jukebox) jukebox).ejectRecord();
            block = jukebox.getWorld().getBlockState(jukebox.getPos());
            return block.getBlock() instanceof BlockJukebox && !(Boolean) block.getValue(BlockJukebox.HAS_RECORD);
        }
        if (!(value.getType() instanceof ItemRecord)) {
            return false;
        }
        ((Jukebox) jukebox).insertRecord(value.createStack());
        block = jukebox.getWorld().getBlockState(jukebox.getPos());
        return block.getBlock() instanceof BlockJukebox && (Boolean) block.getValue(BlockJukebox.HAS_RECORD);
    }

    @Override
    protected Optional<ItemStackSnapshot> getVal(BlockJukebox.TileEntityJukebox jukebox) {
        if (jukebox.getRecord() != null) {
            return Optional.of(((org.spongepowered.api.item.inventory.ItemStack) jukebox.getRecord()).createSnapshot());
        }
        return Optional.empty();
    }

    @Override
    protected ImmutableValue<ItemStackSnapshot> constructImmutableValue(ItemStackSnapshot value) {
        return new ImmutableSpongeValue<>(Keys.REPRESENTED_ITEM, ItemStackSnapshot.NONE, value);
    }

    @Override
    public DataTransactionResult removeFrom(ValueContainer<?> container) {
        if (!(container instanceof BlockJukebox.TileEntityJukebox)) {
            return DataTransactionResult.failNoData();
        }
        Optional<ItemStackSnapshot> oldValue = getVal((TileEntityJukebox) container);
        if (!oldValue.isPresent()) {
            return DataTransactionResult.successNoData();
        }
        try {
            ((Jukebox) container).ejectRecord();
            return DataTransactionResult.successRemove(constructImmutableValue(oldValue.get()));
        } catch (Exception e) {
            SpongeImpl.getLogger().error("There was an issue removing the repesented item from an Jukebox!", e);
            return DataTransactionResult.builder().result(DataTransactionResult.Type.ERROR).build();
        }
    }
}
