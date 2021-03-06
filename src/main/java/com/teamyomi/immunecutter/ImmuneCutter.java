package com.teamyomi.immunecutter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class ImmuneCutter extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        generateDefaultConfiguration();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onStoneCutterDamage(EntityDamageEvent e) {
        if (e.getCause().equals(EntityDamageEvent.DamageCause.CONTACT) && e.getEntity().isOnGround() && checkList(e.getEntity().getType())) {
            Location loc = e.getEntity().getLocation().clone().subtract(0, 0.35, 0);
            var b = loc.getBlock();
            if (b.getType().equals(Material.STONECUTTER)) {
                e.setCancelled(true);
            }
        }
    }

    private boolean checkList(EntityType e) {
        String s = "stonecutter-damage." + e.toString();
        return !getConfig().getBoolean(s);
    }

    private void generateDefaultConfiguration() {
        FileConfiguration config = getConfig();
        config.options().header("Change true to false if you want that type of entity immune to stonecutter!");
        for (EntityType e : EntityType.values()) {
            if (!e.isAlive()) { continue; }
            //noinspection UnnecessaryToStringCall
            String path = "stonecutter-damage." + e.toString(); // .toString() is necessary! fuck you IDE
            if (!config.contains(path)) {
                config.set(path, true);
            }
        }
        saveConfig();
        reloadConfig();
    }
}
