package br.com.smilemc.commons.bukkit.antihax;

import lombok.Getter;
import lombok.Setter;

public abstract class Hack {
	
	@Setter
	@Getter
	private String name;
	@Setter
	@Getter
	private boolean autoBanIsEnabled;
	@Setter
	@Getter
	private long banTime;
}
