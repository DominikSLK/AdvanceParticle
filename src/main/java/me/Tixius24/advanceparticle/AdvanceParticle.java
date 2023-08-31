package me.Tixius24.advanceparticle;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import me.Tixius24.advanceparticle.manager.FileManager;
import me.Tixius24.advanceparticle.manager.MySQLManager;
import me.Tixius24.advanceparticle.manager.AdvanceManager;
import me.Tixius24.advanceparticle.manager.StreamManager;
import me.Tixius24.advanceparticle.metrics.Metrics;
import me.Tixius24.advanceparticle.object.BlockObject;
import me.Tixius24.advanceparticle.packet.Reflection;
import me.Tixius24.advanceparticle.packet.PacketPlayOutWorldParticles;

public class AdvanceParticle extends JavaPlugin implements Listener {
	private HashMap<Player, String> players = new HashMap<Player, String>();

	private FileManager manager;
	private StreamManager stream;
	private MySQLManager mysql;
	private AdvanceManager ap_manager;

	private String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	private int versionNumber = Integer.parseInt(version.split("_")[1]);
	private static AdvanceParticle plugin;

	public boolean useMySQL = false;

	public void onEnable() {
		plugin = this;
		
		if (getVersionNumger() == 20) {
			if (!version.equals("v1_20_R1")) {
				Action_Unsapported_Version();
				return;
			}
		}

		if (getVersionNumger() < 5 || getVersionNumger() > 20) {
			Action_Unsapported_Version();
			return;
		}

		manager = new FileManager(this);
		manager.loadPluginFiles();
		manager.loadMessageFile();
		useMySQL = manager.getPluginConfig().getBoolean("MySQL.enable");
		stream = new StreamManager(this);
		ap_manager = new AdvanceManager(this);

		if (versionNumber > 8 || version.equals("v1_8_R2") || version.equals("v1_8_R3")) 
			new Metrics(this, 7949);

		Bukkit.getPluginManager().registerEvents(this, this);
		getCommand("advanceparticle").setExecutor(new Commands(this));

		mysql = new MySQLManager(this, manager.getPluginConfig().getString("MySQL.host"), 
				manager.getPluginConfig().getString("MySQL.database"),
				manager.getPluginConfig().getString("MySQL.username"), 
				manager.getPluginConfig().getString("MySQL.password"), 
				manager.getPluginConfig().getString("MySQL.table"), 
				manager.getPluginConfig().getInt("MySQL.port"));

		loadDataPluginStart();
		updateParticle();

		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully loaded!");
		consoleLog("§a> Server version:§9 " + version.replace("v", "") + " §aPlugin version §9" + getDescription().getVersion());
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void onDisable() {
		if (useMySQL) {
			if (getMySQL().getConnection() != null) {
				getMySQL().closeConnection();
			}
		}

		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§a> AdvanceParticle plugin has been successfully disable!");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}
	
	private void Action_Unsapported_Version() {
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		consoleLog("§c> Your server version is not supported!!");
		consoleLog("§c> AdvanceParticle plugin is turned off !!!");
		consoleLog("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		Bukkit.getPluginManager().disablePlugin(this);
	}

	public static AdvanceParticle getInstance() {
		return plugin;
	}

	public AdvanceManager getAdvanceManager() {
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
	public void playerConnect(PlayerJoinEvent e) {
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
	public void playerDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();

		if (p == null) {
			return;
		}

		if (getPlayers().containsKey(p)) {
			getPlayers().remove(p);
		}
	}

	private void loadDataPluginStart() {
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
					try {
						for (Player p : players.keySet()) {
							if (getVersionNumger() > 7) {
								if (p.getGameMode() != GameMode.SPECTATOR) {
									for (Player pl : p.getWorld().getPlayers()) {
										Location loc = p.getLocation();
										Reflection.sendPacket(pl, PacketPlayOutWorldParticles.createPacket(players.get(p), loc.getX(), loc.getY(), loc.getZ()));
									}
								}
							} else {
								for (Player pl : p.getWorld().getPlayers()) {
									Location loc = p.getLocation();
									Reflection.sendPacket(pl, PacketPlayOutWorldParticles.createPacket(players.get(p), loc.getX(), loc.getY(), loc.getZ()));
								}
							}
						}

						for (String name : getStream().getBlockStream().keySet()) {
							BlockObject object = getStream().getBlockStream().get(name);
							Object packet = PacketPlayOutWorldParticles.createPacket(object.getParticle(), object.getX(), object.getY(), object.getZ());

							for (Player p : Bukkit.getWorld(object.getWorld()).getPlayers()) {
								Reflection.sendPacket(p, packet);
							}
						}

						Thread.sleep(250); // 1000 = 1 seconds
					} catch (Exception ex) { /* Empty place */ }
				}

			}

		}).start();
	}
	
}