package br.com.smilemc.commons.bungee.account;

import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.server.Server;
import lombok.Getter;
import lombok.Setter;

@Getter
public class BungeeAccount extends Account {

	private UUID uuid;
	@Setter
	private boolean inStaffChat = false;
	@Setter
	private Server server;
	
	public BungeeAccount(String name) {
		super(Common.getAccountManager().getPlayerAccount(name));
		this.uuid = Common.getUuid(name);
		setName(name);
	}

}
