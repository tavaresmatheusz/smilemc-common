package br.com.smilemc.commons.common.server.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.server.Server;
import redis.clients.jedis.Jedis;

public class ServerManager {

	private Map<String, Server> serverMap;

	public ServerManager() {
		serverMap = new HashMap<>();
	}

	public void newServer(Server server) {
		serverMap.put(server.getName(), server);
	}

	public Collection<Server> getServers() {
		return serverMap.values();
	}

	public Server getServer(String serverName) {
		return serverMap.get(serverName) == null ? null : serverMap.get(serverName);
	}

	public int getOnlinePlayers() {

		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
		int onlines = Integer.valueOf(jedis.get(Common.ONLINE_FIELD + "ALL"));
		jedis.close();
		return onlines;
	}

	public void allUpdate() {
		Jedis jedis = Common.getBackendManager().getRedisManager().getJedisPool().getResource();
		for (Server server : serverMap.values()) {

			if (!jedis.exists(Common.ONLINE_FIELD + server.getName()))
				return;
			
			int onlines = Integer.valueOf(jedis.get(Common.ONLINE_FIELD + server.getName()));

			server.setOnline(onlines);

		}
		jedis.close();
	}
}
