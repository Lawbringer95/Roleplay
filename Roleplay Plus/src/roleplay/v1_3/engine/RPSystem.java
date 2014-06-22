package roleplay.v1_3.engine;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.milkbowl.vault.permission.Permission;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import roleplay.v1_3.handler.chat.GenericChatHandler;
import roleplay.v1_3.handler.command.AdminCommandHandler;
import roleplay.v1_3.handler.command.ChatCommandHandler;
import roleplay.v1_3.handler.command.FeaturesCommandHandler;
import roleplay.v1_3.handler.command.LanguageCommandHandler;
import roleplay.v1_3.handler.event.PlayerEventHandler;
import roleplay.v1_3.handler.event.ServerEventHandler;
import roleplay.v1_3.manager.SQLManager;

public class RPSystem extends JavaPlugin {
	
	public Logger log;
	public String logPrefix;
	
	public static Permission permission = null;
	
	public static HashMap<String,Boolean> muted = new HashMap<String,Boolean>();
	public static HashMap<String,Boolean> canSeeGlobalOOC = new HashMap<String,Boolean>();
	public static HashMap<String,Boolean> canSeeLocalOOC = new HashMap<String,Boolean>();
	public static HashMap<String,Boolean> adminDuty = new HashMap<String,Boolean>();
	public static HashMap<String,String> activeLanguage = new HashMap<String,String>();
	public static HashMap<String,Long> drunk = new HashMap<String,Long>();
	public static HashMap<String,Long> high = new HashMap<String,Long>();
	public static HashMap<String,String> description = new HashMap<String,String>();
	public static HashMap<String,String> channel = new HashMap<String,String>();
	public static boolean b_gOOC_Allowed = true;
	
	// Command Handlers
	private AdminCommandHandler acm; // Not done
	private ChatCommandHandler ccm; // Not done
	private FeaturesCommandHandler fcm; // Not done
	private LanguageCommandHandler lcm; // Not done
	
	// Event Handlers
	private PlayerEventHandler pem;
	private ServerEventHandler sem;
	private GenericChatHandler cm;
	
	// Managers
	public FileConfiguration config;
	public FileConfiguration itemConfig;
	public static FileConfiguration chatConfig;
	
