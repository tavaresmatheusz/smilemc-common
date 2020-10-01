package br.com.smilemc.commons.common.account.controller;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.smilemc.commons.common.Common;
import br.com.smilemc.commons.common.account.Account;
import br.com.smilemc.commons.common.account.PlayerAccount;
import br.com.smilemc.commons.common.account.loader.AccountLoader;

public class AccountManager {

	Map<UUID, Account> accountMap;
	AccountLoader accountLoader;

	public AccountManager() {
		accountMap = new HashMap<>();
		accountLoader = new AccountLoader();
	}

	public Collection<Account> getAccounts() {
		return accountMap.values();
	}

	public Account registerAccount(UUID uuid, Account account) {
		return accountMap.put(uuid, account);
	}


	public Account getAccount(UUID uuid) {

		return accountMap.get(uuid);
	}

	public void unregisterAccount(UUID uuid) {
		accountMap.remove(uuid);
	}

	public PlayerAccount getPlayerAccount(String name) {
		return accountLoader.getPlayerAccount(Common.getUuid(name));
	}
}
