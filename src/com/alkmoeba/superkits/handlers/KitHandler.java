package com.alkmoeba.superkits.handlers;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.alkmoeba.superkits.InventoryUtil;
import com.alkmoeba.superkits.SuperKits;
import com.alkmoeba.superkits.objects.Kit;
import com.alkmoeba.superkits.objects.ParseItemException;

public class KitHandler {
	
	private static ArrayList<Kit> kits;
	public static YamlConfiguration config;
    public static void load(YamlConfiguration section){
    	config = section;
    	kits = new ArrayList<Kit>();
    	if (config == null || config.getKeys(false) == null){
    		SuperKits.logger().warning("Did not find any kits to load");
    		return;
    	}
    	Set<String> keys = config.getKeys(false);
    	
    	for (String kitName : keys){
    		ConfigurationSection kitSection = config.getConfigurationSection(kitName);

    		boolean clear = kitSection.getBoolean("clearInventory", true);
    		boolean infiniteEffects = kitSection.getBoolean("infiniteEffects", false);
    		int timeout = 0;
    		boolean global = false, noSignPerms = false, noCommandPerms = false, requireParentPerms = false, inheritParentPerms = true;
    		String parent = "", prefix = "", suffix = "";
    		List<String> permissions = new ArrayList<String>();
    		
    		ConfigurationSection timeoutSection = kitSection.getConfigurationSection("timeout");
    		if(timeoutSection != null){
    			timeout = timeoutSection.getInt("duration", 0);
    			global = timeoutSection.getBoolean("global", false);
    		}

    		ConfigurationSection permsSection = kitSection.getConfigurationSection("permissions");
    		if(permsSection != null){
    			noSignPerms = permsSection.getBoolean("ignoreSignPerms", false);
    			noCommandPerms = permsSection.getBoolean("ignoreCommandPerms", false);
    		}

    		ConfigurationSection inheritance = kitSection.getConfigurationSection("inheritance");
    		if(inheritance != null){
    			requireParentPerms = inheritance.getBoolean("requireParentPerms", false);
    			inheritParentPerms = inheritance.getBoolean("inheritParentPerms", true);
    			parent = inheritance.getString("parent", "");
    		}

    		ConfigurationSection vault = kitSection.getConfigurationSection("vault");
    		if(vault != null){
    			permissions = vault.getStringList("permissions");
    			if(permissions == null)
    				permissions = new ArrayList<String>();
    			prefix = vault.getString("prefix", "");
    			suffix = vault.getString("suffix", "");
    		}

    		List<ItemStack> items = new ArrayList<ItemStack>();
    		for(String str : kitSection.getStringList("items")){
    			try{
    				ItemStack stack = InventoryUtil.parseItem(str);
    				if(stack != null)
    					items.add(stack);
    			}
    			catch(ParseItemException e){
    				e.printStackTrace();
    				continue;
    			}
    		}
    		List<PotionEffect> effects = new ArrayList<PotionEffect>();
    		for(String str : kitSection.getStringList("effects")){
    			try{
    				PotionEffect effect = InventoryUtil.parseEffect(str);
    				if(effect != null)
    					effects.add(effect);
    			}catch(ParseItemException e){
    				e.printStackTrace();
    				continue;
    			}
    		}

    		kits.add(new Kit(kitName, timeout, clear, global, noSignPerms, noCommandPerms, infiniteEffects, requireParentPerms, inheritParentPerms, parent, items, effects, permissions, prefix, suffix));
    		SuperKits.logger().info("Loaded the " + kitName + " kit");
    	}
    }

    public static Kit getKit(String kitName){
    	if(kitName == null || kitName.equals(""))
    		return null;
    	//Check for an identical match first
    	kitName = kitName.replaceAll("_", " ");
    	for(Kit kit : kits)
    		if(kit.name.equalsIgnoreCase(kitName))
    			return kit;
    	//Then check for an abbreviation
    	for(Kit kit : kits)
    		if(kit.name.toLowerCase().startsWith(kitName))
    			return kit;
    	return null;
    }
    
    public static boolean isKit(String kitName){
    	return getKit(kitName) != null;
    }
    
    public static ArrayList<Kit> getKits(){
    	return kits;
    }
    
}
