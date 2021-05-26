package net.just.irc;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;

import org.lwjgl.glfw.GLFW;

import me.lortseam.completeconfig.data.Config;
import me.lortseam.completeconfig.data.Config.Builder;
import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;

public class Main implements ModInitializer {
	
	public static IRCHandler irc = null;
	
	public static boolean autoconnect = true;
	
	public static final String MOD_ID = "justirc";
	
	
	private static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
    	    "key.justirc.config", // The translation key of the keybinding's name
    	    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
    	    GLFW.GLFW_KEY_J, // The keycode of the key
    	    "category.justirc.main" // The translation key of the keybinding's category.
    	));
	
	
	private Config configinfos;
	
    @Override
    public void onInitialize() {
    	
    	System.out.println("Just IRC has started.");
    	
        Builder builder = Config.builder(MOD_ID);
        builder.add(new ircgroup());
        configinfos = builder.build();
              
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) 
            {
        		ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder();
        		MinecraftClient instance = MinecraftClient.getInstance();
    			Screen configScreen = screenBuilder.build(instance.currentScreen, configinfos);
        		
        		instance.openScreen(configScreen);
            }
        });
        
    }

	
}
