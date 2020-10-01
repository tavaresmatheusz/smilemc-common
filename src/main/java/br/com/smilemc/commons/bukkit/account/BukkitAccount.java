package br.com.smilemc.commons.bukkit.account;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;

import br.com.smilemc.commons.bukkit.BukkitCommons;
import br.com.smilemc.commons.bukkit.api.player.AdminMode;
import br.com.smilemc.commons.bukkit.api.skin.Skin;
import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.PlayerAccount;
import br.com.smilemc.commons.common.account.scoreboard.Medal;
import br.com.smilemc.commons.common.account.scoreboard.Tag;
import br.com.smilemc.commons.common.account.status.League;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BukkitAccount extends Account {

	@Getter
	@Deprecated
	private UUID uuid;

	@Setter
	private Tag tag;
	@Setter
	private Medal medal;
	@Setter
	private League league;

	private AdminMode adminMode;
	private PermissionAttachment permissionAttachment;
	@Setter
	private boolean usingFake = false;
	@Setter
	private String fakeName = "";
	@Setter
	private Skin defaultSkin;

	@Deprecated
	public BukkitAccount(PlayerAccount playerAccount) {
		super(playerAccount);

	}

	public String getRealName() {
		return usingFake ? name : fakeName;
	}

	public BukkitAccount(String name) {
		super(Common.getAccountManager().getPlayerAccount(name));
		this.uuid = Common.getUuid(name);
		setName(name);
		adminMode = new AdminMode(this);
		if (Common.getBackendManager().getSqlManager().getString("skins", uuid, "skinName") != null)
			return;
		defaultSkin = new Skin(
				Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(name), "skinName"),
				UUID.fromString(Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(name),
						"skinUuid")));

	}

	public Skin getDefaultSkin() {
		if (defaultSkin == null)
			defaultSkin = new Skin(
					Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(name), "skinName"),
					UUID.fromString(Common.getBackendManager().getSqlManager().getString("skins", Common.getUuid(name),
							"skinUuid")));
		return defaultSkin;
	}

	public void permissionSetup(Player player) {

		permissionAttachment = player.addAttachment(BukkitCommons.getInstance());

		for (String permission : getPermissionSetter().getPerms())
			permissionAttachment.setPermission(permission, true);

		for (String permission : getPermissionList().split(";"))
			permissionAttachment.setPermission(permission, true);

		player.recalculatePermissions();

	}

}
