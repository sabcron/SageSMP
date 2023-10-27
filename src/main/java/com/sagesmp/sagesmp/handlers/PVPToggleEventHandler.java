package com.sagesmp.sagesmp.handlers;

import com.sagesmp.sagesmp.SageSMP;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PVPToggleEventHandler implements Listener {
    @EventHandler
    public void onPVPToggleEvent(PVPToggleEvent event) {
        boolean pvpEnabled = event.isPvPEnabled();
        final int[] timeLeft = {30 * 60};

        if(pvpEnabled) {
            String cmd1 = "rg flag __global__ -w world pvp allow";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd1);
            String cmd2 = "rg flag __global__ -w world_nether pvp allow";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd2);
            String cmd3 = "rg flag __global__ -w world_the_end pvp allow";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd3);

            String announceCommand = "abx announce 1800s &aPVP &lENABLED &f(" + formatTime(timeLeft[0]) + " left)";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), announceCommand);
        } else {
            String cmd1 = "rg flag __global__ -w world pvp deny";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd1);
            String cmd2 = "rg flag __global__ -w world_nether pvp deny";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd2);
            String cmd3 = "rg flag __global__ -w world_the_end pvp deny";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd3);

            String announceCommand = "abx announce 1800s &cPVP &lDISABLED &f(" + formatTime(timeLeft[0]) + " left)";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), announceCommand);
        }

        BukkitRunnable countdownTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (timeLeft[0] > 0) {
                    timeLeft[0]--;
                    if(pvpEnabled) {
                        String announceCommand = "abx announce 1800s &aPVP &lENABLED &f(" + formatTime(timeLeft[0]) + " left)";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), announceCommand);
                    } else {
                        String announceCommand = "abx announce 1800s &cPVP &lDISABLED &f(" + formatTime(timeLeft[0]) + " left)";
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), announceCommand);
                    }
                } else {
                    this.cancel();
                }
            }
        };

        countdownTask.runTaskTimer(SageSMP.getPlugin(SageSMP.class), 20L, 20L);
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return minutes + "m " + remainingSeconds + "s";
    }
}
