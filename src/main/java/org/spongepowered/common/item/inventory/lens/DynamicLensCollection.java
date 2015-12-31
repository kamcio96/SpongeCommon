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
package org.spongepowered.common.item.inventory.lens;

import org.spongepowered.api.item.inventory.InventoryProperty;

/**
 * A type of Lens collection whose members are fixed but properties for member
 * lenses can be modified.
 * 
 * @param <TInventory>
 */
public interface DynamicLensCollection<TInventory, TStack> extends LensCollection<TInventory, TStack> {

    public abstract void setProperty(int index, InventoryProperty<?, ?> property);
    
    public abstract void setProperty(Lens<TInventory, TStack> lens, InventoryProperty<?, ?> property); 

    public abstract void removeProperty(int index, InventoryProperty<?, ?> property);
    
    public abstract void removeProperty(Lens<TInventory, TStack> lens, InventoryProperty<?, ?> property); 
    
}
