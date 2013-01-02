package com.alkmoeba.superkits.objects;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class S_Location{
	
	public static void configSave(Location loc, ConfigurationSection config){
		config.set("world", loc.getWorld().getName());
		config.set("x", loc.getX());
		config.set("y", loc.getY());
		config.set("z", loc.getZ());
	}
	
	public static Location configLoad(ConfigurationSection config){
		if(config == null)
			return new Location(Bukkit.getWorlds().get(0), 0.0, 64.0, 0.0);
		return new Location(Bukkit.getWorld(config.getString("world", "world")), config.getDouble("x", 0.0), config.getDouble("y", 64.0), config.getDouble("z", 0.0));
	}

	public static String stringSave(Location loc){
		return loc.getWorld().getName() + "@" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
	}
	
	public static Location stringLoad(String str){
		try{
			World world = Bukkit.getWorld(str.substring(0, str.indexOf("@")));
			String[] coords = str.substring(str.indexOf("@") + 1).split(",");
			return new Location(world, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
		}
		catch(Exception e){
			Bukkit.getLogger().severe("Was unable to parse Location from String: " + str);
			return new Location(Bukkit.getWorlds().get(0), 0, 64, 0);
		}
	}
}
