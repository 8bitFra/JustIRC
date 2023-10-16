package net.just.irc.mixin.client;

import java.util.concurrent.TimeUnit;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.just.irc.ChatUtils;
import net.just.irc.Ircgroup;
import net.just.irc.Main;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.listener.ClientPacketListener;

@Mixin(ClientCommonNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin implements ClientPacketListener
{
	
		@Inject(
			at = @At("HEAD"),
			method = "sendPacket(Lnet/minecraft/network/packet/Packet;)V",
			cancellable = true
		)
		
		private void onSendPacket(Packet<?> packet, CallbackInfo ci)
		{
			//System.out.println(packet);
			if(Main.autoconnect) this.checkstatus();
		}
	
		private void checkstatus() 
		{
			Main.autoconnect = false;
			new Thread(() -> 
			{
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(Ircgroup.isAutoconnect())
				{
					ChatUtils.sudomessage("@connect");
				}
			}).start();
		}
}
