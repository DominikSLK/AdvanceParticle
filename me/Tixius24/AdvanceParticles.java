package me.Tixius24;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tixius24.Metrics.Metrics_NEW;
import me.Tixius24.Metrics.Metrics_OLD;
import me.Tixius24.Particle.WorldParticle;
import me.Tixius24.Particle.WorldParticle_v1_10_R1;
import me.Tixius24.Particle.WorldParticle_v1_11_R1;
import me.Tixius24.Particle.WorldParticle_v1_12_R1;
import me.Tixius24.Particle.WorldParticle_v1_13_R1;
import me.Tixius24.Particle.WorldParticle_v1_13_R2;
import me.Tixius24.Particle.WorldParticle_v1_14_R1;
import me.Tixius24.Particle.WorldParticle_v1_15_R1;
import me.Tixius24.Particle.WorldParticle_v1_16_R1;
import me.Tixius24.Particle.WorldParticle_v1_16_R2;
import me.Tixius24.Particle.WorldParticle_v1_5_R1;
import me.Tixius24.Particle.WorldParticle_v1_5_R2;
import me.Tixius24.Particle.WorldParticle_v1_5_R3;
import me.Tixius24.Particle.WorldParticle_v1_6_R1;
import me.Tixius24.Particle.WorldParticle_v1_6_R2;
import me.Tixius24.Particle.WorldParticle_v1_6_R3;
import me.Tixius24.Particle.WorldParticle_v1_7_R1;
import me.Tixius24.Particle.WorldParticle_v1_7_R2;
import me.Tixius24.Particle.WorldParticle_v1_7_R3;
import me.Tixius24.Particle.WorldParticle_v1_7_R4;
import me.Tixius24.Particle.WorldParticle_v1_8_R1;
import me.Tixius24.Particle.WorldParticle_v1_8_R2;
import me.Tixius24.Particle.WorldParticle_v1_8_R3;
import me.Tixius24.Particle.WorldParticle_v1_9_R1;
import me.Tixius24.Particle.WorldParticle_v1_9_R2;

public class AdvanceParticles extends JavaPlugin {
	private HashMap<Player, String> players = new HashMap<Player, String>();
	private HashMap<String, HashMap<String, Object>> blockSpawners = new HashMap<String, HashMap<String, Object>>();

	private FileManager manager;
	private Messager message;
	private WorldParticle particle;

	private String version;
	private int versionNumber;
	private static AdvanceParticles plugin;

