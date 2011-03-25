package com.herocraftonline.dev.heroes;

import java.util.logging.Level;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.herocraftonline.dev.heroes.persistance.HeroManager;

@SuppressWarnings("unused")
public class HPlayerListener extends PlayerListener {
    private final Heroes plugin;

    public HPlayerListener(Heroes instance) {
        plugin = instance;
    }

    @Override
    public void onPlayerLogin(PlayerLoginEvent e) {
        HeroManager heroManager = plugin.getHeroManager();
        if (heroManager.checkPlayer(e.getPlayer().getName()) == false) {
            heroManager.newPlayer(e.getPlayer());
            plugin.log(Level.INFO, "Created");
        } else {
            plugin.log(Level.INFO, "Player Found");
        }
    }

}