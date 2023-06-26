package com.gabriaum.field.backend.database.redis;

import com.gabriaum.field.backend.Model;
import lombok.Getter;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

@Getter
public class RedisConnection extends Model {

    private JedisPool pool;

    @Override
    public void connect() {
        if (isConnected())
            return;

        try {
            JedisPoolConfig config = new JedisPoolConfig();

            config.setMaxTotal(128);

            pool = new JedisPool(config, "127.0.0.1", 6379);
        } catch (JedisException ex) {
            throw new RuntimeException("> There was a problem connecting to Redis.", ex);
        }
    }

    @Override
    public void disconnect() {
        if (!isConnected())
            return;

        pool.destroy();
    }

    @Override
    public boolean isConnected() {
        return pool != null;
    }
}
