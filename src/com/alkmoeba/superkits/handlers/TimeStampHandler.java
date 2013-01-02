package com.alkmoeba.superkits.handlers;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.alkmoeba.superkits.objects.Kit;

public class TimeStampHandler {

	public static YamlConfiguration config;
	public static void load(YamlConfiguration section){
		config = section;
	}

	public static long getTimeStamp(Player player, Kit kit){
		String sectionName = player == null ? "global" : player.getName();
		ConfigurationSection playerSection = config.getConfigurationSection(sectionName);
		if(playerSection == null){
			config.createSection(sectionName);
			return 0;
		}
		return playerSection.getLong(kit.name, 0);
	}

	public static void setTimeStamp(Player player, Kit kit){
		String sectionName = player == null ? "global" : player.getName();
		ConfigurationSection playerSection = config.getConfigurationSection(sectionName);
		if(playerSection == null)
			playerSection = config.createSection(sectionName);
		playerSection.set(kit.name, System.currentTimeMillis());
	}
	
}
