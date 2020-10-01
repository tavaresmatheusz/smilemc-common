package br.com.smilemc.commons.common.backend.redis;

import lombok.AllArgsConstructor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@AllArgsConstructor
public class Redis {
	
	private String hostname;
	private int port;
	
	
	public JedisPool setupConnection() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(128);
		JedisPool j = new JedisPool(config, hostname, port, 0);
		return j;
	}
	
}
