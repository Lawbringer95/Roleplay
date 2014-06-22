package roleplay.v1_3.manager;

import java.text.SimpleDateFormat;
import java.util.Date;

import roleplay.v1_3.engine.RPSystem;

public class Timestamp {

	private static SimpleDateFormat sdf = new SimpleDateFormat(RPSystem.chatConfig.getString("timestamp-format"));
	
	public static String NOW = getTimestamp(System.currentTimeMillis());
	
	public static String getTimestamp(long time){
		if(RPSystem.chatConfig.getBoolean("timestamp-enabled")) return "[" + sdf.format(new Date(time)) + "]";
		else return "";
	}
	public static String NOW(){
		if(RPSystem.chatConfig.getBoolean("timestamp-enabled")) return getTimestamp(System.currentTimeMillis());
		else return "";
	}
}
