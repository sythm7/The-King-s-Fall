package fr.tkf.plugin;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import fr.tkf.commands.CommandHeal;
import fr.tkf.commands.CommandSpectate;
import fr.tkf.commands.CommandSpeed;
import fr.tkf.commands.CommandTeam;
import fr.tkf.commands.CommandTerritory;

public class Main extends JavaPlugin {
	
	private static Main instance;
	
	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now enabled.");
		instance = this;
		
		this.getCommand("heal").setExecutor(new CommandHeal());
		this.getCommand("team").setExecutor(new CommandTeam());
		this.getCommand("speed").setExecutor(new CommandSpeed());
		this.getCommand("spectate").setExecutor(new CommandSpectate());
		this.getCommand("territory").setExecutor(new CommandTerritory());
		this.getServer().getPluginManager().registerEvents(new MyPluginListeners(), this);
	}

	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now disabled.");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}