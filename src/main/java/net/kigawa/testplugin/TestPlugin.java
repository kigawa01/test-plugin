package net.kigawa.testplugin;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;
import java.util.List;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("enable " + getName());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void breakTreeEvent(BlockBreakEvent event) {
        if (!event.getBlock().getType().equals(Material.OAK_LOG)) return;
        var tree = new LinkedList<Block>();
        if (!addLog(tree, event.getBlock())) return;

    }

    private boolean addLog(List<Block> tree, Block block) {
        if (tree.contains(block)) return false;
        if (!block.getType().equals(Material.OAK_LOG)) return false;
        tree.add(block);

        var onGround = false;
        for (BlockFace face : BlockFace.values())
            if (addLog(tree, block.getRelative(face))) onGround = true;
        var type = block.getRelative(BlockFace.DOWN).getType();

        if (type.equals(Material.DIRT) | type.equals(Material.GRASS)) onGround = true;
        return onGround;
    }

    private boolean isLog(Material material) {
        if (material.equals(Material.OAK_LOG)) return true;
        if (material.equals(Material.ACACIA_LOG)) return true;
        return false;
    }
}

class TreeBreaker {
    private final int maxX;
    private final int maxY;
    private final int maxZ;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final LinkedList<Block> tree = new LinkedList<>();
    private boolean onGround;
    private boolean hasLeaf;

    public TreeBreaker(Block block, int size, int high) {
        maxX = block.getX() + size;
        maxY = block.getY() + high;
        maxZ = block.getZ() + size;
        minX = block.getX() + size;
        minY = block.getY() + high;
        minZ = block.getZ() + size;

        relativeLog(block);
    }

    private void relativeLog(Block block) {
        if (allowBlock(block)) return;
        var material = block.getType();

        if (!isLog(material)) return;

        tree.add(block);

        for (BlockFace face : BlockFace.values()) {
            relativeLog(block.getRelative(face));
            relativeLeaf(block.getRelative(face));
        }
        if (isDirt(block.getRelative(BlockFace.DOWN).getType())) onGround = true;
    }

    private void relativeLeaf(Block block) {
        if (!allowBlock(block)) return;
        var material = block.getType();

        if (!isLeaf(block.getBlockData())) return;

        tree.add(block);
        for (BlockFace face : BlockFace.values()) {
            relativeLeaf(block.getRelative(face));
        }

        hasLeaf = true;
    }

    private boolean allowBlock(Block block) {
        if (tree.contains(block)) return false;
        if (minX > block.getX() || maxX < block.getX()) return false;
        if (minY > block.getY() || maxX < block.getY()) return false;
        if (minZ > block.getZ() || maxZ < block.getZ()) return false;
        return true;
    }

    private boolean isLog(Material material) {
        if (material.equals(Material.OAK_LOG)) return true;
        if (material.equals(Material.ACACIA_LOG)) return true;
        return false;
    }

    private boolean isLeaf(BlockData blockData) {
        return blockData instanceof Leaves;
    }

    private boolean isDirt(Material material) {
        return material.equals(Material.DIRT);
    }
}
