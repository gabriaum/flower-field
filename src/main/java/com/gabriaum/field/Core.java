package com.gabriaum.field;

import com.gabriaum.field.backend.data.impl.AccountDataImpl;
import com.gabriaum.field.backend.database.mongodb.MongoConnection;
import com.gabriaum.field.backend.database.redis.RedisConnection;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;

public class Core {

    public static final Gson GSON = new Gson();
    public static final JsonParser JSON = new JsonParser();

    @Getter
    @Setter
    protected static MongoConnection mongoConnection;

    @Getter
    @Setter
    protected static RedisConnection redisConnection;

    @Getter
    @Setter
    protected static AccountDataImpl accountData;

    public static void initialize() {
        setMongoConnection(new MongoConnection());
        setRedisConnection(new RedisConnection());

        getMongoConnection().connect();
        getRedisConnection().connect();

        setAccountData(new AccountDataImpl(getMongoConnection(), getRedisConnection()));
    }
}
