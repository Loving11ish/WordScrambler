package me.loving11ish.wordscrambler.commands.tabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ScrambleCommandTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            // return a list of possible subcommands
            if(sender.hasPermission("scramble.staff")) {
                return Arrays.asList("answer", "start", "stop");
            } else {
                return Arrays.asList("answer");
            }
        }
        // return an empty list if no suggestions are available
        return Collections.emptyList();
    }
}
