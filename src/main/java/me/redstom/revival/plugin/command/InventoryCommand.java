package me.redstom.revival.plugin.command;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class InventoryCommand implements CommandExecutor {

    private final Map<String, Inventory> inventoryMap = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;

        int size = Integer.parseInt(args[0]) * 9;
        String name = String.join(" ", args).replace(args[0] + " ", "");

        Inventory inventory = inventoryMap.computeIfAbsent(name, (key) -> Bukkit.createInventory(null, size, name));
        player.openInventory(inventory);

        return false;
    }
}
