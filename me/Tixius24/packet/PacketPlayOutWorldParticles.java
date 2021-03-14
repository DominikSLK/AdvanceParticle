package me.Tixius24.packet;

import me.Tixius24.AdvanceParticle;
import me.Tixius24.manager.DataManager;
import me.Tixius24.object.ParticleObject;

public class PacketPlayOutWorldParticles {

	private static AdvanceParticle plugin = AdvanceParticle.getInstance();

	public static Object createPacket(String particle, double x, double y, double z) {
		ParticleObject profile = ParticleObject.valueOf(particle);

		return createParticlePacket(DataManager.valueOf(particle), profile.getBoolean(), x, y, z, 
				profile.getOffSet1(), profile.getOffSet2(), profile.getOffSet3(), profile.getOffSet4(), profile.getCount());
	}

	private static Object createParticlePacket(DataManager particleManager, boolean b, double X, double Y, double Z, float offSetX, float offSetY, float offSetZ, float speed, int count) {
		Object packet = NMSUtil.getPacket(getPacketName());

		if (plugin.getVersionNumger() > 12) {
			NMSUtil.setField(packet, "j", NMSUtil.getParticle(particleManager.get()));
			NMSUtil.setField(packet, "a", (float) X);
			NMSUtil.setField(packet, "b", (float) Y);
			NMSUtil.setField(packet, "c", (float) Z);
			NMSUtil.setField(packet, "d", offSetX);
			NMSUtil.setField(packet, "e", offSetY);
			NMSUtil.setField(packet, "f", offSetZ);
			NMSUtil.setField(packet, "g", speed);
			NMSUtil.setField(packet, "h", count);
			NMSUtil.setField(packet, "i", b);
			return packet;
		}

		NMSUtil.setField(packet, "a", NMSUtil.getParticle(particleManager.get()));
		NMSUtil.setField(packet, "b", (float) X);
		NMSUtil.setField(packet, "c", (float) Y);
		NMSUtil.setField(packet, "d", (float) Z);
		NMSUtil.setField(packet, "e", offSetX);
		NMSUtil.setField(packet, "f", offSetY);
		NMSUtil.setField(packet, "g", offSetZ);
		NMSUtil.setField(packet, "h", speed);
		NMSUtil.setField(packet, "i", count);
		if (plugin.getVersionNumger() >= 8) NMSUtil.setField(packet, "j", b);

		return packet;
	}

	private static String getPacketName() {
		if (plugin.getVersionNumger() < 7) {
			return "Packet63WorldParticles";
		}

		return "PacketPlayOutWorldParticles";
	}

}