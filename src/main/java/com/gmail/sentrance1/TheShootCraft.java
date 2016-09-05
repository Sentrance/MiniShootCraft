package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class TheShootCraft extends JavaPlugin implements Runnable
{

    // ========================================================================
    // FIELDS
    // ========================================================================

    private int secondsLeft = 100;
    private int ticks = 0;
    private final HashMap<UUID, ShootPlayer> playersData = new HashMap<>();

    // ========================================================================
    // METHODS
    // ========================================================================

    @Override
    public void onEnable()
    {
        getLogger().info("Initialization...");
        getServer().getPluginManager().registerEvents(new ShootListeners(this), this);
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0, 1);
        getLogger().info("Initialized!");
    }

    @Override
    public void run()
    {
        ticks++;

        boolean secondsUpdated;
        if (ticks % 20 == 0)
        {
            secondsUpdated = true;
            secondsLeft--;
            if (secondsLeft <= 0)
            {
                Bukkit.shutdown();
                return;
            }
        }
        else
            secondsUpdated = false;

        for (ShootPlayer e : getShootPlayers())
        {
            e.updateDispName(ticks);
            if (secondsUpdated)
                e.updateTime(secondsLeft);
        }
    }

    @Override
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

    public Collection<ShootPlayer> getShootPlayers()
    {
        return playersData.values();
    }

}
