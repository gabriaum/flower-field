package com.gabriaum.field;

import com.gabriaum.field.command.PvPCommand;
import com.gabriaum.field.listener.account.EntryListener;
import com.gabriaum.field.listener.account.QueryListener;
import com.gabriaum.field.listener.main.InventoryListener;
import com.gabriaum.field.listener.main.StructureListener;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public class Field extends JavaPlugin {

    protected List<UUID> playersPvPActive;
    protected Cache<UUID, Long> cooldown;

    @Override
    public void onLoad() {
        Core.initialize();
    }

    @Override
    public void onEnable() {
        playersPvPActive = new ArrayList<>();
        cooldown = CacheBuilder.newBuilder().expireAfterWrite(30, TimeUnit.SECONDS).build();

        getServer().getPluginManager().registerEvents(new QueryListener(), this);
        getServer().getPluginManager().registerEvents(new EntryListener(), this);
        getServer().getPluginManager().registerEvents(new StructureListener(), this);
        getServer().getPluginManager().registerEvents(new InventoryListener(), this);

        getServer().getPluginCommand("pvp").setExecutor(new PvPCommand());
    }
}
