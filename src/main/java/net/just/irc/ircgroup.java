package net.just.irc;

import me.lortseam.completeconfig.api.ConfigEntries;
import me.lortseam.completeconfig.api.ConfigEntry;
import me.lortseam.completeconfig.api.ConfigGroup;

@ConfigEntries
public class ircgroup implements ConfigGroup
{
	private static String ip = "";
    private static String backupnick = "";
    private static String channel = "";
    private static String password = "";
    @ConfigEntry(requiresRestart = true)
    private static boolean autoconnect;
    
    
	public static String getIp() {
		return ip;
	}
	public static String getBackupnick() {
		return backupnick;
	}
	public static String getChannel() {
		return channel;
	}
	public static String getPassword() {
		return password;
	}
	public static boolean isAutoconnect() {
		return autoconnect;
	}    
}
