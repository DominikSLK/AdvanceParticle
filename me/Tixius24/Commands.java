package me.Tixius24;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
	private AdvanceParticle plugin;

	public Commands(AdvanceParticle pl) {
		plugin = pl;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (!(sender instanceof Player)) {
			sender.sendMessage(plugin.getManager().sendMessage("CONSOLE"));			
			return true;
		}

		Player p = (Player) sender;
		String particle;

		if (args.length == 0 || args.length > 3) {
			sendDefaultHelp(p);
			return true;
		}

		if (args.length == 1) {

			if (args[0].equalsIgnoreCase("help")) {

				if (!p.hasPermission("advanceparticle.help")) { 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM")); 
					return true; 
				}

				sendHelp(p);
				return true;
			}

			if (args[0].equalsIgnoreCase("reload")) {

				if (p.hasPermission("advanceparticle.reload")) { 
					plugin.getManager().loadPluginFiles();
					plugin.getManager().loadMessageFile();
					p.sendMessage("§aPlugin config.yml and messages.yml files has been reloaded!");
				} else 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM"));

				return true; 
			}

			if (args[0].equalsIgnoreCase("particle")) {

				if (p.hasPermission("advanceparticle.particle")) { 
					plugin.getAdvanceManager().listParticle(p);
				} else 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM"));

				return true;
			}

			if (args[0].equalsIgnoreCase("spawner")) {

				if (p.hasPermission("advanceparticle.spawner")) { 
					plugin.getAdvanceManager().listSpawnerParticles(p);
				} else 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM")); 

				return true;
			}

			if (args[0].equalsIgnoreCase("remove")) {

				if (plugin.getPlayers().containsKey(p)) { 
					p.sendMessage(plugin.getManager().sendMessage("REMOVE_PARTICLE_PLAYER"));
					plugin.getAdvanceManager().removePlayerData(p);
				} else 
					p.sendMessage(plugin.getManager().sendMessage("ERROR_PARTICLE_ACTIVE")); 

				return true;
			}

			sendDefaultHelp(p);
		}

		if (args.length == 2) {
			if (args[0].equalsIgnoreCase("add")) {
				particle = args[1].toUpperCase();

				if (!plugin.getAdvanceManager().checkPlayerPerm(p, args[1])) {
					return true; 
				}

				if (!plugin.getAdvanceManager().checkExistParticle(particle, p)) { 
					return true; 
				}

				if (!plugin.getAdvanceManager().checkEnableParticle(p, particle)) { 
					return true; 
				}

				if (plugin.getPlayers().containsKey(p)) { 
					p.sendMessage(plugin.getManager().sendMessage("ALREADY_PARTICLE_PLAYER")); 
					return true; 
				}

				if (!plugin.getAdvanceManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getManager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				plugin.getAdvanceManager().savePlayerData(p, particle);
				p.sendMessage(plugin.getManager().sendMessage("ACTIVE_PARTICLE_PLAYER").replace("%particle%", particle));
				return true;
			}

			if (args[0].equalsIgnoreCase("delete")) {

				if (!p.hasPermission("advanceparticle.delete")) { 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getManager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAdvanceManager().deleteBlockData(args[1]);
				p.sendMessage(plugin.getManager().sendMessage("BLOCK_PARTICLE_DELETE"));
				return true;
			}

			if (args[0].equalsIgnoreCase("info")) {

				if (!p.hasPermission("advanceparticle.info")) { 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getManager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAdvanceManager().getSpawnerInfo(p, args[1]);
				return true;
			}

			if (args[0].equalsIgnoreCase("tp")) {

				if (!p.hasPermission("advanceparticle.teleport")) { 
					p.sendMessage(plugin.getManager().sendMessage("NOPERM")); 
					return true; 
				}

				if (!plugin.getStream().getBlockStream().containsKey(args[1])) { 
					p.sendMessage(plugin.getManager().sendMessage("ERROR_BLOCK")); 
					return true; 
				}

				plugin.getAdvanceManager().teleportToSpawner(p, args[1]);
				p.sendMessage(plugin.getManager().sendMessage("BLOCK_PARTICLE_TP"));
				return true;
			}

			sendDefaultHelp(p);
		}

		if (args.length == 3) {
			particle = args[2].toUpperCase();

			if (!plugin.getAdvanceManager().checkExistParticle(particle, p)) { 
				return true; 
			}

			if (!plugin.getAdvanceManager().checkEnableParticle(p, particle)) { 
				return true; 
			}

			if (plugin.getStream().getBlockStream().containsKey(args[1])) { 
				p.sendMessage(plugin.getManager().sendMessage("ALREADY_BLOCK_NAME")); 
				return true; 
			}

			if (args[0].equalsIgnoreCase("set")) {

				if (!plugin.getAdvanceManager().checkBlockPerm(p, args[1])) { 
					return true; 
				}

				if (!plugin.getAdvanceManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getManager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				plugin.getAdvanceManager().saveBlockData(p, particle, args[1], p.getLocation().getX(), p.getLocation().getY() - 0.5, p.getLocation().getZ());
				p.sendMessage(plugin.getManager().sendMessage("BLOCK_PARTICLE_SET"));
				return true;
			}

			if (args[0].equalsIgnoreCase("setlooking")) {

				if (plugin.getVersionNumger() < 8) {
					p.sendMessage(plugin.getManager().sendMessage("ERROR_USE_SERVER_COMMAND"));
					return true;
				}

				if (!plugin.getAdvanceManager().checkBlockPerm(p, args[1])) { 
					return true; 
				}

				if (!plugin.getAdvanceManager().checkAllowUseParticle(particle)) {
					p.sendMessage(plugin.getManager().sendMessage("ERROR_USE_SERVER_VERSION"));
					return true;
				}

				Location loc = p.getTargetBlock(null, 50).getLocation();
				plugin.getAdvanceManager().saveBlockData(p, particle, args[1], loc.getX() + 0.5, loc.getY() + 0.5, loc.getZ() + 0.5);
				p.sendMessage(plugin.getManager().sendMessage("BLOCK_PARTICLE_SET"));
				return true;
			}

			sendDefaultHelp(p);
		}

		return false;
	}

	private void sendDefaultHelp(Player p) {
		p.sendMessage("§8§l> §c§lAdvanceParticle §7plugin by: §c§lTixius24");
		p.sendMessage("§8§l> §9§lhttps://www.spigotmc.org/resources/71929/");
		p.sendMessage("§8§l§8§l> §7For more info use §9§l/ap help");
	}

	private void sendHelp(Player p) {
		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
		p.sendMessage("§8> §7Plugin version: §c" + plugin.getDescription().getVersion() + " §7Developed by §cTixius24");
		p.sendMessage("§8> §7Commands help:");
		p.sendMessage("§8> ");
		p.sendMessage("§8> §9/ap help §8- §7Show all commands from plugin");
		p.sendMessage("§8> §9/ap particle §8- §7List of the all particles from plugin");
		p.sendMessage("§8> §9/ap add <particle> §8- §7Set particle effect on the player!");
		p.sendMessage("§8> §9/ap remove §8- §7Remove already activated particle from yourself");

		if (p.hasPermission("advanceparticle.help.admin")) {
			p.sendMessage("§8> ");
			p.sendMessage("§8> §7Commands for administrators:");
			p.sendMessage("§8> §9/ap reload §8- §7Reload plugin messages.yml and config.yml");
			p.sendMessage("§8> §9/ap spawner §8- §7List of the all named particle spawners");
			p.sendMessage("§8> §9/ap set <name> <particle> §8- §7Set particle spawner on the block location");
			p.sendMessage("§8> §9/ap setlooking <name> <particle> §8- §7Set particle spawner on looking block location");
			p.sendMessage("§8> §9/ap info <spawnerName> §8- §7Show more information about the spawner");
			p.sendMessage("§8> §9/ap tp <spawnerName> §8- §7Teleport on the spawner location");
			p.sendMessage("§8> §9/ap delete <spawnerName> §8- §7Delete existed particle spawner from list");
		}

		p.sendMessage("§8> ");
		p.sendMessage("§8=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
	}

}