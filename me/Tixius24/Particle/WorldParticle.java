package me.Tixius24.Particle;

import org.bukkit.entity.Player;

import me.Tixius24.ParticleManager;

public interface WorldParticle {

	public Object createParticlePacket(ParticleManager particleManager, boolean b, double getX, double getY, double getZ, float data1, float data2, float data3, float data4, int count);
	public void sendPacket(Player p, Object packet);
}