package net.oasisgames.pexec;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.oasisgames.pexec.config.ConfigConnect;
import net.oasisgames.pexec.events.JoinEvent;

public class Main extends JavaPlugin {
	
	private JoinEvent join;
	
	private static Main main;
	
	public static Main getInstance() {
		return main;
	}
	
	@Override
	public void onEnable() {
		main = this;
		this.saveDefaultConfig();
		ConfigConnect.loadConfig();
		join = new JoinEvent(this);
		Bukkit.getPluginManager().registerEvents(join, this);
		msgConsole("Enabling plugin!");
	}

	public static void msgConsole(String msg) {
		Bukkit.getLogger().info("[PermissionExecutor] " + msg);
	}
	
}
