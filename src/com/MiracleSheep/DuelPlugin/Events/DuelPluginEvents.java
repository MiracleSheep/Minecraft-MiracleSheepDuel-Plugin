package com.MiracleSheep.DuelPlugin.Events;

import com.MiracleSheep.DuelPlugin.DuelPlugin;
import com.MiracleSheep.DuelPlugin.Inventory.DuelSelection;
import com.MiracleSheep.DuelPlugin.Save.ConfigLoader;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.net.http.WebSocket;

public class DuelPluginEvents implements Listener {

    private final DuelPlugin main;

    public DuelPluginEvents(DuelPlugin main) {
        this.main = main;
    }

    //making sure listener sees this
    @EventHandler
    public static void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage(ChatColor.DARK_PURPLE + "[MiracleSheepDuelPlugin] plugin is enabled.");

    }


    @EventHandler
    public void onClick(InventoryClickEvent e) throws ClassCastException {




        if (e.getClickedInventory() == null) {
            return;
        }
        try {
            if (e.getClickedInventory().getHolder() instanceof DuelSelection) {
                e.setCancelled(true);
                Player player = (Player) e.getWhoClicked();
                int slotnum = e.getSlot();
                int kitnum = main.getConfig().getConfigurationSection("Kits").getKeys(false).size();

                if (e.getCurrentItem() == null) {
                    return;
                }

                if (slotnum >= 0 && slotnum < kitnum) {
                    main.sendRequest();
                    main.save.setType(e.getSlot());
                    main.getRequester().closeInventory();


                } else if (slotnum >= kitnum) {
                    player.sendMessage(ChatColor.DARK_RED + "That is not a valid kit");
                }


            }
        } catch (ClassCastException f) {
            f.getStackTrace();
        }

    }


    @EventHandler
    public void onDamage(EntityDamageEvent e) {

        if (!(e.getEntity() instanceof Player)) {return;}


            Player player = (Player) e.getEntity();
        String duelworld = String.valueOf(main.getConfig().getString("worldname"));
        World w = Bukkit.getServer().getWorld("world");
        World d = Bukkit.getServer().getWorld(duelworld);

        if (main.getRequested().getWorld() == main.load.GetCopyWorld() && main.getRequester().getWorld() == main.load.GetCopyWorld() || main.getRequested().getWorld() == main.load.GetWorld() && main.getRequester().getWorld() == main.load.GetWorld()) {

            if (main.getRequester() == player) {
                if (player.getHealth() - e.getDamage() < 0.1) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                    main.dequipkit(player);
                    main.getRequested().sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                    main.dequipkit(main.getRequested());
                    main.getRequester().setFireTicks(0);
                    main.getRequested().setFireTicks(0);
                    main.getRequested().setHealth(20);
                    main.getRequester().setHealth(20);
                    main.getRequested().teleport(w.getSpawnLocation());
                    main.getRequester().teleport(w.getSpawnLocation());
                    main.resetDuelRequest();
                    main.getMultiverseCore().getCore().getMVWorldManager().deleteWorld(main.load.GetCopyWorldName());

                }

            } else if (main.getRequested() == player) {
                if (player.getHealth() - e.getDamage() < 0.1) {
                    e.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You lost... sending you home...");
                    main.dequipkit(player);
                    main.getRequester().sendMessage(ChatColor.GREEN + "You won the duel! Sending you home...");
                    main.dequipkit(main.getRequester());
                    main.getRequester().setFireTicks(0);
                    main.getRequested().setFireTicks(0);
                    main.getRequested().setHealth(20);
                    main.getRequester().setHealth(20);
                    main.getRequested().teleport(w.getSpawnLocation());
                    main.getRequester().teleport(w.getSpawnLocation());
                    main.resetDuelRequest();
                    main.getMultiverseCore().getCore().getMVWorldManager().deleteWorld(main.load.GetCopyWorldName());
                    //main.deleteWorld(main.load.GetCopyWorld().getWorldFolder());
                }
            }



        }

    }
}