package net.oasisgames.pexec.tasks;

import org.bukkit.entity.Player;
import org.geysermc.connector.GeyserConnector;
import org.geysermc.connector.network.session.GeyserSession;
import org.geysermc.floodgate.api.FloodgateApi;

@SuppressWarnings("deprecation")
public class BedrockCheck {

	public static boolean isPlayerBedrockFloodgate(Player player) {
		return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
	}
	
	@Deprecated
	public static boolean isPlayerBedrockGeyser(Player player) {
		GeyserSession session = GeyserConnector.getInstance().getPlayerByUuid(player.getUniqueId());
		if (session == null)
			return false;
		
		return !session.isClosed();
	}
	
}
