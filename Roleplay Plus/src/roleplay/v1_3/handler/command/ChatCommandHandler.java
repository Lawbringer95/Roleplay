package roleplay.v1_3.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class ChatCommandHandler implements CommandExecutor {
	
	RPSystem plugin;
	public ChatCommandHandler(RPSystem plugin){
		this.plugin = plugin;
	}
	
	boolean commandDebug = false;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(cmd.getName().equalsIgnoreCase("o")){
			if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command.");
			// TODO: Global OOC (Normal) Command
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as Player.");
				if(args.length < 1){
					if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as Player without enough args.");
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'Global OOC'.");
					RPSystem.channel.put(p.getName(), "gooc");
					return true;
				}
				
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				if(!RPSystem.b_gOOC_Allowed){
					if(!p.isOp() && !RPSystem.permission.has(p, plugin.config.getString("chat.permission.globalOOC"))){
						p.sendMessage("(( " + Timestamp.NOW() + "Global OOC is disabled. ))");
						return true;
					}
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.globalOOC")) || p.isOp()){
					for(Player player : Bukkit.getOnlinePlayers()){
						if(!RPSystem.canSeeGlobalOOC.get(player.getName())){
							if(p.isOp() || SQLManager.getAdminLevel(p.getName()) >= 1 || RPSystem.permission.has(p, plugin.config.getString("chat.permission.adminOOC"))){
								player.sendMessage(ChatConstants.globalOOC(p,ChatConstants.build(args)));
							}
						}
						else {
							player.sendMessage(ChatConstants.globalOOC(p,ChatConstants.build(args)));
						}
					}
					if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as Player - success.");
					return true;
				}
				else {
					if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as Player, perms denied.");
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(args.length < 1){
					if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as SYSTEM without enough args.");
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /o [message]");
					return true;
				}
				else {
					if(commandDebug) plugin.log.info(plugin.logPrefix + "DEBUG: Encountered Global OOC Command as SYSTEM - success.");
					Bukkit.broadcastMessage(ChatConstants.sys_globalOOC(ChatConstants.build(args)));
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("ao")){
			// TODO: Global OOC (Normal) Command
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'Admin Broadcast OOC'.");
					RPSystem.channel.put(p.getName(), "aooc");
					return true;
				}
				
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.adminOOC")) || p.isOp()){
					if(SQLManager.getAdminLevel(p.getName()) >= 1 || p.isOp()){
						for(Player player : Bukkit.getOnlinePlayers()){
							player.sendMessage(ChatConstants.adminOOC(p,ChatConstants.build(args)));
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(args.length < 1){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /ao [message]");
					return true;
				}
				else {
					Bukkit.broadcastMessage(ChatConstants.sys_adminOOC(ChatConstants.build(args)));
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("b")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'Local OOC'.");
					RPSystem.channel.put(p.getName(), "looc");
					return true;
				}
				
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.localOOC")) || p.isOp()){
					for(Player player : Bukkit.getOnlinePlayers()){
						if(p.getLocation().distance(player.getLocation()) <= plugin.config.getDouble("chat.range.localOOC")){
							if(!RPSystem.canSeeLocalOOC.get(player.getName())){
								if(p.isOp() || SQLManager.getAdminLevel(p.getName()) >= 1 || RPSystem.permission.has(p, plugin.config.getString("chat.permission.adminOOC"))){
									player.sendMessage(ChatConstants.localOOC(p,ChatConstants.build(args)));
								}
							}
							else {
								player.sendMessage(ChatConstants.localOOC(p,ChatConstants.build(args)));
							}
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
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("silence")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /silence [player]"));
					return true;
				}
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.silence")) || p.isOp()){
						if(Bukkit.getPlayer(args[0]).isOnline() && Bukkit.getPlayer(args[0]) != null){
							if(RPSystem.muted.get(Bukkit.getPlayer(args[0]).getName())){
								p.sendMessage(ChatConstants.err("Player is already muted."));
								return true;
							}
							else {
								p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + Bukkit.getPlayer(args[0]).getName() + "' muted.");
								Bukkit.broadcastMessage(ChatConstants.mute(p.getName(), Bukkit.getPlayer(args[0]).getName()));
								RPSystem.muted.put(Bukkit.getPlayer(args[0]).getName(), true);
								return true;
							}
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("unsilence")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /unsilence [player]"));
					return true;
				}
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.silence")) || p.isOp()){
						if(Bukkit.getPlayer(args[0]).isOnline() && Bukkit.getPlayer(args[0]) != null){
							if(!RPSystem.muted.get(Bukkit.getPlayer(args[0]).getName())){
								p.sendMessage(ChatConstants.err("Player is not muted."));
								return true;
							}
							else {
								p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Player '" + Bukkit.getPlayer(args[0]).getName() + "' unmuted.");
								Bukkit.broadcastMessage(ChatConstants.unmute(p.getName(), Bukkit.getPlayer(args[0]).getName()));
								RPSystem.muted.put(Bukkit.getPlayer(args[0]).getName(), false);
								return true;
							}
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("t")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Speak'.");
					RPSystem.channel.put(p.getName(), "speak");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.speak")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.speak")/3){
								pl.sendMessage(ChatConstants.close(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.speak")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.speak")/2){
								pl.sendMessage(ChatConstants.normal(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.speak")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.speak")){
								pl.sendMessage(ChatConstants.far(p, ChatConstants.build(args)));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("low")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Low Speak'.");
					RPSystem.channel.put(p.getName(), "low");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.low")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.low")/3){
								pl.sendMessage(ChatConstants.closeLow(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.low")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.low")/2){
								pl.sendMessage(ChatConstants.normalLow(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.low")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.low")){
								pl.sendMessage(ChatConstants.farLow(p, ChatConstants.build(args)));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("s")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Shout'.");
					RPSystem.channel.put(p.getName(), "shout");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.shout")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.shout")/3){
								pl.sendMessage(ChatConstants.closeShout(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.shout")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.shout")/2){
								pl.sendMessage(ChatConstants.normalShout(p, ChatConstants.build(args)));
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.shout")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.shout")){
								pl.sendMessage(ChatConstants.farShout(p, ChatConstants.build(args)));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("w")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Whisper'.");
					RPSystem.channel.put(p.getName(), "whisper");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.whisper")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.whisper")){
								pl.sendMessage(ChatConstants.whisper(p, ChatConstants.build(args)));
								pl.sendMessage(ChatConstants.action_me(p, "mutters something."));
							}
							// Anyone within (d^2) can see the auto-me
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.whisper")*plugin.config.getDouble("chat.range.whisper")){
								pl.sendMessage(ChatConstants.action_me(p, "mutters something."));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("me")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /me [action]"));
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.me")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.me")){
								pl.sendMessage(ChatConstants.action_me(p, ChatConstants.build(args)));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("do")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /do [action]"));
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.do")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.do")){
								pl.sendMessage(ChatConstants.action_do(p, ChatConstants.build(args)));
							}
						}
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("name")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /name [display name]"));
					return true;
				}
				if(ChatConstants.build(args).length() > 30){
					p.sendMessage(ChatConstants.err("Display name too long."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.setname")) || p.isOp()){
						SQLManager.setDisplayName(p.getName(), ChatConstants.build(args));
						p.sendMessage(ChatColor.YELLOW + "(( " + Timestamp.NOW() + "Changed RP Name to '" + ChatConstants.build(args) + "'. ))");
						return true;
					}
					else {
						p.sendMessage(ChatConstants.err("Permission denied."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("resetname")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /resetname [player]"));
					return true;
				}
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.resetname")) || p.isOp()){
						if(Bukkit.getPlayer(args[0]).isOnline() && Bukkit.getPlayer(args[0]) != null){
							Bukkit.broadcastMessage(ChatConstants.nameReset(p.getName(), Bukkit.getPlayer(args[0]).getName()));
							SQLManager.setDisplayName(Bukkit.getPlayer(args[0]).getName(), Bukkit.getPlayer(args[0]).getName());
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		else if(cmd.getName().equalsIgnoreCase("a")){
			// TODO: Global OOC (Normal) Command
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'Admin Chat'.");
					RPSystem.channel.put(p.getName(), "admin");
					return true;
				}
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.adminOOC")) || p.isOp()){
					if(SQLManager.getAdminLevel(p.getName()) >= 1 || p.isOp()){
						for(Player player : Bukkit.getOnlinePlayers()){
							if(SQLManager.getAdminLevel(player.getName()) >= 1 || player.isOp() || RPSystem.permission.has(p, plugin.config.getString("chat.permission.adminOOC"))){
								player.sendMessage(ChatConstants.adminChat(p,ChatConstants.build(args)));
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
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(args.length < 1){
					sender.sendMessage(Timestamp.NOW() + "Error: Syntax - /a [message]");
					return true;
				}
				else {
					Bukkit.broadcastMessage(ChatConstants.sys_adminChat(ChatConstants.build(args)));
					return true;
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("togb")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.localOOC"))){
					if(RPSystem.canSeeLocalOOC.get(p.getName())){
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You can no longer see Local OOC from players.");
						RPSystem.canSeeLocalOOC.put(p.getName(), false);
						return true;
					}
					else {
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You can now see Local OOC from players.");
						RPSystem.canSeeLocalOOC.put(p.getName(), true);
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
		else if(cmd.getName().equalsIgnoreCase("togooc")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.globalOOC"))){
					if(RPSystem.canSeeGlobalOOC.get(p.getName())){
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You can no longer see Global OOC from players.");
						RPSystem.canSeeGlobalOOC.put(p.getName(), false);
						return true;
					}
					else {
						p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You can now see Global OOC from players.");
						RPSystem.canSeeGlobalOOC.put(p.getName(), true);
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
		else if(cmd.getName().equalsIgnoreCase("ooc")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(RPSystem.permission.has(p,plugin.config.getString("chat.permission.silence")) || p.isOp() || SQLManager.getAdminLevel(p.getName()) >= 1){
					if(RPSystem.b_gOOC_Allowed){
						Bukkit.broadcastMessage("(( " + Timestamp.NOW() + SQLManager.getDisplayName(p.getName()) + " (" + p.getName() + ") has switched off Global OOC. ))");
						RPSystem.b_gOOC_Allowed = false;
						return true;
					}
					else {
						Bukkit.broadcastMessage("(( " + Timestamp.NOW() + SQLManager.getDisplayName(p.getName()) + " (" + p.getName() + ") has switched on Global OOC. ))");
						RPSystem.b_gOOC_Allowed = true;
						return true;
					}
				}
				else {
					p.sendMessage(ChatConstants.err("Permission denied."));
					return true;
				}
			}
			else {
				if(RPSystem.b_gOOC_Allowed){
					Bukkit.broadcastMessage("(( " + Timestamp.NOW() + "SYSTEM has switched off Global OOC. ))");
					RPSystem.b_gOOC_Allowed = false;
					return true;
				}
				else {
					Bukkit.broadcastMessage("(( " + Timestamp.NOW() + "SYSTEM has switched on Global OOC. ))");
					RPSystem.b_gOOC_Allowed = true;
					return true;
				}
			}
		}
		
		return false;
	}

}
