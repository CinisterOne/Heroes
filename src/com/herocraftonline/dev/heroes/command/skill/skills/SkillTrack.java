package com.herocraftonline.dev.heroes.command.skill.skills;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.command.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.util.Messaging;

public class SkillTrack extends ActiveSkill {

    public SkillTrack(Heroes plugin) {
        super(plugin);
        name = "Track";
        description = "Locates a player";
        usage = "/skill track <player>";
        minArgs = 1;
        maxArgs = 1;
        identifiers.add("skill track");
    }

    @Override
    public void init() {
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        return Configuration.getEmptyNode();
    }

    @Override
    public boolean use(Hero hero, String[] args) {
        Player player = hero.getPlayer();

        Player target = plugin.getServer().getPlayer(args[0]);
        if (target == null) {
            Messaging.send(player, "Target not found.");
            return false;
        }

        Location location = target.getLocation();
        Messaging.send(player, "Tracked $1: $2 in $3", target.getName(), "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")", location.getWorld().getName());
        player.setCompassTarget(location);
        notifyNearbyPlayers(player.getLocation(), useText, player.getName(), name);
        return true;
    }

}
