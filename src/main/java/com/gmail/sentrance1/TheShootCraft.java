package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

public class TheShootCraft extends JavaPlugin
{
    // ========================================================================
    // FIELDS
    // ========================================================================

    private int timeLeft = 100;
    private int ticks = 0;
    private HashMap<UUID, ShootPlayer> playerData = new HashMap<UUID, ShootPlayer>();

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
        getLogger().info("Initialization...");
        getServer().getPluginManager().registerEvents(new ShootListeners(playerData), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                ticks++;
                for (ShootPlayer e : playerData.values())
                {
                    e.updateDispName(ticks);
                    if (ticks >= 20)
                    {
                        e.updateTime(timeLeft);
                        if (timeLeft <= 0)
                            Bukkit.shutdown();
                    }
                }
                if (ticks == 20)
                {
                    ticks = 0;
                    timeLeft--;
                }
            }
        }, 0, 1);
        getLogger().info("Initialized!");
    }

    public void onDisable()
    {
        getLogger().info("Disabling...");
        Bukkit.getScheduler().cancelTasks(this);
        getLogger().info("Disabled!");
    }
}
