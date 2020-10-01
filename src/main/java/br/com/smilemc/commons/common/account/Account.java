package br.com.smilemc.commons.common.account;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.com.smilemc.commons.common.account.config.AccountPreferences;
import br.com.smilemc.commons.common.account.config.AccountPunishments;
import br.com.smilemc.commons.common.account.permissions.Group;
import br.com.smilemc.commons.common.account.permissions.group.PermissionSetter;
import br.com.smilemc.commons.common.account.status.League;
import br.com.smilemc.commons.common.account.status.Status;
import br.com.smilemc.commons.common.clan.Clan;
import lombok.Getter;
import lombok.Setter;

@Getter
public abstract class Account {

	private UUID uuid;
	@Setter
	protected String name;
	
	private String permissionList;
	@Setter
	private int xp;
	@Setter
	private int coins;
	@Setter
	private Group group;
	@Setter
	private League league;
	@Setter
	private long groupExpireIn;
	private List<Group> secondaryGroups;
	
	private AccountPreferences accountPreferences;
	private AccountPunishments accountPunishments;
	private Clan clan;
	private Status kitpvp, hg;
	private PermissionSetter permissionSetter;

	public Account(PlayerAccount playerAccount) {

		this.uuid = playerAccount.getUuid();
		this.permissionList = playerAccount.getPermissionList();
		
		this.xp = playerAccount.getXp();

		this.group = playerAccount.getGroup();
		this.league = playerAccount.getLeague();

		this.groupExpireIn = playerAccount.getGroupExpireIn();

		this.accountPreferences = playerAccount.getAccountPreferences();
		this.accountPunishments = playerAccount.getAccountPunishments();
		this.clan = playerAccount.getClan();

		this.kitpvp = playerAccount.getPvpStatus();
		this.hg = playerAccount.getHgStatus();
		this.permissionSetter = new PermissionSetter(this);
		this.secondaryGroups = new ArrayList<>();
	}

	public boolean hasGroupPermission(Group group) {
		return this.group.ordinal() >= group.ordinal();
	}

}
