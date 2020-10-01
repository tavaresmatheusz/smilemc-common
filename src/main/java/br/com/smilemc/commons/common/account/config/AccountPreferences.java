package br.com.smilemc.commons.common.account.config;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
public class AccountPreferences {

	private UUID uuid;

	@Setter
	private boolean receivingTell = true, receivingStaffChatMessages = true, receivingReportMessages = true,
			receivingAntiCheatMessages = true;

	public AccountPreferences(UUID uuid) {
		this.uuid = uuid;
	}

}
