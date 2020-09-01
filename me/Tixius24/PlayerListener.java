package me.Tixius24;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {
	private AdvanceParticles plugin;

	public PlayerListener(AdvanceParticles pl) {
		plugin = pl;
	}

	@EventHandler
	public void playerQuit(PlayerQuitEvent e) {
		if (plugin.getPlayers().containsKey(e.getPlayer())) {
			plugin.getPlayers().remove(e.getPlayer());
		}
	}

}