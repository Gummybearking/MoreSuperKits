package com.alkmoeba.superkits;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.alkmoeba.superkits.handlers.KitHandler;
import com.alkmoeba.superkits.handlers.TimeStampHandler;
import com.alkmoeba.superkits.objects.Kit;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public class SuperKits extends JavaPlugin implements Listener{

	private static PluginLogger log;
	private static HashMap<Player, ArrayList<Kit>> lastSelected;
	private static String mainDirectory;
	private static File kitsFile, timestampsFile;
	private static Permission perms;
	private static Chat chat;

	public void onEnable(){
		log = new PluginLogger(this);
		Bukkit.getPluginManager().registerEvents(this, this);
		
		mainDirectory = getDataFolder().getPath();
		kitsFile = new File(mainDirectory + "/kits.yml");
		timestampsFile = new File(mainDirectory + "/timestamps.yml");
		new File(mainDirectory).mkdir();
		
		try{
			if(!kitsFile.exists()){
				kitsFile.createNewFile();
				getConfig().options().copyDefaults(true);
				getConfig().save(kitsFile);
			}
			KitHandler.load(YamlConfiguration.loadConfiguration(kitsFile));
			getLogger().info("Loaded kits from SuperKits/kits.yml");
			
			
			if(!timestampsFile.exists())
				timestampsFile.createNewFile();
			TimeStampHandler.load(YamlConfiguration.loadConfiguration(timestampsFile));
			getLogger().info("Loaded timestamps from SuperKits/timestamps.yml");
		}
		catch(Exception e){e.printStackTrace();}
		lastSelected = new HashMap<Player, ArrayList<Kit>>();
		
		perms = Bukkit.getServicesManager().getRegistration(Permission.class).getProvider();
		if(perms != null)
			getLogger().info("Hooked into Permissions manager: " + perms.getName());
		chat = Bukkit.getServicesManager().getRegistration(Chat.class).getProvider();
		if(chat != null)
			getLogger().info("Hooked into Chat manager: " + chat.getName());
		
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable(){ public void run(){
			for(World world : Bukkit.getWorlds())
				for(Player player : world.getPlayers()){
					if(!lastSelected.containsKey(player))
						lastSelected.put(player, new ArrayList<Kit>());
					for(Kit kit : lastSelected.get(player)){
						if(kit != null && kit.infiniteEffects)
							for(PotionEffect effect : kit.effects){
								for(PotionEffect active : player.getActivePotionEffects())
									if(effect.getType().getId() == active.getType().getId() && effect.getAmplifier() >= active.getAmplifier())
										player.removePotionEffect(active.getType());
								player.addPotionEffect(effect);
							}
					}
				}
		} }, 0, 20);
	}

	public void onDisable() {
		try{
			TimeStampHandler.config.save(timestampsFile);
		}
		catch(Exception e){ e.printStackTrace(); }
		if(perms != null)
			for(Player player : lastSelected.keySet())
				for(Kit kit : lastSelected.get(player))
					for(String permission : kit.permissions)
						perms.playerRemove(player.getWorld(), player.getName(), permission);
	}

	public static PluginLogger logger(){
		return log;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		
		if(command.getName().equals("givekit")){
	
			if (args.length < 2){
				sender.sendMessage(ChatColor.ITALIC + getCommand("givekit").getUsage());
				return true;
			}
			Player target = Bukkit.getPlayer(args[0]);
			if(!KitHandler.isKit(args[1])){
				sender.sendMessage(ChatColor.ITALIC + "That kit does not exist");
				return true;
			}
			Kit kit = KitHandler.getKit(args[1]);
			giveKit(target, kit, Context.COMMAND_GIVEN);
			target.sendMessage(ChatColor.ITALIC + "You have been given the " + kit.name + " kit");
			sender.sendMessage(ChatColor.ITALIC + target.getName() + " has been given the " + kit.name + " kit");
			return true;
		}
		
		if(command.getName().equals("reloadkits")){
			KitHandler.load(YamlConfiguration.loadConfiguration(kitsFile));
			sender.sendMessage(ChatColor.ITALIC + "Kits have been reloaded");
		}
		
		if(command.getName().equals("kitinfo")){
			if(args.length < 1)
				return false;
			Kit kit = KitHandler.getKit(args[0]);
			sender.sendMessage(ChatColor.ITALIC + "Kit info for " + kit.name);
			sender.sendMessage(ChatColor.ITALIC + "Items:");
			for(ItemStack item : kit.items)
				sender.sendMessage(ChatColor.ITALIC + "  - " + InventoryUtil.itemToString(item));
			sender.sendMessage(ChatColor.ITALIC + "Effects:");
			for(PotionEffect effect : kit.effects)
				sender.sendMessage(ChatColor.ITALIC + "  - " + InventoryUtil.effectToString(effect));
			sender.sendMessage(ChatColor.ITALIC + "Clear inventory: " + kit.clear);
			sender.sendMessage(ChatColor.ITALIC + "Infinite effects: " + kit.infiniteEffects);
			sender.sendMessage(ChatColor.ITALIC + "Timeout: " + kit.timeout);
			sender.sendMessage(ChatColor.ITALIC + "Global timeout: " + kit.globalTimeout);
			sender.sendMessage(ChatColor.ITALIC + "Ignore command perms " + kit.noCmdPerms);
			sender.sendMessage(ChatColor.ITALIC + "Ignore sign perms " + kit.noSignPerms);
			sender.sendMessage(ChatColor.ITALIC + "Parent: " + kit.parent);
			sender.sendMessage(ChatColor.ITALIC + "Require parent perms: " + kit.requireParentPerms);
			sender.sendMessage(ChatColor.ITALIC + "Inherit parent perms: " + kit.inheritParentPerms);
			sender.sendMessage(ChatColor.ITALIC + "Permissions:");
			for(String perm : kit.permissions)
				sender.sendMessage(ChatColor.ITALIC + "  - " + perm);
			sender.sendMessage(ChatColor.ITALIC + "Prefix: " + kit.prefix);
			sender.sendMessage(ChatColor.ITALIC + "Suffix: " + kit.suffix);
		}

		if(command.getName().equals("kitlist")){
			sender.sendMessage(ChatColor.GREEN + "Available kits:");
			for(Kit kit : KitHandler.getKits())
				if(hasKitPerms(sender, kit) && (!kit.items.isEmpty() || !kit.effects.isEmpty())){
					String message = ChatColor.ITALIC + " - " + kit.name;
					if(kit.clear)
						message += " - clears inventory";
					if(kit.timeout > 0)
						message += " - timeout: " + kit.timeout;
					sender.sendMessage(message);
				}
			if(sender.hasPermission("superkits.take"))
				sender.sendMessage(ChatColor.GREEN + "Use '/kit <kitname>' to take a kit.");
		}
		if (!(sender instanceof Player))
			return true;
		Player player = (Player) sender;

		if(command.getName().equals("kit")){
			if (args.length < 1)
				return false;
			if(!KitHandler.isKit(args[0])){
				sender.sendMessage(ChatColor.ITALIC + "That kit does not exist");
				return true;
			}
			Kit kit = KitHandler.getKit(args[0]);
			giveKit(player, kit, Context.COMMAND_TAKEN);
			return true;
		}

		

		return true;
	}



	@EventHandler
	public void cleanse(PlayerDeathEvent event){
		Player player = event.getEntity();
		if(!lastSelected.get(player).isEmpty())
			for(Kit kit : lastSelected.get(player)){
				for(ItemStack stack : kit.items)
					if(stack.getTypeId() == 383){
						removePets(player);
						return;
					}
				if(perms != null)
					for(String permission : kit.permissions)
						perms.playerRemove(player, permission);
			}
		lastSelected.get(player).clear();
	}
	
	@SuppressWarnings("deprecation")
	public Result giveKit(Player player, Kit kit, Context context){
		getLogger().info("Giving kit " + kit + " to player " + player.getName() + " with context " + context.name());
		if(kit == null){
			getLogger().info("Failed to give kit: FAIL_NULL_KIT");
			return Result.FAIL_NULL_KIT;
		}
		
		//Check for kit permissions
		boolean hasKitPerms = hasKitPerms(player, kit);
		if(!hasKitPerms && context != Context.COMMAND_GIVEN && !(context == Context.SIGN_TAKEN && kit.noSignPerms) && !(context == Context.COMMAND_TAKEN && kit.noCmdPerms)){
			player.sendMessage(ChatColor.ITALIC + "You don't have permission to take the " + kit.name + " kit");
			getLogger().info("Failed to give kit: FAIL_NO_PERMS");
			return Result.FAIL_NO_PERMS;
		}
		
		//Give parent kit first
		Kit parentKit = KitHandler.getKit(kit.parent);
		boolean hasParent = parentKit != null;
		if(hasParent){
			getLogger().info("Adding contents of parent kit: " + parentKit.name);
			for(ItemStack stack : parentKit.items){
				if(!kit.containsItem(stack))
					kit.items.add(stack);
				else if(!InventoryUtil.armor.containsKey(stack.getType()) && !InventoryUtil.weapons.containsKey(stack.getType()))
					kit.items.add(stack);
			}
			for(PotionEffect effect : parentKit.effects)
				if(!kit.containsEffect(effect))
					kit.effects.add(effect);
			kit.permissions.addAll(parentKit.permissions);
			if(!player.hasPermission("superkits.notimeout") && context != Context.COMMAND_GIVEN){
				switch(timeoutCheck(player, parentKit)){
				case FAIL_TIMEOUT:
					player.sendMessage(ChatColor.ITALIC + "You need to wait " + timeoutLeft(player, kit) + " more seconds before using a " + parentKit.name + " kit");
					getLogger().info("Failed to give kit: FAIL_TIMEOUT");
					return Result.FAIL_TIMEOUT;
				case FAIL_SINGLE_USE:
					player.sendMessage(ChatColor.ITALIC + "You can only use a " + parentKit.name + " kit once");
					getLogger().info("Failed to give kit: FAIL_SINGLE_USE");
					return Result.FAIL_SINGLE_USE;
				default:
					break;
				}
			}
		}
		
		//Check for timeouts
		if(!player.hasPermission("superkits.notimeout") && context != Context.COMMAND_GIVEN){
			switch(timeoutCheck(player, kit)){
			case FAIL_TIMEOUT:
				player.sendMessage(ChatColor.ITALIC + "You need to wait " + timeoutLeft(player, kit) + " more seconds before using the " + kit.name + " kit");
				getLogger().info("Failed to give kit: FAIL_TIMEOUT");
				return Result.FAIL_TIMEOUT;
			case FAIL_SINGLE_USE:
				player.sendMessage(ChatColor.ITALIC + "You can only use the " + kit.name + " kit once");
				getLogger().info("Failed to give kit: FAIL_SINGLE_USE");
				return Result.FAIL_SINGLE_USE;
			default:
				break;
			}
		}
		
		GiveKitEvent kitEvent = new GiveKitEvent(player, kit, context);
		kitEvent.callEvent();
		if (kitEvent.isCancelled()){
			getLogger().info("Failed to give kit: FAIL_CANCELLED");
			return Result.FAIL_CANCELLED;
		}
		
		//Clear inventory/effects if necessary
		if(kit.clear){
			player.getInventory().clear();
			player.getInventory().setArmorContents(null);
			for(PotionEffect effect : player.getActivePotionEffects())
				player.removePotionEffect(effect.getType());
			if(!lastSelected.get(player).isEmpty())
				for(Kit last : lastSelected.get(player)){
					for(ItemStack stack : last.items)
						if(stack.getTypeId() == 383)
							removePets(player);
					if(perms != null)
						for(String permission : kit.permissions)
							perms.playerRemove(player.getWorld(), player.getName(), permission);
				}
			lastSelected.get(player).clear();
		}
		
		InventoryUtil.addItemsToInventory(player, kitEvent.getKit().items);
		player.updateInventory();
		player.addPotionEffects(kitEvent.getKit().effects);
		if(perms != null)
			for(String permission : kit.permissions)
				perms.playerAdd(player.getWorld(), player.getName(), permission);
		if(chat != null){
			if(!kit.prefix.isEmpty())
				chat.setPlayerPrefix(player, kit.prefix);
			if(!kit.suffix.isEmpty())
				chat.setPlayerSuffix(player, kit.suffix);
		}
		TimeStampHandler.setTimeStamp(kit.globalTimeout ? null : player, kit);
		lastSelected.get(player).add(kit);
		if(context == Context.COMMAND_TAKEN || context == Context.SIGN_TAKEN)
			player.sendMessage(ChatColor.ITALIC + kit.name + " kit taken");
		if(context == Context.COMMAND_GIVEN)
			player.sendMessage(ChatColor.ITALIC + "You were given the " + kit.name + " kit");
		getLogger().info("Successfully gave kit");
		return Result.SUCCESS;
	}
	
	public enum Context{
		COMMAND_GIVEN,
		PARENT_GIVEN,
		PARENT_TAKEN,
		COMMAND_TAKEN,
		SIGN_TAKEN,
	}
	
	public enum Result{
		SUCCESS,
		FAIL_NULL_KIT,
		FAIL_NO_PERMS,
		FAIL_TIMEOUT,
		FAIL_SINGLE_USE,
		FAIL_CANCELLED,
	}
	
	public void removePets(Player player){
		for(LivingEntity entity : player.getWorld().getLivingEntities())
			if(entity instanceof Tameable){
				Tameable pet = (Tameable) entity;
				if(pet.getOwner() == null || pet.getOwner().equals(player))
					entity.remove();
			}
	}
		
	public static ArrayList<Kit> getLastSelected(Player player){
		return lastSelected.get(player);
	}

	public boolean hasKitPerms(CommandSender player, Kit kit){
		if(kit == null)
			return false;
		if(player.hasPermission("superkits.kit.*"))
			return true;
		boolean hasKitPerms = player.hasPermission("superkits.kit." + kit.name);
		Kit parentKit = KitHandler.getKit(kit.parent);
		if(parentKit != null){
			boolean hasParentPerms = player.hasPermission("superkits.kit." + parentKit.name);
			if(kit.inheritParentPerms && hasParentPerms)
				hasKitPerms = hasParentPerms;
			if(kit.requireParentPerms)
				return hasKitPerms && hasParentPerms;
		}
		return hasKitPerms;
	}
	
	public Result timeoutCheck(Player player, Kit kit){
		long timestamp = TimeStampHandler.getTimeStamp(kit.globalTimeout ? null : player, kit);
		long timeout = player.hasPermission("superkits.shorttimeout") && !kit.globalTimeout ? kit.timeout / 3 : kit.timeout;
		if(System.currentTimeMillis() < timestamp + (timeout * 1000))
			return Result.FAIL_TIMEOUT;	
		if(kit.timeout < 0)
			return Result.FAIL_SINGLE_USE;
		return Result.SUCCESS;
	}
	
	public int timeoutLeft(Player player, Kit kit){
		long timestamp = TimeStampHandler.getTimeStamp(kit.globalTimeout ? null : player, kit);
		long timeout = player.hasPermission("superkits.shorttimeout") && !kit.globalTimeout ? kit.timeout / 3 : kit.timeout;
		return (int)(((timestamp + (timeout * 1000)) - System.currentTimeMillis()) / 1000);
	}
	
}
