package me.Tixius24;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;

public class Messager {
	private AdvanceParticles plugin;
	private HashMap<String, String> messages = new HashMap<String, String>();

	public Messager(AdvanceParticles pl) {
		plugin = pl;
		loadPluginMessage();
	}

	public void loadPluginMessage() {
		messages.clear();
		YamlConfiguration mes = plugin.getManager().getPluginMessages();

		for (String key : mes.getKeys(false)) {
			messages.put(key, mes.getString(key));
		}
	}

	public String sendMessage(String key) {
		return ChatColor.stripColor(messages.get(key).replace("%prefix%", messages.get("PREFIX"))).replace("&", "§");
	}

}