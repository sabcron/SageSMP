package com.sagesmp.sagesmp;

import com.sagesmp.sagesmp.barMethods.permBarOverrideAnnounce;

import com.sagesmp.sagesmp.commands.maincmd;
import com.sagesmp.sagesmp.handlers.PVPToggleEvent;
import com.sagesmp.sagesmp.handlers.PVPToggleEventHandler;
import com.sagesmp.sagesmp.handlers.SageSwordRecipe;
import com.sagesmp.sagesmp.utils.ConfigUpdater;
import com.sagesmp.sagesmp.utils.logging;
import com.sagesmp.sagesmp.utils.tabComplete;
import com.sagesmp.sagesmp.utils.updateCheck;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.CommandExecutor;
import net.md_5.bungee.api.chat.TextComponent;
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
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SageSMP extends JavaPlugin implements Listener {
    boolean pvpEnabled = false;
    int interval = 20 * 60 * 30;
    public BukkitTask task;
    public permBarOverrideAnnounce permBarOverrideAnnounce;
    public PermActionBar permActionBar;
    public boolean LegacyColors = false;

    @Override
    public void onEnable() {
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[SageSMP] SageSMP is enabling...");
        logging.log(logging.LogLevel.INFO, "[SageSMP] Plugin version: " + getDescription().getVersion());

        logging.log(logging.LogLevel.INFO, "[SageSMP] Loading files...");

        File file = new File(getDataFolder(), "config.yml");

        if(file.exists()) {
            try {
                logging.log(logging.LogLevel.INFO, "[SageSMP] Updating config.yml file...");
                ConfigUpdater.update(this, "config.yml", file, Collections.emptyList());
                logging.log(logging.LogLevel.INFO, "[SageSMP] config.yml file up to date!");
            } catch (NullPointerException | IOException e) {
                if(e.getClass().equals(IOException.class)) {
                    e.printStackTrace();
                } else {
                    logging.log(logging.LogLevel.OUTLINE , "**************************************************");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] Caught NullPointerException while updating config.yml file.");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] If you are reloading the plugin, this is not an issue and you may safely ignore this warning.");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] If you have reloaded the server using /reload, that may be the cause, please restart your server instead.");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] In any other case, this is a bug! Please report it to solidsilk on Discord!");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] You should never reload your server using /reload as it can cause issues with plugins.");
                    logging.log(logging.LogLevel.WARNING,"[SageSMP] If you continue experiencing issues, please restart your server.");
                    logging.log(logging.LogLevel.OUTLINE , "**************************************************");
                }
            }
        } else {
            logging.log(logging.LogLevel.INFO, "[SageSMP] Did not detect config.yml file. Generating...");
            getConfig().options().copyDefaults();
            saveDefaultConfig();
            logging.log(logging.LogLevel.INFO, "[SageSMP] config.yml file generated!");
        }

        reloadConfig();
        logging.log(logging.LogLevel.INFO, "[SageSMP] Files loaded successfully!");

        String serverVersion = Bukkit.getVersion();

        if (!serverVersion.contains("1.20")) {
            logging.log(logging.LogLevel.ERROR, "[SageSMP] Detected unsupported version " + serverVersion + ". Disabling plugin to prevent fatal errors and crashes." +
                    "\n If you are on 1.20.x, this is a bug! Please report it to solidsilk on Discord.");
            logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        permBarOverrideAnnounce permBarOverrideAnnounce = new permBarOverrideAnnounce(SageSMP.this);
        tabComplete tabComplete = new tabComplete();

        logging.log(logging.LogLevel.INFO, "[SageSMP] Loading" + " commands...");

        CommandExecutor MainCommandExectuer = new maincmd(this, new permBarOverrideAnnounce(SageSMP.this));

        getCommand("abx").setExecutor(MainCommandExectuer);
        getCommand("abx").setTabCompleter(tabComplete);

        logging.log(logging.LogLevel.INFO, "[SageSMP] Commands loaded successfully!");

        this.permActionBar = new PermActionBar(this);
        List<ChatColor> colors = permActionBar.loadColorsFromConfig();

        permActionBar.start();

        new updateCheck(this, 113253).getLatestVersion(version -> {
            logging.log(logging.LogLevel.INFO, "Asking Spigot API if SageSMP is up to date?...");
            if (this.getDescription().getVersion().equalsIgnoreCase(version)) {
                logging.log(logging.LogLevel.INFO,"Spigot API says SageSMP is up to date.");
            } else {
                logging.log(logging.LogLevel.WARNING, "Spigot API says SageSMP is outdated! Newest version: " + version + ", Your version: " + getDescription().getVersion() + ", Please Update Here: https://www.spigotmc.org/resources/113253/");

            }
        });

        logging.log(logging.LogLevel.SUCCESS, "[SageSMP] SageSMP has been successfully enabled!");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");

        NamespacedKey recipeKey = new NamespacedKey(this, "sagesword_recipe");
        new SageSwordRecipe(this, recipeKey);
        getServer().getPluginManager().registerEvents(new PVPToggleEventHandler(this), this);
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

    public class PermActionBar implements Runnable {
        private final SageSMP plugin;
        public boolean enabled = getConfig().getBoolean("PermanentActionBar.Enable");
        private int tickCounter = 0;
        List<ChatColor> colors;

        public List<ChatColor> loadColorsFromConfig() {
            List<ChatColor> colorList = new ArrayList<>();
            List<String> colorStrings = plugin.getConfig().getStringList("PermanentActionBar.ChatColor");
            for (String colorString : colorStrings) {
                try {
                    ChatColor color = ChatColor.valueOf(colorString.toUpperCase().replace(" ", "_"));
                    colorList.add(color);
                } catch (IllegalArgumentException ignored) {

                }
            }
            return colorList;
        }

        public PermActionBar(SageSMP plugin) {
            this.plugin = plugin;
            this.colors = loadColorsFromConfig();
        }

        public void start() {
            task = getServer().getScheduler().runTaskTimer(plugin, this, 0, getConfig().getInt("PermanentActionBar.duration"));
        }

        public void stop() {
            if(task != null) {
                task.cancel();
            }
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (enabled) {
                start();
            } else {
                stop();
            }
        }

        public TextComponent getActionBarMessage() {

            if (plugin.LegacyColors) {
                return new TextComponent(ChatColor.translateAlternateColorCodes('&', formatActionBarMessage()));
            } else

            if(colors.isEmpty()){
                colors.add(ChatColor.WHITE);
            }

            ChatColor color = colors.get(tickCounter % colors.size());

            tickCounter++;
            String actionBarMessage = formatActionBarMessage().replaceAll("&", "§");
            return new TextComponent(TextComponent.fromLegacyText(color + actionBarMessage));
        }

        private String formatActionBarMessage() {
            StringBuilder sb = new StringBuilder();
            sb.append(getConfig().getString("PermanentActionBar.actionBarMessage"));

            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isBold")) {
                sb.insert(0, ChatColor.BOLD);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isItalic")) {
                sb.insert(0, ChatColor.ITALIC);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isUnderline")) {
                sb.insert(0, ChatColor.UNDERLINE);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isStrikethrough")) {
                sb.insert(0, ChatColor.STRIKETHROUGH);
            }
            if (getConfig().getBoolean("PermanentActionBar.MessageStyles.isMagic")) {
                sb.insert(0, ChatColor.MAGIC);
            }

            return sb.toString();
        }

        @Override
        public void run () {

            if (!enabled) {
                return;
            }

            for (Player player : plugin.getServer().getOnlinePlayers()) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, getActionBarMessage());

            }
        }
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
                    if(item != null && isEnderPearl(item, player)) {
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
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST && event.getCurrentItem() != null && isEnderPearl(event.getCurrentItem(), (Player) event.getWhoClicked()) || event.getInventory().getType() == InventoryType.CHEST && event.getCurrentItem() != null && isEnderPearl(event.getCurrentItem(), (Player) event.getWhoClicked()) || event.getInventory().getType() == InventoryType.SHULKER_BOX && event.getCurrentItem() != null && isEnderPearl(event.getCurrentItem(), (Player) event.getWhoClicked())) {
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

    private boolean isEnderPearl(ItemStack item, Player player) {
        if(item.getType() == Material.ENDER_PEARL) {
            int maxEnderPearls = 16;
            int currentCount = 0;

            for (ItemStack inventoryItem : player.getInventory().getContents()) {
                if (inventoryItem != null && inventoryItem.getType() == org.bukkit.Material.ENDER_PEARL) {
                    currentCount += inventoryItem.getAmount();
                }
            }

            if (currentCount + item.getAmount() > maxEnderPearls) {
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

    @Override
    public void onDisable() {
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
        logging.log(logging.LogLevel.INFO, "[SageSMP] SageSMP is disabling...");

        logging.log(logging.LogLevel.INFO, "[SageSMP] Cancelling Permanent ActionBar task...");
        try {
            permActionBar.setEnabled(false);
        } catch (Exception ignored) {}

        logging.log(logging.LogLevel.INFO, "[SageSMP] Permanent ActionBar task cancelled!");

        logging.log(logging.LogLevel.INFO, "[SageSMP] Cancelling all other tasks...");
        try {
            permBarOverrideAnnounce.cancelTask();
        } catch (Exception ignored) {}

        logging.log(logging.LogLevel.INFO, "[SageSMP] All other tasks cancelled!");

        logging.log(logging.LogLevel.SUCCESS ,"SageSMP has been successfully disabled.");
        logging.log(logging.LogLevel.OUTLINE, "*****************************************************************");
    }
}