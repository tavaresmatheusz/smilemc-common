package br.com.smilemc.commons.bungee.ab.connection;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Connection {

	private String address;
	@Setter
	private int numberOfConnectionsInDay;
	@Setter
	private boolean isPremium;
	@Setter
	private int protocol;
	
	public Connection(String address) {
		this.address = address;
	}

}
