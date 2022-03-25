package fr.sythm.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandHeal implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(! player.isOp()) {
			player.sendMessage(ChatColor.RED + "Sorry, only op players can execute this command.");
			return false;
		}
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.sendMessage(ChatColor.GREEN + "You just got healed !");
		
		return true;
	}

	
	
}
