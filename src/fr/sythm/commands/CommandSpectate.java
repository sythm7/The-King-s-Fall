package fr.sythm.commands;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class CommandSpectate implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		String playerName = args[0];
		
		player.setGameMode(GameMode.SPECTATOR);
		
		Player targetPlayer = sender.getServer().getPlayer(playerName);
		
		if(targetPlayer == null) {
			player.sendMessage(ChatColor.RED + "Target player is null.");
			return false;
		}
		
		player.teleport(targetPlayer);
		
		player.setSpectatorTarget(targetPlayer);
		
		return true;
	}

}
