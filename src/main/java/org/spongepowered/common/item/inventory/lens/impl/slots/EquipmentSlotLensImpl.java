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
package org.spongepowered.common.item.inventory.lens.impl.slots;

import net.minecraft.inventory.IInventory;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.equipment.EquipmentType;
import org.spongepowered.common.item.inventory.adapter.InventoryAdapter;
import org.spongepowered.common.item.inventory.adapter.impl.slots.EquipmentSlotAdapter;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.slots.EquipmentSlotLens;

import java.util.function.Predicate;


public class EquipmentSlotLensImpl extends FilteringSlotLensImpl implements EquipmentSlotLens<IInventory, net.minecraft.item.ItemStack> {
    
    private final Predicate<EquipmentType> equipmentTypeFilter;

    public EquipmentSlotLensImpl(int index, Predicate<ItemStack> stackFilter, Predicate<ItemType> typeFilter, Predicate<EquipmentType> equipmentTypeFilter) {
        this(index, EquipmentSlotAdapter.class, stackFilter, typeFilter, equipmentTypeFilter);
    }

    public EquipmentSlotLensImpl(int index, Class<? extends Inventory> adapterType, Predicate<ItemStack> stackFilter, Predicate<ItemType> typeFilter, Predicate<EquipmentType> equipmentTypeFilter) {
        super(index, adapterType, stackFilter, typeFilter);
        this.equipmentTypeFilter = equipmentTypeFilter;
    }

    @Override
    public Predicate<EquipmentType> getEquipmentTypeFilter() {
        return this.equipmentTypeFilter;
    }
    
    @Override
    public InventoryAdapter<IInventory, net.minecraft.item.ItemStack> getAdapter(Fabric<IInventory> inv, Inventory parent) {
        return new EquipmentSlotAdapter(inv, this);
    }

}
