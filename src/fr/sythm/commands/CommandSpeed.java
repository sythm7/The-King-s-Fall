package fr.sythm.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpeed implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(! (sender instanceof Player))
			return false;
		
		Player player = (Player) sender;
		
		if(args.length != 1) {
			player.sendMessage(ChatColor.RED + "Invalid number of arguments -> Usage : /speed <number>\nwith number between 1 and 10 included");
			return false;
		}
		
		float speed = 0;
		
		try {
			speed = Float.valueOf(args[0]);
		}
		catch(NumberFormatException exception) {
			player.sendMessage(ChatColor.RED + "Wrong argument " + args[0] + " -> Usage : /speed <number>\nwith number between 1 and 10 included");
			return false;
		}
		
		if(speed < 0 || speed > 10) {
			player.sendMessage(ChatColor.RED + "Wrong speed number " + speed + " -> Usage : /speed <number>\nwith number between 0 and 10 included");
			return false;
		}
			
		if(player.isFlying()) {
			player.setFlySpeed(this.getRealFlyingSpeed(speed));
			player.sendMessage(ChatColor.GREEN + "Flying speed set to " + speed + " !");
		}
		else {
			player.sendMessage(ChatColor.GREEN + "Walking speed set to " + speed + " !");
			player.setWalkSpeed(this.getRealWalkingSpeed(speed));
		}
		
		return true;
	}
	
	public float getRealWalkingSpeed(float speed) {
		
		if(speed < 1f)
			return 0.2f * speed;
		if(speed <= 5f)
			return 0.2f + (speed - 1) * 0.075f;
		
		return speed * 0.1f;
	}
	
	public float getRealFlyingSpeed(float speed) {
		return 0.1f * speed;
	}
	
}