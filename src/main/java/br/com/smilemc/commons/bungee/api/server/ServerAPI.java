package br.com.smilemc.commons.bungee.api.server;

import br.com.smilemc.commons.bungee.BungeeCommons;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerAPI {
	
	
	public static ServerInfo getServerInfo(String name) {
		return BungeeCommons.getInstance().getProxy().getServerInfo(name);
	}

}
