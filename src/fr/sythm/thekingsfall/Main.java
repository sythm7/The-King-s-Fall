package fr.sythm.thekingsfall;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import fr.sythm.commands.CommandSpeed;
import fr.sythm.utils.HashMapList;

public class Main extends JavaPlugin {

	private HashMapList hashMapList = new HashMapList();
	private static Main instance;
	
	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now enabled.");
		instance = this;
		this.getCommand("speed").setExecutor(new CommandSpeed());
		this.getServer().getPluginManager().registerEvents(new MyPluginListeners(this.hashMapList), this);
	}

	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall  is now disabled.");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}