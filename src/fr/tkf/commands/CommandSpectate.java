package fr.tkf.commands;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.tkf.plugin.Main;


public class CommandSpectate implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		String playerName = args[0];
		
		Player targetPlayer = Bukkit.getServer().getPlayer(playerName);
		
		
		if(targetPlayer == null) {
			player.sendMessage(ChatColor.RED + "Target player is null.");
			return false;
		}
		
		player.setGameMode(GameMode.SPECTATOR);
		
		player.teleport(targetPlayer.getLocation());
		
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> player.setSpectatorTarget(targetPlayer), 5);
		
		return true;
	}

}
