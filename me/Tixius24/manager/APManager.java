package me.Tixius24.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.Tixius24.AdvanceParticle;
import me.Tixius24.object.BlockObject;

public class APManager {
	private AdvanceParticle plugin;

	public APManager(AdvanceParticle pl) {
		plugin = pl;
	}

	public void savePlayerData(Player p, String particle) {
		String name = p.getName();

		if (plugin.useMySQL) {
			if (plugin.getMySQL().existValue("players", name)) {
				plugin.getMySQL().updatePlayerData(name, particle, plugin.getTime());
			}

			plugin.getMySQL().addPlayerData(name, particle, plugin.getTime());
			plugin.getPlayers().put(p, particle);
			return;
		} 

		plugin.getStream().getPlayerStream().put(p.getName(), particle);
		plugin.getStream().savePlayerFile();
		plugin.getPlayers().put(p, particle);
	}

	public void removePlayerData(Player p) {
		String name = p.getName();

		if (plugin.getPlayers().containsKey(p)) {
			if (plugin.useMySQL) {
				plugin.getMySQL().deletePlayerData(name);
				plugin.getPlayers().remove(p);
				return;
			} 

			plugin.getStream().deletePlayerData(name);
			plugin.getPlayers().remove(p);
		}
	}

	public void saveBlockData(Player p , String particle, String spawner, double x, double y, double z) {
		if (plugin.useMySQL) {
			plugin.getMySQL().saveBlockData(spawner, particle, x, y, z, p.getWorld().getName(), plugin.getTime());

			plugin.getStream().getBlockStream().put(spawner, createObject(p.getWorld().getName(), particle, x, y, z));
			return;
		}

		plugin.getStream().getBlockStream().put(spawner, createObject(p.getWorld().getName(), particle, x, y, z));
		plugin.getStream().saveBlockFile();
	}

	public void deleteBlockData(String spawner) {
		if (plugin.useMySQL){
			if (plugin.getMySQL().existValue("blocks", spawner)) {
				plugin.getMySQL().deleteBlockData(spawner);
				plugin.getStream().getBlockStream().remove(spawner);
			}

			return;
		}

		plugin.getStream().deleteBlockData(spawner);
	}

	public void getSpawnerInfo(Player p, String spawner) {
		BlockObject object = plugin.getStream().getBlockStream().get(spawner);
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

	public boolean checkBlockPerm(Player p, String perm) {
		if (!p.hasPermission("advanceparticle.block.*") && !p.hasPermission("advanceparticle.block." + perm.toLowerCase())) { 
			p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
			return false; 
		}

		return true;
	}

	public boolean checkPlayerPerm(Player p, String perm) {
		if (!p.hasPermission("advanceparticle.player.*") && !p.hasPermission("advanceparticle.player." + perm.toLowerCase())) { 
			p.sendMessage(plugin.getMessager().sendMessage("NOPERM")); 
			return false; 
		}

		return true;
	}

	public boolean checkEnableParticle(Player p, String particle) {
		if (!plugin.getManager().getPluginConfig().getBoolean("PARTICLES." + particle)) { 
			p.sendMessage(plugin.getMessager().sendMessage("ERROR_PARTICLE_USE")); 
			return false; 
		}

		return true;
	}

	public boolean checkAllowUseParticle(String particle) {
		if (plugin.getVersionNumger() < 7) {
			if (particle.equalsIgnoreCase("water_wake") || particle.equalsIgnoreCase("barrier")) { 
				return false;
			}
		}

		if (plugin.getVersionNumger() == 7) {
			if (particle.equalsIgnoreCase("barrier")) { 
				return false; 
			}
		}

		return true;
	}

	public boolean checkExistParticle(String particle , Player p) {
		try { 
			if (DataManager.valueOf(particle) == null); 
		} catch (IllegalArgumentException ex) { 
			p.sendMessage(plugin.getMessager().sendMessage("ERROR_PARTICLE")); 
			return false; 
		}

		return true;
	}

	public void teleportToSpawner(Player p, String spawnerName) {
		BlockObject object = plugin.getStream().getBlockStream().get(spawnerName);
		Location loc = new Location(Bukkit.getWorld(object.getWorld()), object.getX(), (object.getY() + 1.0), object.getZ());
		p.teleport(loc);
	}

	public void listParticle(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-=-> §aList of Particles §8<-=-=-=-=-=-=-=");

		for (DataManager m : DataManager.values()) {
			p.sendMessage("§7> §b" + m.toString());
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public void listSpawnerParticles(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-> §aList of Spawners §8<-=-=-=-=-=-=");

		for (String name : plugin.getStream().getBlockStream().keySet()) {
			p.sendMessage("§7> §b" + name);
		}

		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

	public BlockObject createObject(String world, String particle, double x, double y, double z) {
		return new BlockObject(particle, world, x, y,z);
	}

}