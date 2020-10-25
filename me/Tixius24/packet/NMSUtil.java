package me.Tixius24.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.Tixius24.AdvanceParticle;

public class NMSUtil {

	private static AdvanceParticle plugin = AdvanceParticle.getInstance();

	public static void setField(Object packet, String field, Object argument) {
		try {
			Field f = packet.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(packet, argument);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendPacket(Player p, Object packet) {
		try {
			Object nms_player = getPlayer(p);
			Field field = nms_player.getClass().getField("playerConnection");
			Object con_object = field.get(nms_player);
			Method method = getMethod(con_object.getClass(), "sendPacket");
			method.invoke(con_object, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Object getPacket(String packet) {
		Class<?> nms_class = NMSUtil.getNMSClass(packet);
		Object object = null;

		try {
			object = nms_class.getConstructor().newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return object;
	}


	public static Class<?> getNMSClass(String className) {
		String name = "net.minecraft.server." + plugin.version + "." + className;
		Class<?> c = null;
		try {
			c = Class.forName(name);
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return c;
	}

	public static Object getPlayer(Entity ent) {
		Object nms_player = null;
		Method nms_method = getMethod(ent.getClass(), "getHandle");
		try {
			nms_player = nms_method.invoke(ent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return nms_player;
	}

	public static Method getMethod(Class<?> cl, String methodName) {
		for (Method method : cl.getMethods()) {
			if (method.getName().equals(methodName)) {
				return method;
			}
		}

		return null;
	}

	public static Object getParticle(String particleName) {
		if (plugin.getServerVersion() < 8) 
			return particleName;

		if (plugin.getServerVersion() < 13) {
			Class<?> c = NMSUtil.getNMSClass("EnumParticle");

			for (Object object : c.getEnumConstants()) {
				if (object.toString().equals(particleName)) {
					return object;
				}
			}

			return null;
		}

		Object particle = null;

		try {
			Class<?> cls = NMSUtil.getNMSClass("MinecraftKey");
			Object key = cls.getConstructor(String.class).newInstance(particleName);
			Class<?> c = null;

			if (plugin.version.equals("v1_13_R1")) {
				c = NMSUtil.getNMSClass("Particle");

				Object object = c.getDeclaredField("REGISTRY").get(null);
				Method get = object.getClass().getMethod("get", Object.class);
				return get.invoke(object, key);
			}

			c = NMSUtil.getNMSClass("IRegistry");

			Object object = c.getDeclaredField("PARTICLE_TYPE").get(null);
			Method gets = object.getClass().getMethod("get", cls);
			particle = gets.invoke(object, key);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return particle;
	}

}