package com.MiracleSheep.DuelPlugin;
import com.MiracleSheep.DuelPlugin.Events.DuelPluginEvents;
import com.MiracleSheep.DuelPlugin.Inventory.DuelSelection;
import com.MiracleSheep.DuelPlugin.Save.ConfigLoader;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import com.onarandombox.MultiverseCore.api.MVWorldManager;
import com.onarandombox.MultiverseCore.api.MultiverseWorld;
import com.onarandombox.MultiverseCore.api.WorldPurger;
import com.onarandombox.MultiverseCore.utils.PurgeWorlds;
import com.onarandombox.MultiverseCore.utils.WorldManager;
import com.sun.tools.javac.Main;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.*;

//main plugin class
public class DuelPlugin extends JavaPlugin implements CommandExecutor {

    public MultiverseCore getMultiverseCore() {
        Plugin plugin = getServer().getPluginManager().getPlugin("Multiverse-Core");

        if (plugin instanceof MultiverseCore) {
            return (MultiverseCore) plugin;
        }
        throw new RuntimeException("Multiverse not found!");
    }


    public int Worldnum = 0;
    public Acceptclass save = new Acceptclass(this);
    public Player target;
    public Player player;
    public boolean duelready = false;
    public ConfigLoader load = new ConfigLoader(this);


