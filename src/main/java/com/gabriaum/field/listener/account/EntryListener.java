package com.gabriaum.field.listener.account;

import com.gabriaum.field.Core;
import com.gabriaum.field.account.Account;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scoreboard.*;

import static net.md_5.bungee.api.ChatColor.*;

public class EntryListener implements Listener {

    @EventHandler
    public void entry(PlayerJoinEvent event) {
        event.setJoinMessage(null);

        Player player = event.getPlayer();

        handleScoreboard(player);
        handleStack(player);
    }

    protected void handleScoreboard(Player player) {
        Account account = Core.getAccountData().query(player.getUniqueId());

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(player.getName(), "dummy");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(String.valueOf(GOLD) + BOLD + "CAMPO");
        objective.getScore(RESET + " ").setScore(4);

        Team flowers = scoreboard.registerNewTeam("flowers");

        flowers.addEntry(String.valueOf(WHITE));
        flowers.setPrefix(WHITE + "Flores:");
        flowers.setSuffix(" " + GREEN + account.formatToString());

        objective.getScore(String.valueOf(WHITE)).setScore(3);
        objective.getScore(" ").setScore(2);

        objective.getScore(YELLOW + "www.gabriaum.com").setScore(1);

        player.setScoreboard(scoreboard);
    }

    protected void handleStack(Player player) {
        PlayerInventory inventory = player.getInventory();

        inventory.setItem(0, new ItemStack(Material.SHEARS));
    }
}
