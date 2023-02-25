package me.redstom.revival.api.block;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

public interface BlockType {

    ItemStack itemStack();
    Recipe recipe(JavaPlugin plugin);

    String name();

    BlockManager manager();

}
