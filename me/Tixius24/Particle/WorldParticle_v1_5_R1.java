package me.Tixius24.Particle;

import java.lang.reflect.Field;

import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import me.Tixius24.ParticleManager;
import net.minecraft.server.v1_5_R1.Packet;
import net.minecraft.server.v1_5_R1.Packet63WorldParticles;
public class WorldParticle_v1_5_R1 implements WorldParticle {
	
	public Object createParticlePacket(ParticleManager particleManager, boolean b, double getX, double getY, double getZ, float data1, float data2, float data3, float data4, int count) {
		Packet63WorldParticles packet = new Packet63WorldParticles();
		setField(packet, "a", particleManager.get());
		setField(packet, "b", (float) getX);
		setField(packet, "c", (float) getY);
		setField(packet, "d", (float) getZ);
		setField(packet, "e", data1);
		setField(packet, "f", data2);
		setField(packet, "g", data3);
		setField(packet, "h", data4);
		setField(packet, "i", count);
		return packet;
	}

	public void sendPacket(Player p, Object packet) {
		((CraftPlayer)p).getHandle().playerConnection.sendPacket((Packet) packet);
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