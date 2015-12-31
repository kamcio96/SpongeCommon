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
package org.spongepowered.common.item.inventory.adapter.impl.slots;

import net.minecraft.inventory.IInventory;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Builder;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult.Type;
import org.spongepowered.common.interfaces.inventory.IMixinSlot;
import org.spongepowered.common.item.inventory.adapter.impl.Adapter;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.impl.MinecraftFabric;
import org.spongepowered.common.item.inventory.lens.impl.slots.SlotLensImpl;
import org.spongepowered.common.item.inventory.lens.slots.SlotLens;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class SlotAdapter extends Adapter implements Slot {
    
    private final SlotLens<IInventory, net.minecraft.item.ItemStack> slot;
    
    private final int ordinal;
    
    private SlotAdapter nextSlot;
    
    public SlotAdapter(net.minecraft.inventory.Slot slot) {
        this(MinecraftFabric.of(slot.inventory), new SlotLensImpl(((IMixinSlot)slot).getSlotIndex()));
    }

    public SlotAdapter(Fabric<IInventory> inventory, SlotLens<IInventory, net.minecraft.item.ItemStack> lens) {
        super(inventory, lens);
        this.slot = lens;
        this.ordinal = lens.getOrdinal(inventory);
    }
    
    public int getOrdinal() {
        return this.ordinal;
    }
    
    @Override
    public int getStackSize() {
        net.minecraft.item.ItemStack stack = this.slot.getStack(this.inventory);
        return stack != null ? stack.stackSize : 0;
    }

    @Override
    public <T extends Inventory> Iterable<T> slots() {
        // TODO!
        throw new NotImplementedException("Iterate slot slots");
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> T first() {
        return (T) this;
    }

    SlotAdapter setNext(SlotAdapter next) {
        this.nextSlot = next;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Inventory> T next() {
        if (this.nextSlot != null) {
            return (T) this.nextSlot;
        }
        return (T) this.emptyInventory();
    }

    @Override
    public Optional<ItemStack> poll() {
        ItemStack stack = ItemStackUtil.fromNative(this.inventory.getStack(this.ordinal));
        if (stack == null) {
            return Optional.<ItemStack>empty();
        }
        this.inventory.setStack(this.ordinal, null);
        return Optional.<ItemStack>of(stack);
    }

    @Override
    public Optional<ItemStack> poll(int limit) {
        return super.poll(limit);
    }

    @Override
    public Optional<ItemStack> peek() {
        net.minecraft.item.ItemStack stack = this.slot.getStack(this.inventory);
        return ItemStackUtil.cloneDefensiveOptional(stack);
    }

    @Override
    public Optional<ItemStack> peek(int limit) {
        return super.peek(limit);
    }

    @Override
    public InventoryTransactionResult offer(ItemStack stack) {
//        // TODO Correct the transaction based on how offer goes
//        final net.minecraft.item.ItemStack old = this.inventory.getStack(this.ordinal);
//        if (!ItemStackUtil.compare(old, stack)) {
//            return InventoryTransactionResult.failNoTransactions();
//        }
//        boolean canIncrease = getMaxStackSize() != old.stackSize;
//        if (!canIncrease) {
//            return InventoryTransactionResult.failNoTransactions();
//        }
//        int remaining = getMaxStackSize() - old.stackSize;
//        int toBeOffered = stack.getQuantity();
//        if (toBeOffered > remaining) {
//            old.stackSize += toBeOffered - remaining;
//            stack.setQuantity(toBeOffered - remaining);
//        } else {
//            old.stackSize += remaining;
//            // TODO Quantity being set 0 could be a problem...
//            stack.setQuantity(0);
//        }
//        this.inventory.markDirty();
//        return InventoryTransactionResult.successNoTransactions();
        
        Builder result = InventoryTransactionResult.builder();
        net.minecraft.item.ItemStack nativeStack = ItemStackUtil.toNative(stack);
        
        int maxStackSize = this.slot.getMaxStackSize(this.inventory);
        int remaining = stack.getQuantity();
        
        net.minecraft.item.ItemStack old = this.slot.getStack(this.inventory);
        int push = Math.min(remaining, maxStackSize);
        if (old == null && this.slot.setStack(this.inventory, ItemStackUtil.cloneDefensiveNative(nativeStack, push))) {
            remaining -= push;
        } else if (old != null && ItemStackUtil.compare(old, stack)) {
            push = Math.max(Math.min(maxStackSize - old.stackSize, remaining), 0); // max() accounts for oversized stacks
            old.stackSize += push;
            remaining -= push;
        }
        
        if (remaining == stack.getQuantity()) {
            // No items were consumed
            result.type(Type.FAILURE).reject(ItemStackUtil.cloneDefensive(nativeStack));
        } else {
            stack.setQuantity(remaining);
        }
        
        return result.build();
    }

    @Override
    public InventoryTransactionResult set(ItemStack stack) {
        Builder result = InventoryTransactionResult.builder();
        net.minecraft.item.ItemStack nativeStack = ItemStackUtil.toNative(stack);
        
        net.minecraft.item.ItemStack old = this.slot.getStack(this.inventory);
        int remaining = stack.getQuantity();
        int push = Math.min(remaining, this.slot.getMaxStackSize(this.inventory));
        if (this.slot.setStack(this.inventory, ItemStackUtil.cloneDefensiveNative(nativeStack, push))) {
            result.replace(ItemStackUtil.fromNative(old));
            remaining -= push;
        }
        
        if (remaining > 0) {
            result.reject(ItemStackUtil.cloneDefensive(nativeStack, remaining));
        }
        
        return result.build();
    }

    @Override
    public void clear() {
        this.slot.setStack(this.inventory, null);
    }

    @Override
    public int size() {
        return this.slot.getStack(this.inventory) != null ? 1 : 0;
    }

    @Override
    public int totalItems() {
        net.minecraft.item.ItemStack stack = this.slot.getStack(this.inventory);
        return stack != null ? stack.stackSize : 0;
    }

    @Override
    public int capacity() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public boolean contains(ItemStack stack) {
        net.minecraft.item.ItemStack slotStack = this.slot.getStack(this.inventory);
        return slotStack == null ? (stack == null) : ItemStackUtil.compare(slotStack, stack);
    }

    @Override
    public boolean contains(ItemType type) {
        net.minecraft.item.ItemStack slotStack = this.slot.getStack(this.inventory);
        return slotStack == null ? (type == null) : slotStack.getItem().equals(type);
    }

    @Override
    public int getMaxStackSize() {
        return super.getMaxStackSize();
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Collection<T> getProperties(Inventory child, Class<T> property) {
        return Collections.<T>emptyList();
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Collection<T> getProperties(Class<T> property) {
        return Collections.<T>emptyList();
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Optional<T> getProperty(Inventory child, Class<T> property, Object key) {
        return Optional.<T>empty();
    }

    @Override
    public <T extends InventoryProperty<?, ?>> Optional<T> getProperty(Class<T> property, Object key) {
        return Optional.<T>empty();
    }

//    @Override
//    public Iterator<Inventory> iterator() {
//        // TODO 
//        return super.iterator();
//    }
    
}
