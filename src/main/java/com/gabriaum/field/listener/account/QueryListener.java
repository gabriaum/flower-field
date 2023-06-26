package com.gabriaum.field.listener.account;

import com.gabriaum.field.Core;
import com.gabriaum.field.account.Account;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class QueryListener implements Listener {

    @EventHandler
    public void registry(AsyncPlayerPreLoginEvent event) {
        UUID uniqueId = event.getUniqueId();
        String name = event.getName();

        try {
            Account account = Core.getAccountData().query(uniqueId);

            if (account == null)
                account = Core.getAccountData().registry(uniqueId, name);
        } catch (Exception ex) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "");
            ex.printStackTrace();
        }
    }
}
