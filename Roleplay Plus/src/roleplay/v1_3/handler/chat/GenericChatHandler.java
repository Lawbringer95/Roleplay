package roleplay.v1_3.handler.chat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class GenericChatHandler implements Listener {
	
	RPSystem plugin;
	public GenericChatHandler(RPSystem plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		if(containsBackslash(event.getMessage())) event.setCancelled(true);
		
		
		if(!event.isCancelled()){
			if(!RPSystem.muted.get(event.getPlayer().getName()) && !SQLManager.isJailed(event.getPlayer().getName())){
				
				if(!RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("gooc") && !RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("aooc") && !RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("looc") && !RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("admin")){
					if(RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission." + RPSystem.channel.get(event.getPlayer().getName()))) || event.getPlayer().isOp()){
						plugin.log.info(Timestamp.NOW() + event.getPlayer() + " says: " + event.getMessage());
						for(Player pl : Bukkit.getOnlinePlayers()){
							if(event.getPlayer().getWorld().getName().equals(pl.getWorld().getName()) && event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range." + RPSystem.channel.get(event.getPlayer().getName()))/3){
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("speak")) pl.sendMessage(ChatConstants.close(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("low")) pl.sendMessage(ChatConstants.closeLow(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("shout")) pl.sendMessage(ChatConstants.closeShout(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("whisper")) pl.sendMessage(ChatConstants.whisper(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langspeak")) pl.sendMessage(ChatConstants.closeLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langshout")) pl.sendMessage(ChatConstants.closeLangShout(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langlow")) pl.sendMessage(ChatConstants.closeLangLow(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langwhisper")) pl.sendMessage(ChatConstants.whisperLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
							}
							else if(event.getPlayer().getWorld().getName().equals(pl.getWorld().getName()) && event.getPlayer().getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range." + RPSystem.channel.get(event.getPlayer().getName()))/3 && 
									event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range." + RPSystem.channel.get(event.getPlayer().getName()))/2){
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("speak")) pl.sendMessage(ChatConstants.normal(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("low")) pl.sendMessage(ChatConstants.normalLow(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("shout")) pl.sendMessage(ChatConstants.normalShout(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("whisper")) pl.sendMessage(ChatConstants.whisper(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langspeak")) pl.sendMessage(ChatConstants.normalLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langshout")) pl.sendMessage(ChatConstants.normalLangShout(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langlow")) pl.sendMessage(ChatConstants.normalLangLow(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langwhisper")) pl.sendMessage(ChatConstants.whisperLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));					}
							else if(event.getPlayer().getWorld().getName().equals(pl.getWorld().getName()) && event.getPlayer().getLocation().distance(pl.getLocation()) >= plugin.config.getDouble("chat.range." + RPSystem.channel.get(event.getPlayer().getName()))/2 && 
									event.getPlayer().getLocation().distance(pl.getLocation()) < plugin.config.getDouble("chat.range." + RPSystem.channel.get(event.getPlayer().getName()))){
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("speak")) pl.sendMessage(ChatConstants.far(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("low")) pl.sendMessage(ChatConstants.farLow(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("shout")) pl.sendMessage(ChatConstants.farShout(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("whisper")) pl.sendMessage(ChatConstants.whisper(event.getPlayer(), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langspeak")) pl.sendMessage(ChatConstants.farLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langshout")) pl.sendMessage(ChatConstants.farLangShout(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langlow")) pl.sendMessage(ChatConstants.farLangLow(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));
								if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("langwhisper")) pl.sendMessage(ChatConstants.whisperLang(event.getPlayer(), RPSystem.activeLanguage.get(event.getPlayer().getName()), "" + ChatConstants.build(event.getMessage().split(" "))));					}
						}
						
						event.setMessage("");
						event.setCancelled(true);
					}
					
					else {
						event.getPlayer().sendMessage(ChatConstants.err("Permission to speak denied."));
						event.setMessage("");
						event.setCancelled(true);
					}
				}
				else {
					// Handle OOC chats: aooc, gooc, looc, admin
					if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("aooc")){
						if(RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.adminOOC")) || event.getPlayer().isOp()){
							if(SQLManager.getAdminLevel(event.getPlayer().getName()) >= 1 || event.getPlayer().isOp()){
								for(Player player : Bukkit.getOnlinePlayers()){
									player.sendMessage(ChatConstants.adminOOC(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
								}
							}
							else {
								event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
							}
						}
						else {
							event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
						}
						
						event.setMessage("");
						event.setCancelled(true);
					}
					else if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("gooc")){
						if(RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.globalOOC")) || event.getPlayer().isOp()){
							for(Player player : Bukkit.getOnlinePlayers()){
								if(!RPSystem.canSeeGlobalOOC.get(player.getName())){
									if(event.getPlayer().isOp() || SQLManager.getAdminLevel(event.getPlayer().getName()) >= 1 || RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.adminOOC"))){
										player.sendMessage(ChatConstants.globalOOC(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
									}
								}
								else {
									player.sendMessage(ChatConstants.globalOOC(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
								}
							}
						}
						else {
							event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
						}
						
						event.setMessage("");
						event.setCancelled(true);
					}
					else if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("looc")){
						if(RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.localOOC")) || event.getPlayer().isOp()){
							for(Player player : Bukkit.getOnlinePlayers()){
								if(event.getPlayer().getLocation().distance(player.getLocation()) <= plugin.config.getDouble("chat.range.localOOC")){
									if(!RPSystem.canSeeLocalOOC.get(player.getName())){
										if(event.getPlayer().isOp() || SQLManager.getAdminLevel(event.getPlayer().getName()) >= 1 || RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.adminOOC"))){
											player.sendMessage(ChatConstants.localOOC(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
										}
									}
									else {
										player.sendMessage(ChatConstants.localOOC(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
									}
								}
							}
						}
						else {
							event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
						}
						
						event.setMessage("");
						event.setCancelled(true);
					}
					else if(RPSystem.channel.get(event.getPlayer().getName()).equalsIgnoreCase("admin")){
						if(RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.adminOOC")) || event.getPlayer().isOp()){
							if(SQLManager.getAdminLevel(event.getPlayer().getName()) >= 1 || event.getPlayer().isOp()){
								for(Player player : Bukkit.getOnlinePlayers()){
									if(SQLManager.getAdminLevel(player.getName()) >= 1 || player.isOp() || RPSystem.permission.has(event.getPlayer(), plugin.config.getString("chat.permission.adminOOC"))){
										player.sendMessage(ChatConstants.adminChat(event.getPlayer(),ChatConstants.build(event.getMessage().split(" "))));
									}
								}
							}
							else {
								event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
							}
						}
						else {
							event.getPlayer().sendMessage(ChatConstants.err("Permission denied."));
						}
						
						event.setMessage("");
						event.setCancelled(true);
					}
					else {
						event.getPlayer().sendMessage(ChatConstants.err("Channel not found."));
						RPSystem.channel.put(event.getPlayer().getName(), "speak");
						event.setMessage("");
						event.setCancelled(true);
					}
				}
			}
			else {
				if(SQLManager.isJailed(event.getPlayer().getName()) && !event.getPlayer().isOp() && SQLManager.getAdminLevel(event.getPlayer().getName()) < 1){
					event.getPlayer().sendMessage(ChatConstants.err("You cannot speak in admin jail."));
				}
				if(RPSystem.muted.get(event.getPlayer().getName())){
					event.getPlayer().sendMessage(ChatConstants.err("You are muted."));
				}
				event.setMessage("");
				event.setCancelled(true);
			}
		}
	}
	
	// http://stackoverflow.com/questions/3173773/how-to-check-for-back-slash-in-input
	private boolean containsBackslash(String str){
		
		final char[] chars = str.toCharArray();
        char c;
        for(int i = 0; i<chars.length; i++ ){
           c = chars[i];
           if(c ==  '\\' ) {
        	   return true;
           }
        }
		
		return false;		
	}
}