	@Override
	public void onDisable(){
		try {
			SQLManager.disconnect();
		} catch(Exception e){
			log.severe("SQL Database encountered an error while disconnecting.");
			if(e.getMessage()!=null) log.severe("Error: " + e.getMessage());
			if(e.getCause()!=null) log.severe("Caused by: " + e.getCause().toString());
		}
		try {
			this.config.save(this.getDataFolder() + File.separator + "config.yml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onEnable(){
		boolean filesOK = true;
		log = Logger.getLogger("Minecraft");
		logPrefix = "(MRPS " + this.getDescription().getVersion() + ") ";
		
		
		// SQL Stuff
		SQLManager.connect(this);
		
		File f = new File(this.getDataFolder() + File.separator + "config.yml");
		
		config = YamlConfiguration.loadConfiguration(f);
		
		File f2 = new File(this.getDataFolder() + File.separator + "restricted-items.yml");
		log.info("Loading restricted items:");
		itemConfig = YamlConfiguration.loadConfiguration(f2);
		
		boolean restrictionsOK = true;
		if(itemConfig.getStringList("restricted-items").size() > 1){
			
			for(String restrictedItem : itemConfig.getStringList("restricted-items")){
				String blockInfo = "";
				
				if(itemConfig.contains(restrictedItem)){
					if(Material.getMaterial(restrictedItem).isBlock()) blockInfo = blockInfo + "[BLOCK]";
					else blockInfo = blockInfo + "[ITEM]";
					
					if(Material.getMaterial(restrictedItem).isEdible()) blockInfo = blockInfo + "[FOOD]";
					
					if(Material.getMaterial(restrictedItem).isBurnable()) blockInfo = blockInfo + "[FUEL]";
					
					if(Material.getMaterial(restrictedItem).isRecord()) blockInfo = blockInfo + "[RECORD]";
									
					log.info("> " + restrictedItem);
					log.info(">> " + blockInfo);
					if(itemConfig.contains(restrictedItem + ".smelt-permission")) log.info(">> Smelt: " + itemConfig.getString(restrictedItem + ".smelt-permission"));
					else {
						log.severe(">> Smelt: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".craft-permission")) log.info(">> Craft: " + itemConfig.getString(restrictedItem + ".craft-permission"));
					else {
						log.severe(">> Craft: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".interact-permission")) log.info(">> interact: " + itemConfig.getString(restrictedItem + ".interact-permission"));
					else {
						log.severe(">> Interact: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".place-permission")) log.info(">> Place Block: " + itemConfig.getString(restrictedItem + ".place-permission"));
					else {
						log.severe(">> Place Block: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".break-permission")) log.info(">> Break Block: " + itemConfig.getString(restrictedItem + ".break-permission"));
					else {
						log.severe(">> Break Block: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".equip-permission")) log.info(">> Equip Item: " + itemConfig.getString(restrictedItem + ".equip-permission"));
					else {
						log.severe(">> Equip Item: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
					
					if(itemConfig.contains(restrictedItem + ".attack-permission")) log.info(">> Attack: " + itemConfig.getString(restrictedItem + ".attack-permission"));
					else {
						log.severe(">> Attack: PERMISSION NOT FOUND");
						restrictionsOK = false;
					}
				}
				else {
					log.severe("Restricted Item Configuration not found.");
					restrictionsOK = false;
				}
			}
		} else {
			log.info("No restricted items found.");
		}
		
		
		File f3 = new File(this.getDataFolder() + File.separator + "chat.yml");
		chatConfig = YamlConfiguration.loadConfiguration(f3);
		
		// TODO: Test configuration loading
		/*
		log.info("Testing Config:");
		log.info("features.admin.ban:" + config.getBoolean("features.admin.ban"));
		log.info("features.admin.kick:" + config.getBoolean("features.admin.kick"));
		log.info("features.admin.jail:" + config.getBoolean("features.admin.jail"));
		log.info("features.chat:" + config.getBoolean("features.chat"));
		log.info("features.languages:" + config.getBoolean("features.languages"));
		log.info("features.rpg:" + config.getBoolean("features.rpg"));
		
		log.info("Database Information for iTzFusiioNx [TEST DATA]");
		log.info("Display Name: " + SQLManager.getDisplayName("iTzFusiioNx"));
		log.info("Mining Level: " + SQLManager.getSkillLevel("iTzFusiioNx", Skill.MINING));
		SQLManager.createPlayer("TestUser140602c");
		log.info("Display Name: " + SQLManager.getDisplayName("TestUser140602c"));
		log.info("Woodcutting Level: " + SQLManager.getSkillLevel("TestUser140602c",Skill.WOODCUTTING) + " (" + SQLManager.getSkillExperience("TestUser140602c",Skill.WOODCUTTING) + " XP)");
		SQLManager.setSkillExperience("TestUser140602c", Skill.WOODCUTTING, 149);
		log.info("Woodcutting Level: " + SQLManager.getSkillLevel("TestUser140602c",Skill.WOODCUTTING) + " (" + SQLManager.getSkillExperience("TestUser140602c",Skill.WOODCUTTING) + " XP)");
		*/
		
		if(!setupPermissions()){
			log.severe(logPrefix + "Failed to link Vault Permissions.");
		}
		
		PluginManager pm = this.getServer().getPluginManager();
		log.info(logPrefix + "Registering event handlers.");
		
		pem = new PlayerEventHandler(this); pm.registerEvents(pem, this);
		sem = new ServerEventHandler(this); pm.registerEvents(sem, this);
		
		log.info(logPrefix + "Registering chat handlers.");
		cm = new GenericChatHandler(this); pm.registerEvents(cm, this);
		
		log.info(logPrefix + "Registering command module: Admin Commands");
		
		acm = new AdminCommandHandler(this);
		if(config.getBoolean("features.admin.ban")){
			this.getCommand("ban").setExecutor(acm); log.info(logPrefix + "Registered command: /ban"); // DONE
			this.getCommand("unban").setExecutor(acm); log.info(logPrefix + "Registered command: /unban"); // DONE
			this.getCommand("tempban").setExecutor(acm); log.info(logPrefix + "Registered command: /tempban"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.admin.ban");
		
		if(config.getBoolean("features.admin.jail")){
			this.getCommand("ajail").setExecutor(acm); log.info(logPrefix + "Registered command: /ajail"); // DONE
			this.getCommand("arel").setExecutor(acm); log.info(logPrefix + "Registered command: /arel"); // DONE
			this.getCommand("setjailspot").setExecutor(acm); log.info(logPrefix + "Registered command: /setjailspot"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.admin.jail");
		
		if(config.getBoolean("features.admin.kick")){
			this.getCommand("kick").setExecutor(acm); log.info(logPrefix + "Registered command: /kick"); // DONE
			this.getCommand("skick").setExecutor(acm); log.info(logPrefix + "Registered command: /skick"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.admin.kick");
		
		if(config.getBoolean("features.admin.list")){
			this.getCommand("admins").setExecutor(acm); log.info(logPrefix + "Registered command: /admins"); // DONE
			this.getCommand("adminduty").setExecutor(acm); log.info(logPrefix + "Registered command: /adminduty"); // DONE
			this.getCommand("setadminlevel").setExecutor(acm); log.info(logPrefix + "Registered command: /setadminlevel"); // DONE
			this.getCommand("setdonlevel").setExecutor(acm); log.info(logPrefix + "Registered command: /setdonlevel"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.admin.list");
		
		
		log.info(logPrefix + "Registering command module: Chat Commands");
		
		if(config.getBoolean("features.chat")){
			ccm = new ChatCommandHandler(this);
			this.getCommand("o").setExecutor(ccm); log.info(logPrefix + "Registered command: /o"); // DONE
			this.getCommand("name").setExecutor(ccm); log.info(logPrefix + "Registered command: /name");
			this.getCommand("resetname").setExecutor(ccm); log.info(logPrefix + "Registered command: /resetname");
			this.getCommand("ao").setExecutor(ccm); log.info(logPrefix + "Registered command: /ao"); // DONE
			this.getCommand("b").setExecutor(ccm); log.info(logPrefix + "Registered command: /b"); // DONE
			this.getCommand("w").setExecutor(ccm); log.info(logPrefix + "Registered command: /w"); // DONE
			this.getCommand("low").setExecutor(ccm); log.info(logPrefix + "Registered command: /low"); // DONE
			this.getCommand("t").setExecutor(ccm); log.info(logPrefix + "Registered command: /t"); // DONE
			this.getCommand("s").setExecutor(ccm); log.info(logPrefix + "Registered command: /s"); //DONE
			this.getCommand("silence").setExecutor(ccm); log.info(logPrefix + "Registered command: /silence"); // DONE
			this.getCommand("unsilence").setExecutor(ccm); log.info(logPrefix + "Registered command: /unsilence"); // DONE
			this.getCommand("me").setExecutor(ccm); log.info(logPrefix + "Registered command: /me"); // DONE
			this.getCommand("do").setExecutor(ccm); log.info(logPrefix + "Registered command: /do"); // DONE
			this.getCommand("a").setExecutor(ccm); log.info(logPrefix + "Registered command: /a"); // DONE
			this.getCommand("togb").setExecutor(ccm); log.info(logPrefix + "Registered command: /togb"); // DONE
			this.getCommand("togooc").setExecutor(ccm); log.info(logPrefix + "Registered command: /togooc"); // DONE
			this.getCommand("ooc").setExecutor(ccm); log.info(logPrefix + "Registered command: /ooc"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.chat");
		
		log.info(logPrefix + "Registering command module: Core Features");
		fcm = new FeaturesCommandHandler(this);
			this.getCommand("mrps").setExecutor(fcm); log.info(logPrefix + "Registered command: /mrps"); // DONE
		
		log.info(logPrefix + "Registering command module: Language Commands");
		lcm = new LanguageCommandHandler(this);
		if(config.getBoolean("features.languages")){		
			this.getCommand("lw").setExecutor(lcm); log.info(logPrefix + "Registered command: /lw"); // DONE
			this.getCommand("llow").setExecutor(lcm); log.info(logPrefix + "Registered command: /llow"); // DONE
			this.getCommand("lt").setExecutor(lcm); log.info(logPrefix + "Registered command: /lt"); // DONE
			this.getCommand("ls").setExecutor(lcm); log.info(logPrefix + "Registered command: /ls"); // DONE
			this.getCommand("setlang").setExecutor(lcm); log.info(logPrefix + "Registered command: /setlang"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.languages");
		
		log.info(logPrefix + "Registering command module: Dice Commands");
		if(config.getBoolean("features.dice")){
			this.getCommand("roll").setExecutor(fcm); log.info(logPrefix + "Registered command: /roll"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.dice");
		
		log.info(logPrefix + "Registering command module: Alcohol Commands");
		if(config.getBoolean("features.alcohol")){
			this.getCommand("brew").setExecutor(fcm); log.info(logPrefix + "Registered command: /brew"); // DONE
			this.getCommand("drink").setExecutor(fcm); log.info(logPrefix + "Registered command: /drink"); // DONE
			this.getCommand("sober").setExecutor(fcm); log.info(logPrefix + "Registered command: /sober"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.alcohol");
		
		log.info(logPrefix + "Registering command module: Drugs Commands");
		if(config.getBoolean("features.drugs")){
			this.getCommand("makedrugs").setExecutor(fcm); log.info(logPrefix + "Registered command: /makedrugs"); // DONE
			this.getCommand("usedrug").setExecutor(fcm); log.info(logPrefix + "Registered command: /brew"); // DONE
		}
		else log.info(logPrefix + "Skipped feature loading: features.drugs");
		
		log.info(logPrefix + "Registering command module: Description and Attributes Commands");
		if(config.getBoolean("features.descriptions")){
			this.getCommand("examine").setExecutor(fcm); log.info(logPrefix + "Registered command: /examine");
			this.getCommand("attributes").setExecutor(fcm); log.info(logPrefix + "Registered command: /attributes");
		}
		else log.info(logPrefix + "Skipped feature loading: features.descriptions");
		
		
		if(!restrictionsOK){
			log.severe("The plugin was not configured properly.");
			log.severe("All restricted items must have a permissions value to avoid errors during operation.");
			log.severe("The plugin will be disabled to prevent errors from occurring.");
			
			pm.disablePlugin(this);
		}
		
		if(!filesOK){
			log.severe("The plugin was not configured properly.");
			log.severe("One or more of the configuration files are missing.");
			log.severe("You should have the following files: config.yml, restricted-items.yml, chat.yml");
			log.severe("The plugin will be disabled to prevent errors from occurring.");
			
			pm.disablePlugin(this);
		}
		
	}
	private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }
}
