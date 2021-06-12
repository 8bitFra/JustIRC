package net.just.irc;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	
	public static IRCHandler irc = null;
	
	public static boolean autoconnect = true;
	
	public static final String MOD_ID = "justirc";
	
	
	private static Ircgroup settings;
	
	public static Ircgroup getSettings() 
	{
        return settings;
    }
	
    @Override
    public void onInitialize() {
    	
    	System.out.println("Just IRC has started.");
    	
    	settings = new Ircgroup();
        settings.load();
        
    }

	
}
