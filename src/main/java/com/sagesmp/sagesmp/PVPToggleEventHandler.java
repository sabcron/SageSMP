package com.sagesmp.sagesmp;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class PVPToggleEventHandler implements Listener {
    @EventHandler
    public void onPVPToggleEvent(PVPToggleEvent event) {
        boolean pvpEnabled = event.isPvPEnabled();
        final int[] timeLeft = {30 * 60};

        if(pvpEnabled) {
            String world1 = "rg flag __global__ -w world pvp allow";
            String world2 = "rg flag __global__ -w world_nether pvp allow";
            String world3 = "rg flag __global__ -w world_the_end pvp allow";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world1);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world2);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world3);
            String announceCommand = "abx announce 1800s &aPVP &lENABLED &f(" + formatTime(timeLeft[0]) + " left)";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), announceCommand);
        } else {
            String world1 = "rg flag __global__ -w world pvp deny";
            String world2 = "rg flag __global__ -w world_nether pvp deny";
            String world3 = "rg flag __global__ -w world_the_end pvp deny";
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world1);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world2);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), world3);
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
