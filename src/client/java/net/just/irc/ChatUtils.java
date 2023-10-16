package net.just.irc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
public enum ChatUtils
{
	;
	
	private static final MinecraftClient MCInstance = MinecraftClient.getInstance();

	

	public static void component(Text component)
	{
		ChatHud chatHud = MCInstance.inGameHud.getChatHud();
		chatHud.addMessage(component);
	}
	
	public static void message(String message)
	{
		component (Text.literal(message));
	}
	
	public static void sudomessage(String message)
	{
		//MC.player.sendChatMessage(message, null);
		MCInstance.getNetworkHandler().sendChatMessage(message);
		
	}
	
	public static String getUsername()
	{
		return MCInstance.player.getName().getString();
	}

}
