package me.redstom.revival.api.block;

import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface BlockManager {

    BlockManager DEFAULT = new BlockManager() {};

    default void onBlockPlace(BlockPlaceEvent event) {}
    default void onBlockBreak(BlockBreakEvent event) {}

    default void onBlockInteract(PlayerInteractEvent event) {}

}
