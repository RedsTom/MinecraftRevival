package me.redstom.revival.plugin.listener;

import me.redstom.revival.api.repository.RepositoryManager;
import me.redstom.revival.plugin.RevivalPlugin;
import me.redstom.revival.plugin.block.CustomBlock;
import me.redstom.revival.plugin.repository.CustomBlockRepo;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.sql.SQLException;
import java.util.Optional;

public class BlockBreak implements Listener {

    private final CustomBlockRepo repo;

    public BlockBreak() {
        repo = RepositoryManager.getInstance().get(CustomBlockRepo.class);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws SQLException {
        Optional<CustomBlock> cb = repo.findByLocation(event.getBlock().getLocation().add(.5, 0, .5));
        if (cb.isEmpty()) {
            return;
        }

        CustomBlock customBlock = cb.get();
        customBlock.blockType().manager().onBlockBreak(event);

        if (event.isCancelled()) return;

        World world = event.getBlock().getWorld();
        ArmorStand entity = (ArmorStand) world.getEntity(customBlock.uniqueId());

        ItemStack head = null;
        if (entity != null) {
            head = entity.getEquipment().getHelmet();
            entity.remove();
        }


        event.getBlock().setType(Material.AIR);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE && head != null) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), head);
        }


        repo.delete(customBlock);
    }

}
