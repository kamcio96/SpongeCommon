///*
// * This file is part of Sponge, licensed under the MIT License (MIT).
// *
// * Copyright (c) SpongePowered <https://www.spongepowered.org>
// * Copyright (c) contributors
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package org.spongepowered.common.item.inventory.struct;
//
//import static com.google.common.base.Preconditions.*;
//
//import com.google.common.base.Optional;
//import org.spongepowered.api.item.inventory.ItemStack;
//import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//
///**
// * An inventory operation result with type
// */
//public class OperationResult implements InventoryTransactionResult {
//    
//    public static final class Builder {
//        
//        private Type type = Type.SUCCESS;
//        
//        private List<ItemStack> rejected = null; 
//        private List<ItemStack> replaced = null;
//        
//        Builder() {}
//        
//        public Builder type(Type type) {
//            this.type = checkNotNull(type, "type");
//            return this;
//        }
//
//        public Builder reject(net.minecraft.item.ItemStack stack) {
//            return this.reject((ItemStack) stack);
//        }
//        
//        public Builder reject(ItemStack stack) {
//            if (stack == null) {
//                return this;
//            }
//            if (this.rejected == null) {
//                this.rejected = new ArrayList<ItemStack>();
//            }
//            this.rejected.add(stack);
//            return this;
//        }
//        
//        public Builder replace(net.minecraft.item.ItemStack stack) {
//            return this.replace((ItemStack) stack);
//        }
//        
//        public Builder replace(ItemStack stack) {
//            if (stack == null) {
//                return this;
//            }
//            if (this.replaced == null) {
//                this.replaced = new ArrayList<ItemStack>();
//            }
//            this.replaced.add(stack);
//            return this;
//        }
//        
//        public OperationResult build() {
//            if (this.rejected == null && this.replaced == null) {
//                return new OperationResult(this.type);
//            }
//            return new OperationResultDetailed(this.type, this.rejected, this.replaced);
//        }
//    }
//
//    /**
//     * The result type 
//     */
//    private final Type type;
//
//    /**
//     * ctor
//     * 
//     * @param type result type
//     */
//    public OperationResult(Type type) {
//        this.type = type;
//    }
//
//    /* (non-Javadoc)
//     * @see InventoryOperationResult#getType()
//     */
//    @Override
//    public final Type getType() {
//        return this.type;
//    }
//
//    /* (non-Javadoc)
//     * @see InventoryOperationResult#getRejectedItems()
//     */
//    @Override
//    public Optional<Collection<ItemStack>> getRejectedItems() {
//        return Optional.<Collection<ItemStack>>absent();
//    }
//
//    /* (non-Javadoc)
//     * @see InventoryOperationResult#getReplacedItems()
//     */
//    @Override
//    public Optional<Collection<ItemStack>> getReplacedItems() {
//        return Optional.<Collection<ItemStack>>absent();
//    }
//
//    /**
//     * Quick test for whether the operation succeeded or not
//     * 
//     * @return
//     */
//    public boolean isSuccess() {
//        return this.type == Type.SUCCESS;
//    }
//
//    public static Builder builder() {
//        return new Builder();
//    }
//}
