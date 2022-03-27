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
		
//		if(! player.isOp()) {
//			player.sendMessage(ChatColor.RED + "Sorry, only op players can execute this command.");
//			return false;
//		}
		
		if(args.length > 1) {
			player.sendMessage(ChatColor.RED + "Wrong number of arguments. Usage -> \"/heal [player]\" to heal a player.");
			return false;
		}
		
		if(args.length == 0) 
			healPlayer(player);
		else {
			Player target = sender.getServer().getPlayer(args[0]);
			healPlayer(player, target);
		}
		return true;
	}
	
	public void healPlayer(Player player) {
		player.setHealth(20);
		player.setFoodLevel(20);
		player.setSaturation(20);
		player.sendMessage(ChatColor.GREEN + "You just got healed !");
	}
	
	public void healPlayer(Player player, Player target) {
		target.setHealth(20);
		target.setFoodLevel(20);
		target.setSaturation(20);
		player.sendMessage(ChatColor.GREEN + "Successfully healed " + target.getName());
		if(! (player.equals(target)))
			target.sendMessage(ChatColor.GREEN + "You just got healed by " + player.getName() + " !");
	}

	
	
}
