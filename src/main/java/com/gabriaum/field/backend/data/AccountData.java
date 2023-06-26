package com.gabriaum.field.backend.data;

import com.gabriaum.field.account.Account;
import com.gabriaum.field.backend.database.mongodb.MongoConnection;
import com.gabriaum.field.backend.database.redis.RedisConnection;
import com.mongodb.client.MongoCollection;
import lombok.Getter;
import org.bson.Document;

import java.util.UUID;

@Getter
public abstract class AccountData {

    private final MongoCollection<Document> collection;
    private final RedisConnection redis;

    public AccountData(MongoConnection mongo, RedisConnection redis) {
        collection = mongo.getDatabase().getCollection("accounts");

        this.redis = redis;
    }

    public abstract Account registry(UUID uniqueId, String name);

    public abstract Account query(UUID uniqueId);

    public abstract void update(Account account, String field);
}
