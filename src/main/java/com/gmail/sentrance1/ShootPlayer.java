package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

/**
 * Created by Sentrance on 04/09/2016. =)
 */
class ShootPlayer
{
    // ========================================================================
    // FIELDS
    // ========================================================================

    private ScoreboardManager sManager = Bukkit.getScoreboardManager();
    private Scoreboard board = sManager.getNewScoreboard();
    private Objective objective = board.registerNewObjective("The ShootCraft", "dummy");
    private int kills = 0;
    private Score sKills = objective.getScore(ChatColor.DARK_AQUA + "Kills: " + kills);
    private Score sTimeLeft = objective.getScore(ChatColor.DARK_GREEN + "Time left: " + 300);

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    ShootPlayer(Player player)
    {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§b§lThe ShootCraft");
        sTimeLeft.setScore(3);
        sKills.setScore(2);
        objective.getScore("§f").setScore(1);
        player.setScoreboard(board);
    }

    // ========================================================================
    // METHODS
    // ========================================================================

    void newKill()
    {
        this.kills++;
        board.resetScores(sKills.getEntry());
        sKills = objective.getScore(ChatColor.DARK_AQUA + "Kills: " + kills);
        sKills.setScore(2);
    }

    void updateTime(int timeLeft)
    {
        board.resetScores(sTimeLeft.getEntry());
        sTimeLeft = objective.getScore(ChatColor.DARK_GREEN + "Time left: " + timeLeft);
        sTimeLeft.setScore(3);
    }

    void updateDispName(int i)
    {
        if (i >= 0 && i < 5)
            objective.setDisplayName("§b§lThe ShootCraft");
        if (i >= 5 && i < 10)
            objective.setDisplayName("§5§lThe ShootCraft");
        if (i >= 10 && i < 15)
            objective.setDisplayName("§d§lThe ShootCraft");
    }
}
