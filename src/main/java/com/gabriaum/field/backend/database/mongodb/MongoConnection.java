package com.gabriaum.field.backend.database.mongodb;

import com.gabriaum.field.backend.Model;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;

@Getter
public class MongoConnection extends Model {

    private MongoClient client;
    private MongoDatabase database;


    @Override
    public void connect() {
        if (isConnected())
            return;

        try {
            client = new MongoClient("127.0.0.1");
            database = client.getDatabase("gabriaum");
        } catch (MongoException ex) {
            throw new RuntimeException("> There was a problem connecting to MongoDB.", ex);
        }
    }

    @Override
    public void disconnect() {
        if (!isConnected())
            return;

        client.close();
    }

    @Override
    public boolean isConnected() {
        return client != null;
    }
}
