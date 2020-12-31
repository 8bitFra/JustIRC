package net.just.irc;

import net.fabricmc.api.ModInitializer;

public class Main implements ModInitializer {
	
	public static IRCHandler irc = null;
	
	@Override
	public void onInitialize() 
	{
		System.out.println("START");
	}
	
}
