/*
 * This file is part of SpongeCommon, licensed under the MIT License (MIT).
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

import gnu.trove.set.TIntSet;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.common.item.inventory.adapter.InventoryAdapter;
import org.spongepowered.common.item.inventory.lens.slots.SlotLens;

import java.util.Collection;

/**
 * Base Lens interface. A lens presents an indexed view of a number of child
 * lenses, the leaf nodes in this structure being {@link SlotLens}es. Slots can
 * be accessed directly via this lens, and their index within the lens is the
 * slot <tt>ordinal</tt>.
 * 
 * <p>Internally, a lens will always attempt to fetch a particular slot via
 * whichever <em>child</em> lens can provide access to the target slot which is
 * part of the lens's spanning set. For query purposes, the lens may have other
 * child lenses which provide other ways of looking at the same collection of
 * slots, however most of the time access is via the spanning set.</p>
 * 
 * @param <TInventory>
 * @param <TStack>
 */
public interface Lens<TInventory, TStack> extends LensCollection<TInventory, TStack> {
    
    /**
     * Get the corresponding adapter type for this lens
     * 
     * @return class of the adapter which corresponds to this specific lens type
     */
    public abstract Class<? extends Inventory> getAdapterType();
    
    /**
     * Get an instance of the corresponding adapter type for this lens
     * 
     * @return adapter for this lens
     */
    public abstract InventoryAdapter<TInventory, TStack> getAdapter(TInventory inv);
    
    /**
     * Get the number of slots referenced by this lens
     * 
     * @param inv Inventory
     * @return
     */
    public abstract int slotCount(); //TInventory inv);
    
    /**
     * Used by parent lenses when marshalling their spanning tree, queries
     * whether this lens has access to a slot with the specified absolute index.
     * 
     * @param inv Inventory
     * @param index Absolute slot index
     * @return true if this lens has a path to the specified slot index
     */
    @Deprecated
    public abstract boolean hasSlot(int index); //TInventory inv, int index);
    
    @Deprecated
    public abstract TIntSet getSlots(); //TInventory inv);
    
    /**
     * Returns the "real" underlying slot index in the target inventory for the
     * specified slot ordinal. This method returns -1 if the ordinal is less
     * than 0 or greater than or equal to the value returned by
     * {@link #slotCount()}.
     * 
     * @param inv inventory
     * @param ordinal 
     * @return the "real" slot index (ordinal), or -1 for invalid indices
     */
    public abstract int getRealIndex(TInventory inv, int ordinal);
    
    /**
     * Gets the itemstack for the specified slot ordinal. Returns null if the
     * slot is empty, or if the specified ordinal is outside the range of this
     * lens.
     * 
     * @param inv inventory
     * @param ordinal slot ordinal
     * @return the item stack in the specified slot
     */
    public abstract TStack getStack(TInventory inv, int ordinal);
    
    /**
     * Get the maximum stack size from the target inventory
     * 
     * @param inv
     * @return
     */
    public abstract int getMaxStackSize(TInventory inv);

    /**
     * Get child lenses of this lens
     * 
     * @return
     */
    public abstract Collection<Lens<TInventory, TStack>> getChildren();
    
    /**
     * Get child lenses of this lens
     * 
     * @return
     */
    public abstract Collection<Lens<TInventory, TStack>> getSpanningChildren();
    
    /**
     * Set the stack at the specified offset 
     * 
     * @param inv
     * @param ordinal
     * @param stack
     * @return
     */
    public abstract boolean setStack(TInventory inv, int ordinal, TStack stack);
    
    /**
     * Invalidate this lens for the supplied inventory, notify all observers
     * 
     * @param inv inventory
     */
    public abstract void invalidate(TInventory inv);

}
