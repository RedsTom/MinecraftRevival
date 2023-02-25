package me.redstom.revival.plugin.repository;

import me.redstom.revival.api.Load;
import me.redstom.revival.api.block.BlockType;
import me.redstom.revival.api.block.BlockTypeManager;
import me.redstom.revival.api.repository.Repository;
import me.redstom.revival.api.repository.RepositoryManager;
import me.redstom.revival.plugin.block.CustomBlockTypes;
import me.redstom.revival.plugin.block.CustomBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

@Load
public class CustomBlockRepo implements Repository<CustomBlock> {

    private static final CustomBlockRepo instance = new CustomBlockRepo();

    public static void register() {
        RepositoryManager.getInstance().register(instance);
    }

    private CustomBlockRepo() {

    }

    private Connection conn;

    @Override
    public void hydrate(Connection connection) {
        this.conn = connection;
    }

    @Override
    public void create() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                CREATE TABLE IF NOT EXISTS CustomBlock(
                    uniqueId VARCHAR(36) UNIQUE NOT NULL,
                    x DOUBLE NOT NULL,
                    y DOUBLE NOT NULL,
                    z DOUBLE NOT NULL,
                    world VARCHAR(255) NOT NULL,
                    blockType VARCHAR(255) NOT NULL,
                    PRIMARY KEY (x, y, z, world)
                );
                """);

        stmt.execute();
        stmt.close();
    }

    @Override
    public void drop() throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                DROP TABLE IF EXISTS CustomBlock;
                """);

        stmt.execute();
        stmt.close();
    }

    @Override
    public void insert(CustomBlock object) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                INSERT INTO CustomBlock (uniqueId, x, y, z, world, blockType) VALUES (?, ?, ?, ?, ?, ?);
                """);

        stmt.setString(1, object.uniqueId().toString());
        stmt.setDouble(2, object.location().getX());
        stmt.setDouble(3, object.location().getY());
        stmt.setDouble(4, object.location().getZ());
        stmt.setString(5, object.location().getWorld().getName());
        stmt.setString(6, object.blockType().name());

        stmt.execute();
        stmt.close();
    }

    @Override
    public void update(CustomBlock object) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                UPDATE CustomBlock SET blockType=? WHERE x=? AND y=? AND z=? AND world=?;
                """);

        stmt.setString(1, object.blockType().name());
        stmt.setDouble(2, object.location().getX());
        stmt.setDouble(3, object.location().getY());
        stmt.setDouble(4, object.location().getZ());
        stmt.setString(5, object.location().getWorld().getName());

        stmt.execute();
        stmt.close();
    }

    @Override
    public void delete(CustomBlock object) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                DELETE FROM CustomBlock WHERE x=? AND y=? AND z=? AND world=?;
                """);

        stmt.setDouble(1, object.location().getX());
        stmt.setDouble(2, object.location().getY());
        stmt.setDouble(3, object.location().getZ());
        stmt.setString(4, object.location().getWorld().getName());

        stmt.execute();
        stmt.close();
    }

    public Optional<CustomBlock> findByLocation(Location location) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("""
                SELECT * FROM CustomBlock WHERE x=? AND y=? AND z=? AND world=?;
                """);

        stmt.setDouble(1, location.getX());
        stmt.setDouble(2, location.getY());
        stmt.setDouble(3, location.getZ());
        stmt.setString(4, location.getWorld().getName());

        ResultSet rs = stmt.executeQuery();

        if (!rs.next()) {
            return Optional.empty();
        }

        World world = Bukkit.getWorld(rs.getString("world"));
        Location loc = new Location(world, rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z"));

        UUID uuid = UUID.fromString(rs.getString("uniqueId"));

        Optional<BlockType> blockType = BlockTypeManager.getInstance().valueOf(rs.getString("blockType"));

        stmt.close();
        rs.close();

        return Optional.of(CustomBlock.builder()
                .uniqueId(uuid)
                .location(loc)
                .blockType(blockType.orElse(null))
                .build());
    }
}
