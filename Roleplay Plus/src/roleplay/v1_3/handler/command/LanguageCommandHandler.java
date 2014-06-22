package roleplay.v1_3.handler.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.Timestamp;

public class LanguageCommandHandler implements CommandExecutor {
	
	RPSystem plugin;
	public LanguageCommandHandler(RPSystem plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(cmd.getName().equalsIgnoreCase("lt")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Language Speak'.");
					RPSystem.channel.put(p.getName(), "langspeak");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.langspeak")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langspeak")/3){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.closeLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.closeLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langspeak")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langspeak")/2){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.normalLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.normalLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langspeak")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langspeak")){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.farLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.farLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("llow")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Language Low Speak'.");
					RPSystem.channel.put(p.getName(), "langlow");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.langlow")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langlow")/3){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.closeLangLow(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.closeLangLow(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langlow")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langlow")/2){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.normalLangLow(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.normalLangLow(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langlow")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langlow")){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.farLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.farLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		if(cmd.getName().equalsIgnoreCase("ls")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Language Shout'.");
					RPSystem.channel.put(p.getName(), "langshout");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.langshout")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langshout")/3){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.closeLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.closeLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langshout")/3 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langshout")/2){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.normalLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.normalLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
							}
							else if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range.langshout")/2 && 
									p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langshout")){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.farLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.farLangShout(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
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
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command.");
				return true;
			}
		}
		
		
		// Lang Whisper
		else if(cmd.getName().equalsIgnoreCase("lw")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				if(args.length < 1){
					p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "Changed focus to 'IC Language Whisper'.");
					RPSystem.channel.put(p.getName(), "langwhisper");
					return true;
				}
				if(RPSystem.muted.get(p.getName())){
					p.sendMessage(ChatConstants.err("You are muted."));
					return true;
				}
				
				else {
					if(RPSystem.permission.has(p, plugin.config.getString("chat.permission.langwhisper")) || p.isOp()){
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(p.getWorld().getName().equals(pl.getWorld().getName()) && p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langwhisper")){
								if(RPSystem.permission.has(pl, plugin.config.getString("languages.details." + RPSystem.activeLanguage.get(p.getName()) + ".speak-permission")) || pl.isOp()){
									pl.sendMessage(ChatConstants.whisperLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.build(args)));
								}
								else {
									pl.sendMessage(ChatConstants.whisperLang(p, RPSystem.activeLanguage.get(p.getName()), ChatConstants.censorBuild(args)));
								}
								pl.sendMessage(ChatConstants.action_me(p, "mutters something."));
							}
							// Anyone within (d^2) can see the auto-me
							else if(p.getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range.langwhisper")*plugin.config.getDouble("chat.range.langwhisper")){
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
		else if(cmd.getName().equalsIgnoreCase("setlang")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				
				if(args.length < 1){
					p.sendMessage(ChatConstants.err("Syntax - /setlang [LanguageTag]"));
					return true;
				}
				else {
					// Check if language exists
					if(plugin.config.getStringList("languages.list").contains(args[0])){
						if(RPSystem.permission.has(p, plugin.config.getString("languages.details." + args[0] + ".speak-permission")) || p.isOp()){
							p.sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "You are now speaking " + args[0] + ".");
							RPSystem.activeLanguage.put(p.getName(), args[0]);
							return true;
						}
						else {
							p.sendMessage(ChatConstants.err("Permission denied."));
							return true;
						}
					}
					else {
						p.sendMessage(ChatConstants.err("Language not found."));
						return true;
					}
				}
			}
			else {
				sender.sendMessage(Timestamp.NOW() + "Error: You must be a player to execute this command");
				return true;
			}
		}
		return false;
	}

}
