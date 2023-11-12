package com.sagesmp.sagesmp.barMethods;

import com.sagesmp.sagesmp.SageSMP;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class permBarOverrideAnnounce implements Runnable {

    private final SageSMP plugin;
    public BukkitTask task;
    private TextComponent component;
    private int duration;

    public permBarOverrideAnnounce(SageSMP plugin) {
        this.plugin = plugin;
    }

    public void actionbarAnnounce(int duration, String message) {

        this.duration = duration * 20;

        if (message == null || message.isEmpty()) {
            return;
        }

        plugin.permActionBar.stop();

        try {
            task.cancel();
        } catch (Exception ignored) {}

        task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0, 1);

        if (plugin.LegacyColors) {
            component = new TextComponent(ChatColor.translateAlternateColorCodes('&', message));
        } else {
            message = message.replaceAll("&", "§");
            component = new TextComponent(message);
        }

    }

    public void cancelTask() {
        task.cancel();
    }

    @Override
    public synchronized void run() {

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
        }

        duration--;
        if (duration <= 0) {
            try {
                task.cancel();
            } catch (Exception ignored) {}
            plugin.permActionBar.start();
        }
    }

}