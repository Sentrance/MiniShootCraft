package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Sentrance on 05/08/2016.
 */
public class TheShootCraft extends JavaPlugin
{
    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================
    public TheShootCraft()
    {

    }

    // ========================================================================
    // METHODS
    // ========================================================================
    public void onEnable()
    {
        Bukkit.broadcastMessage("[SHOOTCRAFT]Initialisation...");
        getServer().getPluginManager().registerEvents(new PassivListeners(), this);
        getServer().getPluginManager().registerEvents(new ShootListeners(), this);
        getServer().getPluginManager().registerEvents(new ManageScoreboard(this), this);
        Bukkit.broadcastMessage("[SHOOTCRAFT]Initialisation done.");
    }

    public void onDisable()
    {
        Bukkit.broadcastMessage("[SHOOTCRAFT]Disabling...");
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.broadcastMessage("[SHOOTCRAFT]Disabling done.");
    }
}
