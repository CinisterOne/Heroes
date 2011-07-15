package com.herocraftonline.dev.heroes.skill.skills;

import java.util.Set;

import org.bukkit.entity.Creature;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Ghast;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.util.Messaging;

public class SkillSummon extends ActiveSkill {

    public SkillSummon(Heroes plugin) {
        super(plugin);
        setName("Summon");
        setDescription("Summons a creature to fight by your side");
        setUsage("/skill summon <creature>");
        setMinArgs(1);
        setMaxArgs(1);
        getIdentifiers().add("skill summon");
    }

    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = super.getDefaultConfig();
        node.setProperty("max-summons", 3);
        return node;
    }

    @Override
    public boolean use(Hero hero, String[] args) {
        Player player = hero.getPlayer();
        CreatureType creatureType = CreatureType.fromName(args[0].toUpperCase());
        if (creatureType == CreatureType.SKELETON && hero.getSummons().size() <= getSetting(hero.getHeroClass(), "max-summons", 3)) {
            Entity spawnedEntity = player.getWorld().spawnCreature(player.getLocation(), creatureType);
            if (spawnedEntity instanceof Creature && spawnedEntity instanceof Ghast && spawnedEntity instanceof Slime) {
                spawnedEntity.remove();
                return false;
            }
            hero.getSummons().put(spawnedEntity, creatureType);
            notifyNearbyPlayers(player.getLocation(), getUseText(), player.getName(), getName(), creatureType.toString());
            Messaging.send(player, "You have succesfully summoned a " + creatureType.toString());
            return true;
        }
        return false;
    }

    public class SkillEntityListener extends EntityListener {

        @Override
        public void onEntityDeath(EntityDeathEvent event) {
            Entity defender = event.getEntity();
            Set<Hero> heroes = plugin.getHeroManager().getHeroes();
            for (Hero hero : heroes) {
                if (hero.getSummons().containsKey(defender)) {
                    hero.getSummons().remove(defender);
                }
            }

        }

        @Override
        public void onEntityTarget(EntityTargetEvent event) {
            if (event.getTarget() instanceof Player) {
                Set<Hero> heroes = plugin.getHeroManager().getHeroes();
                for (Hero hero : heroes) {
                    if (hero.getSummons().containsKey(event.getEntity())) {
                        if (hero.getPlayer() == event.getTarget()) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }
}
