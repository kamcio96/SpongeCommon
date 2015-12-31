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
package org.spongepowered.common.item.inventory.adapter.impl.comp;

import net.minecraft.inventory.IInventory;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.crafting.CraftingInventory;
import org.spongepowered.api.item.inventory.crafting.CraftingOutput;
import org.spongepowered.api.item.inventory.type.GridInventory;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.common.item.inventory.lens.Fabric;
import org.spongepowered.common.item.inventory.lens.comp.CraftingInventoryLens;
import org.spongepowered.common.item.inventory.util.RecipeUtil;

import java.util.Optional;

public class CraftingInventoryAdapter extends GridInventoryAdapter implements CraftingInventory {

    protected final CraftingInventoryLens<IInventory, net.minecraft.item.ItemStack> craftingLens;
    
    private GridInventory craftingGrid;
    private CraftingOutput result;
    
    public CraftingInventoryAdapter(Fabric<IInventory> inventory, CraftingInventoryLens<IInventory, net.minecraft.item.ItemStack> root) {
        this(inventory, root, null);
    }

    public CraftingInventoryAdapter(Fabric<IInventory> inventory, CraftingInventoryLens<IInventory, net.minecraft.item.ItemStack> root, Inventory parent) {
        super(inventory, root, parent);
        this.craftingLens = root;
    }

    @Override
    public GridInventory getCraftingGrid() {
        if (this.craftingGrid == null) {
            this.craftingGrid = (GridInventory) this.craftingLens.getCraftingGrid().getAdapter(this.inventory, this);
        }
        return this.craftingGrid;
    }

    @Override
    public CraftingOutput getResult() {
        if (this.result == null) {
            this.result = (CraftingOutput) this.craftingLens.getOutputSlot().getAdapter(this.inventory, this);
        }
        return this.result;
    }

    @Override
    public Optional<Recipe> getRecipe() {
        return RecipeUtil.findMatchingRecipe(this.inventory, this.craftingLens.getCraftingGrid(), this.craftingLens.getOutputSlot());
    }

}
