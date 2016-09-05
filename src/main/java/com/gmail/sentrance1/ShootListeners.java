package com.gmail.sentrance1;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

class ShootListeners implements Listener
{
    // ========================================================================
    // STATIC FIELDS
    // ========================================================================

    private static final double SHOOT_STEP = 0.3D; //Précision de tir
    private static final int SHOOT_MAX_CHECKS = 150; //Distance de tir = SHOOT_MAX_CHECKS * SHOOT_STEP
    private static final double SHOOT_RADIUS = 3D; //Hitbox du tir

    // ========================================================================
    // FIELDS
    // ========================================================================

    private final TheShootCraft plugin;

    // ========================================================================
    // CONSTRUCTOR
    // ========================================================================

    ShootListeners(TheShootCraft plugin)
    {
        this.plugin = plugin;
    }

    // ========================================================================
    // METHODS
    // ========================================================================

    private void launchTrail(Player player)
    {
        final Location loc = player.getEyeLocation().clone(); //On récupère la position des yeux
        final Vector dir = loc.getDirection().normalize().multiply(SHOOT_STEP); //On récupère la direction
        final Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers(); //On récupère les entités du monde
        Set<UUID> hurtedPlayers = new HashSet<>(0);
        Block lastBlock = null;
        int doubleKill = 0;
        for (int i = 0; i < SHOOT_MAX_CHECKS; i++)
        {
            loc.add(dir);
            Block block = loc.getBlock(); //Opti: mieux vos des getBlock que getType
            if (lastBlock == null || !lastBlock.equals(block))
            {
                if (block.getType() != Material.AIR)
                    break;
                lastBlock = block;
            }
            for (Player nearPlayers : onlinePlayers) //On vérifie qu'il y a une entité à côté
                if (nearPlayers.getLocation().distanceSquared(loc) <= SHOOT_RADIUS && nearPlayers != player
                    && !nearPlayers.isDead() && hurtedPlayers.add(nearPlayers.getUniqueId()))
                {
                    if (nearPlayers.getInventory().getChestplate() != null)//Si il a une protection l'enlever
                    {
                        if (nearPlayers.getInventory().getChestplate().getType() == Material.DIAMOND_CHESTPLATE)
                            nearPlayers.getInventory().setChestplate(null);
                    }
                    else //Sinon get rekt noob
                    {
                        nearPlayers.damage(1337);
                        doubleKill++;
                        plugin.getShootPlayer(player).newKill();
                    }
                    if (doubleKill == 2) //Si il y a eu un doublekill
                    {
                        Bukkit.broadcastMessage("Wow, " + player.getName() + " just made a doublekill!");
                        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                    }
                }
            if (i > 2) //Evite de faire pop une particule directement sur le tireur
                player.getWorld().playEffect(loc, Effect.COLOURED_DUST, 5);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public boolean onPlayerInteract(PlayerInteractEvent event)
    {
        Action action = event.getAction();
        if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)
            && event.getMaterial() == Material.ARROW)
        {
            this.launchTrail(event.getPlayer());
            return true;
        }
        return false;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public boolean onFoodLevelChange(FoodLevelChangeEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            event.setCancelled(true);
            Player player = (Player) event.getEntity();
            player.setFoodLevel(20);
            player.setSaturation(5);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        plugin.playerJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        plugin.playerQuit(event.getPlayer());
    }
}
