package br.com.smilemc.commons.bungee.ab.connection.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.bungee.ab.connection.Connection;

public class ConnectionManager {

	private Map<String, Connection> connectionMap;

	public ConnectionManager() {
		connectionMap = new HashMap<>();
	}

	public void addConnection(Connection connection) {
		connectionMap.put(connection.getAddress(), connection);
	}

	public Connection getConnection(String proxy) {
		return connectionMap.containsKey(proxy) ? connectionMap.get(proxy) : null;
	}
	
	public Collection<Connection> getConnections() {
		return connectionMap.values();
	}
	
	
}
