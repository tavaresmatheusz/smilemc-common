package br.com.smilemc.commons.common.account.scoreboard;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Medal {

	TOXIC("Toxic", "-", "a");
	
	private String name, symbol, color;
	
	public static Medal getMedalByName(String name) {
		for (Medal medal : Medal.values())
			if (medal.getName().equalsIgnoreCase(name))
				return medal;
		return null;
	}
	
}
