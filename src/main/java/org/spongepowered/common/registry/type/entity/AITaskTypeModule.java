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
package org.spongepowered.common.registry.type.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import org.spongepowered.api.entity.ai.task.AITask;
import org.spongepowered.api.entity.ai.task.AITaskType;
import org.spongepowered.api.entity.ai.task.AITaskTypes;
import org.spongepowered.api.entity.ai.task.builtin.SwimmingAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.AttackLivingAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.AvoidEntityAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.WanderAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.WatchClosestAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.horse.RunAroundLikeCrazyAITask;
import org.spongepowered.api.entity.ai.task.builtin.creature.target.FindNearestAttackableTargetAITask;
import org.spongepowered.api.entity.living.Agent;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.common.SpongeImpl;
import org.spongepowered.common.entity.ai.SpongeAITaskType;
import org.spongepowered.api.registry.CatalogRegistryModule;
import org.spongepowered.api.registry.util.RegisterCatalog;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AITaskTypeModule implements CatalogRegistryModule<AITaskType> {

    public static AITaskTypeModule getInstance() {
        return Holder.INSTANCE;
    }

    @RegisterCatalog(AITaskTypes.class)
    private final Map<String, AITaskType> aiTaskTypes = new HashMap<>();

    @Override
    public Optional<AITaskType> getById(String id) {
        checkNotNull(id);
        if (!id.contains(":")) {
            id = "minecraft:" + id; // assume vanilla
        }
        return Optional.ofNullable(aiTaskTypes.get(id.toLowerCase()));
    }

    @Override
    public Collection<AITaskType> getAll() {
        return ImmutableList.copyOf(aiTaskTypes.values());
    }

    public Optional<AITaskType> getByAIClass(Class clazz) {
        for (AITaskType type : aiTaskTypes.values()) {
            if (type.getAIClass().isAssignableFrom(clazz)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }

    @Override
    public void registerDefaults() {
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "wander", "Wander", WanderAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "avoid_entity", "Avoid Entity", AvoidEntityAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "run_around_like_crazy", "Run Around Like Crazy", RunAroundLikeCrazyAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "swimming", "Swimming", SwimmingAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "watch_closest", "Watch Closest", WatchClosestAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "find_nearest_attackable_target", "Find Nearest Attackable Target",
                FindNearestAttackableTargetAITask.class);
        createAITaskType(SpongeImpl.getMinecraftPlugin(), "attack_living", "Attack Living", AttackLivingAITask.class);
    }

    public AITaskType createAITaskType(Object plugin, String id, String name, Class<? extends AITask<? extends
            Agent>> aiClass) {
        final Optional<PluginContainer> optPluginContainer = SpongeImpl.getGame().getPluginManager().fromInstance(plugin);
        Preconditions.checkArgument(optPluginContainer.isPresent());
        final PluginContainer pluginContainer = optPluginContainer.get();
        final String combinedId = pluginContainer.getId().toLowerCase() + ":" + id;

        final SpongeAITaskType newType = new SpongeAITaskType(combinedId, name, aiClass);
        aiTaskTypes.put(combinedId, newType);
        return newType;
    }

    private AITaskTypeModule() {}

    private static final class Holder {
        private static final AITaskTypeModule INSTANCE = new AITaskTypeModule();
    }
}
