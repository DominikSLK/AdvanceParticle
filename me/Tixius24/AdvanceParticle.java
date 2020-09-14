package me.Tixius24;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tixius24.Metrics.Metrics_NEW;
import me.Tixius24.Metrics.Metrics_OLD;
import me.Tixius24.Particle.NMSUtil;
import me.Tixius24.Particle.PacketPlayOutWorldParticles;

public class AdvanceParticle extends JavaPlugin implements Listener {
	private HashMap<Player, String> players = new HashMap<Player, String>();
	private HashMap<String, HashMap<String, Object>> blockSpawners = new HashMap<String, HashMap<String, Object>>();

	private FileManager manager;
	private Messager message;

	public String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private int versionNumber = Integer.parseInt(version.split("_")[1]);
	private static AdvanceParticle plugin;

	public void onEnable() {
		plugin = this;

		if (getServerVersion() < 5 || getServerVersion() > 16) {
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

		Bukkit.getPluginManager().registerEvents(this, this);
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

	public static AdvanceParticle getInstance() {
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

	public HashMap<Player, String> getPlayers() {
		return players;
	}

	public int getServerVersion() {
		return versionNumber;
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

	public void setParticleAtBlock(Player p , String particle, String spawner, double x, double y, double z) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put(p.getWorld().getName(), PacketPlayOutWorldParticles.createPacket(particle, x, y, z));
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
	
	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		if (plugin.getPlayers().containsKey(e.getPlayer())) {
			plugin.getPlayers().remove(e.getPlayer());
		}
	}

	private void updateParticle() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					for (Player p : players.keySet()) {
						for (Player pl : p.getWorld().getPlayers()) {
							NMSUtil.sendPacket(pl, PacketPlayOutWorldParticles.createPacket(p));
						}
					}

					for (String name : blockSpawners.keySet()) {
						for (String world : blockSpawners.get(name).keySet()) {
							for (Player p : Bukkit.getWorld(world).getPlayers()) {
								NMSUtil.sendPacket(p, blockSpawners.get(name).get(world));
							}
						}

					}

					try {
						Thread.sleep(250); // 1000 = 1 seconds
					} catch (Exception ex) {
						ex.printStackTrace(); 
					}
				}

			}

		}).start();

	}
}