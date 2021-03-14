package me.Tixius24;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import me.Tixius24.manager.MySQLManager;
import me.Tixius24.manager.APManager;
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
	private MySQLManager mysql;
	private APManager ap_manager;

	private String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private int versionNumber = Integer.parseInt(version.split("_")[1]);
	private static AdvanceParticle plugin;

	public boolean useMySQL = false;

	public void onEnable() {
		plugin = this;

		if (getVersionNumger() < 5 || getVersionNumger() > 16) {
			consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			consoleLog("§c> AdvanceParticle plugin cannot support that server version!");
			consoleLog("§c> AdvanceParticle plugin has been turned off !!!");
			consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}


		manager = new FileManager(this);
		manager.loadPluginFile();
		useMySQL = manager.getPluginConfig().getBoolean("MySQL.enable");
		stream = new StreamManager(this);
		message = new Messages(this);
		ap_manager = new APManager(this);

		if (versionNumber < 8 || version.equals("v1_8_R1")) 
			new Metrics_OLD(this, 7949);
		else 
			new Metrics_NEW(this, 7949);

		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("advanceparticle").setExecutor(new Commands(this));

		mysql = new MySQLManager(this, manager.getPluginConfig().getString("MySQL.host"), manager.getPluginConfig().getString("MySQL.database"), manager.getPluginConfig().getString("MySQL.username")
				, manager.getPluginConfig().getString("MySQL.password"), manager.getPluginConfig().getString("MySQL.table"), manager.getPluginConfig().getInt("MySQL.port"));

		loadStartTUP();
		updateParticle();

		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully loaded!");
		consoleLog("§a> Server version:§c " + version.replace("v", "") + " §aPlugin version §c" + getDescription().getVersion());
		consoleLog("§a> Spigot link:§c https://www.spigotmc.org/resources/71929/");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void onDisable() {
		if (useMySQL) {
			if (getMySQL().getConnection() != null) {
				getMySQL().closeConnection();
			}
		}

		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully disable!");
		consoleLog("§a> Spigot link:§c https://www.spigotmc.org/resources/71929/");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public static AdvanceParticle getInstance() {
		return plugin;
	}

	public APManager getAPManager() {
		return ap_manager;
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

	public MySQLManager getMySQL() {
		return mysql;
	}

	public HashMap<Player, String> getPlayers() {
		return players;
	}

	public String getServerVersion() {
		return version;
	}

	public int getVersionNumger() {
		return versionNumber;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		if (p == null) {
			return;
		}

		String name = p.getName();

		if (useMySQL) {
			if (getMySQL().existValue("players", name)) {
				getPlayers().put(p, getMySQL().getPlayerData(name));
			}

			return;
		}

		if (getStream().getPlayerStream().containsKey(name)) {
			getPlayers().put(p, getStream().getPlayerStream().get(name));
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (p == null) {
			return;
		}

		if (getPlayers().containsKey(p)) {
			getPlayers().remove(p);
		}
	}

	private void loadStartTUP() {
		if (useMySQL) {
			int lastID = getMySQL().getLastTableID();

			for (int i = 0; i < lastID; i ++) {
				getMySQL().getBlockData(i);
			}

			return;
		}

		getStream().loadPlayerData();
		getStream().loadBlockData();
	}
	
	public String getTime() {
		return new SimpleDateFormat("yyyy.MM.dd   HH:mm:ss").format(Calendar.getInstance().getTime());
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