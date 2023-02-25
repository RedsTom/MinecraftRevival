package me.redstom.revival.plugin.block;

import me.redstom.revival.api.block.BlockManager;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.TrapDoor;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class VerticalSlabManager implements BlockManager {
    @Override
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlockPlaced();

        TrapDoor data = (TrapDoor) block.getBlockData();
        data.setOpen(true);
        data.setFacing(BlockFace.SOUTH);

        block.setBlockData(data);
    }

    @Override
    public void onBlockInteract(PlayerInteractEvent event) {
        event.setCancelled(true);
        if(event.getAction() == Action.RIGHT_CLICK_BLOCK && event.getPlayer().isSneaking()) {
            event.setCancelled(false);
        }
    }
}
