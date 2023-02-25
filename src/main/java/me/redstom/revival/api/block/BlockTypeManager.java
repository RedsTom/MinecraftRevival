package me.redstom.revival.api.block;

import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class BlockTypeManager {

    private static final BlockTypeManager instance = new BlockTypeManager();

    private final List<BlockType> blockTypes = new ArrayList<>();

    private BlockTypeManager() {
    }

    public void register(BlockType... blockTypes) {
        this.blockTypes.addAll(Arrays.asList(blockTypes));
    }

    public BlockType[] blockTypes() {
        return blockTypes.toArray(BlockType[]::new);
    }

    public Optional<BlockType> fromItemStack(ItemStack is) {
        return blockTypes.stream()
                .filter(type -> type.itemStack().isSimilar(is))
                .findFirst();
    }

    public Optional<BlockType> valueOf(String name) {
        return blockTypes.stream()
                .filter(type -> type.name().equalsIgnoreCase(name))
                .findFirst();
    }

    public void registerAllRecipes(JavaPlugin plugin) {
        for (BlockType block : blockTypes) {
            plugin.getServer().addRecipe(block.recipe(plugin));
        }
    }

    public static BlockTypeManager getInstance() {
        return instance;
    }
}
