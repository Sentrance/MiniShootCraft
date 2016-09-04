package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by Sentrance on 05/08/2016.
 */
public class TheShootCraft extends JavaPlugin
{
    // ========================================================================
    // FIELDS
    // ========================================================================
    private int timeLeft = 300;
    private int seconde = 0;
    private HashMap<UUID, ShootPlayer> playerData = new HashMap<>();

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
        getServer().getPluginManager().registerEvents(new ShootListeners(playerData), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                seconde++;
                for (ShootPlayer e : playerData.values())
                {
                    e.updateDispName(seconde);
                    if (seconde >= 20)
                    {
                        seconde = 0;
                        timeLeft--;
                        e.updateTime(timeLeft);
                        if (timeLeft <= 0)
                            Bukkit.shutdown();
                    }
                }
            }
        }, 0, 1);
        Bukkit.broadcastMessage("[SHOOTCRAFT]Initialisation done.");
    }

    public void onDisable()
    {
        Bukkit.broadcastMessage("[SHOOTCRAFT]Disabling...");
        Bukkit.getScheduler().cancelTasks(this);
        Bukkit.broadcastMessage("[SHOOTCRAFT]Disabling done.");
    }
}
