package roleplay.v1_3.handler.event;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.FurnaceExtractEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class PlayerEventHandler implements Listener {
	
	RPSystem plugin;
	Random r = new Random();
	public PlayerEventHandler(RPSystem plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerMove(PlayerMoveEvent event){
		int n, m;
		
		n = -1;
		m = 10000;
		if(RPSystem.drunk.get(event.getPlayer().getName())+300000 > (System.currentTimeMillis())){
			n = r.nextInt(m)+1;
			
			if(n >= 190 && n < 200){			
				for(Player pl : Bukkit.getOnlinePlayers()){
					if(event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
						pl.sendMessage(ChatConstants.action_me(event.getPlayer(), "stumbles and falls down."));
					}
				}
				event.getPlayer().damage(plugin.config.getDouble("alcohol.damage-on-use"));
				event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You lose your balance and fall over.");
				event.getPlayer().setVelocity(event.getPlayer().getVelocity().add(new Vector(1.50,0.00,0.00)));
			}
			// 0.5% chance to totally pass out
			else if(n == 200){
				
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,1800,100));
				for(Player pl : Bukkit.getOnlinePlayers()){
					if(event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
						pl.sendMessage(ChatConstants.action_me(event.getPlayer(), "passes out."));
					}
				}
				event.getPlayer().damage(plugin.config.getDouble("alcohol.damage-on-use"));
				event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You pass out and fall over.");
				if(event.getPlayer().hasPotionEffect(PotionEffectType.SLOW)){
					event.getPlayer().removePotionEffect(PotionEffectType.SLOW);
					event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,1800,100));
				}
			}
		}
		
		if(RPSystem.high.get(event.getPlayer().getName())+300000 > (System.currentTimeMillis())){
			n = r.nextInt(m);
			
			// 5% chance to just stumble and trip
			if(n >= 190 && n < 200){			
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,100,100));
				for(Player pl : Bukkit.getOnlinePlayers()){
					if(event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
						pl.sendMessage(ChatConstants.action_me(event.getPlayer(), "stumbles and falls down."));
					}
				}
				event.getPlayer().damage(plugin.config.getDouble("drugs.damage-on-use"));
				event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You lose your balance and fall over.");
			}
			// 0.5% chance to totally pass out
			else if(n == 200){
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,1800,100));
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,1800,100));
				for(Player pl : Bukkit.getOnlinePlayers()){
					if(event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
						pl.sendMessage(ChatConstants.action_me(event.getPlayer(), "passes out."));
					}
				}
				event.getPlayer().damage(plugin.config.getDouble("drugs.damage-on-use"));
				event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You pass out and fall over.");
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDeath(EntityDeathEvent event){
		if(event.getEntity() instanceof Player){
			Player p = (Player) event.getEntity();
			RPSystem.drunk.put(p.getName(), 0L);
			RPSystem.high.put(p.getName(), 0L);
			
			if(p.getKiller() instanceof Player){
				// PVP Death
				SQLManager.recordPVPDeath(p.getKiller().getName(), p.getName(), p.getLocation());
				plugin.log.info("PVP RESULT: " + p.getName() + " was killed by " + p.getKiller().getName() + " using " + p.getKiller().getItemInHand().getType().toString());
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onBlockBreak(BlockBreakEvent event){
		Player p = event.getPlayer();
		
		if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
			if(plugin.itemConfig.getStringList("restricted-items").contains(event.getBlock().getType().toString())){
				// If the item is a restricted item...
				
				if(!RPSystem.permission.has(p, plugin.itemConfig.getString(event.getBlock().getType().toString() + ".break-permission")) && !p.isOp()){
					p.sendMessage(ChatConstants.err("Permission to break " + event.getBlock().getType().toString() + " denied."));
					p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(event.getBlock().getType().toString() + ".break-permission") + ChatColor.RED));
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event){
		Player p = event.getPlayer();
		
		if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
			if(plugin.itemConfig.getStringList("restricted-items").contains(event.getBlock().getType().toString())){
				// If the item is a restricted item...
				
				if(!RPSystem.permission.has(p, plugin.itemConfig.getString(event.getBlock().getType().toString() + ".place-permission")) && !p.isOp()){
					p.sendMessage(ChatConstants.err("Permission to place " + event.getBlock().getType().toString() + " denied."));
					p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(event.getBlock().getType().toString() + ".place-permission") + ChatColor.RED));
					event.setCancelled(true);
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerInteractWithBlock(PlayerInteractEvent event){
		Player p = event.getPlayer();
		Block b = event.getClickedBlock();
		
		if(b != null && !b.getType().equals(Material.AIR)){
			if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
				if(plugin.itemConfig.getStringList("restricted-items").contains(b.getType().toString())){
					// If the item is a restricted item...
					
					if(!RPSystem.permission.has(p, plugin.itemConfig.getString(b.getType().toString() + ".interact-permission")) && !p.isOp()){
						p.sendMessage(ChatConstants.err("Permission to interact with " + b.getType().toString() + " denied."));
						p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(b.getType().toString() + ".interact-permission") + ChatColor.RED));
						event.setCancelled(true);
					}
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.NORMAL)
	public void onInventoryClick(CraftItemEvent event){
		boolean eventDebug = false;
		
		if(eventDebug) plugin.log.info("Encountered CraftItemEvent"); 
		if(event.getView().getPlayer() instanceof Player){
			Player p = (Player) event.getView().getPlayer();
			
			if(eventDebug) plugin.log.info("Encountered CraftItemEvent as Player");
			
			Material mat = event.getInventory().getResult().getType();
			
			if(mat != null && !mat.equals(Material.AIR)){
				if(eventDebug) plugin.log.info("Material is not air.");
				if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
					if(eventDebug) plugin.log.info("Player does not have restriction override.");
					if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
						if(eventDebug) plugin.log.info("Item is permission-restricted.");
						// If the item is a restricted item...
						
						if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".craft-permission")) && !p.isOp()){
							if(eventDebug) plugin.log.info("Player does not have permission to craft item.");
							p.sendMessage(ChatConstants.err("Permission to craft " + mat.toString() + " denied."));
							p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".craft-permission") + ChatColor.RED));
							event.setCancelled(true);
						}
					}
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onPlayerSmelt(FurnaceExtractEvent event){
		
		boolean eventDebug = false;
		
		Player p = (Player) event.getPlayer();
		
		if(eventDebug) plugin.log.info("Encountered CraftItemEvent as Player");
		
		Material mat = event.getItemType();
		
		if(mat != null && !mat.equals(Material.AIR)){
			if(eventDebug) plugin.log.info("Material is not air.");
			if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
				if(eventDebug) plugin.log.info("Player does not have restriction override.");
				if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
					if(eventDebug) plugin.log.info("Item is permission-restricted.");
					// If the item is a restricted item...
					
					if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".craft-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have permission to smelt item.");
						p.sendMessage(ChatConstants.err("Permission to smelt " + mat.toString() + " denied."));
						p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".smelt-permission") + ChatColor.RED));
						p.getInventory().remove(new ItemStack(mat,event.getItemAmount()));
					}
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.NORMAL)
	public void onInventoryInteract(InventoryClickEvent event){
		boolean eventDebug = false;
		
		if(event.getWhoClicked() instanceof Player){
			Player p = (Player) event.getWhoClicked();
			Material mat = event.getCurrentItem().getType();
			
			if(event.getClick().isShiftClick()){
				// Detect adding of armor OR adding to furnace
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".interact-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to interact with item.");
								p.sendMessage(ChatConstants.err("Permission to interact with " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".interact-permission") + ChatColor.RED));
								event.setCancelled(true);
							}
						}
					}
				}
			}
			if(event.getSlotType().equals(SlotType.ARMOR)){
				// Detect adding of armor OR adding to furnace
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".equip-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to interact with item.");
								p.sendMessage(ChatConstants.err("Permission to interact with " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".interact-permission") + ChatColor.RED));
								event.setCancelled(true);
							}
						}
					}
				}
			}
			if(event.getSlotType().equals(SlotType.CRAFTING)){
				// Detect adding of armor OR adding to furnace
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".craft-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to interact with item.");
								p.sendMessage(ChatConstants.err("Permission to interact with " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".interact-permission") + ChatColor.RED));
								event.setCancelled(true);
							}
						}
					}
				}
			}
			if(event.getSlotType().equals(SlotType.FUEL)){
				// Detect adding of armor OR adding to furnace
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".interact-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to interact with item.");
								p.sendMessage(ChatConstants.err("Permission to interact with " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".interact-permission") + ChatColor.RED));
								event.setCancelled(true);
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler (priority = EventPriority.NORMAL)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event){
		boolean eventDebug = false;
		if(event.getEntity() instanceof Player){
			// Player got damaged
			Player p = (Player) event.getEntity();
			
			// Check armor being worn by the player to see if it is restricted (wrongly)
			// If the player is wearing restricted armor that they don't normally have permission for, this removes it
			Material mat;
			boolean armorForceRemoved = false;
			if(p.getInventory().getChestplate() != null){
				mat = p.getInventory().getChestplate().getType();
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".equip-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to equip item.");
								p.sendMessage(ChatConstants.err("Permission to equip " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".equip-permission") + ChatColor.RED));
								p.getInventory().setChestplate(new ItemStack(Material.AIR));
								p.getInventory().addItem(new ItemStack(mat,1));
								armorForceRemoved = true;
							}
						}
					}
				}
			}
			
			if(p.getInventory().getLeggings() != null){
				mat = p.getInventory().getLeggings().getType();
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".equip-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to equip item.");
								p.sendMessage(ChatConstants.err("Permission to equip " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".equip-permission") + ChatColor.RED));
								p.getInventory().setChestplate(new ItemStack(Material.AIR));
								p.getInventory().addItem(new ItemStack(mat,1));
								armorForceRemoved = true;
							}
						}
					}
				}
			}
			
			if(p.getInventory().getBoots() != null){
				mat = p.getInventory().getBoots().getType();
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".equip-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to equip item.");
								p.sendMessage(ChatConstants.err("Permission to equip " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".equip-permission") + ChatColor.RED));
								p.getInventory().setChestplate(new ItemStack(Material.AIR));
								p.getInventory().addItem(new ItemStack(mat,1));
								armorForceRemoved = true;
							}
						}
					}
				}
			}
			
			if(p.getInventory().getHelmet() != null){
				mat = p.getInventory().getHelmet().getType();
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".equip-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to equip item.");
								p.sendMessage(ChatConstants.err("Permission to equip " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".equip-permission") + ChatColor.RED));
								p.getInventory().setChestplate(new ItemStack(Material.AIR));
								p.getInventory().addItem(new ItemStack(mat,1));
								armorForceRemoved = true;
							}
						}
					}
				}
			}
			
			if(armorForceRemoved) p.damage(event.getDamage()); // Redoes the damage now that the illegal armor is removed.
		}
		else {
			// Not a player
			if(event.getDamager() instanceof Player){
				// Player did the damage
				Player p = (Player) event.getDamager();
				Material mat = p.getItemInHand().getType();
				
				if(mat != null && !mat.equals(Material.AIR)){
					if(eventDebug) plugin.log.info("Material is not air.");
					if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
						if(eventDebug) plugin.log.info("Player does not have restriction override.");
						if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
							if(eventDebug) plugin.log.info("Item is permission-restricted.");
							// If the item is a restricted item...
							
							if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".attack-permission")) && !p.isOp()){
								if(eventDebug) plugin.log.info("Player does not have permission to attack with item.");
								p.sendMessage(ChatConstants.err("Permission to attack with " + mat.toString() + " denied."));
								p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".attack-permission") + ChatColor.RED));
								event.setCancelled(true);
								event.setDamage(0.00);
							}
						}
					}
				}
			}
			else if(event.getDamager() instanceof Arrow){
				// Player did the damage
				Arrow a = (Arrow) event.getDamager();
				if(a.getShooter() instanceof Player){
					Player p = (Player) a.getShooter();
					Material mat;
					
					mat = Material.BOW;				
					if(mat != null && !mat.equals(Material.AIR)){
						if(eventDebug) plugin.log.info("Material is not air.");
						if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
							if(eventDebug) plugin.log.info("Player does not have restriction override.");
							if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
								if(eventDebug) plugin.log.info("Item is permission-restricted.");
								// If the item is a restricted item...
								
								if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".attack-permission")) && !p.isOp()){
									if(eventDebug) plugin.log.info("Player does not have permission to attack with item.");
									p.sendMessage(ChatConstants.err("Permission to attack with " + mat.toString() + " denied."));
									p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".attack-permission") + ChatColor.RED));
									event.setCancelled(true);
									event.setDamage(0.00);
								}
							}
						}
					}
					
					mat = Material.ARROW;				
					if(mat != null && !mat.equals(Material.AIR)){
						if(eventDebug) plugin.log.info("Material is not air.");
						if(!RPSystem.permission.has(p, plugin.config.getString("block-restriction.override-permission")) && !p.isOp()){
							if(eventDebug) plugin.log.info("Player does not have restriction override.");
							if(plugin.itemConfig.getStringList("restricted-items").contains(mat.toString())){
								if(eventDebug) plugin.log.info("Item is permission-restricted.");
								// If the item is a restricted item...
								
								if(!RPSystem.permission.has(p, plugin.itemConfig.getString(mat.toString() + ".attack-permission")) && !p.isOp()){
									if(eventDebug) plugin.log.info("Player does not have permission to attack with item.");
									p.sendMessage(ChatConstants.err("Permission to attack with " + mat.toString() + " denied."));
									p.sendMessage(ChatConstants.err("You do not have permission: " + ChatColor.DARK_RED + plugin.itemConfig.getString(mat + ".attack-permission") + ChatColor.RED));
									event.setCancelled(true);
								}
							}
						}
					}
				}
			}
		}
	}
	
}
