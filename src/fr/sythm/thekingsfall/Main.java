package fr.sythm.thekingsfall;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import fr.sythm.commands.CommandSpeed;
import fr.sythm.utils.Couple;

public class Main extends JavaPlugin {
	
	private HashMap<Player, Couple<Location, Location>> playersPositionsMap = new HashMap<>();

	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now enabled.");
		this.getCommand("speed").setExecutor(new CommandSpeed());
		this.getServer().getPluginManager().registerEvents(new MyPluginListeners(this.playersPositionsMap), this);
	}

	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall  is now disabled.");
	}
	
}