	public void onEnable() {
		plugin = this;
		version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		versionNumber = Integer.parseInt(version.split("_")[1]);

		if (version.equals("v1_5_R1")) { particle = new WorldParticle_v1_5_R1(); }
		else if (version.equals("v1_5_R2")) { particle = new WorldParticle_v1_5_R2(); }
		else if (version.equals("v1_5_R3")) { particle = new WorldParticle_v1_5_R3(); }
		else if (version.equals("v1_6_R1")) { particle = new WorldParticle_v1_6_R1(); }
		else if (version.equals("v1_6_R2")) { particle = new WorldParticle_v1_6_R2(); }
		else if (version.equals("v1_6_R3")) { particle = new WorldParticle_v1_6_R3(); }
		else if (version.equals("v1_7_R1")) { particle = new WorldParticle_v1_7_R1(); }
		else if (version.equals("v1_7_R2")) { particle = new WorldParticle_v1_7_R2(); }
		else if (version.equals("v1_7_R3")) { particle = new WorldParticle_v1_7_R3(); }
		else if (version.equals("v1_7_R4")) { particle = new WorldParticle_v1_7_R4(); }
		else if (version.equals("v1_8_R1")) { particle = new WorldParticle_v1_8_R1(); }
		else if (version.equals("v1_8_R2")) { particle = new WorldParticle_v1_8_R2(); }
		else if (version.equals("v1_8_R3")) { particle = new WorldParticle_v1_8_R3(); }
		else if (version.equals("v1_9_R1")) { particle = new WorldParticle_v1_9_R1(); }
		else if (version.equals("v1_9_R2")) { particle = new WorldParticle_v1_9_R2(); }
		else if (version.equals("v1_10_R1")) { particle = new WorldParticle_v1_10_R1(); }
		else if (version.equals("v1_11_R1")) { particle = new WorldParticle_v1_11_R1(); }
		else if (version.equals("v1_12_R1")) { particle = new WorldParticle_v1_12_R1(); }
		else if (version.equals("v1_13_R1")) { particle = new WorldParticle_v1_13_R1(); }
		else if (version.equals("v1_13_R2")) { particle = new WorldParticle_v1_13_R2(); }
		else if (version.equals("v1_14_R1")) { particle = new WorldParticle_v1_14_R1(); }
		else if (version.equals("v1_15_R1")) { particle = new WorldParticle_v1_15_R1(); }
		else if (version.equals("v1_16_R1")) { particle = new WorldParticle_v1_16_R1(); }
		else if (version.equals("v1_16_R2")) { particle = new WorldParticle_v1_16_R2(); }

		if (particle == null) {
			consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			consoleLog("§c> AdvanceParticle plugin cannot support that server version!");
			consoleLog("§c> AdvanceParticle plugin has been turned off !!!");
			consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}

		manager = new FileManager(this);
		manager.loadPluginFile();
		manager.loadBlockSpawnerData();
		message = new Messager(this);

		if (versionNumber < 8 || version.equals("v1_8_R1")) {
			new Metrics_OLD(this, 7949);
		} else {
			new Metrics_NEW(this, 7949);
		}

		Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
		getCommand("advanceparticle").setExecutor(new Commands(this));

		updateParticle();

		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully loaded!");
		consoleLog("§a> Server version:§c " + version.replace("v", "") + " §aPlugin version §c" + getDescription().getVersion());
		consoleLog("§a> Spigot link:§c https://www.spigotmc.org/resources/71929/");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void onDisable() {
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully disable!");
		consoleLog("§a> Spigot link:§c https://www.spigotmc.org/resources/71929/");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
	
	public static AdvanceParticles getInstance() {
		return plugin;
	}

	public void consoleLog(String message) {
		Bukkit.getConsoleSender().sendMessage(message);
	}

	public FileManager getManager() {
		return manager;
	}

	public Messager getMessager() {
		return message;
	}

	public WorldParticle getPacket() {
		return particle;
	}

	public HashMap<Player, String> getPlayers() {
		return players;
	}

	public HashMap<String, HashMap<String, Object>> getBlockParticle() {
		return blockSpawners;
	}
	
	public int getVersionNumger() {
		return versionNumber;
	}

	public boolean checkExistParticle(String particle , Player p) {
		try { 
			if (ParticleManager.valueOf(particle) == null); 
		} catch (IllegalArgumentException ex) { 
			p.sendMessage(getMessager().sendMessage("ERROR_PARTICLE")); 
			return false; 
		}

		return true;
	}

	public boolean checkBlockPerm(Player p, String perm) {
		if (!p.hasPermission("advanceparticle.block.*") && !p.hasPermission("advanceparticle.block." + perm.toLowerCase())) { 
			p.sendMessage(getMessager().sendMessage("NOPERM")); 
			return false; 
		}

		return true;
	}

	public boolean checkPlayerPerm(Player p, String perm) {
		if (!p.hasPermission("advanceparticle.player.*") && !p.hasPermission("advanceparticle.player." + perm.toLowerCase())) { 
			p.sendMessage(getMessager().sendMessage("NOPERM")); 
			return false; 
		}

		return true;
	}

	public boolean checkEnableParticle(Player p, String particle) {
		if (!getManager().getPluginConfig().getBoolean("PARTICLES." + particle)) { 
			p.sendMessage(getMessager().sendMessage("ERROR_PARTICLE_USE")); 
			return false; 
		}
		
		return true;
	}

	public void setParticleAtBlock(Player p , String particle, String spawner, double x, double y, double z) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(p.getWorld().getName(), createPacket(particle, x, y, z));
		getBlockParticle().put(spawner, map);
		getManager().saveBlockSpawnerData(spawner, particle, p.getWorld().getName(), x, y, z);
	}

	public void listParticle(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-=-> §aParticle List §8<-=-=-=-=-=-=-=");

		for (ParticleManager m : ParticleManager.values()) {
			p.sendMessage("§7> §b" + m.toString());
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void listSpawnerParticles(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-> §aBlock spawns list §8<-=-=-=-=-=-=");

		for (String name : getBlockParticle().keySet()) {
			p.sendMessage("§7> §c" + name);
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	private void updateParticle() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				for (Player p : players.keySet()) {
					for (Player pl : p.getWorld().getPlayers()) {
						getPacket().sendPacket(pl, createPacket(p));
					}
				}

				for (String name : blockSpawners.keySet()) {
					for (String world : blockSpawners.get(name).keySet()) {
						for (Player p : Bukkit.getWorld(world).getPlayers()) {
							getPacket().sendPacket(p, blockSpawners.get(name).get(world));
						}
					}

				}

			}

		}, 0l, 5L);

	}

	public boolean checkAllowUseParticle(String particle) {
		if (versionNumber < 7) {
			if (particle.equalsIgnoreCase("water_wake") || particle.equalsIgnoreCase("barrier")) { 
				return false;
			}
		}

		if (versionNumber == 7) {
			if (particle.equalsIgnoreCase("barrier")) { 
				return false; 
			}
		}

		return true;
	}

	private Object createPacket(Player p) {
		String particle = players.get(p);
		Profiles profile = Profiles.valueOf(particle);

		Location loc = p.getLocation();

		return getPacket().createParticlePacket(ParticleManager.valueOf(particle), profile.getBoolean(), 
				loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), profile.getOffSet1(), profile.getOffSet2(), profile.getOffSet3(), profile.getOffSet4(), profile.getCount());
	}

	public Object createPacket(String particle, double x, double y, double z) {
		Profiles profile = Profiles.valueOf(particle);

		return getPacket().createParticlePacket(ParticleManager.valueOf(particle), profile.getBoolean(), x, y, z, 
				profile.getOffSet1(), profile.getOffSet2(), profile.getOffSet3(), profile.getOffSet4(), profile.getCount());
	}
}