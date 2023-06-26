package com.gabriaum.field.account.inventory;

import com.gabriaum.field.account.Account;
import com.gabriaum.field.account.inventory.type.LevelType;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

import static net.md_5.bungee.api.ChatColor.*;

@RequiredArgsConstructor
public class ShearInventory {

    private final Account account;

    public void handle(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 3, "Tesoura");

        inventory.clear();

        /* Information */
        ItemStack shearInfo = new ItemStack(Material.SHEARS);
        ItemMeta shearInfoMeta = shearInfo.getItemMeta();

        shearInfoMeta.setDisplayName(GREEN + "Informações");
        shearInfoMeta.setLore(Arrays.asList(
                WHITE + "Nível: " + account.getShearLevel().getLevel(),
                WHITE + "Ganhos: " + GREEN + account.getShearLevel().getEarnings()));

        shearInfo.setItemMeta(shearInfoMeta);

        inventory.setItem(12, shearInfo);

        /* Upgrade */
        ItemStack shearUpgrade = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) (account.getShearLevel().equals(LevelType.TEN) ? 14 : 5));
        ItemMeta shearUpgradeMeta = shearUpgrade.getItemMeta();

        shearUpgradeMeta.setDisplayName(GREEN + "Evoluir");
        shearUpgradeMeta.setLore(Arrays.asList("",
                WHITE + "Próximo nível: " + YELLOW + (account.getShearLevel().equals(LevelType.TEN) ? RED + "Nenhum" : account.nextShearLevel().getLevel()),
                WHITE + "Custo: " + YELLOW + (account.getShearLevel().equals(LevelType.TEN) ? RED + "Nenhum" : account.nextShearLevel().getPrice() + " flores"),
                "",
                (account.getShearLevel().equals(LevelType.TEN) ? RED + "Você está no último nível." : GREEN + "Clique para evoluir.")
        ));

        shearUpgrade.setItemMeta(shearUpgradeMeta);

        inventory.setItem(14, shearUpgrade);

        player.openInventory(inventory);
    }
}
