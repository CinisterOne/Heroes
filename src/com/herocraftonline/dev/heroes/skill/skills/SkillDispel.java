package com.herocraftonline.dev.heroes.skill.skills;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.effects.Dispellable;
import com.herocraftonline.dev.heroes.effects.Effect;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.skill.TargettedSkill;

public class SkillDispel extends TargettedSkill {

    public SkillDispel(Heroes plugin) {
        super(plugin, "Dispel");
        setDescription("Removes all magical effects from your target");
        setUsage("/skill dispel");
        setArgumentRange(0, 1);
        setIdentifiers(new String[]{"skill dispel"});
    }

    @Override
    public boolean use(Hero hero, LivingEntity target, String[] args) {
        if (!(target instanceof Player)) {
            return false;
        }

        Player targetPlayer = (Player) target;
        Hero targetHero = getPlugin().getHeroManager().getHero(targetPlayer);
        for (Effect effect : targetHero.getEffects()) {
            if (effect instanceof Dispellable) {
                targetHero.removeEffect(effect);
            }
        }

        broadcastExecuteText(hero, target);
        return true;
    }

}
