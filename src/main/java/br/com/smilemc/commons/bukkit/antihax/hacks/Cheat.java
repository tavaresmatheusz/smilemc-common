package br.com.smilemc.commons.bukkit.antihax.hacks;

import br.com.smilemc.commons.bukkit.antihax.Hack;
import lombok.Getter;

@Getter
public class Cheat {
	
	private Hack hackClass;
	private int warns = 0;

	public void addWarn() {
		warns++;
	}
	
}
