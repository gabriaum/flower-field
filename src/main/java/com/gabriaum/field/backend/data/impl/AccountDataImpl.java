package com.gabriaum.field.backend.data.impl;

import com.gabriaum.field.Core;
import com.gabriaum.field.account.Account;
import com.gabriaum.field.backend.data.AccountData;
import com.gabriaum.field.backend.database.mongodb.MongoConnection;
import com.gabriaum.field.backend.database.redis.RedisConnection;
import com.gabriaum.field.util.JsonUtil;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class AccountDataImpl extends AccountData {
    public AccountDataImpl(MongoConnection mongo, RedisConnection redis) {
        super(mongo, redis);
    }

    @Override
    public Account registry(UUID uniqueId, String name) {
        Document found = getCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first();

        if (found != null) {
            Account account = Core.GSON.fromJson(Core.GSON.toJson(found), Account.class);

            try (Jedis jedis = getRedis().getPool().getResource()) {
                if (!jedis.exists("account$" + uniqueId)) {
                    Map<String, String> map = new HashMap<>();
                    JsonObject object = Core.GSON.toJsonTree(account).getAsJsonObject();

                    object.entrySet().forEach(entry -> map.put(entry.getKey(), Core.GSON.toJson(entry.getValue())));

                    jedis.hmset("account$" + uniqueId, map);
                }
            }

            return Core.GSON.fromJson(Core.GSON.toJson(found), Account.class);
        }

        Account account = new Account(uniqueId, name);

        getCollection().insertOne(Document.parse(Core.GSON.toJson(account)));

        try (Jedis jedis = getRedis().getPool().getResource()) {
            Map<String, String> map = new HashMap<>();
            JsonObject object = Core.GSON.toJsonTree(account).getAsJsonObject();

            object.entrySet().forEach(entry -> map.put(entry.getKey(), Core.GSON.toJson(entry.getValue())));

            jedis.hmset("account$" + uniqueId, map);
        }

        return account;
    }

    @Override
    public Account query(UUID uniqueId) {
        try (Jedis jedis = getRedis().getPool().getResource()) {
            if (jedis.exists("account$" + uniqueId.toString())) {
                return Core.GSON.fromJson(Core.GSON.toJson(jedis.get("account$" + uniqueId)), Account.class);
            }
        }

        Document document = getCollection().find(Filters.eq("uniqueId", uniqueId.toString())).first();

        return document != null ? Core.GSON.fromJson(Core.GSON.toJson(document), Account.class) : null;
    }

    @Override
    public void update(Account account, String field) {
        JsonObject object = JsonUtil.jsonTree(account);

        Object value = object.has(field) ? JsonUtil.elementToBson(object.get(field)) : null;
        Document query = getCollection().find(Filters.eq("uniqueId", account.getUniqueId().toString())).first();

        if (query != null) {
            getCollection().updateOne(query, new Document(value != null ? "$set" : "$unset", new Document(field, value)));

            try (Jedis jedis = getRedis().getPool().getResource()) {
                Map<String, String> accountMap = new HashMap<>();

                object.entrySet().forEach(entry -> accountMap.put(entry.getKey(), Core.GSON.toJson(entry.getValue())));

                if (jedis.exists("account$" + account.getUniqueId().toString()))
                    jedis.del("account$" + account.getUniqueId().toString());

                jedis.hmset("account$" + account.getUniqueId().toString(), accountMap);
            }
        }
    }
}
