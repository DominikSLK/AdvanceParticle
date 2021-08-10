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
			Field field = nms_player.getClass().getField(plugin.getVersionNumger() > 16 ? "b" : "playerConnection");
			Object con_object = field.get(nms_player);
			Method method = getMethod(con_object.getClass(), "sendPacket");
			method.invoke(con_object, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String className) {
		Class<?> c = null;
		try {
			if (plugin.getVersionNumger() < 17) {
				c = Class.forName("net.minecraft.server." + plugin.getServerVersion() + "." + className);
			} else {
				c = Class.forName("net.minecraft.network.protocol.game." + className);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return c;
	}

	private static Class<?> getMinecraftKeyClass(String className) {
		try {
			if (plugin.getVersionNumger() > 16) {
				return Class.forName("net.minecraft.resources." + className);
			}

			return getNMSClass(className);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static Class<?> getIRegistryClass(String className) {
		try {
			if (plugin.getVersionNumger() > 16) {
				return Class.forName("net.minecraft.core." + className);
			}

			return getNMSClass(className);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
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
		if (plugin.getVersionNumger() < 8) 
			return particleName;

		if (plugin.getVersionNumger() < 13) {
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
			Class<?> cls = getMinecraftKeyClass("MinecraftKey");
			Object key = cls.getConstructor(String.class).newInstance(particleName);
			Class<?> c = null;

			if (plugin.getServerVersion().equals("v1_13_R1")) {
				c = NMSUtil.getNMSClass("Particle");

				Object object = c.getDeclaredField("REGISTRY").get(null);
				Method get = object.getClass().getMethod("get", Object.class);
				return get.invoke(object, key);
			}

			c = getIRegistryClass("IRegistry");

			Object object = c.getDeclaredField(plugin.getVersionNumger() > 16 ? "ab" : "PARTICLE_TYPE").get(null);
			Method gets = object.getClass().getMethod("get", cls);
			particle = gets.invoke(object, key);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return particle;
	}

}