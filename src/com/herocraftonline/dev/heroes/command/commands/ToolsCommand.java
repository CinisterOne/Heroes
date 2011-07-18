package com.herocraftonline.dev.heroes.command.commands;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.classes.HeroClass;
import com.herocraftonline.dev.heroes.command.BaseCommand;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.util.MaterialUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

public class ToolsCommand extends BaseCommand {

    public ToolsCommand(Heroes plugin) {
        super(plugin);
        setName("Tools");
        setDescription("Displays tools available for your class");
        setUsage("/hero tools");
        setMinArgs(0);
        setMaxArgs(0);
        getIdentifiers().add("hero tools");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Hero hero = plugin.getHeroManager().getHero(player);
            HeroClass heroClass = hero.getHeroClass();

            Set<String> allTools = heroClass.getAllowedWeapons();
            String[] categories = {"Sword", "Spade", "Pickaxe", "Axe", "Hoe"};
            String[] categorizedTools = new String[categories.length];

            for (int i = 0; i < categories.length; i++) {
                categorizedTools[i] = "";
            }

            for (String tool : allTools) {
                for (int i = 0; i < categories.length; i++) {
                    if (tool.endsWith(categories[i].toUpperCase())) {
                        if (categorizedTools[i] == null) {
                            categorizedTools[i] = "";
                        }
                        categorizedTools[i] += MaterialUtil.getFriendlyName(tool).split(" ")[0] + ", ";
                        break;
                    }
                }
            }

            for (int i = 0; i < categories.length; i++) {
                if (!categorizedTools[i].isEmpty()) {
                    categorizedTools[i] = categorizedTools[i].substring(0, categorizedTools[i].length() - 2);
                }
            }

            sender.sendMessage("§c--------[ §fAllowed Tools§c ]--------");
            for (int i = 0; i < categories.length; i++) {
                player.sendMessage("  §a" + categories[i] + ": §f" + categorizedTools[i]);
            }
        }
    }

}
