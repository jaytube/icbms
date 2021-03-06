package com.wz.config;

import org.apache.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 类RedisConfig的功能描述: Redis配置
 * 
 * @auther hxy
 * @date 2017-11-15 21:49:31
 */
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix = "spring.redis")
public class RedisConfig {
	private static Logger logger = Logger.getLogger(RedisConfig.class);

	private String hostName;

	private int port;

	private String password;

	private int timeout;

	@Bean
	public JedisPoolConfig getRedisConfig() {
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxIdle(100);
		config.setMaxWaitMillis(2000);
		config.setMaxTotal(500);
		return config;
	}

	@Bean
	public JedisPool getJedisPool(JedisPoolConfig redisConfig) {
		JedisPool pool = new JedisPool(redisConfig, hostName, port, timeout, password);
		logger.info("init JredisPool ...");
		return pool;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

}
