package net.just.irc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import net.just.irc.ChatUtils;
import net.just.irc.Main;
import net.just.irc.ircgroup;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class EntitySpawnManager{


	@Inject(
			method = "onEntitySpawn(Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;)V",
			at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/network/packet/s2c/play/EntitySpawnS2CPacket;getEntityTypeId()Lnet/minecraft/entity/EntityType;"),
			cancellable = true,
			locals = LocalCapture.CAPTURE_FAILHARD
	) 
	
	private void onEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci, double x, double y, double z, EntityType<?> type) 
	{
		if(Main.autoconnect && ircgroup.isAutoconnect())
		{
			Main.autoconnect = false;
			ChatUtils.sudomessage("@connect");
		}
	}
}
