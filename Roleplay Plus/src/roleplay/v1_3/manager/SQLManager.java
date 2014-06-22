package roleplay.v1_3.manager;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Location;

import roleplay.v1_3.engine.RPSystem;

public class SQLManager {

	private static Connection SQL_CONNECTION;
	private static ResultSet SQL_RESULTSET;
	private static Statement SQL_STATEMENT;
	private static RPSystem plugin;
		
	public static void connect(RPSystem plugin){
		SQLManager.plugin = plugin;
		// connect to database and prepare to take queries
		 SQL_CONNECTION = null;
	     SQL_RESULTSET = null;
	     SQL_STATEMENT = null;
	 
	     plugin.log.info(plugin.logPrefix + "Trying to connect database.");
	     try {
	    	 Class.forName("org.sqlite.JDBC");
	            
	         // jdbc:sqlite:%dir%\\mrps.db
	         plugin.log.info(plugin.logPrefix + "Connecting...");
	         SQL_CONNECTION = DriverManager.getConnection("jdbc:sqlite:" + plugin.getDataFolder() + File.separator + "mrps.db");
	     } catch (Exception e) {
	            e.printStackTrace();
	            plugin.log.severe(plugin.logPrefix + "Failed to connect.");
	        } finally {
	            try {
	            	plugin.log.info(plugin.logPrefix + "Connected.");
	            } catch (Exception e) {
	            	plugin.log.severe(plugin.logPrefix + "Unable to join connection thread.");
	                e.printStackTrace();
	            }
	        }
	}
	public static void disconnect(){
		try {
			SQL_RESULTSET.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SQL_CONNECTION.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getDisplayName(String username){
		
        try {
        	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM names WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getString("displayname");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return username;
		}
		return username;
	}
	public static void setDisplayName(String username, String displayname){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE names SET displayname='" + displayname + "' WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getOverallLevel(String username){
		
	    try {
	    	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM lvl_overall WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getInt("level");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return 0;
		}
		return 0;
	}
	public static void setOverallLevel(String username, int level){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE lvl_overall SET level=" + level + " WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getSkillLevel(String username,String skillTag){
		
	    try {
	    	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM lvl_skill WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getInt(skillTag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return 0;
		}
		return 0;
	}
	public static void setSkillLevel(String username, String skill, int level){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE lvl_skill SET " + skill + "=" + level + " WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getSkillExperience(String username,String skillTag){
		
	    try {
	    	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM lvl_xp WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getInt(skillTag);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return 0;
		}
		return 0;
	}
	public static void setSkillExperience(String username, String skill, int level){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE lvl_xp SET " + skill + "=" + level + " WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void recordPVPDeath(String killer, String victim, Location loc){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("INSERT INTO deathlog VALUES('" + killer + "','" + victim + "'," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ")");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        plugin.log.info(plugin.logPrefix + "Deathlog: " + killer + " has killed " + victim + " at (" + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ() + ").");
	}
	public static String getDescription(String username){
		
        try {
        	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM describe WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getString("desc");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return username;
		}
		return username;
	}
	public static void setDescription(String username, String desc30chars){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE describe SET desc='" + desc30chars + "' WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getDonatorLevel(String username){
		
	    try {
	    	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM userlevel WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getInt("donator");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return 0;
		}
		return 0;
	}
	public static void setDonatorLevel(String username, int level){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE userlevel SET donator=" + level + " WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static int getAdminLevel(String username){
		
	    try {
	    	SQL_STATEMENT = SQL_CONNECTION.createStatement();
			SQL_RESULTSET = SQL_STATEMENT
			        .executeQuery("SELECT * FROM userlevel WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			while (SQL_RESULTSET.next()) {
				return SQL_RESULTSET.getInt("admin");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			    return 0;
		}
		return 0;
	}
	public static void setAdminLevel(String username, int level){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("UPDATE userlevel SET admin=" + level + " WHERE username='" + username + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void createPlayer(String username){
		// NOTE: This sets the default values, creating the player in the database
		plugin.log.info(plugin.logPrefix + "Creating new player: " + username + "...");
		// DISPLAY NAME
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO names VALUES ('" + username + "','" + username + "')");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": names Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": names Entry FAILED");
				}
				// OVERALL LEVEL
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO lvl_overall VALUES ('" + username + "',1)");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": lvl_overall Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": lvl_overall Entry FAILED");
				}
				// SKILL LEVELS
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO lvl_skill VALUES ('" + username + "',1,1,1,1,1,1,1,1,1,20)");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": lvl_skill Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": lvl_skill Entry FAILED");
				}
				// SKILL XP
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO lvl_xp VALUES ('" + username + "',0,0,0,0,0,0,0,0,0,0,0)");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": lvl_xp Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": lvl_xp Entry FAILED");
				}
				// USERLEVEL
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO userlevel VALUES ('" + username + "',0,0)");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": userlevel Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": userlevel Entry FAILED");
				}
				// DESCRIPTION
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO describe VALUES ('" + username + "','None')");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": describe Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": describe Entry FAILED");
				}
				// STATUS
				try {
					SQL_STATEMENT = SQL_CONNECTION.createStatement();	
					SQL_STATEMENT.execute("INSERT INTO status VALUES ('" + username + "','Normal')");
					SQL_STATEMENT.close();
					plugin.log.info(plugin.logPrefix + "Creating new player: " + username + ": status Entry OK");
				} catch(SQLException e){
					e.printStackTrace();
					plugin.log.severe(plugin.logPrefix + "Creating new player: " + username + ": status Entry FAILED");
				}
	}
	
