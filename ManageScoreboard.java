package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

/**
 * Created by Sentrance on 19/08/2016.
 */
class ManageScoreboard implements Listener
{
    // ========================================================================
    // FIELDS
    // ========================================================================
    private Plugin oPlugin;
    private int i = 0;
    private int firstTime = 0;
    private int timeLeft = 300;

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================
    ManageScoreboard(Plugin plugin)
    {
        this.oPlugin = plugin;
    }

    // ========================================================================
    // METHODS
    // ========================================================================
    private void confScoreboard(Objective objective)
    {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        BukkitScheduler Scheduler = Bukkit.getServer().getScheduler(); // La suite permet de faire un clignotement
        Scheduler.scheduleSyncRepeatingTask(this.oPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                if (i >= 0 && i < 5)
                    objective.setDisplayName("§b§lThe ShootCraft");
                if (i >= 5 && i < 10)
                    objective.setDisplayName("§5§lThe ShootCraft");
                if (i >= 10 && i < 15)
                    objective.setDisplayName("§d§lThe ShootCraft");
                i++;
                if (i >= 15)
                    i = 0;
            }
        }, 20, 20);
        objective.getScore(ChatColor.DARK_GREEN + "Time left: " + 300).setScore(3); //On initialise nos données
        objective.getScore(ChatColor.DARK_AQUA + "Kills: " + 0).setScore(2);
        objective.getScore("§f").setScore(1);
    }

    public void dispTimeLeft(Scoreboard board, Objective objective)
    {
        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.oPlugin, new Runnable()
        {
            @Override
            public void run()
            {
                for (String line : board.getEntries()) //On cherche la ligne qui nous intéresse, on la reset puis on a l'update
                {
                    if (line.contains("Time left: "))
                    {
                        board.resetScores(line);
                        objective.getScore(ChatColor.DARK_GREEN + "Time left: " + timeLeft).setScore(3);
                    }
                }
            }
        }, 0, 1);
    }

    @EventHandler
    public void onGameStart(PlayerJoinEvent playerJoin)
    {
        ScoreboardManager sManager = Bukkit.getScoreboardManager();
        Scoreboard board = sManager.getNewScoreboard();
        Objective objective = board.registerNewObjective("The ShootCraft", "dummy");
        confScoreboard(objective);
        dispTimeLeft(board, objective);
        if (firstTime == 0)
        {
            Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this.oPlugin, new Runnable()
            {
                @Override
                public void run()
                {
                    timeLeft--;
                    if (timeLeft <= 0)
                        Bukkit.shutdown();
                }
            }, 0, 20);
            firstTime = 1;
        }
        Player player = playerJoin.getPlayer();
        player.setScoreboard(board);
    }
}
