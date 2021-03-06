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
package org.spongepowered.common;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Objects;
import net.minecraft.server.MinecraftServer;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Game;
import org.spongepowered.api.Platform;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.SimpleCommandManager;
import org.spongepowered.api.config.ConfigManager;
import org.spongepowered.api.data.property.PropertyRegistry;
import org.spongepowered.api.plugin.PluginManager;
import org.spongepowered.api.service.ServiceManager;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.scheduler.Scheduler;
import org.spongepowered.api.world.TeleportHelper;
import org.spongepowered.common.command.SpongeCommandDisambiguator;
import org.spongepowered.common.data.property.SpongePropertyRegistry;
import org.spongepowered.common.registry.SpongeGameRegistry;
import org.spongepowered.common.config.SpongeConfigManager;
import org.spongepowered.common.data.SpongeDataManager;
import org.spongepowered.common.scheduler.SpongeScheduler;

import javax.inject.Singleton;

@Singleton
public abstract class SpongeGame implements Game {

    private final Platform platform;

    private final PluginManager pluginManager;
    private final EventManager eventManager;
    private final SpongeGameRegistry gameRegistry;
    private final ServiceManager serviceManager;
    private final TeleportHelper teleportHelper;
    private final ConfigManager configManager;
    private final CommandManager commandManager;

    protected SpongeGame(Platform platform, PluginManager pluginManager, EventManager eventManager, SpongeGameRegistry gameRegistry,
            ServiceManager serviceManager, TeleportHelper teleportHelper, Logger logger) {
        this.platform = checkNotNull(platform, "platform");
        this.pluginManager = checkNotNull(pluginManager, "pluginManager");
        this.eventManager = checkNotNull(eventManager, "eventManager");
        this.gameRegistry = checkNotNull(gameRegistry, "gameRegistry");
        this.serviceManager = checkNotNull(serviceManager, "serviceManager");
        this.teleportHelper = checkNotNull(teleportHelper, "teleportHelper");
        this.configManager = new SpongeConfigManager();
        this.commandManager = new SimpleCommandManager(LoggerFactory.getLogger(logger.getName()), new SpongeCommandDisambiguator(this));

    }

    @Override
    public Platform getPlatform() {
        return this.platform;
    }

    @Override
    public PluginManager getPluginManager() {
        return this.pluginManager;
    }

    @Override
    public EventManager getEventManager() {
        return this.eventManager;
    }

    @Override
    public SpongeGameRegistry getRegistry() {
        return this.gameRegistry;
    }

    @Override
    public ServiceManager getServiceManager() {
        return this.serviceManager;
    }

    @Override
    public CommandManager getCommandManager() {
        return this.commandManager;
    }

    @Override
    public TeleportHelper getTeleportHelper() {
        return this.teleportHelper;
    }

    @Override
    public ConfigManager getConfigManager() {
        return this.configManager;
    }

    @Override
    public Scheduler getScheduler() {
        return SpongeScheduler.getInstance();
    }

    @Override
    public Server getServer() {
        return (Server) MinecraftServer.getServer();
    }

    @Override
    public DataManager getDataManager() {
        return SpongeDataManager.getInstance();
    }

    @Override
    public PropertyRegistry getPropertyRegistry() {
        return SpongePropertyRegistry.getInstance();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("platform", this.platform)
                .toString();
    }

}
