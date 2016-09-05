package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
    private final HashMap<UUID, ShootPlayer> playersData = new HashMap<UUID, ShootPlayer>();

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
        getServer().getPluginManager().registerEvents(new ShootListeners(this), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
        {
            @Override
            public void run()
            {
                ticks++;
                for (ShootPlayer e : playersData.values())
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

    // ------------------------------------------------------------------------
    // Players data
    // ------------------------------------------------------------------------

    public void playerJoin(Player player)
    {
        playersData.put(player.getUniqueId(), new ShootPlayer(player));
    }

    public void playerQuit(Player player)
    {
        playersData.remove(player.getUniqueId());
    }

    public ShootPlayer getShootPlayer(Player player)
    {
        return playersData.get(player.getUniqueId());
    }

}
