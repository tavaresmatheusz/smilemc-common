package br.com.smilemc.commons.common.account.config;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.smilemc.commons.common.punishments.Punish;
import br.com.smilemc.commons.common.punishments.Punish.PunishType;
import lombok.Getter;


@Getter
public class AccountPunishments {

	private UUID uuid;

	private List<Punish> punishments;

	private boolean muted = false, banned = false;
	
	public AccountPunishments(UUID uuid) {
		this.uuid = uuid;
		punishments = new ArrayList<>();
	}

	public void addPunishment(Punish punish) {
		punishments.add(punish);
		
		if (punish.isActive())
			if (punish.getPunishType() == PunishType.BAN)
				banned = true;
			else
				muted = true;
		
	}

}
