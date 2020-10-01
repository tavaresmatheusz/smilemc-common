package br.com.smilemc.commons.common.backend.redis;

import br.com.smilemc.commons.common.Common;
import lombok.Getter;
import redis.clients.jedis.JedisPool;

public class RedisManager {
	@Getter
	JedisPool jedisPool;
	
	public RedisManager() {
		// TODO Auto-generated constructor stub
		Redis redis = Common.getDefaultRedis();
		jedisPool = redis.setupConnection();
	}
	

	
}
