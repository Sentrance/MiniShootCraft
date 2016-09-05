package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

class ShootPlayer
{
    // ========================================================================
    // STATIC FIELDS
    // ========================================================================

    private static final String GAME_NAME = "The ShootCraft";

    // ========================================================================
    // FIELDS
    // ========================================================================

    private ScoreboardManager sManager = Bukkit.getScoreboardManager();
    private Scoreboard board = sManager.getNewScoreboard();
    private Objective objective = board.registerNewObjective(GAME_NAME, "dummy");
    private int kills = 0;
    private Score sKills = objective.getScore(ChatColor.DARK_AQUA + "Kills: " + kills);
    private Score sTimeLeft = objective.getScore(ChatColor.DARK_GREEN + "Time left: " + 300);

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    ShootPlayer(Player player)
    {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(ChatColor.AQUA.toString() + ChatColor.BOLD + GAME_NAME);
        sTimeLeft.setScore(3);
        sKills.setScore(2);
        objective.getScore(ChatColor.WHITE.toString()).setScore(1);
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

    void updateDispName(final int ticks)
    {
        final ChatColor color;

        int secTicks = ticks % 20;
        if (secTicks < 5)
            color = ChatColor.AQUA;
        else if (secTicks < 10)
            color = ChatColor.DARK_PURPLE;
        else if (secTicks < 15)
            color = ChatColor.LIGHT_PURPLE;
        else
            return;
        objective.setDisplayName(color.toString() + ChatColor.BOLD + GAME_NAME);
    }
}
