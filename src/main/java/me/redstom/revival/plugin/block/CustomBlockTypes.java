package me.redstom.revival.plugin.block;

import lombok.Getter;
import me.redstom.revival.api.Load;
import me.redstom.revival.api.block.BlockManager;
import me.redstom.revival.api.block.BlockType;
import me.redstom.revival.api.block.BlockTypeManager;
import me.redstom.revival.plugin.util.ItemBuilder;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.UnaryOperator;

@Load
public enum CustomBlockTypes implements BlockType {
    TOOLSTATION(
            new ItemBuilder(Material.CRAFTING_TABLE)
                    .name(Component.text("Toolstation").style(Style.style(TextDecoration.ITALIC.withState(false))))
                    .customData(1)
                    .build(),
            recipe -> recipe
                    .shape("XX", "XX")
                    .setIngredient('X', Material.STICK),
            new ToolstationManager()
    ),
    VERTICAL_SLAB(
            new ItemBuilder(Material.OAK_TRAPDOOR)
                    .name(Component.text("Vertical Oak Slab").style(Style.style(TextDecoration.ITALIC.withState(false))))
                    .customData(1)
                    .build(),
            recipe -> recipe
                    .shape("X  ", "X  ", "X  ")
                    .setIngredient('X', Material.OAK_PLANKS),
            new VerticalSlabManager()
    );

    public static void register() {
        BlockTypeManager.getInstance().register(values());
    }

    private final ItemStack itemStack;
    private final UnaryOperator<ShapedRecipe> recipe;
    @Getter
    private final BlockManager manager;

    CustomBlockTypes(ItemStack itemStack, UnaryOperator<ShapedRecipe> recipe) {
        this.itemStack = itemStack;
        this.recipe = recipe;
        this.manager = BlockManager.DEFAULT;
    }

    CustomBlockTypes(ItemStack itemStack, UnaryOperator<ShapedRecipe> recipe, BlockManager manager) {
        this.itemStack = itemStack;
        this.recipe = recipe;
        this.manager = manager;
    }

    public ItemStack itemStack() {
        return new ItemStack(itemStack);
    }

    public Recipe recipe(JavaPlugin plugin) {
        NamespacedKey key = new NamespacedKey(plugin, name().toLowerCase());
        return recipe.apply(new ShapedRecipe(key, itemStack));
    }
}
