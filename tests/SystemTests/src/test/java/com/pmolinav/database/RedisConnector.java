package com.pmolinav.database;

import redis.clients.jedis.Jedis;

public class RedisConnector {

    private static final String HOST = System.getenv().getOrDefault("REDIS_HOST", "localhost");
    private static final int PORT = Integer.parseInt(System.getenv().getOrDefault("REDIS_PORT", "6379"));
    private static final String PASSWORD = System.getenv().getOrDefault("REDIS_PASSWORD", "");

    private Jedis jedis;

    public RedisConnector() {
        connect();
    }

    private void connect() {
        try {
            jedis = new Jedis(HOST, PORT);
            if (PASSWORD != null && !PASSWORD.isBlank()) {
                jedis.auth(PASSWORD);
            }
            // Test Redis connection.
            String pong = jedis.ping();
            if (!"PONG".equalsIgnoreCase(pong)) {
                throw new IllegalStateException("Unexpected response from Redis: " + pong);
            }
            System.out.println("Redis connection established successfully to " + HOST + ":" + PORT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Unexpected error while trying to connect to Redis.", e);
        }
    }

    public String getValue(String key) {
        return jedis.get(key);
    }

    public void setValue(String key, String value) {
        jedis.set(key, value);
    }

    public void deleteKey(String key) {
        jedis.del(key);
    }

    public void deleteAll() {
        jedis.flushAll();
    }

    public boolean keyExists(String key) {
        return jedis.exists(key);
    }

    public void close() {
        if (jedis != null) {
            jedis.close();
        }
    }
}