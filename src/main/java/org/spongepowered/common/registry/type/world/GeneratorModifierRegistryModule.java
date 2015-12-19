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
package org.spongepowered.common.registry.type.world;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.spongepowered.api.extra.skylands.SkylandsWorldGeneratorModifier;
import org.spongepowered.api.world.gen.WorldGeneratorModifier;
import org.spongepowered.api.world.gen.WorldGeneratorModifiers;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GeneratorModifierRegistryModule implements CatalogRegistryModule<WorldGeneratorModifier> {
    
    public static GeneratorModifierRegistryModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(WorldGeneratorModifiers.class) private final Map<String, WorldGeneratorModifier> modifierMappings = new HashMap<>();

    @Override
    public Optional<WorldGeneratorModifier> getById(String id) {
        return Optional.ofNullable(this.modifierMappings.get(checkNotNull(id).toLowerCase()));
    }

    @Override
    public Collection<WorldGeneratorModifier> getAll() {
        return ImmutableList.copyOf(this.modifierMappings.values());
    }

    @Override
    public void registerDefaults() {
        registerModifier(new SkylandsWorldGeneratorModifier());
    }

    public void registerModifier(WorldGeneratorModifier modifier) {
        checkNotNull(modifier, "modifier");
        String id = modifier.getId();
        checkId(id, "World generator ID");

        this.modifierMappings.put(id.toLowerCase(), modifier);
    }

    private void checkId(String id, String subject) {
        checkArgument(id.indexOf(' ') == -1, subject + " " + id + " may not contain a space");
    }

    /**
     * Checks that all modifiers are registered.
     * 
     * @param modifiers
     *            The modifiers
     * @throws IllegalArgumentException
     *             If a modifier is not registered
     */
    public void checkAllRegistered(Collection<WorldGeneratorModifier> modifiers) {
        // We simply call toIds, that checks all world generators
        toIds(modifiers);
    }

    /**
     * Gets the string list for the modifiers, for saving purposes.
     *
     * @param modifiers The modifiers
     * @return The string list
     * @throws IllegalArgumentException If any of the modifiers is not
     *         registered
     */
    public ImmutableCollection<String> toIds(Collection<WorldGeneratorModifier> modifiers) {
        ImmutableList.Builder<String> ids = ImmutableList.builder();
        for (WorldGeneratorModifier modifier : modifiers) {
            checkNotNull(modifier, "modifier (in collection)");
            String id = modifier.getId();
            checkArgument(this.modifierMappings.containsKey(id.toLowerCase()), "unregistered modifier in collection");
            ids.add(id);
        }
        return ids.build();
    }

    /**
     * Gets the world generator modifiers with the given id. If no world
     * generator modifier can be found with a certain id, a message is logged
     * and the id is skipped.
     *
     * @param ids The ids
     * @return The modifiers
     */
    public Collection<WorldGeneratorModifier> toModifiers(Collection<String> ids) {
        List<WorldGeneratorModifier> modifiers = Lists.newArrayList();
        for (String id : ids) {
            WorldGeneratorModifier modifier = this.modifierMappings.get(id.toLowerCase());
            if (modifier != null) {
                modifiers.add(modifier);
            } else {
                SpongeImpl.getLogger().error("World generator modifier with id " + id + " not found. Missing plugin?");
            }
        }
        return modifiers;
    }

    private static final class Holder {
        private static final GeneratorModifierRegistryModule INSTANCE = new GeneratorModifierRegistryModule();
    }
}
