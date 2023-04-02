package me.loving11ish.wordscrambler.commands;

import me.loving11ish.wordscrambler.WordScrambler;
import me.loving11ish.wordscrambler.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {

    WordScrambler wordScramblerPlugin = WordScrambler.getPlugin();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("reloadscramble")) {
            if(sender instanceof Player) {
                Player p = (Player) sender;
                try {
                    wordScramblerPlugin.reloadConfig();
                    wordScramblerPlugin.taskTimers.startCountdownOne();
                    p.sendMessage(ColorUtils.translateColorCodes("&aConfig saved and reloaded successfully!"));
                    p.sendMessage(ColorUtils.translateColorCodes("&aScramble countdown started!"));
                } catch (Exception e) {
                    sender.sendMessage(ColorUtils.translateColorCodes("&cAn error occurred while reloading the config!"));
                    e.printStackTrace();
                }
                return true;
            }
        }
        return true;
    }
}
