package me.loving11ish.wordscrambler.utils;

import me.loving11ish.wordscrambler.WordScrambler;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class TaskTimers {

    public Integer taskId1;
    public Integer taskId2;

    WordScrambler wordScramblerPlugin = WordScrambler.getPlugin();
    FileConfiguration config = WordScrambler.getPlugin().getConfig();

    public void startCountdownOne() {
        scrambleTaskSystem();
        taskId1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(WordScrambler.getPlugin(), new Runnable() {
            int time = 600;
            @Override
            public void run() {
                if (time == 0){
                    scrambleTaskSystem();
                    startCountdownTwo();
                    Bukkit.getScheduler().cancelTask(taskId1);
                    return;
                }else {
                    time --;
                }
            }
        }, 0, 20);
    }

    public void startCountdownTwo(){
        taskId2 = Bukkit.getScheduler().scheduleSyncRepeatingTask(WordScrambler.getPlugin(), new Runnable() {
            int time = 600;
            @Override
            public void run() {
                if (time == 0){
                    scrambleTaskSystem();
                    startCountdownOne();
                    Bukkit.getScheduler().cancelTask(taskId2);
                    return;
                }else {
                    time --;
                }

            }
        }, 0, 20);
    }

    private void scrambleTaskSystem(){
        wordScramblerPlugin.answerSubmitted = false;
        String wordToScramble = wordScramblerPlugin.getRandomWord();
        String scrambledWord = wordScramblerPlugin.scramble(wordToScramble);
        String scrambleAnnouncement = config.getString("scramble-announcement");
        String scramblePrefix = config.getString("announcement-prefix");

        // Replace placeholders with actual values
        scrambleAnnouncement = scrambleAnnouncement.replace("%scramble-prefix%", scramblePrefix);
        scrambleAnnouncement = scrambleAnnouncement.replace("%scrambled-word%", scrambledWord);

        // Broadcast the message
        Bukkit.broadcastMessage(ColorUtils.translateColorCodes(scrambleAnnouncement));

        wordScramblerPlugin.lastScrambledWord = wordToScramble;
    }
}
