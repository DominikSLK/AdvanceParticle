package me.Tixius24.Particle;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_16_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.Tixius24.ParticleManager;
import net.minecraft.server.v1_16_R1.IRegistry;
import net.minecraft.server.v1_16_R1.MinecraftKey;
import net.minecraft.server.v1_16_R1.Packet;
import net.minecraft.server.v1_16_R1.PacketPlayOutWorldParticles;

public class WorldParticle_v1_16_R1 implements WorldParticle {

	public Object createParticlePacket(ParticleManager particleManager, boolean b, double getX, double getY, double getZ, float data1, float data2, float data3, float data4, int count) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles();
		setField(packet, "j", IRegistry.PARTICLE_TYPE.get(new MinecraftKey(particleManager.get())));
		setField(packet, "a", (float) getX);
		setField(packet, "b", (float) getY);
		setField(packet, "c", (float) getZ);
		setField(packet, "d", data1);
		setField(packet, "e", data2);
		setField(packet, "f", data3);
		setField(packet, "g", data4);
		setField(packet, "h", count);
		setField(packet, "i", b);
		return packet;
	}

	public void sendPacket(Player p, Object packet) {
		((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet<?>) packet);
	}

	private void setField(Object packet, String field, Object argument) {
		try {
			Field f = packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(packet, argument);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}