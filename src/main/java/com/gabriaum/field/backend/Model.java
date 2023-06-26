package com.gabriaum.field.backend;

public abstract class Model {

    public abstract void connect();
    public abstract void disconnect();

    public abstract boolean isConnected();
}
