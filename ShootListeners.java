package com.gmail.sentrance1;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * Created by Sentrance on 05/08/2016.
 */
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
    private HashMap killsPerPlayer = new HashMap();
    private int doubleKill = 0;

    // ========================================================================
    // METHODS
    // ========================================================================
    private void launchTrail(Player player)
    {
        final Location loc = player.getEyeLocation().clone(); //On récupère la position des yeux
        final Vector dir = loc.getDirection().normalize().multiply(SHOOT_STEP); //On récupère la direction
        final Collection<? extends Player> onlinePlayers = Bukkit.getServer().getOnlinePlayers(); //On récupère les entités du monde
        Set<UUID> hurtedPlayers = new HashSet<>();
        Block lastBlock = null;
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
                    && nearPlayers.getHealth() > 0 && hurtedPlayers.add(nearPlayers.getUniqueId()))
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
                        if (!killsPerPlayer.containsKey(player.getUniqueId()))
                            killsPerPlayer.put(player.getUniqueId(), 1);
                        else
                        {
                            int oldKills = (int)killsPerPlayer.get(player.getUniqueId());
                            killsPerPlayer.replace(player.getUniqueId(), ++oldKills);
                        }
                    }
                    if (doubleKill == 2) //Si il y a eu un doublekill
                    {
                        Bukkit.broadcastMessage("Wow, " + player.getName() + " just made a doublekill!");
                        player.getInventory().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
                    }
                    updateKills(player, player.getScoreboard(),
                                player.getScoreboard().getObjective(DisplaySlot.SIDEBAR));//explicit
                }
            if (i > 2) //Evite de faire pop une particule directement sur le tireur
                player.getWorld().playEffect(loc, Effect.COLOURED_DUST, 5);
        }
        doubleKill = 0; //On reset le compteur de doublekill
    }

    private void updateKills(Player player, Scoreboard board, Objective objective)
    {
        for (String line : board.getEntries()) //On update le scoreboard
        {
            if (line.contains("Kills: "))
            {
                board.resetScores(line);
                objective.getScore(ChatColor.DARK_AQUA + "Kills: " + killsPerPlayer.get(player.getUniqueId())).setScore(2);
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public boolean onPlayerShoot(PlayerInteractEvent ClickEvent)
    {
        Action action = ClickEvent.getAction();
        if ((action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR)
            && ClickEvent.getMaterial() == Material.ARROW)
        {
            this.launchTrail(ClickEvent.getPlayer());
            return true;
        }
        return false;
    }
}
