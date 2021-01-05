# Minecraft-MiracleSheepDuel-Plugin

# Description
This is a duel plugin that lets you duel other players. It is compatible with the multiverse plugin, and teleport the players to another world where they duke it out.  
It also has a kit feature, that allows you to create new kits to equip and duel others with

# Features
* A /duel command which reads the kits in the config.yml and brings up a custom inventory.
* Compatibility with multiworld, name your duel world "Arena" specify the coordinates of you arena in the config
*  a /equip and /unequip kit, capable of saving your inventory.
* a /ktlist and /kititems command, allowing you to see the kits you have in the config and what items they have

# Planned updates
* allows enchanted items as part of a kit
* Making the config easier to understand
* adding golden heads to the game
* general bug fixing

# Update as of January 4th, 2021

## Implemented so far:
* Config is fully organized
* Items can have up to six enchantments
* Amount and durability of each item can be customized
* Duel kit Icons that appear in the selection screen can be customized in the config
* Custom locations and arenas can be set for each kit
* Illegal enchantments can be toggled on and off in the config
* A clone world feature has been added where the plugin will make a clone of your duel world and send the players to that instead, then delete it when the players are done
* depends and uses multiverse API

## To be implemented
* Golden heads(might not, not really neccesary. Was thinking about 1.8.9 when i thought up this feature, but considering that I am using it in 1.16.4, probabbly won't add)
* Bug fixes involving the accept command while in a duel and multiple people dueling at the same time
* a /spectate feature and coordinates in the config where spectators will warp to based on the arena and kit selected
* *General polishing*
