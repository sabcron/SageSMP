package com.sagesmp.sagesmp.utils;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class tabComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> tabCompletions = new ArrayList<>();

        if (strings.length == 1) {
            tabCompletions.addAll(Arrays.asList("announce"));
            return tabCompletions;
        } else if (strings[0].equalsIgnoreCase("announce") || strings[0].equalsIgnoreCase("broadcast") || strings[0].equalsIgnoreCase("bc")) {
            if (strings.length == 2) {
                tabCompletions.addAll(Arrays.asList("3s", "4s", "5s", "6s", "7s", "8s", "9s", "10s"));
                return tabCompletions;
            }
        }

        return null;
    }
}
