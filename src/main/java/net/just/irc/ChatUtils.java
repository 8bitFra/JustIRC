package net.just.irc;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
public enum ChatUtils
{
	;
	
	private static final MinecraftClient MC = MinecraftClient.getInstance();
	

	public static void component(Text component)
	{
		ChatHud chatHud = MC.inGameHud.getChatHud();
		chatHud.addMessage(component);
	}
	
	public static void message(String message)
	{
		component(new LiteralText(message));
	}
	
	public static void sudomessage(String message)
	{
		MC.player.sendChatMessage(message);
	}

}
