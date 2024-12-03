package me.Tixius24.advanceparticle.packet;

import me.Tixius24.advanceparticle.AdvanceParticle;
import me.Tixius24.advanceparticle.object.EnumParticleObject;

public class PacketPlayOutWorldParticles {

	private static AdvanceParticle plugin = AdvanceParticle.getInstance();

	public static Object createPacket(String particle, double x, double y, double z) {
		try {
			EnumParticleObject po = EnumParticleObject.valueOf(particle);

			Class<?> nms_class = Reflection.getNMSClass(getPacketName());

			if (plugin.getVersionNumber() > 16) {
				if (plugin.getServerVersion().equalsIgnoreCase("v1_21_R3")) {
					return nms_class.getConstructor(getParticleParamClass(), boolean.class, boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class).
							newInstance(Reflection.getParticle(po.get()), po.getBoolean(), po.getBoolean(), x, y, z, po.OffSetX(), po.OffSetY(), po.OffSetZ(), po.getSpeed(), po.getCount());
				} else {
					return nms_class.getConstructor(getParticleParamClass(), boolean.class, double.class, double.class, double.class, float.class, float.class, float.class, float.class, int.class).
							newInstance(Reflection.getParticle(po.get()), po.getBoolean(), x, y, z, po.OffSetX(), po.OffSetY(), po.OffSetZ(), po.getSpeed(), po.getCount());
				}
			}

			Object packet = nms_class.getConstructor().newInstance();

			if (plugin.getVersionNumber() > 12) {
				Reflection.setField(packet, "j", Reflection.getParticle(po.get()));
				Reflection.setField(packet, "a", (float) x);
				Reflection.setField(packet, "b", (float) y);
				Reflection.setField(packet, "c", (float) z);
				Reflection.setField(packet, "d", po.OffSetX());
				Reflection.setField(packet, "e", po.OffSetY());
				Reflection.setField(packet, "f", po.OffSetZ());
				Reflection.setField(packet, "g", po.getSpeed());
				Reflection.setField(packet, "h", po.getCount());
				Reflection.setField(packet, "i", po.getBoolean());
				return packet;
			}

			Reflection.setField(packet, "a", Reflection.getParticle(po.get()));
			Reflection.setField(packet, "b", (float) x);
			Reflection.setField(packet, "c", (float) y);
			Reflection.setField(packet, "d", (float) z);
			Reflection.setField(packet, "e", po.OffSetX());
			Reflection.setField(packet, "f", po.OffSetY());
			Reflection.setField(packet, "g", po.OffSetZ());
			Reflection.setField(packet, "h", po.getSpeed());
			Reflection.setField(packet, "i", po.getCount());
			if (plugin.getVersionNumber() >= 8) Reflection.setField(packet, "j", po.getBoolean());

			return packet;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static String getPacketName() {
		if (plugin.getVersionNumber() < 7) {
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