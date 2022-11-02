package net.oasisgames.pexec.tasks;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.oasisgames.pexec.Main;
import net.oasisgames.pexec.config.ConfigConnect;

public class Tasks {
	
	private final YamlConfiguration config;
	private final LuckPerms luckPerms;
	private final boolean msgs;
	private final boolean geyserOrFloodgate;
	
	public Tasks() {
		config = ConfigConnect.getConfig();
		RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
		LuckPerms api;
		if (provider != null) {
			api = LuckPermsProvider.get();
		} else {
			api = null;
		}
		luckPerms = api;
		msgs = config.getBoolean("messagePlayers");
		geyserOrFloodgate = config.getBoolean("geyserOrFloodgate");
	}

	//Store info: Task Name, Perm string
	private	Map<String, String> permMap = new HashMap<String, String>();
	//Store info: Task Name, Add/Remove bool
	private	Map<String, Boolean> addMap = new HashMap<String, Boolean>();
	//Store info: Task Name, Positive/Negative bool
	private	Map<String, Boolean> posMap = new HashMap<String, Boolean>();
	//Store info: Task Name, Floodgate bool
	private	Map<String, Boolean> floodMap = new HashMap<String, Boolean>();
	//Total tasks list
	protected List<String> taskList = new ArrayList<String>();
	
	
	public void createNodes(String section) {
		if (config.getConfigurationSection(section) == null)
			return;
		for (String tasks : config.getConfigurationSection(section).getKeys(false)) {
			boolean groupPerm = config.getBoolean(section + "." + tasks + ".groupPerm");
			String perm = config.getString(section + "." + tasks + ".permission");
			boolean giveOrTake = config.getBoolean(section + "." + tasks + ".giveOrTake");
			boolean positiveOrNegative = config.getBoolean(section + "." + tasks + ".negativeOrPositive");
			boolean floodgate = config.getBoolean(section + "." + tasks + ".floodgate");
			if (groupPerm) {
				//Imediate dispursement of group perms on load
				String group = config.getString(section + "." + tasks + ".group");
				Node node = Node.builder(perm)
						.value(positiveOrNegative)
						.build();
				Group lpGroup = luckPerms.getGroupManager().getGroup(group);
				if (giveOrTake) {
					//ADD TO GROUP
					lpGroup.data().add(node);
				} else {
					//REMOVE FROM GROUP
					lpGroup.data().remove(node);
				}
				luckPerms.getGroupManager().saveGroup(lpGroup);
				Main.msgConsole(tasks + " has been activated for group: " + group  + ".");
			} else {
				permMap.put(tasks, perm);
				addMap.put(tasks, giveOrTake);
				posMap.put(tasks, positiveOrNegative);
				floodMap.put(tasks, floodgate);
				taskList.add(tasks);
				Main.msgConsole(tasks + " has been activated for join.");
			}
		}
	}
	
	private void applyPermChanges(Player player) {
		for (String tasks : taskList) {
			String perm = permMap.get(tasks);
			boolean floodgate = floodMap.get(tasks);
			boolean posNeg = posMap.get(tasks);
			boolean gOrT = addMap.get(tasks);
			Node node = Node.builder(perm)
					.value(posNeg)
					.build();
			User user = luckPerms.getUserManager().getUser(player.getUniqueId());
			if (floodgate) {
				if (geyserOrFloodgate) {
					if (!BedrockCheck.isPlayerBedrockGeyser(player))
						return;
				} else {
					if (!BedrockCheck.isPlayerBedrockFloodgate(player))
						return;
				}
				//FLOODGATE TASKS
				if (gOrT) {
					//ADD PERM
					user.data().add(node);
					if (msgs) {player.sendMessage(ChatColor.GREEN + "[PermissionExecutor] " + ChatColor.GRAY + " Floodgate permission node added: " + node.getKey());}
				} else {
					//REMOVE PERM
					user.data().remove(node);
					if (msgs) {player.sendMessage(ChatColor.GREEN + "[PermissionExecutor] " + ChatColor.GRAY + " Floodgate permission node removed: " + node.getKey());}
				}
			} else {
				//NON FLOODGATE TASKS
				if (gOrT) {
					//ADD PERM
					user.data().add(node);
					if (msgs) {player.sendMessage(ChatColor.GREEN + "[PermissionExecutor] " + ChatColor.GRAY + " permission node added: " + node.getKey());}
				} else {
					//REMOVE PERM
					user.data().remove(node);
					if (msgs) {player.sendMessage(ChatColor.GREEN + "[PermissionExecutor] " + ChatColor.GRAY + " permission node removed: " + node.getKey());}
				}
			}
			luckPerms.getUserManager().saveUser(user);
		}
	}
	
	public void runTasks(Player player) {
		applyPermChanges(player);
	}
}
