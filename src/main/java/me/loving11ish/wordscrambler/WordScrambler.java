package me.loving11ish.wordscrambler;

import me.loving11ish.wordscrambler.commands.ReloadCommand;
import me.loving11ish.wordscrambler.commands.ScrambleCommand;
import me.loving11ish.wordscrambler.commands.tabCompleter.ScrambleCommandTabCompleter;
import me.loving11ish.wordscrambler.utils.ColorUtils;
import me.loving11ish.wordscrambler.utils.Rewards;
import me.loving11ish.wordscrambler.utils.TaskTimers;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

public final class WordScrambler extends JavaPlugin {

    Logger log = this.getLogger();
    private static WordScrambler plugin;

    public TaskTimers taskTimers;
    public Rewards rewards;
    public List<String> wordsToScramble;
    public String lastScrambledWord;
    public boolean answerSubmitted = false;

    private static Economy econ = null;
    private final PluginDescriptionFile pluginInfo = getDescription();
    private final String pluginVersion = pluginInfo.getVersion();

    @Override
    public void onEnable() {
        plugin = this;

        //Server version compatibility check
        if (!(Bukkit.getServer().getVersion().contains("1.19"))){
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &4This plugin is only supported on the Minecraft versions listed below:"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &41.19.x"));
            log.warning(ColorUtils.translateColorCodes("&6WordScrambler: &4Is now disabling!"));
            log.warning(ColorUtils.translateColorCodes("&4-------------------------------------------"));
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }else {
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &aA supported Minecraft version has been detected"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &6Continuing plugin startup"));
            log.info(ColorUtils.translateColorCodes("&a-------------------------------------------"));
        }
        //Check for vault
        if (!setupEconomy() ) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", pluginInfo.getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Load the configuration
        getConfig().options().copyDefaults(true);
        saveConfig();
        wordsToScramble = getConfig().getStringList("words-to-scramble");

        // Register the rewards
        rewards = new Rewards();

        // Schedule the task to send messages
        taskTimers = new TaskTimers();

        // Register the /scramble command
        getCommand("scramble").setExecutor(new ScrambleCommand());
        getCommand("reloadscramble").setExecutor(new ReloadCommand());

        // Register the tab completer
        getCommand("scramble").setTabCompleter(new ScrambleCommandTabCompleter());

        // Plugin startup message
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin by: &b&lSavageDev & Loving11ish"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3has been loaded successfully"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin Version: &d&l" + pluginVersion));
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));

        // Schedule first word
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {
                taskTimers.startCountdownOne();
            }
        }, 100L);
    }

    @Override
    public void onDisable() {
        stopCountdown();

        // Plugin shutdown message
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin by: &b&lSavageDev & Loving11ish"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3has been disabled successfully"));
        log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Plugin Version: &d&l" + pluginVersion));
        log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
    }

    public void stopCountdown() {
        try {
            if (Bukkit.getScheduler().isCurrentlyRunning(taskTimers.taskId1)||Bukkit.getScheduler().isQueued(taskTimers.taskId1)){
                Bukkit.getScheduler().cancelTask(taskTimers.taskId1);
            }
            if (Bukkit.getScheduler().isCurrentlyRunning(taskTimers.taskId2)||Bukkit.getScheduler().isQueued(taskTimers.taskId2)){
                Bukkit.getScheduler().cancelTask(taskTimers.taskId2);
            }
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        } catch (Exception e) {
            log.info(ColorUtils.translateColorCodes("-------------------------------------------"));
            log.info(ColorUtils.translateColorCodes("&6WordScrambler: &3Stopped Scramble Timers"));
        }
    }

    public String getRandomWord() {
        if (wordsToScramble.isEmpty()) {
            throw new RuntimeException("No words to scramble.");
        }
        int randomIndex = new Random().nextInt(wordsToScramble.size());
        return wordsToScramble.get(randomIndex);
    }

    public String scramble(String word) {
        Random random = new Random();
        char[] chars = word.toCharArray();
        String scrambledWord;
        do {
            for (int i = chars.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                char temp = chars[i];
                chars[i] = chars[j];
                chars[j] = temp;
            }
            scrambledWord = new String(chars);
        } while (scrambledWord.equals(word));
        return scrambledWord;
    }

    public static WordScrambler getPlugin() {
        return plugin;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null || !getServer().getPluginManager().isPluginEnabled("Vault")) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
