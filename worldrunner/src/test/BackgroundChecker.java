package test;

public class BackgroundChecker {
	// used to update ui when player switch from background to foreground
	public static boolean isBackground = false;
	// used to update the enemies hp if they get attacked
	public static boolean monsterWasAttacked = false;
	// used to update the enemy ui when there's a new enemy
	public static boolean newEnemies = false;
	// used to update the player's monster's hp ui if they get attacked
	public static boolean playerMonsterWasAttacked = false;
	// used to notify the end of a battle. Prevents player monsters
	// from carrying over their attacks
	public static boolean finishedCurrentBattle = false;
}
