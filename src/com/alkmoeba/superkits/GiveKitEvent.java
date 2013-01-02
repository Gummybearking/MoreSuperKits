package com.alkmoeba.superkits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import com.alkmoeba.superkits.SuperKits.Context;
import com.alkmoeba.superkits.objects.Kit;

public class GiveKitEvent extends Event implements Cancellable {
	private static final HandlerList handlers = new HandlerList();
	private Player player;
	private Kit kit;
	private Context context;
	private boolean cancelled;
 
    public GiveKitEvent(Player player, Kit kit, Context context) {
		this.player = player;
		this.kit = kit;
		this.context = context;
		cancelled = false;
    }
    
    public Player getPlayer(){ return player; }
    public Kit getKit(){ return kit; }
    public Context getContext(){ return context; }
    
    public void setItems(ArrayList<ItemStack> items){ kit.items = items; }
	public void setEffects(ArrayList<PotionEffect> effects) { kit.effects = effects; }
	public void setPermissions(ArrayList<String> permissions){ kit.permissions = permissions; }
  
    public HandlerList getHandlers() { return handlers; } 
    public static HandlerList getHandlerList() { return handlers; }
    public void callEvent(){
    	Bukkit.getServer().getPluginManager().callEvent(this);
    	if(context == Context.COMMAND_GIVEN)
    		cancelled = false;
    }
	
	@Override
	public boolean isCancelled(){ return cancelled; }
	@Override
    public void setCancelled(boolean cancel){ this.cancelled = cancel; }
	
}
