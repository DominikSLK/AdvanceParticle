package me.Tixius24.advanceparticle.manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import me.Tixius24.advanceparticle.AdvanceParticle;

public class FileManager {
	private AdvanceParticle plugin;
	private File cf;
	private YamlConfiguration c;
	private File mf;
	private YamlConfiguration m;
	
	private HashMap<String, String> messages = new HashMap<String, String>();

	public FileManager(AdvanceParticle pl) {
		plugin = pl;
	}
	
	public void loadMessageFile() {
		messages.clear();
		YamlConfiguration mes = plugin.getManager().getPluginMessages();

		for (String key : mes.getKeys(false)) {
			messages.put(key, mes.getString(key));
		}
	}

	public void loadPluginFiles() {
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

	public String sendMessage(String key) {
		return ChatColor.stripColor(messages.get(key).replace("%prefix%", messages.get("PREFIX"))).replace("&", "§");
	}

}