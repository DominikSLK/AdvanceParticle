package me.Tixius24;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tixius24.manager.FileManager;
import me.Tixius24.manager.ParticleManager;
import me.Tixius24.manager.StreamManager;
import me.Tixius24.metrics.Metrics_NEW;
import me.Tixius24.metrics.Metrics_OLD;
import me.Tixius24.object.BlockObject;
import me.Tixius24.packet.NMSUtil;
import me.Tixius24.packet.PacketPlayOutWorldParticles;

public class AdvanceParticle extends JavaPlugin implements Listener {
	private HashMap<Player, String> players = new HashMap<Player, String>();

	private FileManager manager;
	private Messages message;
	private StreamManager stream;

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
		stream = new StreamManager(this);
		message = new Messages(this);

		if (versionNumber < 8 || version.equals("v1_8_R1")) 
			new Metrics_OLD(this, 7949);
		else 
			new Metrics_NEW(this, 7949);

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

	public StreamManager getStream() {
		return stream;
	}

	public Messages getMessager() {
		return message;
	}

	public HashMap<Player, String> getPlayers() {
		return players;
	}

	public int getServerVersion() {
		return versionNumber;
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

	public void getSpawnerInfo(Player p, String spawner) {
		BlockObject object = getStream().getBlockStream().get(spawner);
		p.sendMessage(" ");
		p.sendMessage("§7Spawner Data:");	
		p.sendMessage("§8> §7Spawner Name: §a" + spawner);
		p.sendMessage("§8> §7World: §a" + object.getWorld());
		p.sendMessage("§8> §7Particle: §a" + object.getParticle());
		p.sendMessage("§8> §7X: §a" + object.getX());
		p.sendMessage("§8> §7Y: §a" + object.getY());
		p.sendMessage("§8> §7Z: §a" + object.getZ());
		p.sendMessage(" ");
	}
	
	public void teleportToSpawner(Player p, String spawnerName) {
		BlockObject object = getStream().getBlockStream().get(spawnerName);
		Location loc = new Location(Bukkit.getWorld(object.getWorld()), object.getX(), (object.getY() + 1.0), object.getZ());
		p.teleport(loc);
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

	public void listParticle(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-=-> §aList of Particles §8<-=-=-=-=-=-=-=");

		for (ParticleManager m : ParticleManager.values()) {
			p.sendMessage("§7> §b" + m.toString());
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void listSpawnerParticles(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-> §aList of Spawners §8<-=-=-=-=-=-=");

		for (String name : getStream().getBlockStream().keySet()) {
			p.sendMessage("§7> §b" + name);
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		if (getStream().getPlayerStream().containsKey(e.getPlayer().getName())) {
			String particle = getStream().getPlayerStream().get(e.getPlayer().getName());

			if (particle.equals("NOT_SET")) {
				return;
			}

			getPlayers().put(e.getPlayer(), particle);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		if (getPlayers().containsKey(e.getPlayer())) {
			getStream().savePlayerData(e.getPlayer(), getPlayers().get(e.getPlayer()));
			getPlayers().remove(e.getPlayer());
		}
	}

	private void updateParticle() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				while (true) {
					for (Player p : players.keySet()) {
						for (Player pl : p.getWorld().getPlayers()) {
							Location loc = p.getLocation();
							NMSUtil.sendPacket(pl, PacketPlayOutWorldParticles.createPacket(players.get(p), loc.getX(), loc.getY(), loc.getZ()));
						}
					}

					for (String name : getStream().getBlockStream().keySet()) {
						BlockObject object = getStream().getBlockStream().get(name);
						Object packet = PacketPlayOutWorldParticles.createPacket(object.getParticle(), object.getX(), object.getY(), object.getZ());

						for (Player p : Bukkit.getWorld(object.getWorld()).getPlayers()) {
							NMSUtil.sendPacket(p, packet);
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