package net.just.irc;

import org.lwjgl.glfw.GLFW;

import me.lortseam.completeconfig.gui.ConfigScreenBuilder;
import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class MainClient implements ClientModInitializer
{

	private static KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
    	    "key.justirc.config", // The translation key of the keybinding's name
    	    InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
    	    GLFW.GLFW_KEY_J, // The keycode of the key
    	    "category.justirc.main" // The translation key of the keybinding's category.
    	));
	
	@Override
	public void onInitializeClient() {
		if (FabricLoader.getInstance().isModLoaded("cloth-config2")) 
        {
            ConfigScreenBuilder.setMain(Main.MOD_ID, new ClothConfigScreenBuilder());
        }
        
    	
              
        
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (keyBinding.wasPressed()) 
            {
        		ConfigScreenBuilder screenBuilder = new ClothConfigScreenBuilder();
        		MinecraftClient instance = MinecraftClient.getInstance();
    			Screen configScreen = screenBuilder.build(instance.currentScreen, Main.getSettings());
        		
        		instance.setScreen(configScreen);
            }
        });
		
	}

}
