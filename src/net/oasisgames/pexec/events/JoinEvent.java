package net.oasisgames.pexec.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import net.oasisgames.pexec.Main;
import net.oasisgames.pexec.tasks.Tasks;

public class JoinEvent implements Listener {

	Main plugin;
	final Tasks tasks;
	public JoinEvent(Main main) {
		plugin = main;
		tasks = new Tasks();
		tasks.createNodes("tasks");
	}
	
	@EventHandler
	public void onJoinLoadPermission(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		tasks.runTasks(player);
	}
	
}
