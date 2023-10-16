package net.just.irc;

import java.io.*;
import java.net.*;
import java.util.concurrent.atomic.AtomicBoolean;


public class IRCHandler 
{
    private String server = "";

    private String nick = "";
    
    private String channelname = "";
    
    private String password = "";
    
    private Integer port = 6667;
    
    
    private BufferedWriter writer;
    private BufferedReader reader;
    
    private Socket socket = null;
    
    private boolean flag = true;
    
    private AtomicBoolean processed = new AtomicBoolean(true);

    public IRCHandler(String server, String nick, String channelname, String password, Integer port) 
    {
		super();
		this.server = server;
		this.nick = nick;
		this.channelname = channelname;
		this.password = password;
		this.port = port;
	}
    
    
	public void startConn(){
		new Thread(() -> {
			try 
			{
				socket = new Socket(server, port);
				
				writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				synchronized(processed) 
			    {
			    	login(writer);
			        ChatUtils.message("\u00A7aconnected " + server + " success");
				    ChatUtils.message("\u00A76waiting for the channel...");
				    
				    Listener();
				    
				    try {
						processed.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        join("#" + channelname + " " + password);
			        
			        try {
	    				Thread.sleep(1000);
	    			} catch (InterruptedException e) {
	    				// TODO Auto-generated catch block
	    				e.printStackTrace();
	    			}
			        
			        if(isOpen())
			        {
			        	ChatUtils.message("\u00A7e[\u00A7aDONE\u00A7e]");
			        }
			    }
				
				
				
			} catch (Exception e) 
			{
				e.printStackTrace();
				
				ChatUtils.message("\u00A7cUnknown Host");
				//socket = null;
				
			}
	         
		}).start();    
       //System.exit(0);
    }
	
	
	public void Listener()
	{
		new Thread(() -> {
            try {
                String line = null;
                while ((line = reader.readLine()) != null) {
                	
                	if(line.startsWith("PING"))
            		{
            			writer.write(line.replace("PING", "PONG"));
            			sendGroupMsg("PONG");
            			//ChatUtils.message("Rispondo al PING: PONG");
            		}
                	
                	//ChatUtils.message(line);
                	
                	if(flag)
                	{
                		if(line.contains("Cannot join channel"))
                		{
                			ChatUtils.message("\u00A7cCannot join channel (probably incorrect channel key)");
                			closeConnection();
                		}
                		
                		if(line.toLowerCase().contains("message of the day"))
                		{
                			synchronized(processed) 
                			{
                	            processed.notify();
                	        }
                		}
                		
                		if(line.toLowerCase().contains("/names"))
                		{
                			flag = false;
                		}
                	}
                	else
                	{
                		try
                		{
                			String[] f1 = line.split(":");
                        	String[] f2 = f1[1].split("!");
                        	String[] message = line.split("#"+channelname+" :");
                        	
                        	ChatUtils.message("\u00A7c" + f2[0] + " \u00A7f>> " + message[1]);
                		}
                		catch(Exception e) {}
                	}

                }
            } catch (Exception e) {
                System.err.println(e.getMessage());
                if (isOpen()) login(writer);
            }

        }).start();
	}

    public void join(String channelname) {
        try {
            writer.write("JOIN " + channelname + "\r\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("join error " + e.getMessage());
            ChatUtils.message("\u00A7cChannel connection failed!");
            login(writer);
        }
    }

    public void login(BufferedWriter writer) {
        try {
            writer.write("NICK " + nick + "\r\n");
            writer.write("USER " + nick + " mc * : "+  nick + "\r\n");
            writer.flush();
        } catch (IOException e) {
            System.err.println("login error " + e.getMessage());
            ChatUtils.message("\u00A7cLogin failed!");
        	closeConnection();
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
    	if(socket == null || socket.isClosed())
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
	
	public Socket getSocket()
	{
		return socket;
	}
}
