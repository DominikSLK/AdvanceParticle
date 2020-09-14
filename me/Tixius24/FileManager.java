package me.Tixius24;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import me.Tixius24.Particle.PacketPlayOutWorldParticles;

public class FileManager {
	private AdvanceParticle plugin;
	private File cf;
	private YamlConfiguration c;
	private File mf;
	private YamlConfiguration m;

	public FileManager(AdvanceParticle pl) {
		plugin = pl;
	}

	public void loadPluginFile() {
		plugin.getDataFolder().mkdirs();
		cf = new File(plugin.getDataFolder(), "config.yml");
		mf = new File(plugin.getDataFolder(), "messages.yml");

		if (!cf.exists()) {
			try {
				Files.copy(plugin.getResource("config.yml"), cf.toPath());
			} catch (IOException ex) {}
		}

		if (!mf.exists()) {
			try {
				Files.copy(plugin.getResource("messages.yml"), mf.toPath());
			} catch (IOException ex) {}
		}

		c = YamlConfiguration.loadConfiguration(cf);
		m = YamlConfiguration.loadConfiguration(mf);
	}

	public YamlConfiguration getPluginConfig() {
		return c;
	}

	public YamlConfiguration getPluginMessages() {
		return m;
	}

	public void saveBlockSpawnerData(String name, String particle, String world, double x, double y, double z) {
		File f = new File(plugin.getDataFolder(), "/data/" + name + ".yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(f);

		config.set("World", world);
		config.set("X", x);
		config.set("Y", y);
		config.set("Z", z);
		config.set("Particle", particle);

		try {config.save(f);} catch (IOException ex) {}
	}

	public void deleteBlockSpawnerData(String name) {
		File f = new File(plugin.getDataFolder(), "/data/" + name + ".yml");
		f.delete();
	}

	public void loadBlockSpawnerData() {
		File f = new File(plugin.getDataFolder(), "/data/");

		if (!f.exists()) {
			return;
		}

		for (File f2 : f.listFiles()) {
			String name = f2.getName().replace(".yml", "");
			YamlConfiguration c = YamlConfiguration.loadConfiguration(f2);

			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put(c.getString("World"), PacketPlayOutWorldParticles.createPacket(c.getString("Particle"), c.getDouble("X"), c.getDouble("Y"), c.getDouble("Z")));
			plugin.getBlockParticle().put(name, map);
		}

	}

}