package me.Tixius24.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.bukkit.configuration.file.YamlConfiguration;
import me.Tixius24.AdvanceParticle;

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
		File data = new File(plugin.getDataFolder(), "/data/");

		if (!data.exists()) 
			data.mkdirs();

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

}