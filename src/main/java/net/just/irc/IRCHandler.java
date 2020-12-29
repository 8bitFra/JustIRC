package net.just.irc;

import java.io.*;
import java.net.*;


public class IRCHandler 
{
    private String server = "";

    private String nick = "";
    
    private String channelname = "";
    
    private String password = "";
    
    private boolean flag = true;
    
    private BufferedWriter writer;
    private BufferedReader reader;
    
    private Socket socket = null;

    public IRCHandler(String server, String nick, String channelname, String password) 
    {
		super();
		this.server = server;
		this.nick = nick;
		this.channelname = channelname;
		this.password = password;
	}

	public void startConn(){
		new Thread(() -> {
			try 
			{
				socket = new Socket(server, 6667);
				
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			} catch (UnknownHostException e2) 
			{
				e2.printStackTrace();
				
				ChatUtils.message("\u00A7cUnknown Host");
				socket = null;
				
			} catch (IOException e2) 
			{
				e2.printStackTrace();
				socket = null;
			}

	        new Thread(() -> {
	            try {
	                String line = null;
	                while ((line = reader.readLine()) != null) {
	                	
	                	if(flag)
	                	{
	                		//ChatUtils.message(line);
	                		//System.out.println(line);
	                		if(line.contains("incorrect channel key"))
	                		{
	                			ChatUtils.message("\u00A7cCannot join channel (incorrect channel key)");
	                			closeConnection();
	                		}
	                	}
	                	else
	                	{
	                		String[] f1 = line.split(":");
	                		String[] f2 = f1[1].split("!");
	                		/*StringTokenizer st = new StringTokenizer(line,":");
	                		StringTokenizer st1 = new StringTokenizer(st.nextToken(),"!");*/
	                		ChatUtils.message("\u00A7c" + f2[0] + " \u00A7f>> " + f1[2]);
	                		//ChatUtils.message(line);
	                	}
	                }
	            } catch (Exception e) {
	                System.err.println(e.getMessage());
	                login(writer);
	            }

	        }).start();

	        if(login(writer))
	        {
	        	
	        	ChatUtils.message("\u00A7aconnected " + server + " success");
		        ChatUtils.message("\u00A76waiting for the channel...");
	        	
		        try
		        {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
	        	if(join("#" + channelname + " " + password))
	        	{
	        		try {
	    				Thread.sleep(10000);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
	        		if(isOpen())
	        		{
	        			ChatUtils.message("\u00A7e[\u00A7aDONE\u00A7e]");
	        			flag = false;
	        		}
	        	}
	        	else
	        	{
	        		ChatUtils.message("\u00A7c Channel connection failed!");
	        		closeConnection();
	        	}
	        }
	        else
	        {
	        	ChatUtils.message("\u00A7c Login failed!");
	        	closeConnection();
	        }
	        
	        
		}).start();
        
        
        
       //sendGroupMsg(groupMsg);
        //System.exit(0);
    }

    public boolean join(String channelname) {
        try {
            writer.write("JOIN " + channelname + "\r\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("join error " + e.getMessage());
            login(writer);
            return false;
        }
    }

    public boolean login(BufferedWriter writer) {
        try {
            writer.write("NICK " + nick + "\r\n");
            writer.write("USER " + nick + " mc * : "+  nick + "\r\n");
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("login error " + e.getMessage());
            return false;
        }
    }

    public void sendGroupMsg(String groupMsg) {
    	
    	String channelname = "#" + this.channelname;
    	
        try 
        {
        	if ("quit".equals(groupMsg)) 
        	{
        		writer.write("PART " + channelname + " brb ~ ~\r\n");
                writer.flush();
                closeConnection();
            }
                
            writer.write("PRIVMSG " + channelname + " :" + groupMsg + "\r\n");
            writer.flush();
                
        } catch (Exception e) 
        {
            System.err.println("sendGroupMsg error " + e.getMessage());
            login(writer);
        }
    }
    
    public void closeConnection() {
    	try 
    	{
			socket.close();
		} catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
    
    public boolean isOpen()
    {
    	if(socket.isClosed() || socket == null)
    	{
    		return false;
    	}
    	else
    	{
    		return true;
    	}
    }

	public String getServer() 
	{
		return server;
	}


	public String getChannelname() 
	{
		return channelname;
	}

	public String getNick() 
	{
		return nick;
	}
    
}
