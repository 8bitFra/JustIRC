package net.just.irc.mixin.client;
import net.just.irc.ChatUtils;
import net.just.irc.IRCHandler;
import net.just.irc.Main;
import net.just.irc.MainClient;
import net.just.irc.Ircgroup;
import net.minecraft.client.network.ClientPlayNetworkHandler;

import java.util.StringTokenizer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;





@Mixin(ClientPlayNetworkHandler.class)
public class ChatManager 
{
	private String prefix = "@";
	private String global = "!";
	private String previusGlobal = null;
	
	private boolean nickrequired = false;
	private boolean takename = true;
	
	private String ip = "";
	private String nick = "";
	private String channel = "";
	private String password = "";
	private Integer port = 6667;
	
	
	private String help = "\n\n\u00A7cHELP \u00A7f\n\n"
			+ prefix + "status - check the irc connecion status\n\n"
			+ prefix + "connect - connect to the irc server \n\u00A7eusage: "+prefix+"connect serverip;port;channel;password \n(password is optional)\u00A7f\n\n"
			+ prefix + "disconnect - disconnect from the irc server\n\n"
			+ "\u00A79>>\u00A7c When an irc connection is active, if you want to write in the normal chat you must use the prefix '"+global+"' \u00A79<<\u00A7f\n\n";
	
	
    @Inject(at = @At("HEAD"), method = "sendChatMessage", cancellable = true)
    private void onSendChatMessage(String message, CallbackInfo info) 
    {
    	
    	if(nickrequired)
    	{
    		nick = message;
    		nickrequired = false;
    		takename = false;
    		
    		ChatUtils.sudomessage(prefix+"connect "+ip+";"+port+";"+channel+";"+password);
    		
    		info.cancel();
    	}
    	else
    	{
    		if (message.equals(prefix + "help") || message.equals(prefix + "h"))
        	{
        		ChatUtils.message(help);
        		info.cancel();
        	}
        	else if(message.equals(prefix + "status"))
        	{
        		if(MainClient.irc!=null && MainClient.irc.isOpen())
        		{
        			ChatUtils.message("\u00A7aConnected to this server: \u00A7e" + MainClient.irc.getServer() + "\u00A7a Channel: \u00A7e" + MainClient.irc.getChannelname());
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
        			if(Ircgroup.getIp()!="" && Ircgroup.getChannel()!="" && Ircgroup.getBackupnick()!="" && Ircgroup.getChannel()!="")
        			{
        				if(MainClient.irc != null && MainClient.irc.getSocket()!=null)
        				{
        					if(MainClient.irc.isOpen())
        					{
        						MainClient.irc.closeConnection();
        					}	
        				}
        				
        				String nick = ChatUtils.getUsername();
        				
        				if(Character.isDigit(nick.charAt(0)))
        					nick = Ircgroup.getBackupnick();
        				
        				MainClient.irc = new IRCHandler(Ircgroup.getIp(),nick,Ircgroup.getChannel(),Ircgroup.getPassword(),Ircgroup.getPort());

        				MainClient.irc.startConn();
        			}
        			else
        			{
        				ChatUtils.message("\u00A7cError\n"
        					  + "\u00A7eMissing config fields or Syntax Error\n"
  							  + "\u00A7eusage: "+prefix+"connect serverip;port;channel;password \n(password is optional)\u00A7f");
        				System.out.println("Unable to Connect: Missing config fields or Syntax Error");
        			}
        		}
        		else
        		{
        			StringTokenizer st = new StringTokenizer(message," ");
            		
            		st.nextToken();
            		String[] fields = st.nextToken().split(";");

            		if(fields.length<2)
            		{
            			ChatUtils.message("\u00A7cTry again. Some important fields are empty");
            		}
            		else
            		{  			
            			ip = fields[0];
            			if(takename)
            			{
            				nick = ChatUtils.getUsername();
            			}
            			
            			try {
            				port = Integer.parseInt(fields[1]);
            			}
            			catch(Exception e) {
            				ChatUtils.message("\u00A7cError: port must be a number, fallback to default");
            				port = 6667;
            			}
            			channel = fields[2];
            			if(fields.length>3)
            			{
            				password = fields[3];
            			}
            			
            			if(port>=65536)
            			{
            				ChatUtils.message("\u00A7cError: port must be valid, fallback to default");
            				port = 6667;
            			}

            			if(!Character.isDigit(nick.charAt(0)))
            			{
            				takename = true;
            				if(ip!="" && nick!="" && channel!="")
                    		{
                				if(MainClient.irc != null && MainClient.irc.getSocket()!=null)
                				{
                					if(MainClient.irc.isOpen())
                					{
                						MainClient.irc.closeConnection();
                					}	
                				}
                				
                				MainClient.irc = new IRCHandler(ip,nick,channel,password,port);

                				MainClient.irc.startConn();
                    		}
            				else
                    		{
                    			ChatUtils.message("\u00A7cTry again. Some important fields are empty");
                    		}
            			}
            			else
            			{
            				nickrequired=true;
            				ChatUtils.message("\u00A76Error: Your name starts with a number. Write a new nickname:");
            			}
          			
            		}
            		
        		}

        		info.cancel();
        	}
        	else if(message.equals(prefix + "disconnect"))
        	{
        		if(MainClient.irc!=null && MainClient.irc.isOpen())
        		{
        			MainClient.irc.closeConnection();
        			ChatUtils.message("\u00A7cDisconnected");
        		}
        		
        		info.cancel();
        	}
        	else
        	{
        		if (MainClient.irc!=null && MainClient.irc.isOpen() && String.valueOf(message.charAt(0)).equals(global))
        		{
        			previusGlobal = message.substring(1, message.length());
        			ChatUtils.sudomessage(message.substring(1, message.length()));
        			info.cancel();
        		}
        		else if(MainClient.irc!=null && MainClient.irc.isOpen() && !String.valueOf(message.charAt(0)).equals("/") && !message.equals(previusGlobal))
        		{
        			MainClient.irc.sendGroupMsg(message);
        			ChatUtils.message("\u00A7b" + MainClient.irc.getNick() + " \u00A7f>> " + message);
        			info.cancel();
        		}
        		
        		if(message.equals(previusGlobal))
        		{
        			previusGlobal=null;
        		}
        	}
    	}
    	
    }

}