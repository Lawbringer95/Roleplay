package roleplay.v1_3.handler.event;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import roleplay.v1_3.engine.RPSystem;
import roleplay.v1_3.manager.ChatConstants;
import roleplay.v1_3.manager.SQLManager;
import roleplay.v1_3.manager.Timestamp;

public class ServerEventHandler implements Listener {
	
	RPSystem plugin;
	public ServerEventHandler(RPSystem plugin){
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerLogin(PlayerJoinEvent event){
		if(!RPSystem.muted.containsKey(event.getPlayer().getName())) RPSystem.muted.put(event.getPlayer().getName(), false);
		if(!RPSystem.canSeeGlobalOOC.containsKey(event.getPlayer().getName())) RPSystem.canSeeGlobalOOC.put(event.getPlayer().getName(), true);
		if(!RPSystem.canSeeLocalOOC.containsKey(event.getPlayer().getName())) RPSystem.canSeeLocalOOC.put(event.getPlayer().getName(), true);
		RPSystem.adminDuty.put(event.getPlayer().getName(), false);
		RPSystem.activeLanguage.put(event.getPlayer().getName(), "Common");
		if(!RPSystem.drunk.containsKey(event.getPlayer().getName())) RPSystem.drunk.put(event.getPlayer().getName(), 0L);
		if(!RPSystem.high.containsKey(event.getPlayer().getName())) RPSystem.high.put(event.getPlayer().getName(), 0L);
		if(!RPSystem.description.containsKey(event.getPlayer().getName())) RPSystem.description.put(event.getPlayer().getName(),"No Attributes Set.");
		if(!RPSystem.channel.containsKey(event.getPlayer().getName())) RPSystem.channel.put(event.getPlayer().getName(),"speak");
		
		if(plugin.config.getBoolean("features.admin.list") && event.getPlayer().isOp() && SQLManager.getAdminLevel(event.getPlayer().getName()) >= plugin.config.getInt("admin.admin-level-for-op")){
			plugin.log.info("Granted op to " + event.getPlayer().getName() + ": Admin Level " + SQLManager.getAdminLevel(event.getPlayer().getName()) + "(" + plugin.config.getInt("admin.admin-level-for-op") + " required)");
			event.getPlayer().setOp(true);
		}
		else if(plugin.config.getBoolean("features.admin.list") && event.getPlayer().isOp() && SQLManager.getAdminLevel(event.getPlayer().getName()) < plugin.config.getInt("admin.admin-level-for-op")){
			plugin.log.info("Revoked op from " + event.getPlayer().getName() + ": Admin Level " + SQLManager.getAdminLevel(event.getPlayer().getName()) + "(" + plugin.config.getInt("admin.admin-level-for-op") + " required)");
			event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "NOTICE: You do not have Admin Level " + plugin.config.getInt("admin.admin-level-for-op") + " or higher.");
			event.getPlayer().sendMessage(ChatColor.YELLOW + Timestamp.NOW() + "NOTICE: Operator status has been removed from your account.");
		}
		
		if(!SQLManager.playerExists(event.getPlayer().getName())){
			plugin.log.info(plugin.logPrefix + "Player '" + event.getPlayer().getName() + "' does not exist.");
			SQLManager.createPlayer(event.getPlayer().getName());
			plugin.log.info(plugin.logPrefix + "Done.");
		}
		else {
			plugin.log.info(plugin.logPrefix + "Player '" + event.getPlayer().getName() + "' loaded.");
			
			// Check for admin jail
			if(SQLManager.isJailed(event.getPlayer().getName())){
				Location jailLocation = new Location(plugin.getServer().getWorld(plugin.config.getString("jail.location.world")),plugin.config.getDouble("jail.location.x"),plugin.config.getDouble("jail.location.y"),plugin.config.getDouble("jail.location.z"));
				
				event.getPlayer().teleport(jailLocation);
				
				event.getPlayer().sendMessage(ChatColor.DARK_RED + "(( " + Timestamp.NOW() + "You are in admin jail. ))");
				event.getPlayer().sendMessage(ChatColor.RED + "(( " + Timestamp.NOW() + "Jailed by: " + SQLManager.getJailer(event.getPlayer().getName()) + " ))");
				event.getPlayer().sendMessage(ChatColor.RED + "(( " + Timestamp.NOW() + "Jailed on: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getJailTime(event.getPlayer().getName())))) + " ))");
				event.getPlayer().sendMessage(ChatColor.RED + "(( " + Timestamp.NOW() + "To Be Released: " + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getJailExpire(event.getPlayer().getName())))) + " ))");
				event.getPlayer().sendMessage(ChatColor.RED + "(( " + Timestamp.NOW() + "Reason: " + SQLManager.getJailReason(event.getPlayer().getName()) + " ))");
				event.getPlayer().sendMessage(ChatColor.RED + "(( " + Timestamp.NOW() + "Note: Jail time continues counting even when offline. ))");
			}
		}
		
		// Alcohol and Drugs - Potion Effect Management
		
		for(PotionEffect eff : event.getPlayer().getActivePotionEffects()){
			event.getPlayer().removePotionEffect(eff.getType());
		}
		
		if(RPSystem.high.get(event.getPlayer().getName())+300000 > (System.currentTimeMillis())){
			long milliseconds = Long.valueOf((RPSystem.high.get(event.getPlayer().getName())+300000)-System.currentTimeMillis());
			int ticks = ((int)milliseconds/1000)*20;
			
			plugin.log.info("Player has effect: Drug.HIGH (" + ((int)milliseconds/1000) + " seconds)");
			
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.HUNGER,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,ticks,4));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,ticks,1));
		}
		
		if(RPSystem.drunk.get(event.getPlayer().getName())+300000 > (System.currentTimeMillis())){
			long milliseconds = Long.valueOf((RPSystem.drunk.get(event.getPlayer().getName())+300000)-System.currentTimeMillis());
			int ticks = ((int)milliseconds/1000)*20;
			
			plugin.log.info("Player has effect: Alcohol.DRUNK (" + ((int)milliseconds/1000) + " seconds)");
			
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,ticks,2));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS,ticks,0));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,ticks,1));
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW,ticks,0));
		}
		
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event){
		if(SQLManager.isJailed(event.getPlayer().getName()) && !event.getPlayer().isOp() && SQLManager.getAdminLevel(event.getPlayer().getName()) < 1){
			event.getPlayer().sendMessage(ChatConstants.err("You cannot use commands in admin jail."));
			event.setCancelled(true);
		}
	}
	
	@EventHandler (priority = EventPriority.HIGH)
	public void onPlayerLogin(PlayerLoginEvent event){
		// Check for bans in the database
		
		if(SQLManager.isBanned(event.getPlayer().getName())){
			plugin.log.info("Banned player " + event.getPlayer().getName() + " attempted to enter the server.");
			Player pl = event.getPlayer();
			event.setKickMessage(""
											+ ChatColor.DARK_RED + "[!]\n"
											+ ChatColor.DARK_RED + "You are banned from this server.\n"
											+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(pl.getName()) + "\n"
											+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss").format(new java.util.Date(SQLManager.getBanTime(pl.getName())))) + "\n"
											+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm:ss").format(new java.util.Date(SQLManager.getBanExpire(pl.getName())))) + "\n"
											+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(pl.getName()));
			event.setResult(Result.KICK_OTHER);
		}
	}
	
	/*
	 * final Player pl = event.getPlayer();
						try {
									pl.kickPlayer(""
											+ ChatColor.DARK_RED + "[!]\n"
											+ ChatColor.DARK_RED + "You are banned from this server.\n"
											+ ChatColor.DARK_RED + "Banned by: " + ChatColor.RED + SQLManager.getBanner(pl.getName()) + "\n"
											+ ChatColor.DARK_RED + "Banned on: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanTime(pl.getName())))) + "\n"
											+ ChatColor.DARK_RED + "Banned until: " + ChatColor.RED + (new java.text.SimpleDateFormat("MM/dd/yyyy kk:mm").format(new java.util.Date(SQLManager.getBanExpire(pl.getName())))) + "\n"
											+ ChatColor.DARK_RED + "Reason: " + ChatColor.RED + SQLManager.getBanReason(pl.getName())
										);
									
						} catch(ClassCastException e){
							e.printStackTrace();
							plugin.log.severe("Internal exception caused by " + e.getCause().toString());
						}
	 */

}
