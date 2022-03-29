package fr.tkf.plugin;

import fr.tkf.utils.UtilsAttributes;

public class Economy {

	private static final int baseMoneyValue = 10; //Base value of money. Change if needed.
	
	private final int valuePerSecond = 10;
	
	private final int valuePerKill = 100;
	
	private final float valuePerKillModifier = 0.9f;
	
	// Missing parameters. TODO when economy strategy will be decided.
	
	private UtilsAttributes utilsAttributes = new UtilsAttributes();
	
	public Economy() {
		
	}

}
