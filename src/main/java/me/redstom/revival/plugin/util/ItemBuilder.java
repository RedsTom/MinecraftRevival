package me.redstom.revival.plugin.util;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder(Material material) {
        this.itemStack = new ItemStack(material);
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder name(Component name) {
        ItemMeta im = itemStack.getItemMeta();
        im.displayName(name);
        itemStack.setItemMeta(im);

        return this;
    }

    public ItemBuilder customData(int data) {
        ItemMeta im = itemStack.getItemMeta();
        im.setCustomModelData(data);
        itemStack.setItemMeta(im);

        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}
