package me.Tixius24.packet;

import me.Tixius24.AdvanceParticle;
import me.Tixius24.object.ParticleObject;

public class PacketPlayOutWorldParticles {

	private static AdvanceParticle plugin = AdvanceParticle.getInstance();

	public static Object createPacket(String particle, double x, double y, double z) {
		try {
			ParticleObject po = ParticleObject.valueOf(particle);

			Class<?> nms_class = NMSUtil.getNMSClass(getPacketName());

			if (plugin.getVersionNumger() > 16) {
				return nms_class.getConstructor(getParticleParamClass(), boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class).
						newInstance(NMSUtil.getParticle(po.get()), po.getBoolean(), x, y, z, po.OffSetX(), po.OffSetY(), po.OffSetZ(), po.getSpeed(), po.getCount());
			}

			Object packet = nms_class.getConstructor().newInstance();

			if (plugin.getVersionNumger() > 12) {
				NMSUtil.setField(packet, "j", NMSUtil.getParticle(po.get()));
				NMSUtil.setField(packet, "a", (float) x);
				NMSUtil.setField(packet, "b", (float) y);
				NMSUtil.setField(packet, "c", (float) z);
				NMSUtil.setField(packet, "d", po.OffSetX());
				NMSUtil.setField(packet, "e", po.OffSetY());
				NMSUtil.setField(packet, "f", po.OffSetZ());
				NMSUtil.setField(packet, "g", po.getSpeed());
				NMSUtil.setField(packet, "h", po.getCount());
				NMSUtil.setField(packet, "i", po.getBoolean());
				return packet;
			}

			NMSUtil.setField(packet, "a", NMSUtil.getParticle(po.get()));
			NMSUtil.setField(packet, "b", (float) x);
			NMSUtil.setField(packet, "c", (float) y);
			NMSUtil.setField(packet, "d", (float) z);
			NMSUtil.setField(packet, "e", po.OffSetX());
			NMSUtil.setField(packet, "f", po.OffSetY());
			NMSUtil.setField(packet, "g", po.OffSetZ());
			NMSUtil.setField(packet, "h", po.getSpeed());
			NMSUtil.setField(packet, "i", po.getCount());
			if (plugin.getVersionNumger() >= 8) NMSUtil.setField(packet, "j", po.getBoolean());

			return packet;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static String getPacketName() {
		if (plugin.getVersionNumger() < 7) {
			return "Packet63WorldParticles";
		}

		return "PacketPlayOutWorldParticles";
	}

	private static Class<?> getParticleParamClass() {
		Class<?> c = null;
		try {
			c = Class.forName("net.minecraft.core.particles.ParticleParam");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return c;
	}

}