package br.com.smilemc.commons.common.server;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Server {

	private String name;
	@Setter
	private int online, maxPlayers;
	@Setter
	private ServerStage serverStage;
	
	
	public Server(String name, int maxPlayers) {
		// TODO Auto-generated constructor stub
		this.name = name;
		this.maxPlayers = maxPlayers;
		this.online = 0;
		this.serverStage = ServerStage.NONE;
	}

	
	
	}
