package com.alkmoeba.superkits.objects;

import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class Kit{

	public String name, parent, prefix, suffix;
	public long timeout;
	public boolean clear, globalTimeout, noSignPerms, noCmdPerms, infiniteEffects, requireParentPerms, inheritParentPerms;
	public List<ItemStack> items;
	public List<PotionEffect> effects;
	public List<String> permissions;
	
	public Kit(String name, long timeout, boolean clear, boolean globalTimeout, boolean noSignPerms, boolean noCmdPerms, boolean infiniteEffects, boolean requireParentPerms, boolean inheritParentPerms, String parent, List<ItemStack> items, List<PotionEffect> effects, List<String> permissions, String prefix, String suffix){
		this.name = name;
		this.timeout = timeout;
		this.clear = clear;
		this.globalTimeout = globalTimeout;
		this.noSignPerms = noSignPerms;
		this.noCmdPerms = noCmdPerms;
		this.infiniteEffects = infiniteEffects;
		this.requireParentPerms = requireParentPerms;
		this.inheritParentPerms = inheritParentPerms;
		this.parent = parent;
		this.items = items;
		this.effects = effects;
		this.permissions = permissions;
		this.prefix = prefix;
		this.suffix = suffix;
	}
	
	public String toString(){
		return name;
	}
	
	public boolean containsItem(ItemStack stack){
		if(stack == null)
			return false;
		for(ItemStack item : items)
			if(item.getType() == stack.getType())
				return true;
		return false;
	}
	
	public boolean containsEffect(PotionEffect potion){
		if(potion == null)
			return false;
		for(PotionEffect effect : effects)
			if(effect.getType() == potion.getType())
				return true;
		return false;
	}
	
	public boolean equals(Object other){
		if(!(other instanceof Kit))
			return false;
		Kit otherKit = (Kit) other;
		return name == otherKit.name;
	}
}