    //function that gets called when the plugin is enabled
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DuelPluginEvents(this), this);
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[MiracleSheepDuelPlugin] plugin is enabled.");

    }


    //function that gets called on disable
    @Override
    public void onDisable() {
        try {
        } catch (Exception e) {
            e.printStackTrace();
        }
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "[MiracleSheepDuelPlugin] plugin is disabled.");

    }

    //function what gets called when a player executes a command
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {


        if (!(sender instanceof Player)) {
            sender.sendMessage("Only Players can use that command");
            return true;
        }

        player = (Player) sender;


        if (cmd.getName().equalsIgnoreCase("Kitlist")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            String s = String.valueOf(getConfig().getConfigurationSection("Kits").getKeys(false));
            player.sendMessage(ChatColor.GOLD + "The kits are:");
            player.sendMessage(ChatColor.GOLD + s);
        }


        if (cmd.getName().equalsIgnoreCase("KitItems")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            if (args.length == 1) {
                String KitName = args[0];
                int itemList = getConfig().getStringList("Kits." + KitName + ".Items").size();
                player.sendMessage(ChatColor.GOLD + "The kit items are:");
                String[] Items = new String[getConfig().getStringList("Kits." + KitName + ".Items").size()];

                for (int i = 0; i < itemList; i++) {
                    Items[i] = getConfig().getStringList("Kits." + KitName + ".Items").get(i);
                }


                for (int i = 0; i < itemList; i++) {
                    int length = Items[i].length();
                    int findspace = Items[i].indexOf(" ");
                    String Item = Items[i].substring(0, findspace);
                    String Items2 = Items[i].substring(findspace + 1);
                    int findspace2 = Items2.indexOf(" ");
                    Short meta = Short.parseShort(Items2.substring(0, findspace2));
                    String Items3 = Items2.substring(findspace2 + 1);
                    int findspace3 = Items3.indexOf(" ");
                    int amount = Integer.parseInt(Items3.substring(0, findspace3));


                    player.sendMessage(ChatColor.GOLD + "-" + Item + "x" + amount);

                }


                player.sendMessage(ChatColor.GOLD + "End of list.");


            } else {
                player.sendMessage(ChatColor.GOLD + "This command has the wrong number of arguments.");
            }
        }


        if (cmd.getName().equalsIgnoreCase("Kitequip")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            if (!sender.hasPermission("Duel.kits")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            if (args.length == 1) {

                equipkit(player, args[0]);

            } else {
                player.sendMessage(ChatColor.DARK_RED + "This command has the wrong number of arguments.");
            }
        }


        if (cmd.getName().equalsIgnoreCase("Kitunequip")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            if (!sender.hasPermission("Duel.kits")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permis`sion to perform this command");
                return true;
            }

            player.getInventory().clear();
            restoreInventory(player);
        }


        if (cmd.getName().equalsIgnoreCase("Duel")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }

            if (args.length == 1) {
                try {
                    target = Bukkit.getPlayerExact(args[0]);

                if (player == target) {
                    player.sendMessage(ChatColor.DARK_RED + "Are you stupid!? You can't duel yourself!");
                    return true;
                }

                duelready = true;
                Worldnum += 1;
                saveDuelRequest(Worldnum);
                    int length = getConfig().getString("worldname").length();
                    String name = getConfig().getString("worldname");


                    if (player.getWorld().getName().startsWith(name)){
                        player.sendMessage(ChatColor.DARK_RED + "You are already in a duel!");
                        Worldnum -= 1;
                        return true;
                    }else if (target.getWorld().getName().startsWith(name)) {
                        player.sendMessage(ChatColor.DARK_RED + "That person is already in a duel!");
                        Worldnum -= 1;
                        return true;
                    }
                } catch (Throwable e) {
                    player.sendMessage(ChatColor.DARK_RED + "That is not a valid player!");
                    return true;
                }

                player.openInventory(returnGui().getInventory());

            } else {
                player.sendMessage(ChatColor.DARK_RED + "Too many or not enough arguments");
                player.sendMessage(ChatColor.DARK_RED + "Use /duel <playername>");
            }
        }


        if (cmd.getName().equalsIgnoreCase("accept")) {
            if (!sender.hasPermission("Duel.all")) {
                player.sendMessage(ChatColor.DARK_RED + "You do not have permission to perform this command");
                return true;
            }


            if (getRequested(Worldnum) == player) {

                int length = getConfig().getString("worldname").length();
                String name = getConfig().getString("worldname");


                if (player.getWorld().getName().startsWith(name)){
                    player.sendMessage(ChatColor.DARK_RED + "You are already in a duel!");
                    return true;
                }

                if (duelready == false) {
                    player.sendMessage(ChatColor.DARK_RED + "You must send another duel request!");
                    return true;
                }
                duelready = false;

                if (load.Clone() == true) {

                    getMultiverseCore().getCore().getMVWorldManager().cloneWorld(load.GetWorldName(),load.GetCopyWorldName());
                    getRequester(Worldnum).teleport(load.getPlayerOneSpawn(getConfig().getString("Kits." + save.dueltype + ".Arena")));
                    getRequested(Worldnum).teleport(load.getPlayerTwoSpawn(getConfig().getString("Kits." + save.dueltype + ".Arena")));

                } else {
                    getRequester(Worldnum).teleport(load.getPlayerOneSpawn(getConfig().getString("Kits." + save.dueltype + ".Arena")));
                    getRequested(Worldnum).teleport(load.getPlayerTwoSpawn(getConfig().getString("Kits." + save.dueltype + ".Arena")));

                }

                getRequested(Worldnum).setHealth(20);
                getRequester(Worldnum).setHealth(20);
                getRequester(Worldnum).setFoodLevel(20);
                getRequested(Worldnum).setFoodLevel(20);
                equipkit(getRequested(Worldnum), save.dueltype);
                equipkit(getRequester(Worldnum), save.dueltype);

            } else {
                player.sendMessage(ChatColor.DARK_RED + "No one has sent you a duel request!");

            }


        }

        return true;
    }


    public void saveInventory(Player player) {
        File f = new File(this.getDataFolder().getAbsolutePath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("inventory.armor", player.getInventory().getArmorContents());
        c.set("inventory.content", player.getInventory().getContents());
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void restoreInventory(Player player) {
        File f = new File(this.getDataFolder().getAbsolutePath(), player.getName() + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        ItemStack[] content = ((List<ItemStack>) c.get("inventory.armor")).toArray(new ItemStack[0]);
        player.getInventory().setArmorContents(content);
        content = ((List<ItemStack>) c.get("inventory.content")).toArray(new ItemStack[0]);
        player.getInventory().setContents(content);
    }


    public void saveDuelRequest(int num) {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel" + num + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("requested", target.getName());
        c.set("requester", player.getName());
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetDuelRequest(int num) {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel" + num + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("requested", "None");
        c.set("requester", "None");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Player getRequested(int num) {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel" + num + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        String name = (String) c.get("requested");
        Player requested = Bukkit.getPlayer(name);

        return requested;
    }

    public Player getRequester(int num) {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel" + num + ".yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        String name = (String) c.get("requester");
        Player requester = Bukkit.getPlayer(name);
        return requester;
    }


    public void equipkit(Player player, String type) {
        saveInventory(player);
        player.getInventory().clear();
        String[] Items = new String[getConfig().getStringList("Kits." + type + ".Items").size()];

        for (int i = 0; i < getConfig().getStringList("Kits." + type + ".Items").size(); i++) {
            Items[i] = getConfig().getStringList("Kits." + type + ".Items").get(i);
        }

        for (int i = 0; i < getConfig().getStringList("Kits." + type + ".Items").size(); i++) {

            int length = Items[i].length();
            int findspace = Items[i].indexOf(" ");
            String Item = Items[i].substring(0, findspace);

            String Items2 = Items[i].substring(findspace + 1);
            int findspace2 = Items2.indexOf(" ");
            Short meta = Short.parseShort(Items2.substring(0, findspace2));

            String Items3 = Items2.substring(findspace2 + 1);
            int findspace3 = Items3.indexOf(" ");
            int amount = Integer.parseInt(Items3.substring(0, findspace3));

            ItemStack stack = new ItemStack(Material.getMaterial(Item), amount, meta);
            ItemMeta metaa = stack.getItemMeta();

            String Items4 = Items3.substring(findspace3 + 1);
            int findspace4 = Items4.indexOf(" ");
            String Enchant = Items4.substring(0, findspace4);


            if (Enchant.startsWith("n")) {

            } else {

                String Items5 = Items4.substring(findspace4 + 1);
                int findspace5 = Items5.indexOf(" ");
                int lv = Integer.parseInt(Items5.substring(0, findspace5));

                Enchantment enchant = Enchantment.getByName(Enchant);
                metaa.addEnchant(enchant, lv, load.IllegalEnchants());


                String Items6 = Items5.substring(findspace5 + 1);
                int findspace6 = Items6.indexOf(" ");
                String Enchant2 = Items6.substring(0, findspace6);
                if (Enchant2.startsWith("n")) {


                } else {

                    String Items7 = Items6.substring(findspace6 + 1);
                    int findspace7 = Items7.indexOf(" ");
                    int lv2 = Integer.parseInt(Items7.substring(0, findspace7));

                    Enchantment enchant2 = Enchantment.getByName(Enchant2);
                    metaa.addEnchant(enchant2, lv2, load.IllegalEnchants());


                    String Items8 = Items7.substring(findspace7 + 1);
                    int findspace8 = Items8.indexOf(" ");
                    String Enchant3 = Items8.substring(0, findspace8);


                    if (Enchant3.startsWith("n")) {

                    } else {


                        String Items9 = Items8.substring(findspace8 + 1);
                        int findspace9 = Items9.indexOf(" ");
                        int lv3 = Integer.parseInt(Items9.substring(0, findspace9));


                        Enchantment enchant3 = Enchantment.getByName(Enchant3);
                        metaa.addEnchant(enchant3, lv3, load.IllegalEnchants());

                        String Items10 = Items9.substring(findspace9 + 1);
                        int findspace10 = Items10.indexOf(" ");
                        String Enchant4 = Items10.substring(0, findspace10);

                        if (Enchant4.startsWith("n")) {

                        } else {


                            String Items11 = Items10.substring(findspace10 + 1);
                            int findspace11 = Items9.indexOf(" ");
                            int lv4 = Integer.parseInt(Items9.substring(0, findspace9));


                            Enchantment enchant4 = Enchantment.getByName(Enchant4);
                            metaa.addEnchant(enchant4, lv4, load.IllegalEnchants());


                            String Items12 = Items11.substring(findspace11 + 1);
                            int findspace12 = Items12.indexOf(" ");
                            String Enchant5 = Items12.substring(0, findspace12);

                            if (Enchant5.startsWith("n")) {

                            } else {


                                String Items13 = Items12.substring(findspace12 + 1);
                                int findspace13 = Items13.indexOf(" ");
                                int lv5 = Integer.parseInt(Items13.substring(0, findspace13));


                                Enchantment enchant5 = Enchantment.getByName(Enchant5);
                                metaa.addEnchant(enchant5, lv5, load.IllegalEnchants());


                                String Items14 = Items13.substring(findspace13 + 1);
                                int findspace14 = Items14.indexOf(" ");
                                String Enchant6 = Items14.substring(0, findspace14);

                                if (Enchant6.startsWith("n")) {

                                } else {

                                    String Items15 = Items14.substring(findspace14 + 1);
                                    int findspace15 = Items15.indexOf(" ");
                                    int lv6 = Integer.parseInt(Items15.substring(0, findspace15));


                                    Enchantment enchant6 = Enchantment.getByName(Enchant6);
                                    metaa.addEnchant(enchant6, lv6, load.IllegalEnchants());
                                }


                            }

                        }


                    }


                }


            }


            stack.setItemMeta(metaa);
            player.getInventory().addItem(stack);


        }



    }


    public void dequipkit(Player player) {
        player.getInventory().clear();
        restoreInventory(player);
    }

    public void sendRequest() {
        target.sendMessage(ChatColor.DARK_RED + "You have been duel requested by " + player.getName() + ".");
        target.sendMessage(ChatColor.DARK_RED + "Please type /accept to accept the duel request and be teleported");
    }

    public DuelSelection returnGui() {
        DuelSelection gui = new DuelSelection(this);
        return (gui);
    }


    public boolean deleteWorld(File path) {
        player.sendMessage(ChatColor.DARK_RED + "Deleteworld function triggered");
        if (path.exists()) {
            File files[] = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }



    public void copyWorld(File source, File target){
        try {
            ArrayList<String> ignore = new ArrayList<String>(Arrays.asList("uid.dat", "session.dat"));
            if(!ignore.contains(source.getName())) {
                if(source.isDirectory()) {
                    if(!target.exists())
                        target.mkdirs();
                    String files[] = source.list();
                    for (String file : files) {
                        File srcFile = new File(source, file);
                        File destFile = new File(target, file);
                        copyWorld(srcFile, destFile);
                    }
                } else {
                    InputStream in = new FileInputStream(source);
                    OutputStream out = new FileOutputStream(target);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) > 0)
                        out.write(buffer, 0, length);
                    in.close();
                    out.close();
                }
            }
        } catch (IOException e) {

        }
    }




}








