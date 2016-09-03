package com.gmail.sentrance1;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/**
 * Created by Sentrance on 05/08/2016.
 */
class PassivListeners implements Listener
{
    // ========================================================================
    // METHODS
    // ========================================================================
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public boolean onPlayerDecreasingFood(FoodLevelChangeEvent foodEvent)
    {
        if (foodEvent.getEntity() instanceof Player)
        {
            foodEvent.setCancelled(true);
            Player player = (Player) foodEvent.getEntity();
            player.setFoodLevel(20);
            player.setSaturation(5);
            return true;
        }
        return false;
    }

}
