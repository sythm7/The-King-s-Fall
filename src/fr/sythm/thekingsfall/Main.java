package fr.sythm.thekingsfall;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import fr.sythm.commands.CommandHeal;
import fr.sythm.commands.CommandSpectate;
import fr.sythm.commands.CommandSpeed;
import fr.sythm.commands.CommandTeam;
import fr.sythm.commands.CommandTerritory;
import fr.sythm.utils.HashMapList;

public class Main extends JavaPlugin {

	private HashMapList hashMapList = new HashMapList();
	
	private LinkedHashSet<Territory> territoriesList = new LinkedHashSet<>();
	
	private ArrayList<Team> teamsList = new ArrayList<>();
	
	private static Main instance;
	
	@Override
	public void onEnable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now enabled.");
		instance = this;
		this.getCommand("heal").setExecutor(new CommandHeal());
		this.getCommand("team").setExecutor(new CommandTeam(teamsList));
		this.getCommand("speed").setExecutor(new CommandSpeed());
		this.getCommand("spectate").setExecutor(new CommandSpectate());
		this.getCommand("territory").setExecutor(new CommandTerritory(this.hashMapList.getPlayersPositionsMap(), this.territoriesList));
		this.getServer().getPluginManager().registerEvents(new MyPluginListeners(this.hashMapList), this);
	}

	@Override
	public void onDisable() {
		System.out.println(ChatColor.GREEN + "TheKingsFall is now disabled.");
	}
	
	public static Main getInstance() {
		return instance;
	}
	
}