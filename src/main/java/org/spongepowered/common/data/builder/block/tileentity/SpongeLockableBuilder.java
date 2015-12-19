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
package org.spongepowered.common.data.builder.block.tileentity;

import net.minecraft.inventory.IInventory;
import org.spongepowered.api.Game;
import org.spongepowered.api.block.tileentity.carrier.TileEntityCarrier;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.util.persistence.InvalidDataException;
import org.spongepowered.api.data.DataManager;

import java.util.List;
import java.util.Optional;

public class SpongeLockableBuilder<T extends TileEntityCarrier> extends AbstractTileBuilder<T> {

    public SpongeLockableBuilder(Game game) {
        super(game);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> build(DataView container) throws InvalidDataException {
        Optional<T> lockOptional = super.build(container);
        if (!lockOptional.isPresent()) {
            throw new InvalidDataException("The container had insufficient data to create a lockable tile entity!");
        }
        TileEntityCarrier lockable = lockOptional.get();
        if (!container.contains(new DataQuery("Contents"))) {
            throw new InvalidDataException("The provided container does not contain the data to make a lockable tile entity!");
        }
        DataManager service = this.game.getDataManager();
        List<DataView> contents = container.getViewList(new DataQuery("Contents")).get();
        for (DataView content: contents) {
            net.minecraft.item.ItemStack stack =
                    (net.minecraft.item.ItemStack) content.getSerializable(new DataQuery("Item"), ItemStack.class).get();
            ((IInventory) lockable).setInventorySlotContents(content.getInt(new DataQuery("Slot")).get(), stack);
        }
        if (container.contains(Keys.LOCK_TOKEN.getQuery())) {
            lockable.offer(Keys.LOCK_TOKEN, container.getString(Keys.LOCK_TOKEN.getQuery()).get());
        }
        return Optional.of((T) lockable);
    }
}
