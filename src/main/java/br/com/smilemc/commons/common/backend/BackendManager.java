package br.com.smilemc.commons.common.backend;

import br.com.smilemc.commons.common.backend.redis.RedisManager;
import br.com.smilemc.commons.common.backend.sql.SqlManager;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BackendManager {
	
	
	private SqlManager sqlManager;
	private RedisManager redisManager;

}
