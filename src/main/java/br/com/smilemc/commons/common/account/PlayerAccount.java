package br.com.smilemc.commons.common.account;

import java.util.UUID;

import br.com.smilemc.commons.common.account.config.AccountPreferences;
import br.com.smilemc.commons.common.account.config.AccountPunishments;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.status.League;
import br.com.smilemc.commons.common.account.status.Status;
import br.com.smilemc.commons.common.clan.Clan;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlayerAccount {

	private UUID uuid;
	private String permissionList;
	
	private int xp;
	private int coins;

	private Group group;
	private League league;

	private long groupExpireIn;

	private AccountPreferences accountPreferences;
	private AccountPunishments accountPunishments;
	private Clan clan;
	private Status pvpStatus;
	private Status hgStatus;
	
	
	

}