	public static void setStatusNormal(String username){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();	
			SQL_STATEMENT.execute("INSERT INTO status VALUES ('" + username + "','Normal')");
			SQL_STATEMENT.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	public static void setStatusBanned(String username){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();	
			SQL_STATEMENT.execute("INSERT INTO status VALUES ('" + username + "','Banned')");
			SQL_STATEMENT.close();
		} catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public static void recordBan(String username,String banner,long time,long expireTime,String reason){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("INSERT INTO bans VALUES('" + username + "','" + banner + "'," + time + "," + expireTime + "," + "'" + reason + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void recordAjail(String username,String banner,long time,long expireTime,String reason){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("INSERT INTO jails VALUES('" + username + "','" + banner + "'," + time + "," + expireTime + "," + "'" + reason + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void recordKick(String username,String kicker,long time,String reason){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.execute("INSERT INTO kicks VALUES('" + username + "','" + kicker + "'," + time + "," + "'" + reason + "')");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			SQL_STATEMENT.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean playerExists(String playerName){
		try {
			SQL_STATEMENT = SQL_CONNECTION.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT COUNT(*) AS rows FROM names WHERE username='" + playerName + "'");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if(SQL_RESULTSET.getInt("rows") > 0) {
				SQL_RESULTSET.close();
				SQL_STATEMENT.close();
				return true;
			}
			else {
				SQL_RESULTSET.close();
				SQL_STATEMENT.close();
				return false;
			}
		} catch (SQLException e) {
			// If this occurs, the database might lock up?
			e.printStackTrace();
			return false;
		}
	}
	public static boolean isBanned(String playerName){
    	boolean banned = false;
    	
    	try {
    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM bans WHERE username='" + playerName + "'");
    		
    		// This loop checks all bans on record for the user to see if any tempbans are still active
    		// Note tempbans are done in DAYS, the argument is a Double
    		// For example 0.5 would be 0.5(1 day)=12hours
    		// .1 -> 2.4 hours
    		// 7 = 7 days, etc.
    		while (SQL_RESULTSET.next()) {       	
            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
            		banned = true; // The user is banned
            	}
            }
    		
    		SQL_RESULTSET.close();
    		SQL_STATEMENT.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return banned;
    }
	public static String getBanner(String playerName){
		String banner = "Not Banned";
		if(isBanned(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM bans WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		banner = SQL_RESULTSET.getString("banner");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return banner;
	}
	public static long getBanTime(String playerName){
		long bantime = 0;
		if(isBanned(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM bans WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		bantime = SQL_RESULTSET.getLong("time");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return bantime;
	}
	public static long getBanExpire(String playerName){
		long expire = 0;
		if(isBanned(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM bans WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		expire = SQL_RESULTSET.getLong("expire");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return expire;
	}
	public static String getBanReason(String playerName){
		String reason = "Not Banned";
		if(isBanned(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM bans WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		reason = SQL_RESULTSET.getString("reason");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return reason;
	}
	
	// ADMIN JAIL
	public static boolean isJailed(String playerName){
    	boolean banned = false;
    	
    	try {
    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM jails WHERE username='" + playerName + "'");
    		
    		// This loop checks all admin jails on record to see if any are active
    		// Admin jail is checked on login - a player has to relog when their time is up
    		while (SQL_RESULTSET.next()) {       	
            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
            		banned = true; // The user is banned
            	}
            }
    		
    		SQL_RESULTSET.close();
    		SQL_STATEMENT.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return banned;
    }
	public static String getJailer(String playerName){
		String banner = "Not Jailed";
		if(isJailed(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM jails WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		banner = SQL_RESULTSET.getString("jailer");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return banner;
	}
	public static long getJailTime(String playerName){
		long bantime = 0;
		if(isJailed(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM jails WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		bantime = SQL_RESULTSET.getLong("time");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return bantime;
	}
	public static long getJailExpire(String playerName){
		long expire = 0;
		if(isJailed(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM jails WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		expire = SQL_RESULTSET.getLong("expire");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return expire;
	}
	public static String getJailReason(String playerName){
		String reason = "Not Jailed";
		if(isJailed(playerName)){
			try {
	    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
	    		SQL_RESULTSET = SQL_STATEMENT.executeQuery("SELECT * FROM jails WHERE username='" + playerName + "'");
	    		
	    		// This loop checks all bans on record for the user to see if any tempbans are still active
	    		// Note tempbans are done in DAYS, the argument is a Double
	    		// For example 0.5 would be 0.5(1 day)=12hours
	    		// .1 -> 2.4 hours
	    		// 7 = 7 days, etc.
	    		while (SQL_RESULTSET.next()) {       	
	            	if(SQL_RESULTSET.getLong("expire") > System.currentTimeMillis()){
	            		reason = SQL_RESULTSET.getString("reason");
	            	}
	            }
	    		
	    		SQL_RESULTSET.close();
	    		SQL_STATEMENT.close();
	    	} catch(SQLException e){
	    		e.printStackTrace();
	    	}
		}
		
		return reason;
	}
	public static void unban(String playerName){
    	// This method moves the expire dates up to the current time
    	try {
    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
    		SQL_STATEMENT.execute("UPDATE bans SET expire=1 WHERE username='" + playerName + "'");
    		SQL_STATEMENT.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
    }
	public static void unjail(String playerName){
    	// This method moves the expire dates up to the current time
    	try {
    		SQL_STATEMENT = SQL_CONNECTION.createStatement();
    		SQL_STATEMENT.execute("UPDATE jails SET expire=1 WHERE username='" + playerName + "'");
    		SQL_STATEMENT.close();
    	} catch(SQLException e){
    		e.printStackTrace();
    	}
    }
	
}
