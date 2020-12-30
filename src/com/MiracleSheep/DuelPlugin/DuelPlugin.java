package com.MiracleSheep.DuelPlugin;
import com.MiracleSheep.DuelPlugin.Events.DuelPluginEvents;
import com.MiracleSheep.DuelPlugin.Inventory.DuelSelection;
import com.MiracleSheep.DuelPlugin.Save.ConfigLoader;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;


public class DuelPlugin extends JavaPlugin implements CommandExecutor {


    public Acceptclass save = new Acceptclass(this);
    public Player target;
    public Player player;
    public ConfigLoader load = new ConfigLoader(this);

    //function that gets called when the plugin is enabled
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new DuelPluginEvents(this), this);
        resetDuelRequest();
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

                for(int i = 0; i < itemList; i++) {
                    Items[i] = getConfig().getStringList("Kits." + KitName + ".Items").get(i);
                }


                for (int i = 0 ; i < itemList ; i++) {
                    int length = Items[i].length();
                    int findspace = Items[i].indexOf(" ");
                    String Item = Items[i].substring(0,findspace);
                    String Items2 = Items[i].substring(findspace+1);
                    int findspace2 = Items2.indexOf(" ");
                    Short meta = Short.parseShort(Items2.substring(0,findspace2));
                    String Items3 = Items2.substring(findspace2 + 1);
                    int findspace3 = Items3.indexOf(" ");
                    int amount = Integer.parseInt(Items3.substring(0,findspace3));


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

            equipkit(player,args[0]);

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
                target = Bukkit.getPlayerExact(args[0]);
                if (player == target) {
                    player.sendMessage(ChatColor.DARK_RED + "Are you stupid!? You can't duel yourself!");
                    return true;}


                if (getRequested() == target && getRequester() == player) {
                    player.sendMessage(ChatColor.DARK_RED + "Sorry, but you already duel requested someone.");
                    return true;
                }

                saveDuelRequest();
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


            if (getRequested() == player) {
                getRequested().setHealth(20);
                getRequester().setHealth(20);
                equipkit(getRequested(), save.dueltype);
                equipkit(getRequester(), save.dueltype);
                getRequester().teleport(load.getPlayerOneSpawn("ArenaOne"));
                getRequested().teleport(load.getPlayerTwoSpawn("ArenaOne"));



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


    public void saveDuelRequest() {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("requested", target.getName());
        c.set("requester", player.getName());
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resetDuelRequest() {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        c.set("requested", "None");
        c.set("requester", "None");
        try {
            c.save(f);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public Player getRequested() {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        String name = (String) c.get("requested");
        Player requested = Bukkit.getPlayer(name);

        return requested;
    }

    public Player getRequester() {
        File f = new File(this.getDataFolder().getAbsolutePath(), "Duel.yml");
        FileConfiguration c = YamlConfiguration.loadConfiguration(f);
        String name = (String) c.get("requester");
        Player requester = Bukkit.getPlayer(name);
        return requester;
    }




    public void equipkit(Player player, String type) {
                saveInventory(player);
                player.getInventory().clear();
                String[] Items = new String[getConfig().getStringList("Kits." + type + ".Items").size()];

                for(int i = 0; i < getConfig().getStringList("Kits." + type + ".Items").size(); i++) {
                    Items[i] = getConfig().getStringList("Kits." + type + ".Items").get(i);
                }

                for(int i = 0; i < getConfig().getStringList("Kits." + type + ".Items").size(); i++) {

                    int length = Items[i].length();
                    int findspace = Items[i].indexOf(" ");
                    String Item = Items[i].substring(0,findspace);

                    String Items2 = Items[i].substring(findspace+1);
                    int findspace2 = Items2.indexOf(" ");
                    Short meta = Short.parseShort(Items2.substring(0,findspace2));

                    String Items3 = Items2.substring(findspace2 + 1);
                    int findspace3 = Items3.indexOf(" ");
                    int amount = Integer.parseInt(Items3.substring(0,findspace3));

                    ItemStack stack = new ItemStack(Material.getMaterial(Item), amount, meta);

                    String Items4 = Items3.substring(findspace3 + 1);
                    int findspace4 = Items4.indexOf(" ");
                    String Enchant = Items4.substring(0,findspace4);
                    player.sendMessage(ChatColor.DARK_RED + Enchant);

                    if (Enchant.startsWith("n")) {
                        player.sendMessage(ChatColor.DARK_RED + "Enchant is equal to none");
                    } else {
                        player.sendMessage(ChatColor.DARK_RED + "Triggered");
                        String Items5 = Items4.substring(findspace4 + 1);
                        int findspace5 = Items5.indexOf(" ");
                        int lv = Integer.parseInt(Items5.substring(0,findspace5));

                        Enchantment enchant = Enchantment.getByName(Enchant);
                        stack.addEnchantment(enchant, lv);


                        String Items6 = Items5.substring(findspace5 + 1);
                        int findspace6 = Items6.indexOf(" ");
                        String Enchant2 = Items6.substring(0,findspace6);
                        player.sendMessage(ChatColor.DARK_RED + "Enchant2: " + Enchant2);
                        if (Enchant2.startsWith("n")) {




                        } else {

                            player.sendMessage(ChatColor.DARK_RED + "Second enchant Triggered");
                            String Items7 = Items6.substring(findspace6 + 1);
                            player.sendMessage(ChatColor.DARK_RED + "made new substr");
                            int findspace7 = Items7.indexOf(" ");
                            player.sendMessage(ChatColor.DARK_RED + "retreived location of nearest space");
                            int lv2 = Integer.parseInt(Items7.substring(0,findspace7));
                            player.sendMessage(ChatColor.DARK_RED + "declared lv2");

                            player.sendMessage(ChatColor.DARK_RED + "lv2: " + lv2);
                            Enchantment enchant2 = Enchantment.getByName(Enchant2);
                            stack.addEnchantment(enchant2, lv2);
                            player.sendMessage(ChatColor.DARK_RED + "Enchanted succesfully");

                            String Items8 = Items7.substring(findspace7 + 1);
                            player.sendMessage(ChatColor.DARK_RED + "Items8 created");
                            int findspace8 = Items8.indexOf(" ");
                            player.sendMessage(ChatColor.DARK_RED + "Finding space eight: " + findspace8);
                            String Enchant3 = Items8.substring(0,findspace8);
                            player.sendMessage(ChatColor.DARK_RED + "determining next enchant: " + Enchant3);
                            player.sendMessage(ChatColor.DARK_RED + "found next enchant");

                            if (Enchant3.startsWith("n")) {
                                player.sendMessage(ChatColor.DARK_RED + "No more enchants");
                            } else {
                                player.sendMessage(ChatColor.DARK_RED + "third enchant Triggered");

                                String Items9 = Items8.substring(findspace8 + 1);
                                int findspace9 = Items9.indexOf(" ");
                                int lv3 = Integer.parseInt(Items9.substring(0,findspace9));


                                Enchantment enchant3 = Enchantment.getByName(Enchant3);
                                stack.addEnchantment(enchant3, lv3);

                                String Items10 = Items9.substring(findspace9 + 1);
                                int findspace10 = Items10.indexOf(" ");
                                String Enchant4 = Items10.substring(0,findspace10);

                                if (Enchant4.startsWith("n")) {

                                } else {


                                    String Items11 = Items10.substring(findspace10 + 1);
                                    int findspace11 = Items9.indexOf(" ");
                                    int lv4 = Integer.parseInt(Items9.substring(0,findspace9));


                                    Enchantment enchant4 = Enchantment.getByName(Enchant4);
                                    stack.addEnchantment(enchant4, lv4);



                                    String Items12 = Items11.substring(findspace11 + 1);
                                    int findspace12 = Items12.indexOf(" ");
                                    String Enchant5 = Items12.substring(0,findspace12);

                                        if (Enchant5.startsWith("n")) {

                                        } else {


                                            String Items13 = Items12.substring(findspace12 + 1);
                                            int findspace13 = Items13.indexOf(" ");
                                            int lv5 = Integer.parseInt(Items13.substring(0,findspace13));


                                            Enchantment enchant5 = Enchantment.getByName(Enchant5);
                                            stack.addEnchantment(enchant5, lv5);



                                            String Items14 = Items13.substring(findspace13 + 1);
                                            int findspace14 = Items14.indexOf(" ");
                                            String Enchant6 = Items14.substring(0,findspace14);

                                            if (Enchant6.startsWith("n")) {

                                            } else {

                                                String Items15 = Items14.substring(findspace14 + 1);
                                                int findspace15 = Items15.indexOf(" ");
                                                int lv6 = Integer.parseInt(Items15.substring(0,findspace15));


                                                Enchantment enchant6 = Enchantment.getByName(Enchant6);
                                                stack.addEnchantment(enchant6, lv6);
                                            }


                                        }

                                }



                            }




                        }



                    }



                    player.getInventory().addItem(stack);




                }
               /* if (it.hasNext()) {
                    while (it.hasNext()) {
                        String[] item = Items.split(" ");
                        String id = item[0];
                        short meta = (short) Integer.parseInt(item[1]);
                        int amount = Integer.parseInt(item[2]);
                        ItemStack stack = new ItemStack(Material.getMaterial(id), amount, meta);
                        if (!item[3].contains("none")) {
                            int level = Integer.parseInt(item[4]);
                            Enchantment enchant = Enchantment.getByName(item[3].toUpperCase());
                            stack.addEnchantment(enchant, level);

                            if (!item[5].contains("none")) {

                                int level2 = Integer.parseInt(item[6]);
                                Enchantment enchant2 = Enchantment.getByName(item[5].toUpperCase());
                                stack.addEnchantment(enchant, level);


                                if (!item[7].contains("none")) {

                                    int lv2 = Integer.parseInt(item[8]);
                                    Enchantment e2 = Enchantment.getByName(item[7].toUpperCase());
                                    stack.addEnchantment(enchant, level);

                                }



                            }

                        }
                        player.getInventory().addItem(stack);
                    }
                }*/
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
        return(gui);
    }



}












