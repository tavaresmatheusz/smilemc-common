package br.com.smilemc.commons.bukkit.antihax.player;

import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.bukkit.antihax.hacks.Cheat;

public class HackPlayer {

	private Map<String, Cheat> cheats;
	
	public HackPlayer() {
		// TODO Auto-generated constructor stub
		cheats = new HashMap<>();
	}
	
	public Cheat getCheat(Cheat cheat) {
		return cheats.get(cheat.getHackClass().getName());
	}
	
	public void addCheat(Cheat cheat) {
		cheats.put(cheat.getHackClass().getName(), cheat);
	}
	
}
