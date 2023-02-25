package me.redstom.revival.plugin.listener;

import me.redstom.revival.api.block.BlockType;
import me.redstom.revival.api.block.BlockTypeManager;
import me.redstom.revival.api.repository.RepositoryManager;
import me.redstom.revival.plugin.block.CustomBlock;
import me.redstom.revival.plugin.repository.CustomBlockRepo;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.SQLException;

public class BlockPlace implements Listener {

    private final CustomBlockRepo repo;

    public BlockPlace() {
        repo = RepositoryManager.getInstance().get(CustomBlockRepo.class);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws SQLException {
        ItemStack item = event.getItemInHand();
        ItemMeta meta = item.getItemMeta();

        if (!meta.hasCustomModelData()) {
            return;
        }
        BlockType blockType = BlockTypeManager.getInstance().fromItemStack(item).get();
        if (blockType != null) {
            blockType.manager().onBlockPlace(event);
        }

        if (event.isCancelled()) {
            return;
        }

        item.setAmount(1);

        Location location = event.getBlock().getLocation();
        location.add(.5, 0, .5);

        ArmorStand generated = location.getWorld().spawn(location, ArmorStand.class, stand -> {
            stand.setInvisible(true);
            stand.setCanMove(false);
            stand.setCanTick(false);
            stand.setCollidable(false);
            stand.setRemoveWhenFarAway(false);
            stand.setInvulnerable(true);
            stand.setMarker(true);

            stand.setDisabledSlots(EquipmentSlot.values());
            stand.setItem(EquipmentSlot.HEAD, item);
        });

        CustomBlock cb = CustomBlock.builder()
                .uniqueId(generated.getUniqueId())
                .location(generated.getLocation())
                .blockType(blockType)
                .build();

        repo.insert(cb);
    }
}
