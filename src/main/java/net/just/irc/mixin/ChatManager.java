package net.just.irc.mixin;
import net.just.irc.ChatUtils;
import net.just.irc.IRCHandler;
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
	
	private String help = "\n\n§cHELP §f\n\n"
			+ prefix + "status - check the irc connecion status\n\n"
			+ prefix + "connect - connect to the irc server \n§e(usage: "+prefix+"connect serverip;nick;channel;password)§f\n\n"
			+ prefix + "disconnect - disctonnect from the irc server\n\n"
			+ "§9>>§c When an irc connection is active, if you want to write in the normal chat you must use the prefix '"+global+"' §9<<§f\n\n";
	
	
	private IRCHandler irc = null;
	
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
    		if(irc!=null && irc.isOpen())
    		{
    			ChatUtils.message("§aConnected to this server: §e" + irc.getServer() + "§a Channel: §e" + irc.getChannelname());
    		}
    		else
    		{
    			ChatUtils.message("§cDisconnected");
    		}
    		info.cancel();
    	}
    	else if(message.contains(prefix + "connect"))
    	{
    		if(message.equals(prefix + "connect"))
    		{
    			ChatUtils.message("§cSyntax Error\n"
    							  + "§e(usage: "+prefix+"connect serverip;nick;channel;password)§f");
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
        			ChatUtils.message("§cTRY AGAIN! SOME IMPORTANT FIELDS ARE EMPTY");
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
        				if(irc != null)
        				{
        					if(irc.isOpen())
        					{
        						irc.closeConnection();
        					}	
        				}
        				
            			irc = new IRCHandler(ip,nick,channel,password);

            			irc.startConn();
            		}
            		else
            		{
            			ChatUtils.message("§cTRY AGAIN! SOME IMPORTANT FIELDS ARE EMPTY");
            		}
        			
        		}
        		
    		}

    		info.cancel();
    	}
    	else if(message.equals(prefix + "disconnect"))
    	{
    		if(irc!=null && irc.isOpen())
    		{
    			irc.closeConnection();
    			ChatUtils.message("§cDisconnected");
    		}
    		
    		info.cancel();
    	}
    	else
    	{
    		if (irc!=null && irc.isOpen() && String.valueOf(message.charAt(0)).equals(global))
    		{
    			previusGlobal = message.substring(1, message.length());
    			ChatUtils.sudomessage(message.substring(1, message.length()));
    			info.cancel();
    		}
    		else if(irc!=null && irc.isOpen() && !String.valueOf(message.charAt(0)).equals("/") && !message.equals(previusGlobal))
    		{
    			irc.sendGroupMsg(message);
    			ChatUtils.message("§b" + irc.getNick() + " §f>> " + message);
    			info.cancel();
    		}
    		
    		if(message.equals(previusGlobal))
    		{
    			previusGlobal=null;
    		}
    	}
    }

}