package net.just.irc;

import net.fabricmc.api.ModInitializer;
import me.lortseam.completeconfig.data.Config;

public class Main implements ModInitializer {
	
	public static IRCHandler irc = null;
	
	public static boolean autoconnect = true;
	
	public static final String MOD_ID = "justirc";

    @Override
    public void onInitialize() {
    	
    	System.out.println("Just IRC has started.");
    	
        Config.builder(MOD_ID)
                .add(new ircgroup())
                .build();
        
        ;
       
    }

	
}
