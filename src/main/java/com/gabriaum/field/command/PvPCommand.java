package com.gabriaum.field.command;

import com.gabriaum.field.Field;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

import static net.md_5.bungee.api.ChatColor.*;

public class PvPCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (Field.getPlugin(Field.class).getCooldown().asMap().containsKey(player.getUniqueId())) {
            long duration = Field.getPlugin(Field.class).getCooldown().asMap().get(player.getUniqueId()) - System.currentTimeMillis();

            player.sendMessage(RED + "Aguarde " + TimeUnit.MILLISECONDS.toSeconds(duration) + " segundos para usar isto novamente.");
            return false;
        }

        Field.getPlugin(Field.class).getCooldown().put(player.getUniqueId(), System.currentTimeMillis() + 30000);

        if (Field.getPlugin(Field.class).getPlayersPvPActive().contains(player.getUniqueId())) {
            Field.getPlugin(Field.class).getPlayersPvPActive().remove(player.getUniqueId());

            player.sendMessage(RED + "Você saiu do modo PvP!");
            return true;
        }

        Field.getPlugin(Field.class).getPlayersPvPActive().add(player.getUniqueId());

        player.sendMessage(GREEN + "Você entrou do modo PvP!");
        return false;
    }
}
