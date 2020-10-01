package br.com.smilemc.commons.common.punishments;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Punish {
	
	private UUID uuid;
	private String author, reason;
	private long time;
	private PunishType punishType;
	private boolean active = false;
	
	public enum PunishType {
		BAN, MUTE;
	}
	
	
}
