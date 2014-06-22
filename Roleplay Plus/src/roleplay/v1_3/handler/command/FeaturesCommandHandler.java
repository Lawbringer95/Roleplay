package roleplay.v1_3.handler.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class FeaturesCommandHandler implements CommandExecutor {
	
	RPSystem plugin;
	public FeaturesCommandHandler(RPSystem plugin){
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("mrps")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				p.sendMessage(ChatColor.GRAY + Timestamp.NOW() + "-- [MRPS v" + plugin.getDescription().getVersion() + "] --");
				p.sendMessage(ChatColor.GRAY + Timestamp.NOW() + "* Development / Testing Build *");
				p.sendMessage(ChatColor.GRAY + Timestamp.NOW() + "Developed by: iTzFusiioNx / Apollo");
				p.sendMessage(ChatColor.GRAY + Timestamp.NOW() + "Contact: Skype@itzfusiionx");
				return true;
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "-- [MRPS v" + plugin.getDescription().getVersion() + "] --");
				sender.sendMessage(Timestamp.NOW() + "* Development / Testing Build *");
				sender.sendMessage(Timestamp.NOW() + "Developed by: iTzFusiioNx / Apollo");
				sender.sendMessage(Timestamp.NOW() + "Contact: Skype@itzfusiionx");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("roll")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /roll [sides of dice]"));
					return true;
				}
				
				int lim,n;
				try {
					lim = Integer.parseInt(args[0]);
				} catch(NumberFormatException e){
					lim = 1;
					p.sendMessage(ChatConstants.err("That is not a valid number"));
					return true;
				}
				
				if(lim < 1 || lim > Integer.MAX_VALUE){
					p.sendMessage(ChatConstants.err("That is not a valid number"));
					lim = 1;
					return true;
				}
				
				if(RPSystem.permission.has(p,plugin.config.getString("dice.roll-permission")) || p.isOp()){
					n = (new Random()).nextInt(lim)+1;
					for(Player player : Bukkit.getOnlinePlayers()){
						if(p.getLocation().distance(player.getLocation()) <= plugin.config.getDouble("dice.range")){					
							player.sendMessage(ChatConstants.roll(p, lim, n));
						}
					}
					return true;
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("brew")){
			// Alcohol Brewing
			if(sender instanceof Player){
				
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /brew [AlcoholName]"));
					return true;
				}
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("alcohol.brew-permission")) || p.isOp()){
						// Check item in hand to see if it can be brewed into alcohol
						if(plugin.config.getIntegerList("alcohol.items").contains(p.getItemInHand().getTypeId())){
							// The item is valid to be brewed...
							ItemStack alcoholStack = new ItemStack(p.getItemInHand().getType(),1);
							// Deduct 1 from inventory
							p.getInventory().remove(alcoholStack);
							
							// Add meta
							ItemMeta alcoholMeta = alcoholStack.getItemMeta();
							alcoholMeta.setDisplayName("Alcohol: " + ChatConstants.build(args));
							List<String> lore = new ArrayList<String>();
							lore.add(0,"Brewed by " + SQLManager.getDisplayName(p.getName()));
							alcoholMeta.setLore(lore);
							
							alcoholStack.setItemMeta(alcoholMeta);
							
							p.getInventory().addItem(alcoholStack);
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Brewed!");
							return true;
						}
						else {
							p.sendMessage(ChatConstants.err("That item cannot be brewed into alcohol."));
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				} // end of args check else
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("makedrugs")){
			// Alcohol Brewing
			if(sender instanceof Player){
				
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /makedrugs [DrugName]"));
					return true;
				}
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("drugs.make-permission")) || p.isOp()){
						// Check item in hand to see if it can be brewed into alcohol
						if(plugin.config.getIntegerList("drugs.items").contains(p.getItemInHand().getTypeId())){
							// The item is valid to be brewed...
							ItemStack alcoholStack = new ItemStack(p.getItemInHand().getType(),1);
							// Deduct 1 from inventory
							p.getInventory().remove(alcoholStack);
							
							// Add meta
							ItemMeta alcoholMeta = alcoholStack.getItemMeta();
							alcoholMeta.setDisplayName("Drug: " + ChatConstants.build(args));
							List<String> lore = new ArrayList<String>();
							lore.add(0,"Made by " + SQLManager.getDisplayName(p.getName()));
							alcoholMeta.setLore(lore);
							
							alcoholStack.setItemMeta(alcoholMeta);
							
							p.getInventory().addItem(alcoholStack);
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Drug made!");
							return true;
						}
						else {
							p.sendMessage(ChatConstants.err("That item cannot be made into drugs."));
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				} // end of args check else
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("sober")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /sober [drugs|alcohol]"));
					return true;
				}
				
				if(args[0].equalsIgnoreCase("drugs")){
					if(RPSystem.permission.has(p, plugin.config.getString("drugs.sober-permission"))){
						if(RPSystem.high.get(p.getName())+300000 > (System.currentTimeMillis())){
							for(PotionEffect eff : p.getActivePotionEffects()){
								p.removePotionEffect(eff.getType());
							}
							RPSystem.high.put(p.getName(), 0L);
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You feel normal.");
							return true;
						}
						else {
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You are not high on drugs.");
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
				if(args[0].equalsIgnoreCase("alcohol")){
					if(RPSystem.permission.has(p, plugin.config.getString("alcohol.sober-permission"))){
						if(RPSystem.drunk.get(p.getName())+300000 > (System.currentTimeMillis())){
							for(PotionEffect eff : p.getActivePotionEffects()){
								p.removePotionEffect(eff.getType());
							}
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You feel normal.");
							RPSystem.drunk.put(p.getName(), 0L);
							return true;
						}
						else {
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You are not drunk.");
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Syntax - /sober [drugs|alcohol]"));
					return true;
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("usedrug")){
			if(sender instanceof Player){
				final Player p = (Player) sender;
				
				if(p.getItemInHand() == null){
					p.sendMessage(ChatConstants.err("You are not holding any drugs."));
					return true;
				}
				if(!p.getItemInHand().hasItemMeta()){
					p.sendMessage(ChatConstants.err("You are not holding any drugs."));
					return true;
				}
				else {
					if(!p.getItemInHand().getItemMeta().hasDisplayName()){
						p.sendMessage(ChatConstants.err("You are not holding any drugs."));
						return true;
					}
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("drugs.use-permission")) || p.isOp()){
					if(p.getItemInHand().getItemMeta().getDisplayName().contains("Drug:")){						
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
								pl.sendMessage(ChatConstants.action_me(p, "uses some " + p.getItemInHand().getItemMeta().getDisplayName().replaceAll("Drug: ", "") + "."));
							}
						}
						p.getInventory().removeItem(p.getItemInHand());
						RPSystem.high.put(p.getName(), System.currentTimeMillis());
						
						// Apply effects
						// 6000 is 5 minutes in ticks (1 tick = 1/20 sec)
						if(plugin.config.getBoolean("drugs.effects-enabled")){
							p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,6000,4));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,6000,1));
						}
						p.damage(plugin.config.getDouble("drugs.damage-on-use"));
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You start to get high.");
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("You are not holding any drugs."));
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("drink")){
			if(sender instanceof Player){
				final Player p = (Player) sender;
				
				if(p.getItemInHand() == null){
					p.sendMessage(ChatConstants.err("You are not holding any alcoholic beverage."));
					return true;
				}
				if(!p.getItemInHand().hasItemMeta()){
					p.sendMessage(ChatConstants.err("You are not holding any alcoholic beverage."));
					return true;
				}
				else {
					if(!p.getItemInHand().getItemMeta().hasDisplayName()){
						p.sendMessage(ChatConstants.err("You are not holding any alcoholic beverage."));
						return true;
					}
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("alcohol.drink-permission")) || p.isOp()){
					if(p.getItemInHand().getItemMeta().getDisplayName().contains("Alcohol:")){			
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
								pl.sendMessage(ChatConstants.action_me(p, "drinks some " + p.getItemInHand().getItemMeta().getDisplayName().replaceAll("Alcohol: ", "") + "."));							}
						}
						p.getInventory().removeItem(p.getItemInHand());
						RPSystem.drunk.put(p.getName(), System.currentTimeMillis());
						
						// Apply effects
						// 6000 is 5 minutes in ticks (1 tick = 1/20 sec)
						if(plugin.config.getBoolean("alcohol.effects-enabled")){
							p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,6000,2));
							p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,6000,0));
							p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,6000,1));
							p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,6000,0));
						}
						p.damage(plugin.config.getDouble("alcohol.damage-on-use"));
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You begin feeling tipsy.");
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("You are not holding any alcoholic beverage."));
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("examine")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /examine [player]"));
					return true;
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("describe.examine-permission")) || p.isOp()){
					Player target = plugin.getServer().getPlayer(args[0]);
					
					if(target != null){
						p.sendMessage(Timestamp.NOW() + "Description of " + SQLManager.getDisplayName(target.getName()) + ":");
						p.sendMessage(Timestamp.NOW() + RPSystem.description.get(target.getName()));
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Player not found."));
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				Player target = plugin.getServer().getPlayer(args[0]);
				
				if(target != null){
					sender.sendMessage(Timestamp.NOW() + "Description of " + SQLManager.getDisplayName(target.getName()) + ":");
					sender.sendMessage(Timestamp.NOW() + RPSystem.description.get(target.getName()));
					return true;
				}
				else {
					sender.sendMessage(ChatConstants.err("Player not found."));
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("attributes")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /attributes [description]"));
					return true;
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("describe.attributes-permission")) || p.isOp()){
					String desc = ChatConstants.build(args);
					RPSystem.description.put(p.getName(),desc);
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Description set.");
					return true;
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage("You must be a player to use this command.");
				return true;
			}
		}
			
		
		return false;
	}

}
