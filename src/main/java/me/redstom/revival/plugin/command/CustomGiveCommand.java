package me.redstom.revival.plugin.command;

import me.redstom.revival.api.block.BlockType;
import me.redstom.revival.api.block.BlockTypeManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class CustomGiveCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Usage: /customgive <player> <block> [amount]").color(NamedTextColor.RED));
            return false;
        }

        Player to = Bukkit.getPlayer(args[0]);
        if (to == null) {
            sender.sendMessage(Component.text("Unknown player").color(NamedTextColor.RED));
            return false;
        }

        String typeName = args[1];

        BlockType[] types = BlockTypeManager.getInstance().blockTypes();
        BlockType type = Stream.of(types)
                .filter(blockType -> blockType.name().equalsIgnoreCase(typeName))
                .findFirst()
                .orElseThrow(() -> {
                    sender.sendMessage(Component.text("Unknown block").color(NamedTextColor.RED));
                    return new IllegalArgumentException();
                });

        int amount = 1;
        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid amount").color(NamedTextColor.RED));
                return false;
            }
        }

        ItemStack is = type.itemStack();
        is.setAmount(amount);

        to.getInventory().addItem(is);

        sender.sendMessage(Component.text("Gave ")
                .append(Component.text(amount))
                .append(Component.text(" "))
                .append(is.displayName())
                .append(Component.text(" to "))
                .append(to.displayName()));

        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .toList();
        }

        if (args.length == 2) {
            BlockType[] types = BlockTypeManager.getInstance().blockTypes();

            return Stream.of(types)
                    .map(BlockType::name)
                    .map(String::toLowerCase)
                    .filter(lowerCase -> lowerCase.startsWith(args[1].toLowerCase()))
                    .toList();
        }

        return Collections.emptyList();
    }
}
