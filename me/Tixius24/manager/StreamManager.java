package me.Tixius24.manager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Tixius24.AdvanceParticle;
import me.Tixius24.object.BlockObject;

public class StreamManager {
	private AdvanceParticle plugin;

	private HashMap<String, String> playerData = new HashMap<String, String>();
	private HashMap<String, BlockObject> blockData = new HashMap<String, BlockObject>();

	public StreamManager(AdvanceParticle pl) {
		plugin = pl;
		loadPlayerData();
		loadBlockData();
	}

	public HashMap<String, String> getPlayerStream() {
		return playerData;
	}

	public HashMap<String, BlockObject> getBlockStream() {
		return blockData;
	}

	@SuppressWarnings("unchecked")
	private void loadPlayerData() {
		try {
			FileInputStream in = new FileInputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/player.db");
			ObjectInputStream str = new ObjectInputStream(in);
			playerData = (HashMap<String, String>) str.readObject();
			str.close();
			in.close();
		} catch (Exception ex) {
			// If is plugin first running on the server player.db file is missing, here is code on the default generating file

			try {
				FileOutputStream out = new FileOutputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/player.db");
				ObjectOutputStream str = new ObjectOutputStream(out);
				str.writeObject(playerData);
				str.close();
				out.close();
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

		}

	}

	@SuppressWarnings("unchecked")
	private void loadBlockData() {
		try {
			FileInputStream in = new FileInputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/block.db");
			ObjectInputStream str = new ObjectInputStream(in);
			blockData =  (HashMap<String, BlockObject>) str.readObject();
			str.close();
			in.close();
		} catch (Exception ex) {
			// If is plugin first running on the server block.db file is missing, here is code on the default generating file

			try {
				FileOutputStream out = new FileOutputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/block.db");
				ObjectOutputStream str = new ObjectOutputStream(out);
				str.writeObject(blockData);
				str.close();
				out.close();
			} catch (Exception ex1) {
				ex1.printStackTrace();
			}

		}

	}

	public void deleteSpawnerData(String spawnerName) {
		getBlockStream().remove(spawnerName);
		saveBlockFile();
	}

	public void savePlayerData(Player p, String particle) {
		try {
			playerData.put(p.getName(), particle);
			FileOutputStream out = new FileOutputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/player.db");
			ObjectOutputStream str = new ObjectOutputStream(out);
			str.writeObject(playerData);
			str.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void saveBlockData(Player p , String particle, String spawner, double x, double y, double z) {
		BlockObject object = new BlockObject(particle, p.getWorld().getName(), x, y,z);
		blockData.put(spawner, object);
		saveBlockFile();
	}

	private void saveBlockFile() {
		try {
			FileOutputStream out = new FileOutputStream(plugin.getDataFolder().getAbsolutePath().toString() + "/data/block.db");
			ObjectOutputStream str = new ObjectOutputStream(out);
			str.writeObject(blockData);
			str.close();
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}