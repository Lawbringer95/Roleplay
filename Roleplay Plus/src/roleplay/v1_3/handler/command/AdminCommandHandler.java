package roleplay.v1_3.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class AdminCommandHandler implements CommandExecutor {
	
	RPSystem plugin;
	public AdminCommandHandler(RPSystem plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("ban")){
			// Permanent Ban Command
			// Does not require the player to be online as it doesn't actually access the player other than to kick
			
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 2){
					p.sendMessage(ChatConstants.err("Syntax - /ban [player] [reason]"));
					return true;
				}
				else {
					String pname = args[0];
					String reason = ChatConstants.build(args).replaceAll(args[0], "");
					
					if(RPSystem.permission.has(sender,plugin.config.getString("admin.permission.ban")) || p.isOp()){
						SQLManager.recordBan(pname, p.getName(), System.currentTimeMillis(), System.currentTimeMillis()*2, reason);
						
						if(Bukkit.getPlayer(pname) != null){
							Bukkit.broadcastMessage(ChatConstants.ban(p.getName(),pname));
							Bukkit.getPlayer(pname).kickPlayer(""
									+ ChatColor.DARK_RED + "[!]\n"
									+ ChatColor.DARK_RED + "You have been banned from this server.\n"
									+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(Bukkit.getPlayer(pname).getName()) + "\n"
									+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanTime(Bukkit.getPlayer(pname).getName())))) + "\n"
									+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanExpire(Bukkit.getPlayer(pname).getName())))) + "\n"
									+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(Bukkit.getPlayer(pname).getName())
							);
							return true;
						}
						else {
							// If the player is not online
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + pname + "' offline-banned.");
							plugin.log.info(pname + " has been banned by " + p.getName());
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				// Console
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /ban [player] [reason]");
					return true;
				}
				else {
					String pname = args[0];
					String reason = ChatConstants.build(args).replaceFirst(args[0], "");
					SQLManager.recordBan(pname, "SYSTEM", System.currentTimeMillis(), System.currentTimeMillis()*2, reason);
						
					if(Bukkit.getPlayer(pname) != null){
						Bukkit.broadcastMessage(ChatConstants.ban("SYSTEM",pname));
						Bukkit.getPlayer(pname).kickPlayer(""
								+ ChatColor.DARK_RED + "[!]\n"
								+ ChatColor.DARK_RED + "You have been banned from this server.\n"
								+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(Bukkit.getPlayer(pname).getName()) + "\n"
								+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanTime(Bukkit.getPlayer(pname).getName())))) + "\n"
								+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanExpire(Bukkit.getPlayer(pname).getName())))) + "\n"
								+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(Bukkit.getPlayer(pname).getName())
						);
						sender.sendMessage(Timestamp.NOW() + "Player '" + Bukkit.getPlayer(pname).getName() + "' banned.");
						return true;
					}
					else {
						// If the player is not online
						sender.sendMessage(Timestamp.NOW() + "Player '" + pname + "' offline-banned.");
						plugin.log.info(pname + " has been banned by SYSTEM");
						return true;
					}
				}
			}
			
		} // End of Ban Command
		
		if(cmd.getName().equalsIgnoreCase("unban")){
			// Release Bans Command
			// Does not require the player to be online as it doesn't actually access the player other than to kick
			
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /unban [player]"));
					return true;
				}
				else {
					String pname = args[0];
					
					if(RPSystem.permission.has(sender,plugin.config.getString("admin.permission.unban")) || p.isOp()){
						if(SQLManager.isBanned(pname)){
							SQLManager.unban(pname);
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + pname + "' unbanned.");
							return true;
						}
						else {
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + pname + "' is not banned.");
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				// Console
				if(args.length < 1){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /unban [player]");
					return true;
				}
				else {
					String pname = args[0];
					if(SQLManager.isBanned(pname)){
						SQLManager.unban(pname);
						sender.sendMessage(Timestamp.NOW() + "Player '" + pname + "' unbanned.");
						return true;
					}
					else {
						sender.sendMessage(Timestamp.NOW() + "Player '" + pname + "' is not banned.");
						return true;
					}
				}
			}
			
		} // End of Unban Command
		else if(cmd.getName().equalsIgnoreCase("tempban")){
			// Permanent Ban Command
			// Does not require the player to be online as it doesn't actually access the player other than to kick
			
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 3){
					p.sendMessage(ChatConstants.err("Syntax - /tempban [player] [days] [reason]"));
					return true;
				}
				else {
					String pname = args[0];
					String reason2 = ChatConstants.build(args).replaceAll(args[0], "");
					String reason = reason2.replaceAll(args[1], "");
					
					if(RPSystem.permission.has(sender,plugin.config.getString("admin.permission.tempban")) || p.isOp()){
						
						double bandays = 0.0;
						try {
							bandays = Double.parseDouble(args[1]);
						} catch(NumberFormatException e){
							bandays = 0.0;
						}
						
						long time = (long)((bandays * 86400000));
						
						SQLManager.recordBan(pname, p.getName(), System.currentTimeMillis(), System.currentTimeMillis()+time, reason);
						
						if(Bukkit.getPlayer(pname) != null){
							Bukkit.broadcastMessage(ChatConstants.tempban(p.getName(),pname,bandays));
							Bukkit.getPlayer(pname).kickPlayer(""
									+ ChatColor.DARK_RED + "[!]\n"
									+ ChatColor.DARK_RED + "You have been banned from this server.\n"
									+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(Bukkit.getPlayer(pname).getName()) + "\n"
									+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanTime(Bukkit.getPlayer(pname).getName())))) + "\n"
									+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanExpire(Bukkit.getPlayer(pname).getName())))) + "\n"
									+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(Bukkit.getPlayer(pname).getName())
							);
							return true;
						}
						else {
							// If the player is not online
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + pname + "' offline-tempbanned.");
							plugin.log.info(pname + " has been banned by " + p.getName());
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				// Console
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /tempban [player] [days] [reason]");
					return true;
				}
				else {
					double bandays = 0.0;
					try {
						bandays = Double.parseDouble(args[1]);
					} catch(NumberFormatException e){
						bandays = 0.0;
					}
					
					long time = (long)((bandays * 86400000));
					
					String pname = args[0];
					String reason2 = ChatConstants.build(args).replaceAll(args[0], "");
					String reason = reason2.replaceAll(args[1], "");
					SQLManager.recordBan(pname, "SYSTEM", System.currentTimeMillis(), System.currentTimeMillis()+time, reason);
						
					if(Bukkit.getPlayer(pname) != null){
						Bukkit.broadcastMessage(ChatConstants.tempban("SYSTEM",pname,bandays));
						Bukkit.getPlayer(pname).kickPlayer(""
								+ ChatColor.DARK_RED + "[!]\n"
								+ ChatColor.DARK_RED + "You have been banned from this server.\n"
								+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(Bukkit.getPlayer(pname).getName()) + "\n"
								+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanTime(Bukkit.getPlayer(pname).getName())))) + "\n"
								+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanExpire(Bukkit.getPlayer(pname).getName())))) + "\n"
								+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(Bukkit.getPlayer(pname).getName())
						);
						sender.sendMessage(Timestamp.NOW() + "Player '" + Bukkit.getPlayer(pname).getName() + "' tempbanned.");
						return true;
					}
					else {
						// If the player is not online
						sender.sendMessage(Timestamp.NOW() + "Player '" + pname + "' offline-tempbanned.");
						plugin.log.info(pname + " has been temp-banned by SYSTEM");
						return true;
					}
				}
			}
			
		} // End of TempBan Command
		
		else if(cmd.getName().equalsIgnoreCase("ajail")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 3){
					p.sendMessage(ChatConstants.err("Syntax - /ajail [player] [minutes] [reason]"));
					return true;
				}
				else {
					String reasonA = ChatConstants.build(args).replaceFirst(args[0], "");
					String reason = reasonA.replaceFirst(args[1], "");
					int minutes; long timeDiff;
					
					try {
						minutes = Integer.parseInt(args[1]);
					} catch(NumberFormatException e){
						p.sendMessage(ChatConstants.err("Invalid number of minutes."));
						minutes = 0;
						return true;
					}
					
					timeDiff = minutes * 60000; // ms to min
					
					if(RPSystem.permission.has(p, plugin.config.getString("admin.permission.jail")) || p.isOp()){
						Player target = plugin.getServer().getPlayer(args[0]);
						
						if(target == null){
							p.sendMessage(ChatConstants.err("Player not found."));
							return true;
						}
						else {
							SQLManager.recordAjail(target.getName(), p.getName(), System.currentTimeMillis(), System.currentTimeMillis()+timeDiff, reason);
							Bukkit.broadcastMessage(ChatConstants.jail(p.getName(), args[0], minutes));
							if(target.isOnline()){
								Location jailLocation = new Location(plugin.getServer().getWorld(plugin.config.getString("jail.location.world")),plugin.config.getDouble("jail.location.x"),plugin.config.getDouble("jail.location.y"),plugin.config.getDouble("jail.location.z"));		
								target.teleport(jailLocation);
							}
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + args[0] + "' admin-jailed.");
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
					
				}
				
			}
			else {
				if(args.length < 3){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /ajail [player] [minutes] [reason]");
					return true;
				}
				else {
					String reasonA = ChatConstants.build(args).replaceFirst(args[0], "");
					String reason = reasonA.replaceFirst(args[1], "");
					int minutes; long timeDiff;
					
					try {
						minutes = Integer.parseInt(args[1]);
					} catch(NumberFormatException e){
						sender.sendMessage(Timestamp.NOW() + "Error: Invalid number of minutes.");
						minutes = 0;
						return true;
					}
					
					timeDiff = minutes * 60000; // ms to min
					
					Player target = plugin.getServer().getPlayer(args[0]);
						
					if(target == null){
						sender.sendMessage(ChatConstants.err("Player not found."));
						return true;
					}
					else {
						SQLManager.recordAjail(target.getName(), "SYSTEM", System.currentTimeMillis(), System.currentTimeMillis()+timeDiff, reason);
						Bukkit.broadcastMessage(ChatConstants.jail("SYSTEM", args[0], minutes));
						if(target.isOnline()){
							Location jailLocation = new Location(plugin.getServer().getWorld(plugin.config.getString("jail.location.world")),plugin.config.getDouble("jail.location.x"),plugin.config.getDouble("jail.location.y"),plugin.config.getDouble("jail.location.z"));		
							target.teleport(jailLocation);
						}
						sender.sendMessage(Timestamp.NOW() + "Player '" + args[0] + "' admin-jailed.");
						return true;
					}
					
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("arel")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /arel [player]"));
					return true;
				}
				else {
					
					if(RPSystem.permission.has(p, plugin.config.getString("admin.permission.unjail")) || p.isOp()){
						Player target = plugin.getServer().getPlayer(args[0]);
						
						if(target == null){
							p.sendMessage(ChatConstants.err("Player not found."));
							return true;
						}
						else {
							if(SQLManager.isJailed(args[0])){
								SQLManager.unjail(args[0]);
								Bukkit.broadcastMessage(ChatConstants.unjail("SYSTEM", args[0]));
								p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + args[0] + "' released from admin-jail.");
								return true;
							}
							else {
								p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + args[0] + "' is not in admin jail.");
								return true;
							}
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
					
				}
				
			}
			else {
				if(args.length < 1){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /arel [player]");
					return true;
				}
				else {
					
					Player target = plugin.getServer().getPlayer(args[0]);
						
					if(target == null){
						sender.sendMessage(ChatConstants.err("Player not found."));
						return true;
					}
					else {
						if(SQLManager.isJailed(args[0])){
							SQLManager.unjail(args[0]);
							Bukkit.broadcastMessage(ChatConstants.unjail("SYSTEM", args[0]));
							sender.sendMessage(Timestamp.NOW() + "Player '" + args[0] + "' released from admin-jail.");
							return true;
						}
						else {
							sender.sendMessage(Timestamp.NOW() + "Player '" + args[0] + "' is not in admin jail.");
							return true;
						}
					}
					
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("setjailspot")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(RPSystem.permission.has(p,plugin.config.getString("admin.permission.setjailspot")) || p.isOp()){
					plugin.config.set("jail.location.x", p.getLocation().getX());
					plugin.config.set("jail.location.y", p.getLocation().getY());
					plugin.config.set("jail.location.z", p.getLocation().getZ());
					plugin.config.set("jail.location.world", p.getLocation().getWorld().getName());
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Set admin jail spot.");
					return true;
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("kick")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 2){
					p.sendMessage(ChatConstants.err("Syntax - /kick [player] [reason]"));
					return true;
				}
				
				if(RPSystem.permission.has(p,plugin.config.getString("admin.permission.kick")) || p.isOp()){
					Player target = plugin.getServer().getPlayer(args[0]);
					String reason = ChatConstants.build(args).replaceFirst(args[0], "");
					
					if(target == null){
						p.sendMessage(ChatConstants.err("Player not found."));
						return true;
					}
					else {
						// Player is found
						ChatConstants.kick(p.getName(), target.getName());
						SQLManager.recordKick(target.getName(), p.getName(), System.currentTimeMillis(), reason);
						target.kickPlayer(
								"" + ChatColor.DARK_RED + "[!]\n"
								+ ChatColor.DARK_RED + "You have been kicked from the server by " + p.getName() + ".\n"
								+ ChatColor.DARK_RED + "Time: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss")).format(new java.util.Date(System.currentTimeMillis())) + "\n"
								+ ChatColor.DARK_RED + "Reason:\n"
								+ ChatColor.RED + reason
							);
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /kick [player] [reason]");
					return true;
				}
				
				Player target = plugin.getServer().getPlayer(args[0]);
				String reason = ChatConstants.build(args).replaceFirst(args[0], "");
					
				if(target == null){
					sender.sendMessage(Timestamp.NOW() + "Error: Player not found.");
					return true;
				}
				else {
					// Player is found
					ChatConstants.kick("SYSTEM", target.getName());
					SQLManager.recordKick(target.getName(), "SYSTEM", System.currentTimeMillis(), reason);
					target.kickPlayer(
							"" + ChatColor.DARK_RED + "[!]\n"
							+ ChatColor.DARK_RED + "You have been kicked from the server by SYSTEM.\n"
							+ ChatColor.DARK_RED + "Time: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss")).format(new java.util.Date(System.currentTimeMillis())) + "\n"
							+ ChatColor.DARK_RED + "Reason:\n"
							+ ChatColor.RED + reason
						);
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("skick")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 2){
					p.sendMessage(ChatConstants.err("Syntax - /skick [player] [reason]"));
					return true;
				}
				
				if(RPSystem.permission.has(p,plugin.config.getString("admin.permission.kick")) || p.isOp()){
					Player target = plugin.getServer().getPlayer(args[0]);
					String reason = ChatConstants.build(args).replaceFirst(args[0], "");
					
					if(target == null){
						p.sendMessage(ChatConstants.err("Player not found."));
						return true;
					}
					else {
						// Player is found
						ChatConstants.kick(p.getName(), target.getName());
						// SQLManager.recordKick(target.getName(), p.getName(), System.currentTimeMillis(), reason);
						target.kickPlayer(
								"" + ChatColor.DARK_RED + "[!]\n"
								+ ChatColor.DARK_RED + "You have been kicked from the server by " + p.getName() + ".\n"
								+ ChatColor.DARK_RED + "Time: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss")).format(new java.util.Date(System.currentTimeMillis())) + "\n"
								+ ChatColor.DARK_RED + "Reason:\n"
								+ ChatColor.RED + reason
							);
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /skick [player] [reason]");
					return true;
				}
				
				Player target = plugin.getServer().getPlayer(args[0]);
				String reason = ChatConstants.build(args).replaceFirst(args[0], "");
					
				if(target == null){
					sender.sendMessage(Timestamp.NOW() + "Error: Player not found.");
					return true;
				}
				else {
					// Player is found
					// ChatConstants.kick("SYSTEM", target.getName());
					SQLManager.recordKick(target.getName(), "SYSTEM", System.currentTimeMillis(), reason);
					target.kickPlayer(
							"" + ChatColor.DARK_RED + "[!]\n"
							+ ChatColor.DARK_RED + "You have been kicked from the server by SYSTEM.\n"
							+ ChatColor.DARK_RED + "Time: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss")).format(new java.util.Date(System.currentTimeMillis())) + "\n"
							+ ChatColor.DARK_RED + "Reason:\n"
							+ ChatColor.RED + reason
						);
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("admins")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				p.sendMessage("" + ChatColor.GRAY + "(( " + Timestamp.NOW() + "Admins Online: ))");
				for(String name : RPSystem.adminDuty.keySet()){
					Player pl = plugin.getServer().getPlayer(name);
					if(pl != null){
						if(SQLManager.getAdminLevel(name) >= 1 || pl.isOp()){
							if(RPSystem.adminDuty.get(name)) p.sendMessage("" + ChatColor.DARK_GREEN + "(( " + Timestamp.NOW() + name + " | Level: " + SQLManager.getAdminLevel(name) + " | Adminduty: Yes ))");
							else p.sendMessage("" + ChatColor.GRAY + "(( " + Timestamp.NOW() + name + " | Level: " + SQLManager.getAdminLevel(name) + " | Adminduty: No ))");
						}
					}
				}
				
				return true;
			}
			else {
				sender.sendMessage("" + ChatColor.GRAY + "(( " + Timestamp.NOW() + "Admins Online: ))");
				for(String name : RPSystem.adminDuty.keySet()){
					Player pl = plugin.getServer().getPlayer(name);
					if(pl != null){
						if(SQLManager.getAdminLevel(name) >= 1 || pl.isOp()){
							if(RPSystem.adminDuty.get(name)) sender.sendMessage("" + ChatColor.DARK_GREEN + "(( " + Timestamp.NOW() + name + " | Level: " + SQLManager.getAdminLevel(name) + " | Adminduty: Yes ))");
							else sender.sendMessage("" + ChatColor.GRAY + "(( " + Timestamp.NOW() + name + " | Level: " + SQLManager.getAdminLevel(name) + " | Adminduty: No ))");
						}
					}
				}
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("adminduty")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(SQLManager.getAdminLevel(p.getName()) >= 1 || p.isOp()){
					if(RPSystem.adminDuty.get(p.getName())){
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You are no longer on admin duty.");
						RPSystem.adminDuty.put(p.getName(), false);
						return true;
					}
					else {
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You are now on admin duty.");
						RPSystem.adminDuty.put(p.getName(), true);
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "You must be a player to use this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("setadminlevel")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				p.sendMessage(ChatConstants.err("This command can only be executed from the console."));
				return true;
			}
			else {
				// Console Only Command
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /setadminlevel [player] [level]");
					return true;
				}
				else {
					int level = 0;
					try { level = Integer.parseInt(args[1]); }
					catch(NumberFormatException e){
						sender.sendMessage(Timestamp.NOW() + "Error: Must specify a valid level.");
						return true;
					}
					SQLManager.setAdminLevel(args[0],level);
					sender.sendMessage(Timestamp.NOW() + "Set Player '" + args[0] + "' to Level " + args[1] + " admin.");
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("setdonlevel")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				p.sendMessage(ChatConstants.err("This command can only be executed from the console."));
				return true;
			}
			else {
				// Console Only Command
				if(args.length < 2){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /setdonlevel [player] [level]");
					return true;
				}
				else {
					int level = 0;
					try { level = Integer.parseInt(args[1]); }
					catch(NumberFormatException e){
						sender.sendMessage(Timestamp.NOW() + "Error: Must specify a valid level.");
						return true;
					}
					SQLManager.setDonatorLevel(args[0],level);
					sender.sendMessage(Timestamp.NOW() + "Set Player '" + args[0] + "' to Level " + args[1] + " Donator/VIP.");
					return true;
				}
			}
		}
		
		return false;
	}

}
