package me.redstom.revival.plugin;

import me.redstom.revival.api.block.BlockTypeManager;
import me.redstom.revival.api.repository.RepositoryManager;
import me.redstom.revival.plugin.block.CustomBlockTypes;
import me.redstom.revival.plugin.command.CustomGiveCommand;
import me.redstom.revival.plugin.command.InventoryCommand;
import me.redstom.revival.plugin.listener.BlockBreak;
import me.redstom.revival.plugin.listener.BlockPlace;
import me.redstom.revival.plugin.listener.PlayerInteract;
import me.redstom.revival.plugin.repository.CustomBlockRepo;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class RevivalPlugin extends JavaPlugin {

    private Connection connection;

    static {
        CustomBlockTypes.register();
        CustomBlockRepo.register();
    }

    @Override
    public void onLoad() {
        try {
            Path dataFolder = getDataFolder().toPath();

            if (!Files.exists(dataFolder)) {
                Files.createDirectories(dataFolder);
            }

            Path dbFile = dataFolder.resolve("db.sqlite");

            if (!Files.exists(dbFile)) {
                Files.createFile(dbFile);
            }

            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);

            RepositoryManager.getInstance().hydrateAll(connection);
            RepositoryManager.getInstance().createAll();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        getLogger().info("Registering recipes...");
        BlockTypeManager.getInstance().registerAllRecipes(this);

        getLogger().info("Registering commands...");
        getCommand("inventory").setExecutor(new InventoryCommand());
        getCommand("custom-give").setExecutor(new CustomGiveCommand());


        getLogger().info("Registering listeners...");
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);

        getLogger().info("Finished!");
    }
}