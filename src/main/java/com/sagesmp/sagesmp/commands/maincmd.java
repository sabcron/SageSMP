package com.sagesmp.sagesmp.commands;

import com.sagesmp.sagesmp.SageSMP;
import com.sagesmp.sagesmp.barMethods.permBarOverrideAnnounce;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class maincmd implements CommandExecutor {

    private final permBarOverrideAnnounce plugin1;
    private final SageSMP plugin;

    public maincmd(SageSMP plugin, permBarOverrideAnnounce plugin1) {
        this.plugin = plugin;
        this.plugin1 = plugin1;
    }

    String AvailableCommands =
            ChatColor.AQUA + "Available Commands:\n" +
                    ChatColor.RESET + "/abx announce " + ChatColor.YELLOW + "[duration] <message>" + ChatColor.GRAY + " - Announces a message to all players through the actionbar.";

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if(strings.length >= 2) {
            if (strings[0].equalsIgnoreCase("announce") ||
                    strings[0].equalsIgnoreCase("broadcast") ||
                    strings[0].equalsIgnoreCase("bc") &&
                            strings[1] != null) {
                if (commandSender.hasPermission("abx.announce")) {
                    if(plugin.getConfig().getBoolean("Announcements.Enable")) {
                        int duration = plugin.getConfig().getInt("Announcements.defaultDuration");
                        if (strings[1].matches("\\d+s")) {

                            duration = Integer.parseInt(strings[1].substring(0, strings[1].length() - 1));

                            strings = java.util.Arrays.copyOfRange(strings, 2, strings.length);

                        } else {
                            strings = java.util.Arrays.copyOfRange(strings, 1, strings.length);
                            commandSender.sendMessage(ChatColor.AQUA + "[SageSMP] " + ChatColor.RESET + ChatColor.RED + "Duration not specified, using default duration of " + plugin.getConfig().getInt("Announcements.defaultDuration") + " seconds.");
                        }

                        String message = String.join(" ", strings);
                        plugin1.actionbarAnnounce(duration, message);
                    } else {
                        commandSender.sendMessage(ChatColor.AQUA + "[SageSMP] " + ChatColor.RESET + ChatColor.RED + "Announcements are disabled in the configuration file.");
                    }
                } else {
                    commandSender.sendMessage(ChatColor.RED + "Insufficient Permission.");
                }
                return true;
            }
        } else if(strings.length == 1 && strings[0].equalsIgnoreCase("announce")) {
            commandSender.sendMessage(ChatColor.AQUA + "[SageSMP] " + ChatColor.RESET + ChatColor.RED + "You must specify a message.");
            return true;
        }



        switch(strings.length) {

            case 0:
                commandSender.sendMessage(AvailableCommands);
                break;

            default:
                commandSender.sendMessage(AvailableCommands);
                break;

        }

        return false;
    }
}