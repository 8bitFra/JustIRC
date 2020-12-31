package net.just.irc.mixin;
import net.just.irc.ChatUtils;
import net.just.irc.IRCHandler;
import net.just.irc.Main;
import net.minecraft.client.network.ClientPlayerEntity;

import java.util.StringTokenizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ChatManager 
{
	private String prefix = "#";
	private String global = "!";
	private String previusGlobal = null;
	
	private String help = "\n\n\u00A7cHELP \u00A7f\n\n"
			+ prefix + "status - check the irc connecion status\n\n"
			+ prefix + "connect - connect to the irc server \n\u00A7e(usage: "+prefix+"connect serverip;nick;channel;password)\u00A7f\n\n"
			+ prefix + "disconnect - disctonnect from the irc server\n\n"
			+ "\u00A79>>\u00A7c When an irc connection is active, if you want to write in the normal chat you must use the prefix '"+global+"' \u00A79<<\u00A7f\n\n";
	
	
	
    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo info) 
    {
    	if (message.equals(prefix + "help") || message.equals(prefix + "h"))
    	{
    		ChatUtils.message(help);
    		info.cancel();
    	}
    	else if(message.equals(prefix + "status"))
    	{
    		if(Main.irc!=null && Main.irc.isOpen())
    		{
    			ChatUtils.message("\u00A7aConnected to this server: \u00A7e" + Main.irc.getServer() + "\u00A7a Channel: \u00A7e" + Main.irc.getChannelname());
    		}
    		else
    		{
    			ChatUtils.message("\u00A7cDisconnected");
    		}
    		info.cancel();
    	}
    	else if(message.contains(prefix + "connect"))
    	{
    		if(message.equals(prefix + "connect"))
    		{
    			ChatUtils.message("\u00A7cSyntax Error\n"
    							  + "\u00A7e(usage: "+prefix+"connect serverip;nick;channel;password)\u00A7f");
    		}
    		else
    		{
    			StringTokenizer st = new StringTokenizer(message," ");
        		
        		st.nextToken();
        		String[] fields = st.nextToken().split(";");
        		
        		
        		String ip = "";
        		String nick = "";
        		String channel = "";
        		String password = "";
        		
        		if(fields.length<3)
        		{
        			ChatUtils.message("\u00A7cTRY AGAIN! SOME IMPORTANT FIELDS ARE EMPTY");
        		}
        		else
        		{
        			ip = fields[0];
        			nick = fields[1];
        			channel = fields[2];
        			if(fields.length>3)
        			{
        				password = fields[3];
        			}
        			
        			if(ip!="" && nick!="" && channel!="")
            		{
        				if(Main.irc != null)
        				{
        					if(Main.irc.isOpen())
        					{
        						Main.irc.closeConnection();
        					}	
        				}
        				
        				Main.irc = new IRCHandler(ip,nick,channel,password);

        				Main.irc.startConn();
            		}
            		else
            		{
            			ChatUtils.message("\u00A7cTRY AGAIN! SOME IMPORTANT FIELDS ARE EMPTY");
            		}
        			
        		}
        		
    		}

    		info.cancel();
    	}
    	else if(message.equals(prefix + "disconnect"))
    	{
    		if(Main.irc!=null && Main.irc.isOpen())
    		{
    			Main.irc.closeConnection();
    			ChatUtils.message("\u00A7cDisconnected");
    		}
    		
    		info.cancel();
    	}
    	else
    	{
    		if (Main.irc!=null && Main.irc.isOpen() && String.valueOf(message.charAt(0)).equals(global))
    		{
    			previusGlobal = message.substring(1, message.length());
    			ChatUtils.sudomessage(message.substring(1, message.length()));
    			info.cancel();
    		}
    		else if(Main.irc!=null && Main.irc.isOpen() && !String.valueOf(message.charAt(0)).equals("/") && !message.equals(previusGlobal))
    		{
    			Main.irc.sendGroupMsg(message);
    			ChatUtils.message("\u00A7b" + Main.irc.getNick() + " \u00A7f>> " + message);
    			info.cancel();
    		}
    		
    		if(message.equals(previusGlobal))
    		{
    			previusGlobal=null;
    		}
    	}
    }

}