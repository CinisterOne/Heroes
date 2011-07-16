package com.herocraftonline.dev.heroes.damage;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Snowball;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.PluginManager;
import org.bukkit.util.config.Configuration;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.classes.HeroClass;
import com.herocraftonline.dev.heroes.persistence.Hero;

public class DamageManager {

    public enum ProjectileType {
        ARROW,
        SNOWBALL,
        EGG;

        public static ProjectileType valueOf(Entity entity) {
            if (entity instanceof Arrow) {
                return ARROW;
            } else if (entity instanceof Snowball) {
                return SNOWBALL;
            } else if (entity instanceof Egg) {
                return EGG;
            } else {
                throw new IllegalArgumentException(entity.getClass().getSimpleName() + " is not a projectile.");
            }
        }
    }

    private Heroes plugin;
    private HeroesDamageListener listener;
    private HashMap<Material, Integer> itemDamage;
    private HashMap<ProjectileType, Integer> projectileDamage;
    private HashMap<CreatureType, Integer> creatureHealth;
    private HashMap<CreatureType, Integer> creatureDamage;
    private HashMap<DamageCause, Integer> environmentalDamage;

    public DamageManager(Heroes plugin) {
        this.plugin = plugin;
        listener = new HeroesDamageListener(plugin, this);
    }

    /**
     * Register the events for the damage system
     */
    public void registerEvents() {
        if (plugin.getConfigManager().getProperties().damageSystem) {
            PluginManager pluginManager = plugin.getServer().getPluginManager();
            pluginManager.registerEvent(Type.ENTITY_DAMAGE, listener, Priority.Highest, plugin);
            pluginManager.registerEvent(Type.CREATURE_SPAWN, listener, Priority.Highest, plugin);
        }
    }

    public Integer getItemDamage(Material item) {
        if (itemDamage.containsKey(item)) {
            return itemDamage.get(item);
        } else {
            return null;
        }
    }

    public Integer getCreatureHealth(CreatureType type) {
        if (creatureHealth.containsKey(type)) {
            int health = creatureHealth.get(type);
            return health > 200 ? 200 : (health < 0 ? health : 0);
        } else {
            return null;
        }
    }

    public Integer getCreatureDamage(CreatureType type) {
        if (creatureDamage.containsKey(type)) {
            return creatureDamage.get(type);
        } else {
            return null;
        }
    }

    public Integer getEnvironmentalDamage(DamageCause cause) {
        if (environmentalDamage.containsValue(cause)) {
            return environmentalDamage.get(cause);
        } else {
            return null;
        }
    }

    public Integer getProjectileDamage(ProjectileType type) {
        if (projectileDamage.containsKey(type)) {
            return projectileDamage.get(type);
        } else {
            return null;
        }
    }

    public void load(Configuration config) {
        List<String> keys;

        creatureHealth = new HashMap<CreatureType, Integer>();
        keys = config.getKeys("creature-health");
        if (keys != null) {
            for (String key : keys) {
                CreatureType type = CreatureType.fromName(key);
                int health = config.getInt("creature-health." + key, 10);
                if (type != null) {
                    creatureHealth.put(type, health);
                }
            }
        }

        creatureDamage = new HashMap<CreatureType, Integer>();
        keys = config.getKeys("creature-damage");
        if (keys != null) {
            for (String key : keys) {
                CreatureType type = CreatureType.fromName(key);
                int damage = config.getInt("creature-damage." + key, 10);
                if (type != null) {
                    creatureDamage.put(type, damage);
                }
            }
        }

        itemDamage = new HashMap<Material, Integer>();
        keys = config.getKeys("item-damage");
        if (keys != null) {
            for (String key : keys) {
                Material item = Material.matchMaterial(key);
                int damage = config.getInt("item-damage." + key, 2);
                if (item != null) {
                    itemDamage.put(item, damage);
                }
            }
        }

        environmentalDamage = new HashMap<DamageCause, Integer>();
        keys = config.getKeys("environmental-damage");
        if (keys != null) {
            for (String key : keys) {
                try {
                    DamageCause cause = DamageCause.valueOf(key.toUpperCase());
                    int damage = config.getInt("environmental-damage." + key, 0);
                    environmentalDamage.put(cause, damage);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }

        projectileDamage = new HashMap<ProjectileType, Integer>();
        keys = config.getKeys("projectile-damage");
        if (keys != null) {
            for (String key : keys) {
                try {
                    ProjectileType type = ProjectileType.valueOf(key.toUpperCase());
                    int damage = config.getInt("projectile-damage." + key, 0);
                    projectileDamage.put(type, damage);
                } catch (IllegalArgumentException e) {
                    continue;
                }
            }
        }
    }

    public static int getVisualDamage(Hero hero, int damage) {
        return hero.getPlayer().getHealth() - (int) (damage / 20.0 * hero.getHeroClass().getMaxHealth());
    }

}