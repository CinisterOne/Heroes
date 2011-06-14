package com.herocraftonline.dev.heroes.skill.skills;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.classes.HeroClass;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.skill.TargettedSkill;
import com.herocraftonline.dev.heroes.util.Messaging;

public class SkillBandage extends TargettedSkill {

    private HashMap<Integer, Integer> playerSchedulers = new HashMap<Integer, Integer>();

    public SkillBandage(Heroes plugin) {
        super(plugin);
        name = "Bandage";
        description = "Bandages the target";
        usage = "/skill bandage [target]";
        minArgs = 0;
        maxArgs = 1;
        identifiers.add("skill bandage");
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        node.setProperty(SETTING_MAXDISTANCE, 5);
        node.setProperty("health", 5);
        return node;
    }

    @Override
    public boolean use(Hero hero, LivingEntity target, String[] args) {
        Player player = hero.getPlayer();
        if (target instanceof Player) {
            Player tPlayer = (Player) target;
            if (!(player.getItemInHand().getType() == Material.PAPER)) {
                Messaging.send(player, "You need paper to perform this.");
                return false;
            }
            
            target.setHealth(target.getHealth() + getSetting(hero.getHeroClass(), "health", t));
            
        }
        return false;
    }
}