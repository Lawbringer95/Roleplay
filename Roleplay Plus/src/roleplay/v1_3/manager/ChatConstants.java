package roleplay.v1_3.manager;

import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import roleplay.v1_3.engine.RPSystem;

public class ChatConstants {

	public static String err(String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.errorMessage");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String whisper(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.whisper");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String action_me(Player p, String action){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.me");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(action));

		
		return regex;
	}
	public static String action_do(Player p, String action){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.do");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(action));

		
		return regex;
		}
	public static String roll(Player p, int max, int result){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.roll");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%r", Integer.toString(result));
		regex = regex.replaceAll("%x", Integer.toString(max));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		
		
		return regex;	}
	public static String close(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.speak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String normal(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.speak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String far(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.speak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String closeLow(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.low");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String normalLow(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.low");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String farLow(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.low");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String closeShout(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.shout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String normalShout(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.shout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String farShout(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.shout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String globalOOC(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.globalOOC");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%p", p.getName());
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String sys_globalOOC(String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.globalOOC");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", "SYSTEM");
		regex = regex.replaceAll("%p", "Console");
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String localOOC(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.localOOC");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%p", p.getName());
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String adminOOC(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.adminOOC");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%p", p.getName());
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String adminChat(Player p, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.adminChat");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%p", p.getName());
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String sys_adminChat(String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.adminChat");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", "SYSTEM");
		regex = regex.replaceAll("%p", "Console");
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String sys_adminOOC(String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.adminOOC");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%s", "SYSTEM");
		regex = regex.replaceAll("%p", "Console");
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String mute(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.silence");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String unmute(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.unsilence");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String kick(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.kick");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String ban(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.ban");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%banner", muter);
		regex = regex.replaceAll("%banned", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String tempban(String muter, String mutedPlayerName, double days){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.ban");
		int realDays = 0;
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%banner", muter);
		if(days >= 1) {
			regex.replaceAll("%y", "days");
			realDays = (int) days;
		}
		else if(days >= 0.014666666666666667 && days < 1){
			regex = regex.replaceAll("%y", "hours");
			realDays = (int) (24 * days);
		}
		else if(days >= 0.00024444444444444445 && days < 0.014666666666666667){
			regex = regex.replaceAll("%y", "minutes");
			realDays = (int) (1440 * days);
		}
		else if(days < 0.00024444444444444445) {
			regex = regex.replaceAll("%y", "seconds");
			realDays = (int) (186400 * days);
		}
		regex = regex.replaceAll("%banned", mutedPlayerName);
		regex = regex.replaceAll("%n", Integer.toString(realDays));
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String unban(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.unban");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String jail(String muter, String mutedPlayerName, int minutes){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.jail");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		regex = regex.replaceAll("%n", Integer.toString(minutes));
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String unjail(String muter, String mutedPlayerName){
		return "" + ChatColor.RED + "(( " + Timestamp.NOW() + "AdmCmd: " + mutedPlayerName + " has been released from admin jail by " + muter + ". ))";
	}
	public static String nameReset(String muter, String mutedPlayerName){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.resetname");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%admin", muter);
		regex = regex.replaceAll("%l", mutedPlayerName);
		
		regex = build(regex.split(" "));

		
		return regex;	}
	public static String build(String[] args){
		String s = "";
		
		for(String str : args){
			s = s + " " + str;
		}
		
		s = s.trim();
		
		if(RPSystem.chatConfig.getBoolean("enable-colors")){	
			s = s.replaceAll("&1","" + ChatColor.DARK_BLUE + "");
			s = s.replaceAll("&2","" + ChatColor.GREEN + "");
			s = s.replaceAll("&3","" + ChatColor.DARK_AQUA + "");
			s = s.replaceAll("&4","" + ChatColor.DARK_RED + "");
			s = s.replaceAll("&5","" + ChatColor.DARK_PURPLE + "");
			s = s.replaceAll("&6","" + ChatColor.GOLD + "");
			s = s.replaceAll("&7","" + ChatColor.GRAY + "");
			s = s.replaceAll("&8","" + ChatColor.DARK_GRAY + "");
			s = s.replaceAll("&9","" + ChatColor.BLUE + "");
			s = s.replaceAll("&0","" + ChatColor.BLACK + "");
			s = s.replaceAll("&a","" + ChatColor.GREEN + "");
			s = s.replaceAll("&b","" + ChatColor.AQUA +  "");
			s = s.replaceAll("&c","" + ChatColor.RED + "");
			s = s.replaceAll("&d","" + ChatColor.LIGHT_PURPLE + "");
			s = s.replaceAll("&e","" + ChatColor.YELLOW + "");
			s = s.replaceAll("&f","" + ChatColor.WHITE + "");
		}
		else {
			s = s.replaceAll("&1","");
			s = s.replaceAll("&2","");
			s = s.replaceAll("&3","");
			s = s.replaceAll("&4","");
			s = s.replaceAll("&5","");
			s = s.replaceAll("&6","");
			s = s.replaceAll("&7","");
			s = s.replaceAll("&8","");
			s = s.replaceAll("&9","");
			s = s.replaceAll("&0","");
			s = s.replaceAll("&a","");
			s = s.replaceAll("&b","");
			s = s.replaceAll("&c","");
			s = s.replaceAll("&d","");
			s = s.replaceAll("&e","");
			s = s.replaceAll("&f","");
		}
		
		return s + ChatColor.WHITE;
	}
	
	// This scrambles the text all up
	public static String censorBuild(String[] args){
		String str = build(args);

        Random randomGenerator = new Random();

        int lengthOfStr = str.length();

        char[] chars = str.toCharArray();



        // do for length of str
        for (int i=0; i < lengthOfStr; i++)
        {

        int n = randomGenerator.nextInt(lengthOfStr);

        chars[i] = chars[n];

        String newStr = new String(chars);
        str = newStr;
        }

        return str;
	}
	
	public static String closeLang(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langspeak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;
	}
	public static String normalLang(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langspeak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String farLang(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langspeak");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String closeLangLow(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langlow");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String normalLangLow(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langlow");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String farLangLow(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langlow");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String closeLangShout(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langshout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.close"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String normalLangShout(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langshout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.normal"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String farLangShout(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langshout");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		regex = regex.replaceAll("%d", RPSystem.chatConfig.getString("range.far"));
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));

		
		return regex;	}
	public static String whisperLang(Player p, String language, String message){
		// Initial message
		String regex = RPSystem.chatConfig.getString("format.langwhisper");
		
		// Token replacement
		regex = regex.replaceAll("%t", Timestamp.NOW());
		
		regex = regex.replaceAll("%s", SQLManager.getDisplayName(p.getName()));
		regex = regex.replaceAll("%g", language);
		
		regex = build(regex.split(" "));
		regex = regex.replaceAll("%m", ChatColor.stripColor(message));
		
		return regex;	}
}
