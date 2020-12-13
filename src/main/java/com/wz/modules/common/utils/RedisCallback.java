package com.wz.modules.common.utils;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {
	public T call(Jedis jedis, Object params);
}
