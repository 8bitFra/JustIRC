package net.just.irc;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.lortseam.completeconfig.gui.cloth.ClothConfigScreenBuilder;
import net.minecraft.client.MinecraftClient;

public class ModMenuIntegration implements ModMenuApi {

    private static final ClothConfigScreenBuilder configScreenBuilder = new ClothConfigScreenBuilder();

    MinecraftClient instance = MinecraftClient.getInstance();
    
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> configScreenBuilder.build(instance.currentScreen, Main.getSettings());
    }

}
