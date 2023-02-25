package me.redstom.revival.plugin.block;

import me.redstom.revival.api.block.BlockManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class ToolstationManager implements BlockManager {

    @Override
    public void onBlockInteract(PlayerInteractEvent event) {
        System.out.println("Called !");

        event.setCancelled(true);

        Inventory inventory = Bukkit.createInventory(null, InventoryType.DROPPER, Component.text("Toolstation"));
        event.getPlayer().openInventory(inventory);
    }
}
