package br.com.smilemc.commons.bungee.ab.antiproxy.controller;

import java.util.HashMap;
import java.util.Map;

import br.com.smilemc.commons.bungee.ab.antiproxy.Proxy;

public class ProxyManager {

	private Map<String, Proxy> proxyMap;

	public ProxyManager() {
		proxyMap = new HashMap<>();
	}

	public void addProxy(String address, Proxy proxy) {
		proxyMap.put(address, proxy);
	}

	public Proxy getProxy(String proxy) {
		return proxyMap.containsKey(proxy) ? proxyMap.get(proxy) : null;
	}

}
