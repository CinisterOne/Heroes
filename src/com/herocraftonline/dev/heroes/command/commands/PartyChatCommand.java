package com.herocraftonline.dev.heroes.command.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.herocraftonline.dev.heroes.Heroes;
import com.herocraftonline.dev.heroes.command.BaseCommand;
import com.herocraftonline.dev.heroes.party.HeroParty;
import com.herocraftonline.dev.heroes.persistence.Hero;
import com.herocraftonline.dev.heroes.util.Messaging;

public class PartyChatCommand extends BaseCommand {

    public PartyChatCommand(Heroes plugin) {
        super(plugin);
        name = "Party Leave";
        description = "Party leave command";
        usage = "/party <msg>";
        minArgs = 0;
        maxArgs = 100000000;
        identifiers.add("party");
        identifiers.add("p");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Hero hero = plugin.getHeroManager().getHero(p);
            HeroParty party = hero.getParty();

            if (party != null && party.getMemberCount() > 0) {
                for (Player player : hero.getParty().getMembers()) {
                    player.sendMessage(ChatColor.GOLD + p.getName() + " > " + args.toString());
                }
            } else {
                Messaging.send(sender, "You are not in a Party.");
            }
        }
    }
}
