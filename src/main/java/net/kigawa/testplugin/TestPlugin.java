package net.kigawa.testplugin;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Leaves;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.LinkedList;

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
        TreeBreaker treeBreaker = new TreeBreaker(event.getBlock(), 10, 20);
        var roots = treeBreaker.getRoot();
        if (roots.isEmpty() || !treeBreaker.hasLeaf()) return;

        ItemStack tool = event.getPlayer().getInventory().getItemInMainHand();
        World world = event.getBlock().getWorld();
        for (Block block : treeBreaker.getTree()) {
            for (ItemStack itemStack : block.getDrops(tool)) {
                world.dropItem(block.getLocation(), itemStack);
            }

            var meta = tool.getItemMeta();
            if (meta instanceof Damageable) {
                var toolMeta = ((Damageable) meta);
                toolMeta.setDamage(toolMeta.getDamage() - 2);
            }

            block.breakNaturally(tool);
        }

        event.getPlayer().getInventory().setItemInMainHand(tool);

        for (Block block : roots) {
            block.setType(Material.OAK_SAPLING);
        }
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
    private boolean hasLeaf;
    private LinkedList<Block> root;

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
        if (isDirt(block.getRelative(BlockFace.DOWN).getType())) {
            root.add(block);
        }
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
        if (minY > block.getY() || maxY < block.getY()) return false;
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

    public LinkedList<Block> getRoot() {
        return root;
    }

    public boolean hasLeaf() {
        return hasLeaf;
    }

    public LinkedList<Block> getTree() {
        return tree;
    }
}
