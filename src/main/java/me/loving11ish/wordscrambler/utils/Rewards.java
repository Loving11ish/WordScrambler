package me.loving11ish.wordscrambler.utils;

import me.loving11ish.wordscrambler.WordScrambler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Rewards {

    FileConfiguration config = WordScrambler.getPlugin().getConfig();

    public void giveReward(Player player, String word) {
        int length = word.length();
        int reward;
        String smolcmd = "eco give " + player.getName() + " " + config.getInt("reward-short");
        String midcmd = "eco give " + player.getName() + " " + config.getInt("reward-medium");
        String jumbocmd = "eco give " + player.getName() + " " + config.getInt("reward-long");
        if (length < 5) {
            reward = config.getInt("reward-short");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), smolcmd);
        } else if (length < 8) {
            reward = config.getInt("reward-medium");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), midcmd);
        } else {
            reward = config.getInt("reward-long");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), jumbocmd);
        }
        // Give the player the defined reward
        player.sendMessage(ColorUtils.translateColorCodes("&aYou unscrambled the word and got $" + reward));
        // Code to add reward to player's account
    }
}
