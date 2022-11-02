package net.oasisgames.pexec.config;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import net.oasisgames.pexec.Main;

public class ConfigConnect {
	
	private static Main plugin = Main.getInstance();
	
	private static YamlConfiguration config;
	
	public static YamlConfiguration getConfig() {
		return config;
	}
	
	public static void loadConfig() {
		File file = new File(plugin.getDataFolder().getAbsolutePath() + "/config.yml");
		config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
		} catch (IOException e) {
			Bukkit.getLogger().info("[WARNING] Critical configuration error within plugin!");
		}
	}
	
	public void reloadConfig() {
		loadConfig();
	}

}
