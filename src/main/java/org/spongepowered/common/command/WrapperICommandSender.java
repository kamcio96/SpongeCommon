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
package org.spongepowered.common.command;

import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.source.LocatedSource;
import org.spongepowered.common.interfaces.IMixinCommandSource;
import org.spongepowered.common.text.SpongeTexts;
import org.spongepowered.common.util.VecHelper;

/**
 * Wrapper around a CommandSource that is not part of the base game to allow it
 * to access MC commands.
 */
public class WrapperICommandSender implements ICommandSender {

    final CommandSource source;

    private WrapperICommandSender(CommandSource source) {
        this.source = source;
    }

    @Override
    public String getName() {
        return this.source.getName();
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(this.source.getName());
    }

    @Override
    public void addChatMessage(IChatComponent component) {
        this.source.sendMessage(SpongeTexts.toText(component));
    }

    @Override
    public boolean canCommandSenderUseCommand(int permLevel, String commandName) {
        return this.source.hasPermission(commandName);
    }

    @Override
    public BlockPos getPosition() {
        if (this.source instanceof LocatedSource) {
            return VecHelper.toBlockPos(((LocatedSource) this.source).getLocation());
        }
        return BlockPos.ORIGIN;
    }

    @Override
    public Vec3 getPositionVector() {
        if (this.source instanceof LocatedSource) {
            return VecHelper.toVector(((LocatedSource) this.source).getLocation().getPosition());
        }
        return new Vec3(0, 0, 0);
    }

    @Override
    public World getEntityWorld() {
        if (this.source instanceof LocatedSource) {
            return (World) ((LocatedSource) this.source).getWorld();
        }
        return null;
    }

    @Override
    public Entity getCommandSenderEntity() {
        if (this.source instanceof Entity) {
            return (Entity) this.source;
        }
        return null;
    }

    @Override
    public boolean sendCommandFeedback() {
        return true;
    }

    @Override
    public void setCommandStat(CommandResultStats.Type type, int amount) {

    }

    public static ICommandSender of(CommandSource source) {
        if (source instanceof IMixinCommandSource) {
            return ((IMixinCommandSource) source).asICommandSender();
        }
        if (source instanceof WrapperCommandSource) {
            return ((WrapperCommandSource) source).sender;
        }
        return new WrapperICommandSender(source);
    }
}
