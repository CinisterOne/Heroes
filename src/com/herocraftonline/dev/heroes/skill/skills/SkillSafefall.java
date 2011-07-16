package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.util.config.Configuration;
import org.bukkit.util.config.ConfigurationNode;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.effects.ExpirableEffect;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.skill.ActiveSkill;
import com.herocraftonline.dev.heroes.skill.Skill;

public class SkillSafefall extends ActiveSkill {
    
    private String applyText;
    private String expireText;

    public SkillSafefall(Heroes plugin) {
        super(plugin);
        setName("Safefall");
        setDescription("Stops you from taking fall damage for a short amount of time");
        setUsage("/skill safefall");
        setMinArgs(0);
        setMaxArgs(0);
        getIdentifiers().add("skill safefall");

        registerEvent(Type.ENTITY_DAMAGE, new SkillEntityListener(), Priority.Normal);
    }
    
    @Override
    public ConfigurationNode getDefaultConfig() {
        ConfigurationNode node = Configuration.getEmptyNode();
        node.setProperty("duration", 20000);
        node.setProperty("apply-text", "%hero% has braced for landing!");
        node.setProperty("expire-text", "%hero% has lost safefall!");
        return node;
    }

    @Override
    public void init() {
        applyText = getSetting(null, "apply-text", "%hero% has braced for landing!").replace("%hero%", "$1");
        expireText = getSetting(null, "expire-text", "%hero% has lost safefall!").replace("%hero%", "$1");
    }

    @Override
    public boolean use(Hero hero, String[] args) {
        int duration = getSetting(hero.getHeroClass(), "duration", 20000);
        hero.addEffect(new SafefallEffect(this, duration));

        return true;
    }

    public class SafefallEffect extends ExpirableEffect {

        public SafefallEffect(Skill skill, long duration) {
            super(skill, "Safefall", duration);
        }

        @Override
        public void apply(Hero hero) {
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), applyText, player.getDisplayName());
        }

        @Override
        public void remove(Hero hero) {
            Player player = hero.getPlayer();
            broadcast(player.getLocation(), expireText, player.getDisplayName());
        }

    }

    public class SkillEntityListener extends EntityListener {

        @Override
        public void onEntityDamage(EntityDamageEvent event) {
            if (event.isCancelled() || event.getCause() != DamageCause.FALL) {
                return;
            }

            Entity defender = event.getEntity();
            if (defender instanceof Player) {
                Player player = (Player) defender;
                Hero hero = plugin.getHeroManager().getHero(player);
                if (hero.hasEffect("Safefall")) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
