package com.sagesmp.sagesmp;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class SageSMP extends JavaPlugin implements Listener {
    boolean pvpEnabled = false;
    int interval = 20 * 60 * 30;

    @Override
    public void onEnable() {
        NamespacedKey recipeKey = new NamespacedKey(this, "sagesword_recipe");
        new SageSwordRecipe(this, recipeKey);
        getServer().getPluginManager().registerEvents(new PVPToggleEventHandler(), this);
        getServer().getPluginManager().registerEvents(this, this);

        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                PVPToggleEvent pvpToggleEvent1 = new PVPToggleEvent(pvpEnabled);
                getServer().getPluginManager().callEvent(pvpToggleEvent1);
            }
        };

        task.runTaskLater(this, 2 * 20);

        getServer().getScheduler().scheduleSyncRepeatingTask(this, () -> {
            pvpEnabled = !pvpEnabled;
            PVPToggleEvent pvpToggleEvent = new PVPToggleEvent(pvpEnabled);
            getServer().getPluginManager().callEvent(pvpToggleEvent);
        }, interval, interval);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnderChestInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_CLICK_BLOCK")) {
            if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.ENDER_CHEST) {
                Player player = event.getPlayer();
                for (int slot = 0; slot < player.getEnderChest().getSize(); slot++) {
                    ItemStack item = player.getEnderChest().getItem(slot);
                    if (item != null && isSageSword(item) || item != null && item.getType() == Material.DRAGON_EGG) {
                        player.getEnderChest().setItem(slot, new ItemStack(Material.AIR));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST && event.getCurrentItem() != null && isSageSword(event.getCurrentItem())) {
            event.setCancelled(true);
        }
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST && event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.DRAGON_EGG) {
            event.setCancelled(true);
        }
    }

    private boolean isSageSword(ItemStack item) {
        if (item.getType() == Material.NETHERITE_SWORD) {
            ItemMeta itemMeta = item.getItemMeta();
            if (itemMeta != null && itemMeta.hasDisplayName() && itemMeta.getDisplayName().equals("§5SageSword")) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        ItemStack item = event.getItem().getItemStack();
        if (item.getType() == org.bukkit.Material.ENDER_PEARL) {
            int maxEnderPearls = 16;
            int currentCount = 0;

            for (ItemStack inventoryItem : event.getPlayer().getInventory().getContents()) {
                if (inventoryItem != null && inventoryItem.getType() == org.bukkit.Material.ENDER_PEARL) {
                    currentCount += inventoryItem.getAmount();
                }
            }

            if (currentCount + item.getAmount() > maxEnderPearls) {
                event.setCancelled(true);
            }
        }
    }
}