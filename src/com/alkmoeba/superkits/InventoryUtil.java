package com.alkmoeba.superkits;

import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;

import net.milkbowl.vault.item.Items;
import net.milkbowl.vault.item.ItemInfo;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.alkmoeba.superkits.InventoryUtil.Armor.ArmorLevel;
import com.alkmoeba.superkits.InventoryUtil.Armor.ArmorType;
import com.alkmoeba.superkits.InventoryUtil.Weapon.WeaponLevel;
import com.alkmoeba.superkits.InventoryUtil.Weapon.WeaponType;
import com.alkmoeba.superkits.objects.ParseItemException;

public class InventoryUtil {

	public static class Armor{
		public ArmorLevel lvl;
		public ArmorType type;
		Armor(ArmorType type, ArmorLevel lvl){this.lvl=lvl; this.type=type;}
		public enum ArmorLevel{WOOL,LEATHER,GOLD,CHAIN,IRON,DIAMOND};
		public enum ArmorType{HELM,CHEST,LEGGINGS,BOOTS};
	}
	public static final HashMap<Material,Armor> armor;
	static {
		armor = new HashMap<Material,Armor>();
		
		armor.put(Material.WOOL,new Armor(ArmorType.HELM, ArmorLevel.WOOL));
		armor.put(Material.LEATHER_HELMET,new Armor(ArmorType.HELM, ArmorLevel.LEATHER));
		armor.put(Material.GOLD_HELMET,new Armor(ArmorType.HELM, ArmorLevel.GOLD));
		armor.put(Material.CHAINMAIL_HELMET,new Armor(ArmorType.HELM, ArmorLevel.CHAIN));
		armor.put(Material.IRON_HELMET,new Armor(ArmorType.HELM, ArmorLevel.IRON));
		armor.put(Material.DIAMOND_HELMET,new Armor(ArmorType.HELM, ArmorLevel.DIAMOND));

		armor.put(Material.LEATHER_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.LEATHER));
		armor.put(Material.GOLD_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.GOLD));
		armor.put(Material.CHAINMAIL_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.CHAIN));
		armor.put(Material.IRON_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.IRON));
		armor.put(Material.DIAMOND_CHESTPLATE,new Armor(ArmorType.CHEST,ArmorLevel.DIAMOND));

		armor.put(Material.LEATHER_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.LEATHER));
		armor.put(Material.GOLD_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.GOLD));
		armor.put(Material.CHAINMAIL_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.CHAIN));
		armor.put(Material.IRON_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.IRON));
		armor.put(Material.DIAMOND_LEGGINGS,new Armor(ArmorType.LEGGINGS,ArmorLevel.DIAMOND));
		
		armor.put(Material.LEATHER_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.LEATHER));
		armor.put(Material.GOLD_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.GOLD));
		armor.put(Material.CHAINMAIL_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.CHAIN));
		armor.put(Material.IRON_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.IRON));
		armor.put(Material.DIAMOND_BOOTS,new Armor(ArmorType.BOOTS,ArmorLevel.DIAMOND));
	}
	
	public static class Weapon{
		public WeaponLevel lvl;
		public WeaponType type;
		Weapon(WeaponType type, WeaponLevel lvl){this.lvl=lvl; this.type=type;}
		public enum WeaponLevel{WOOD,STONE,GOLD,IRON,DIAMOND};
		public enum WeaponType{AXE,BOW,SWORD};
	}
	public static final HashMap<Material,Weapon> weapons;
	static {
		weapons = new HashMap<Material,Weapon>();
		
		weapons.put(Material.WOOD_SWORD,new Weapon(WeaponType.SWORD, WeaponLevel.WOOD));
		weapons.put(Material.STONE_SWORD,new Weapon(WeaponType.SWORD, WeaponLevel.STONE));
		weapons.put(Material.GOLD_SWORD,new Weapon(WeaponType.SWORD, WeaponLevel.GOLD));
		weapons.put(Material.IRON_SWORD,new Weapon(WeaponType.SWORD, WeaponLevel.IRON));
		weapons.put(Material.DIAMOND_SWORD,new Weapon(WeaponType.SWORD, WeaponLevel.DIAMOND));
		
		weapons.put(Material.WOOD_AXE,new Weapon(WeaponType.AXE, WeaponLevel.WOOD));
		weapons.put(Material.STONE_AXE,new Weapon(WeaponType.AXE, WeaponLevel.STONE));
		weapons.put(Material.GOLD_AXE,new Weapon(WeaponType.AXE, WeaponLevel.GOLD));
		weapons.put(Material.IRON_AXE,new Weapon(WeaponType.AXE, WeaponLevel.IRON));
		weapons.put(Material.DIAMOND_AXE,new Weapon(WeaponType.AXE, WeaponLevel.DIAMOND));

		weapons.put(Material.BOW,new Weapon(WeaponType.BOW, WeaponLevel.WOOD));
	}
	
	public static class EnchantmentWithLevel{
		public Enchantment enc;
		public Integer lvl;
		public String toString(){return(enc!=null?enc.getName():"null")+":"+lvl;}
	}
	public static Enchantment getEnchantmentByCommonName(String name){
		name = name.toLowerCase();
		if(name.contains("fire") && name.contains("prot")) return Enchantment.PROTECTION_FIRE;
		if((name.contains("exp") || name.contains("blast")) && name.contains("prot")) return Enchantment.PROTECTION_EXPLOSIONS;
		if((name.contains("arrow") || name.contains("proj")) && name.contains("prot")) return Enchantment.PROTECTION_PROJECTILE;
		if(name.contains("prot")) return Enchantment.PROTECTION_ENVIRONMENTAL;
		if(name.contains("fall")) return Enchantment.PROTECTION_FALL;
		if(name.contains("respiration")) return Enchantment.OXYGEN;
		if(name.contains("aqua")) return Enchantment.WATER_WORKER;
		if(name.contains("sharp")) return Enchantment.DAMAGE_ALL;
		if(name.contains("smite")) return Enchantment.DAMAGE_UNDEAD;
		if(name.contains("arth")) return Enchantment.DAMAGE_ARTHROPODS;
		if(name.contains("knockback")) return Enchantment.KNOCKBACK;
		if(name.contains("fire")) return Enchantment.FIRE_ASPECT;
		if(name.contains("loot")) return Enchantment.LOOT_BONUS_MOBS;
		if(name.contains("power")) return Enchantment.ARROW_DAMAGE;
		if(name.contains("punch")) return Enchantment.ARROW_KNOCKBACK;
		if(name.contains("flame")) return Enchantment.ARROW_FIRE;
		if(name.contains("infin")) return Enchantment.ARROW_INFINITE; 
		if(name.contains("dig") || name.contains("eff")) return Enchantment.DIG_SPEED;
		if(name.contains("dura") || name.contains("unbreaking")) return Enchantment.DURABILITY;
		if(name.contains("silk")) return Enchantment.SILK_TOUCH;
		if(name.contains("fort")) return Enchantment.LOOT_BONUS_BLOCKS;return null;
	}
	
	public static PotionEffectType getEffectByCommonName(String name){
		name = name.toLowerCase();
		if(name.contains("regen")) return PotionEffectType.REGENERATION;
		if(name.contains("poison")) return PotionEffectType.POISON;
		if(name.contains("strength")) return PotionEffectType.INCREASE_DAMAGE;
		if(name.contains("weak")) return PotionEffectType.WEAKNESS;
		if(name.contains("heal")) return PotionEffectType.HEAL;
		if(name.contains("harm")) return PotionEffectType.HARM;
		if(name.contains("speed") || name.contains("swift")) return PotionEffectType.SPEED;
		if(name.contains("slow")) return PotionEffectType.SLOW;
		if(name.contains("haste")) return PotionEffectType.FAST_DIGGING;
		if(name.contains("fat")) return PotionEffectType.SLOW_DIGGING;
		if(name.contains("hung")) return PotionEffectType.HUNGER;
		if(name.contains("resist")) return PotionEffectType.DAMAGE_RESISTANCE;
		if(name.contains("blind")) return PotionEffectType.BLINDNESS;
		if(name.contains("confus") || name.contains("naus")) return PotionEffectType.CONFUSION;
		if(name.contains("fire")) return PotionEffectType.FIRE_RESISTANCE;
		if(name.contains("jump")) return PotionEffectType.JUMP;
		if(name.contains("water") || name.contains("aqua")) return PotionEffectType.WATER_BREATHING;
		if(name.contains("invis")) return PotionEffectType.INVISIBILITY;
		if(name.contains("night")) return PotionEffectType.NIGHT_VISION;
		return null;
	}

	
	
	public static void addItemsToInventory(Player player, List<ItemStack> items) {
		for (ItemStack stack : items)
			InventoryUtil.addItemToInventory(player, stack);
	}
	private static ItemStack[] splitIntoStacks(ItemStack item, int amount) {
		 
		final int maxSize = item.getMaxStackSize();
		final int remainder = amount % maxSize;
		final int fullStacks = (int) Math.floor(amount / item.getMaxStackSize());
		 
		ItemStack fullStack = item.clone();
		ItemStack finalStack = item.clone();
		fullStack.setAmount(maxSize);
		finalStack.setAmount(remainder);
		
		
		ItemStack[] items = new ItemStack[fullStacks+1];
		 
		for (int i=0; i<fullStacks; i++) {
			items[i] = fullStack;
		}
		items[items.length - 1] = finalStack;
		return items;
	}

	public static void addItemToInventory(Player player, ItemStack stack) {
		PlayerInventory inv = player.getInventory();
		
		
		
		addItemToInventory(inv, stack, player );
	}

	private static void addItemToInventory(PlayerInventory inv, ItemStack stack, Player p ){
		int maxStackSize = stack.getType().getMaxStackSize();
		if(stack.getType() == Material.POTION)
			maxStackSize = 16;
		if(stack.getAmount() <= maxStackSize){
			if(inv.firstEmpty() == -1){
				p.getWorld().dropItem(p.getLocation(), stack);
				return;
			}
			inv.addItem(stack);
			return;
		}else{
			ItemStack[] items = splitIntoStacks(stack, stack.getAmount());
			for(ItemStack item : items){
				if(inv.firstEmpty() == -1){
					p.getWorld().dropItem(p.getLocation(), stack);
				}else {inv.addItem(item);}
			}
		}
		
			
			
		
		
	}
	
	public static ItemStack parseItem(String str) throws ParseItemException{
		try{
			String[] enchantSplit  = str.split("!");
			String[] itemSplit     = enchantSplit[0].split(":");
			
			Material mat = getMat(itemSplit[0]);
			if(mat == null)
				throw new ParseItemException("invalid item specified", str);
			int amount = 1;
			short data = 0;
			if(itemSplit.length > 1){
				
				String myStr  = itemSplit[itemSplit.length - 1];
				int myInt     = 0;
				if(myStr.contains("<")) {
					myInt = myStr.indexOf("<");
					myStr = myStr.replace(myStr.substring(myInt), "").replace("<", "");
				}
				
				if(isInt(myStr)) {
					amount = Integer.parseInt(myStr);
				}
				
				
				if(itemSplit.length > 2){
					if(isInt(itemSplit[1]))
						data = Short.parseShort(itemSplit[1]);
					else if(mat == Material.MONSTER_EGG || mat == Material.MOB_SPAWNER){
						EntityType entity = EntityType.fromName(itemSplit[1]);
						if(entity != null && entity.isSpawnable() && entity.isAlive())
							data = entity.getTypeId();
						else
							throw new ParseItemException("invalid entity type specified", str);
					}
					else
						throw new ParseItemException("invalid data specified", str);
				}
			}
			ItemStack stack = new ItemStack(mat, amount, data);
			
			for (int i = 1; i < enchantSplit.length;i++){
				EnchantmentWithLevel ewl = parseEnchantment(enchantSplit[i].trim());
				if(ewl == null)
					throw new ParseItemException("invalid enchantment specified", str);
				stack.addUnsafeEnchantment(ewl.enc, ewl.lvl);
			}
			if(str.contains("<")){
				stack = setName(stack,str,amount);
				debug(str);
			}
			stack.setAmount(amount);
		
			if(itemSplit[0].contains("%")) {
				String mats = itemSplit[0];
				//LEATHER_THING?r?g?b
				String colors[] = mats.split("%");
				
				String r = colors[1];
				String g = colors[2];
				String b = colors[3];

					
				mats = mats.replace( r ,  "");
				mats = mats.replace( g ,  "");
				mats = mats.replace( b ,  "");
				LeatherArmorMeta lim = (LeatherArmorMeta) stack.getItemMeta();

				lim.setColor(Color.fromRGB(Integer.valueOf(r), Integer.valueOf(g), Integer.valueOf(b)));
				System.out.println("\n\n\n\nShouldBe: " + r+","+g+","+b);
				stack.setItemMeta(lim);
			}else{
				System.out.println("\n\n" + itemSplit[0]);
			}

			return stack;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	public static ItemStack setName(ItemStack stack,String str, int amount) {
		String[] NameLoreSplit = str.split("<");
		String[] SecondSplit   = NameLoreSplit[1].split(",");
		String name            = ChatColor.translateAlternateColorCodes('&', SecondSplit[0]);
		String[] Lore          = null;
		if(SecondSplit.length >= 2) {
			String toSplit = SecondSplit[1];
			for(String s : SecondSplit){
				if(!s.equals(SecondSplit[0]) && !s.equals(SecondSplit[1])) toSplit = toSplit +","+ s;
			}
			Lore = toSplit.split("-");
		}
		return renameItem(stack,name, Lore);
	}
	public static ItemStack renameItem(ItemStack input, String newName, String[] Lore){
		ItemMeta meta = input.getItemMeta();
		List<String> tagList = new LinkedList<String>();
		if (newName != null) {
			meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',newName));
		}
		
		if(Lore != null){
			for (String line : Lore) {
				line = ChatColor.translateAlternateColorCodes('&', line);
				tagList.add(line);
			}
			meta.setLore(tagList);
		}
		
		input.setItemMeta(meta);
		return input;
		
	}
	public static double power(int base, int power) {
		return Math.pow(base, power);
	}
	public static PotionEffect parseEffect(String str) throws ParseItemException{
		try{
			String[] split = str.split(":");
			PotionEffectType type = PotionEffectType.getByName(split[0]);
			if(type == null)
				type = getEffectByCommonName(split[0]);
			if(type == null && isInt(split[0])){
				type = PotionEffectType.getById(Integer.parseInt(split[0]));
			}
			if(type == null)
				type = PotionEffectType.getByName(split[0]);
			if(type == null)
				throw new ParseItemException("invalid effect specified", str);
			return new PotionEffect(type, split.length > 2 ? Integer.parseInt(split[2]) * 20 : 1200, split.length > 1 ? Integer.parseInt(split[1]) - 1 : 0);
		}
		catch(NumberFormatException e){
			throw new ParseItemException("invalid amplifier/duration", str);
		}
	}

	public static EnchantmentWithLevel parseEnchantment(String str) {
		str = str.toLowerCase();
		String[] split = str.split(":");
		Enchantment enc = Enchantment.getByName(split[0]);
		if(enc == null)
			enc = getEnchantmentByCommonName(split[0]);
		if(enc == null)
			return null;
		int lvl = 1;
		if(split.length > 1){
			String[] Split = split[1].split("<");
			lvl = isInt(Split[0]) ? Integer.parseInt(Split[0]) : 1;
		}
		EnchantmentWithLevel ewl = new EnchantmentWithLevel();
		ewl.enc = enc;
		ewl.lvl = lvl;
		return ewl;
	}

	public static Material getMat(String name) {
		name = name.toLowerCase();
		Material mat = null;
		if(mat == null){
			ItemInfo item = Items.itemByName(name);
			if(item != null) {
				mat = item.getType();
			} else {
				try{ mat = Material.getMaterial(Integer.parseInt(name)); }
				catch(NumberFormatException e){}
			}
		}
		if(mat == null){
			try{ mat = Material.getMaterial(Integer.parseInt(name)); }
			catch(NumberFormatException e){}
		}
		if(mat == null)
			mat = Material.matchMaterial(name);
		if((name.contains("spawn") || name.contains("mob")) && name.contains("egg"))
			mat = Material.MONSTER_EGG;
		if(mat != null)
			return mat;
		
		String mats = name;
		if(!mats.contains("%"))
			return mat;
		//LEATHER_THING?r?g?b
		String colors[] = mats.split("%");
		return Material.getMaterial(colors[0]);
	}

	
	public static String itemToString(ItemStack stack){
		String str = stack.getAmount() + " " + stack.getType().name().toLowerCase().replaceAll("_", " ") + (stack.getData().getData() == 0 ? "" : ":" + stack.getData().getData());
		for(Enchantment enc : stack.getEnchantments().keySet())
			str += " " + enc.getName().toLowerCase().replaceAll("_", " ") + stack.getEnchantmentLevel(enc);
		return str;
	}
	
	public static String effectToString(PotionEffect effect){
		return effect.getType().getName().toLowerCase().replaceAll("_", " ") + " " + effect.getAmplifier() + " for " + effect.getDuration()/20.0 + " seconds";
	}

	public static boolean isInt(String str){try{Integer.parseInt(str);return true;}catch(Exception e) {return false;}}
	public static void debug(String debug) {
		boolean shouldDebug = false;
		if(shouldDebug)System.out.println("\n\n\nDEBUG:SUPERKITS: " + debug);
	}
	public boolean isLeather(Material m) {
		return m == Material.LEATHER_BOOTS || m== Material.LEATHER_LEGGINGS || m == Material.LEATHER_CHESTPLATE || m == Material.LEATHER_HELMET;
	}
	
}
