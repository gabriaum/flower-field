package com.gabriaum.field.listener.main;

import com.gabriaum.field.Core;
import com.gabriaum.field.Field;
import com.gabriaum.field.account.Account;
import com.gabriaum.field.account.inventory.ShearInventory;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

import static net.md_5.bungee.api.ChatColor.*;

public class StructureListener implements Listener {

    @EventHandler
    public void blockBreak(BlockBreakEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        Account account = Core.getAccountData().query(player.getUniqueId());

        Block block = event.getBlock();

        if (block.getType().equals(Material.RED_ROSE) || block.getType().equals(Material.YELLOW_FLOWER)) {
            if (!player.getItemInHand().getType().equals(Material.SHEARS)) {
                player.sendMessage(RED + "Equipe a tesoura para coletar as flores!");
                return;
            }

            block.setType(Material.AIR);

            double normalValue = account.getShearLevel().getEarnings();
            String pvpValue = Double.toString((normalValue + ((normalValue * 30) / 100)));

            int
                    firstDigits = Integer.parseInt(pvpValue.substring(0, pvpValue.indexOf('.'))),
                    decimalDigit = Character.getNumericValue(pvpValue.charAt(pvpValue.indexOf('.') + 1));

            ArmorStand stand = block.getWorld().spawn(block.getLocation(), ArmorStand.class);

            stand.setVisible(false);
            stand.setMarker(true);
            stand.setCustomName(GREEN + "+" + (Field.getPlugin(Field.class).getPlayersPvPActive().contains(account.getUniqueId()) ? firstDigits + "." + decimalDigit : normalValue));
            stand.setCustomNameVisible(true);
            stand.setGravity(false);

            Bukkit.getScheduler().runTaskLater(Field.getPlugin(Field.class), () -> {
                block.setType(new Random().nextBoolean() ? Material.RED_ROSE : Material.YELLOW_FLOWER);

                stand.remove();
            }, 30);

            Bukkit.getScheduler().runTaskTimer(Field.getPlugin(Field.class), () -> {
                stand.teleport(stand.getLocation().add(0.0, 0.2, 0.0));

                if (player.getLocation().distanceSquared(stand.getLocation()) <= 25)
                    stand.setCustomNameVisible(true);
                else
                    stand.setCustomNameVisible(false);
            }, 0, 1);

            account.addFlower();
            player.getScoreboard().getTeam("flowers").setSuffix(" " + GREEN + account.formatToString());
        }
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_")) {
            Player player = event.getPlayer();
            Account account = Core.getAccountData().query(player.getUniqueId());

            ItemStack stack = event.getItem();

            if (stack.getType().equals(Material.AIR))
                return;

            if (stack.getType().equals(Material.SHEARS))
                new ShearInventory(account).handle(player);
        }
    }

    @EventHandler
    public void drop(PlayerDropItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void damage(EntityDamageEvent event) {
        Player player = (Player) event.getEntity();

        event.setCancelled(!Field.getPlugin(Field.class).getPlayersPvPActive().contains(player.getUniqueId()));
    }
}
