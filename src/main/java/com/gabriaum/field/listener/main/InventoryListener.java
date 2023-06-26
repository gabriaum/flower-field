package com.gabriaum.field.listener.main;

import com.gabriaum.field.Core;
import com.gabriaum.field.account.Account;
import com.gabriaum.field.account.inventory.type.LevelType;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import static net.md_5.bungee.api.ChatColor.*;

public class InventoryListener implements Listener {

    @EventHandler
    public void shear(InventoryClickEvent event) {
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        Account account = Core.getAccountData().query(player.getUniqueId());

        Inventory inventory = event.getClickedInventory();

        if (inventory.getTitle().equals("Tesoura")) {
            Material stack = event.getCurrentItem().getType();

            if (stack.equals(Material.AIR))
                return;

            if (stack.equals(Material.STAINED_GLASS_PANE) && !account.getShearLevel().equals(LevelType.TEN)) {
                player.closeInventory();

                if (account.getFlowers() < account.nextShearLevel().getPrice()) {
                    player.sendMessage(RED + "Você não possui flores o suficiente para evoluir sua tesoura!");

                    player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1, 1);
                    return;
                }

                account.upgradeShearLevel();

                player.sendMessage(YELLOW + "Você evoluiu sua tesoura para o nível " + WHITE + account.getShearLevel().getLevel() + YELLOW + " com sucesso!");
                player.getScoreboard().getTeam("flowers").setSuffix(" " + GREEN + account.formatToString());

                player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
            }
        }
    }
}
