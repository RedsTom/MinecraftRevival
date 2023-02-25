package me.redstom.revival.plugin.block;

import lombok.*;
import me.redstom.revival.api.block.BlockType;
import org.bukkit.Location;

import java.util.UUID;

@Builder
@NoArgsConstructor @AllArgsConstructor

@Getter @Setter

public class CustomBlock {

    private UUID uniqueId;
    private Location location;
    private BlockType blockType;
}
