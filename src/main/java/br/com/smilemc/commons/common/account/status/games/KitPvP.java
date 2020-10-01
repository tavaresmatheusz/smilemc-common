package br.com.smilemc.commons.common.account.status.games;

import java.util.UUID;

import br.com.smilemc.commons.common.account.status.Status;

public class KitPvP extends Status {


	public KitPvP(UUID uuid) {
		super(uuid, StatusType.PVP);
	}

	public void addKills(int kills) {
		this.kills += kills;
		update("kills", kills);
	}

	public void addDeaths(int deaths) {
		this.deaths += deaths;
		update("deaths", deaths);
	}
	
	public void addKillStreak(int ks) {
		this.killStreak = ks;
		update("killStreak", killStreak);
	}
	
	public void removeKillStreak() {
		if (killStreak > beastKs) {
			beastKs = killStreak;
			update("beastKs", beastKs);
			
		}
		
		this.killStreak = 0;
		update("killStreak", killStreak);
	}

}
