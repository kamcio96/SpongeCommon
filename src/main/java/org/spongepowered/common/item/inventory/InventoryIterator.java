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
package org.spongepowered.common.item.inventory;

import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.Lens;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class InventoryIterator<TInventory, TStack> implements Iterator<Inventory> {
    
    protected final List<Lens<TInventory, TStack>> children;
    
    protected final Fabric<TInventory> inventory;
    
    protected final Inventory context;

    protected int next = 0;

    public InventoryIterator(Lens<TInventory, TStack> lens, Fabric<TInventory> inventory) {
        this(lens, inventory, null);
    }
    
    public InventoryIterator(Lens<TInventory, TStack> lens, Fabric<TInventory> inventory, Inventory context) {
        this.children = lens.getChildren();
        this.inventory = inventory;
        this.context = context;
    }

    @Override
    public boolean hasNext() {
        return this.next < this.children.size();
    }

    @Override
    public Inventory next() {
        try {
            return this.children.get(this.next++).getAdapter(this.inventory, this.context);
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        }
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
