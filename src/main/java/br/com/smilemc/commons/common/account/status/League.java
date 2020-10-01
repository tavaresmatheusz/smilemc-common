package br.com.smilemc.commons.common.account.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum League {
	
	UNRANKED("Unranked", "-", "§f", 0);
		
	String name, symbol, color;
	int minXp;
	
	public static League getLeagueByName(String name) {
		
		for (League v : League.values())
			if (v.getName().equalsIgnoreCase(name))
				return v;

		return null;
	}

	public static League getLeagueByOrdinal(int ordinal) {
		
		for (League v : League.values())
			if (v.ordinal() == ordinal)
				return v;

		return null;
	}
	
}
