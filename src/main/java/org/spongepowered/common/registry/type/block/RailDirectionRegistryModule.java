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
package org.spongepowered.common.registry.type.block;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.BlockRailBase;
import org.spongepowered.api.data.type.RailDirection;
import org.spongepowered.api.data.type.RailDirections;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public final class RailDirectionRegistryModule implements CatalogRegistryModule<RailDirection> {

    @RegisterCatalog(RailDirections.class)
    private final Map<String, RailDirection> railDirectionMappings = new ImmutableMap.Builder<String, RailDirection>()
        .put("ascending_east", (RailDirection) (Object) BlockRailBase.EnumRailDirection.ASCENDING_EAST)
        .put("ascending_north", (RailDirection) (Object) BlockRailBase.EnumRailDirection.ASCENDING_NORTH)
        .put("ascending_south", (RailDirection) (Object) BlockRailBase.EnumRailDirection.ASCENDING_SOUTH)
        .put("ascending_west", (RailDirection) (Object) BlockRailBase.EnumRailDirection.ASCENDING_WEST)
        .put("north_east", (RailDirection) (Object) BlockRailBase.EnumRailDirection.NORTH_EAST)
        .put("north_south", (RailDirection) (Object) BlockRailBase.EnumRailDirection.NORTH_SOUTH)
        .put("north_west", (RailDirection) (Object) BlockRailBase.EnumRailDirection.NORTH_WEST)
        .put("east_west", (RailDirection) (Object) BlockRailBase.EnumRailDirection.EAST_WEST)
        .put("south_east", (RailDirection) (Object) BlockRailBase.EnumRailDirection.SOUTH_EAST)
        .put("south_west", (RailDirection) (Object) BlockRailBase.EnumRailDirection.SOUTH_WEST)
        .build();

    @Override
    public Optional<RailDirection> getById(String id) {
        return Optional.ofNullable(this.railDirectionMappings.get(checkNotNull(id).toLowerCase()));
    }

    @Override
    public Collection<RailDirection> getAll() {
        return ImmutableList.copyOf(this.railDirectionMappings.values());
    }

}
