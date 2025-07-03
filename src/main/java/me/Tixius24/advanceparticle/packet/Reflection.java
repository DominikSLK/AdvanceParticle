package me.Tixius24.advanceparticle.packet;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import me.Tixius24.advanceparticle.AdvanceParticle;

public class Reflection {

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
	
	private static Object getPlayer(Entity ent) {
		Object nms_player = null;
		Method nms_method = null;

		for (Method m : ent.getClass().getMethods()) {
			if (m.getName().equals("getHandle")) {
				nms_method = m;
			}
		}

		try {
			nms_player = nms_method.invoke(ent);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return nms_player;
	}

	public static void sendPacket(Player p, Object packet) {
		try {
			Object nms_player = getPlayer(p);
			Field field = nms_player.getClass().getField(plugin.getServerVersion().equalsIgnoreCase("v1_21_R5") ? "g" : (plugin.getServerVersion().equalsIgnoreCase("v1_21_R2") || plugin.getServerVersion().equalsIgnoreCase("v1_21_R3") || plugin.getServerVersion().equalsIgnoreCase("v1_21_R4")) ? "f" : plugin.getVersionNumber() > 19 ? "c" : (plugin.getVersionNumber() > 16 ? "b" : "playerConnection"));
			Object con_object = field.get(nms_player);

			Method method = getMethod(con_object.getClass(), (plugin.getVersionNumber() > 19 && !plugin.getServerVersion().equalsIgnoreCase("v1_20_R1")) ? "b" : (plugin.getVersionNumber() > 17 ? "a" : "sendPacket"));
			method.invoke(con_object, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private static Method getMethod(Class<?> cl, String methodName) {
		try {
			return cl.getMethod(methodName, getNMSClass("Packet"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Class<?> getNMSClass(String className) {
		try {
			if (plugin.getVersionNumber() < 17) {
				return Class.forName("net.minecraft.server." + plugin.getServerVersion() + "." + className);
			} else {
				if (className.equals("Packet")) {
					return Class.forName("net.minecraft.network.protocol.Packet");
				}
				
				return Class.forName("net.minecraft.network.protocol.game." + className);
			}
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static Object getParticle(String particleName) {
		if (plugin.getVersionNumber() < 8)
			return particleName;

		if (plugin.getVersionNumber() < 13) {
			Class<?> c = Reflection.getNMSClass("EnumParticle");

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
			Object key;
			Class<?> c;

			if (plugin.getVersionNumber() > 20) {
				key = cls.getMethod("b", String.class).invoke(null, particleName);
			} else {
				key = cls.getConstructor(String.class).newInstance(particleName);
			}

			if (plugin.getServerVersion().equals("v1_13_R1")) {
				c = Reflection.getNMSClass("Particle");

				Object object = c.getDeclaredField("REGISTRY").get(null);
				Method get = object.getClass().getMethod("get", Object.class);
				return get.invoke(object, key);
			}

			c = getIRegistryClass();

			Object object = c.getDeclaredField(getIRegistryParam(plugin.getVersionNumber())).get(null);
			Method gets = object.getClass().getMethod(plugin.getVersionNumber() > 17 ? "a" : "get", cls);
			particle = gets.invoke(object, key);
		} catch(Exception e) {
			e.printStackTrace();
		}

		return particle;
	}
	
	private static Class<?> getMinecraftKeyClass(String className) {
		try {
			if (plugin.getVersionNumber() > 16) {
				return Class.forName("net.minecraft.resources." + className);
			}

			return getNMSClass(className);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	private static Class<?> getIRegistryClass() {
		try {
			if (plugin.getVersionNumber() > 16) {
				if (plugin.getServerVersion().equals("v1_19_R2") || plugin.getServerVersion().equals("v1_19_R3") || plugin.getVersionNumber() > 19) {
					return Class.forName("net.minecraft.core.registries.BuiltInRegistries");
				}
				
				return Class.forName("net.minecraft.core.IRegistry");
			}

			return getNMSClass("IRegistry");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
	
	private static String getIRegistryParam(int version) {
		switch(version) {
		case 17:
			return "ab";
		case 18:
			if (plugin.getServerVersion().equals("v1_18_R2")) {
				return "Z";
			}
			
			return "ac";
		case 19:
			if (plugin.getServerVersion().equals("v1_19_R2") || plugin.getServerVersion().equals("v1_19_R3")) {
				return "k";
			}
			return "aa";
		case 20:
			if (plugin.getServerVersion().equals("v1_20_R3") || plugin.getServerVersion().equals("v1_20_R4")) {
				return "j";
			}
			return "k";
		case 21:
			return "i";
		default:
			return "PARTICLE_TYPE";
		}
	}

}