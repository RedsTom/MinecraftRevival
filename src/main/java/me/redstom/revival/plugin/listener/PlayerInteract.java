package me.redstom.revival.plugin.listener;

import me.redstom.revival.api.repository.RepositoryManager;
import me.redstom.revival.plugin.block.CustomBlock;
import me.redstom.revival.plugin.repository.CustomBlockRepo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.sql.SQLException;
import java.util.Optional;

public class PlayerInteract implements Listener {

    private final CustomBlockRepo repo;

    public PlayerInteract() {
        this.repo = RepositoryManager.getInstance().get(CustomBlockRepo.class);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) throws SQLException {
        if (event.getClickedBlock() == null || event.getAction().isLeftClick()) return;

        Optional<CustomBlock> cb = repo.findByLocation(event.getClickedBlock().getLocation().add(.5, 0, .5));
        cb.ifPresent(customBlock -> customBlock.blockType().manager().onBlockInteract(event));
    }
}