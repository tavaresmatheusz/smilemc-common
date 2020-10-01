package br.com.smilemc.commons.common.punishments;

public abstract class Punishment {
	
	public abstract void punish(String author, String reason, long time);
	public abstract void unpunish();
	
	
}
