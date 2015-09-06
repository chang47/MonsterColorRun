package battleHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.brnleehng.worldrunner.Hub;

import util.BattleHelper;
import Abilities.Buff;
import Abilities.DamageAbility;
import Abilities.DamageAllAbility;
import Abilities.SupportAbility;
import DB.DBManager;
import DB.Model.BattleMonster;
import DB.Model.Monster;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.ArrayAdapter;

public class BattleInfo {

	public static final String PREF_NAME = "BattleInfo";
	
	Intent intent;
	
    public static int exp;
    
    // list of stickers that were found, temporarily changed to be a list
    // of monsters
    public static ArrayList<Monster> found;
    
    // calculate running metrics
    public static int steps;
    public static long countUp;
    public static double distance;
    public static int coins;
    public static int iPartyAttacked;
    public static int battleSteps;
    
    // shouldn't have since the DB technically should only be accessed via
    // the controller (Hub), but we'll just have it here anyways for now
    private DBManager db;
    
    //For logging purposes
	public static ArrayList<String> list = new ArrayList<String>();

    
    // list of messages that are used to display the progress
    // made in the game. You just add it into an adapter and it'll do everything for you
    //private List<String> list; 
    
    //Sets the list of monsters for various purposes
    public static ArrayList<Monster> monsterList;
    public static ArrayList<Monster> partyList;
    public static ArrayList<BattleMonster> partyMonsterBattleList;
    public static ArrayList<BattleMonster> enemyMonsterBattleList;
    
    //Shows the player's log
    public static Dialog showLog;
    
    public static int partyMonstersSize;

    // Player
    public static int deadPartyMonsters;
    // Enemy
    public static int deadEnemies;
    
    //Sets amount of enemies
	public static int enemyPartySize;
	
	//Sets how many monsters are needed to be beaten
	public static int fightObjective;
	
	//Sets if the finish button can be used
	public static boolean finishEnabled = false;
	public static boolean caughtAlready = false;
	
	/**
	 * Should only be called at the start of a battle and never again 
	 * anywhere else
	 */
	public static void combatStart() {
		if (!BackgroundChecker.battleStarted) {
			// ensures that method can't be called again
			BackgroundChecker.battleStarted = true;
			
			// initialize fields
			partyList = Hub.equippedStickers;
	        partyMonsterBattleList = new ArrayList<BattleMonster>();
	        enemyMonsterBattleList = new ArrayList<BattleMonster>();
	        // TODO what's the best way to add the stickers in?
	        /*enemyProgressBarList = new ArrayList<ProgressBar>();
	        playerProgressBarList = new ProgressBar[5];*/
	        deadPartyMonsters = 0;
	        deadEnemies = 0;
	        enemyPartySize = 0;
	        partyMonstersSize = 0;
	        steps = 0;
	        distance = 0;
	        coins = 0;
			countUp = 0;
			battleSteps = 0;
			exp = 0;
			fightObjective = 0;
			finishEnabled = false;
			caughtAlready = false;
			found = new ArrayList<Monster>();
			
	        generateEnemies();
	        generateParty();

		}
	} 
	
	// only calls this when the battle finishes otherwise you can't start
	// a new combat session and the game will crash
	// good time to null and get rid of unnecessary variables
	public static void combatFinish() {
		if (BackgroundChecker.battleStarted) {
			found.clear();
			// TODO probably source of null pointer exception, to be discovered 
			//found = null;
			BackgroundChecker.init();
			//BackgroundChecker.battleStarted = false;
		}
	}
	
	/**
	 * Creates a list of new monsters to fight with
	 */
	public static void generateEnemies() {
		
		// resets conditions for UI the battle logic
		BackgroundChecker.finishedCurrentBattle = true;
		battleSteps = 0;
		BackgroundChecker.newEnemies = true;
		BackgroundChecker.monsterWasAttacked = false;
    	caughtAlready = false;
    	deadEnemies = 0;
    	enemyPartySize = (int) ((Math.random() * 3.0) + 1);
    	
    	// TODO remove
    	if (list.size() < 100)
    		list.add("new enemy party with " + enemyPartySize + " monsters");
		enemyMonsterBattleList.clear();
		
		//Creates the monsters and adds the UI elements for them
		for (int i = 0; i < enemyPartySize; i++) {
    		int monsterGen = (int) (Math.random() * Hub.enemyList.size());
    		enemyMonsterBattleList.add(new BattleMonster(Hub.enemyList.get(monsterGen)));
    		Log.d("enemy health", "current: " + enemyMonsterBattleList.get(i).currentHp + " max: " +
    				enemyMonsterBattleList.get(i).hp);
		}
    }
	
	 /**
     * Generates the player's party
     */
    private static void generateParty() {
    	// sets ui flags
    	BackgroundChecker.playerMonsterWasAttacked = false;
    	//partyMonsterBattleList.clear();
    	partyMonsterBattleList = new ArrayList<BattleMonster>();
    	for (int i = 0; i < partyList.size(); i++) {
    		if (partyList.get(i) == null) {
    			partyMonsterBattleList.add(null);
    		} else {
    			partyMonstersSize++;
    			partyMonsterBattleList.add(new BattleMonster(partyList.get(i), true));
    		}
    	}
    }
    
    /**
     * The enemy attacks the users. In the event that the player monster dies. 
     * Their steps and abilities are restarted
     */
    public static void enemyTurn() {
    	for (int i = 0; i < enemyPartySize; i++) {
        	//Attacks Regularly
        	if (enemyMonsterBattleList.get(i).currentHp > 0) {
		        if (battleSteps % enemyMonsterBattleList.get(i).step == 0) {
		        	iPartyAttacked = BattleHelper.AIAttack(enemyMonsterBattleList.get(i), partyMonsterBattleList);

		        	if (iPartyAttacked == -1) {
		        		throw new Error("attacked index is -1 for attacking user party, impossible!");
		        	}
		        	// for ui update
		        	BackgroundChecker.playerMonsterWasAttacked = true;
		        	partyMonsterBattleList.get(iPartyAttacked).currentHp -= BattleHelper.Attack(enemyMonsterBattleList.get(i), 
		        			partyMonsterBattleList.get(iPartyAttacked));
		        	// TODO remove
		        	if (list.size() < 100) 
		        		list.add("Enemy " + enemyMonsterBattleList.get(i).monster.name + " Attacks " + 
		            partyMonsterBattleList.get(iPartyAttacked).monster.name + " For " + 
		            BattleHelper.Attack(enemyMonsterBattleList.get(i), partyMonsterBattleList.get(iPartyAttacked)));
		
					if (partyMonsterBattleList.get(iPartyAttacked).currentHp <= 0) {
						deadPartyMonsters++;
						if (deadPartyMonsters >= partyMonstersSize) {
							//Entire Party is dead, resurrect them and change monsters
							reviveParty(partyMonsterBattleList.size());
							generateEnemies();
							return;
						}
					}
		        }
        	}
        }
    }
    
    /**
     * Player monsters attack the enemies. If the enemies are killed they have 
     * to start over in the attacking steps, but retains ability steps
     */   
    public static void playerTurn() {
    	if (partyMonsterBattleList == null) {
    		Log.d("random crash", "partyMonsterBattleList is null");
    		Log.d("random crash", "finished current battle status: " + BackgroundChecker.finishedCurrentBattle);
    		Log.d("random crash", "has the combat started? " + BackgroundChecker.battleStarted);
    		Log.d("random crash", "was the monster attacked? " + BackgroundChecker.monsterWasAttacked);
    		Log.d("random crash", "was the player monster attacked? " + BackgroundChecker.playerMonsterWasAttacked);
    		Log.d("random crash", "was in the background? " + BackgroundChecker.isBackground);
    		Log.d("random crash", "Are there now new enemies? " + BackgroundChecker.newEnemies);
    		Log.d("random crash", "number of steps " + steps);
    		if (partyList == null) {
    			Log.d("random crash", "partyList is null");
    		} else {
    			Log.d("random crash", "partyList is not null");	
    		}
    		
    		List<Monster> list = Hub.partyList;
    		if (list != null) {
    			Log.d("random crash", "Hub partyList is null");
    		} else {
    			Log.d("random crash", "Hub partyList is not null");
    		}
    	}
    	for (int i = 0; i < partyMonsterBattleList.size(); i++) {
        	if (partyMonsterBattleList.get(i) != null && 
        			partyMonsterBattleList.get(i).currentHp > 0) {
	        	if (battleSteps % partyMonsterBattleList.get(i).step == 0) {
	        		partyMonsterBattleList.get(i).abilityStep--;
	        		BackgroundChecker.monsterWasAttacked = true;
	        		
	        		int iEnemyAttacked = BattleHelper.AIAttack(partyMonsterBattleList.get(i), enemyMonsterBattleList);
	        		Log.d("fight size", "size of enemy is: " + enemyMonsterBattleList.size() + " size of user party is: " + partyMonsterBattleList.size());
	        		Log.d("index attack", "attack index is: " + iEnemyAttacked + " alive enemey is: " + deadEnemies);
	        		if (iEnemyAttacked == -1) {
	        			generateEnemies();
	        			return;
		        		//throw new Error("attacked index is -1 when attacking enemies with attack, impossible!");
		        	}
	        		double damage = BattleHelper.Attack(partyMonsterBattleList.get(i), enemyMonsterBattleList.get(iEnemyAttacked));
	        		enemyMonsterBattleList.get(iEnemyAttacked).currentHp -= damage;
	        		// TODO remove
	        		if (list.size() < 10)
	        			list.add(partyMonsterBattleList.get(i).monster.name + " Attacks " + enemyMonsterBattleList.get(iEnemyAttacked).monster.name + " For " + damage + "!");
	        		Iterator iterator = partyMonsterBattleList.get(i).buffs.entrySet().iterator();
	        		// decrease buff of monsters
	        		while (iterator.hasNext()) {
	        			Map.Entry<Integer, Buff> pair = (Entry<Integer, Buff>) iterator.next();
	        			int attribute = pair.getKey();
	        			Buff buff = pair.getValue();
	        			//Log.d("buff with duration at: ", "" + buff.duration);
	        			buff.duration--;
	        			Log.d("duration", "" + partyMonsterBattleList.get(i).monster.name + " buff " + buff.name + " has duration " + buff.duration);
	        			//partyBattleList.get(i).buffs.get(iterator).duration--;
	        			//Check if above code actually decreases
	        			if (buff.duration <= 0) {
	        				//Log.d("removed attribute", "" + iterator);
	        				// partyBattleList.get(b).buffs.get(3).duration
	        				
	        				// important to be after, because recalculate checks for the attribute key
	        				if (attribute == 3) {
	        					partyMonsterBattleList.get(i).RecalculateSpeed();
	        	        		//Log.d("Speed","New Speed Calculated (Buff Removed): " + partyMonsterBattleList.get(i).currentStep);
	        				}
	        				iterator.remove();
	        				//partyBattleList.get(i).buffs.remove(iterator);
	        			}
	        		}
	        		checkEnemyDead(iEnemyAttacked);
	        		
	        		if (BackgroundChecker.finishedCurrentBattle)
	        			return;
	        	}
        	}
        }
    }
    
    /**
     * Player uses their party's abilities. If they win, their steps get restarted
     * but maintains their ability step
     * 1) Damage all
     * 2) Support
     */
    public static void playerAbilityTurn() {
    	for (int i = 0; i < partyMonsterBattleList.size(); i++) {
        	if (partyMonsterBattleList.get(i) != null && partyMonsterBattleList.get(i).currentHp > 0) {
        		if (partyMonsterBattleList.get(i).abilityStep < 0) { 
        			partyMonsterBattleList.get(i).resetAbilityStep();
	        		//Applies ability to attack enemy

	        		if (partyMonsterBattleList.get(i).monster.activeAbility.getClass() == DamageAllAbility.class) {
	        			BackgroundChecker.monsterWasAttacked = true;
		        		//int iEnemyAttack = BattleHelper.AIAttack(partyMonsterBattleList.get(i), enemyMonsterBattleList);
		        		DamageAllAbility dAbility = (DamageAllAbility) partyMonsterBattleList.get(i).monster.activeAbility;
	        			double damage = dAbility.damage * partyMonsterBattleList.get(i).monster.attack;
	        			for (int j = 0; j < enemyMonsterBattleList.size(); j++) {
	        				BattleMonster monster = enemyMonsterBattleList.get(j);
	        				if (monster != null) {
	        					monster.currentHp -= damage;
	        					checkEnemyDead(j);
	        					if (BackgroundChecker.finishedCurrentBattle)
	        	        			return;
	        				}
	        			}	        					
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + 
//	            				" For " + damage + "!");

	        		} else if (partyMonsterBattleList.get(i).monster.activeAbility.getClass() == SupportAbility.class) {
	        			//Applies party buffs
	        			SupportAbility support = (SupportAbility)partyMonsterBattleList.get(i).monster.activeAbility;
	        	        for (int b = 0; b < partyMonsterBattleList.size(); b++) {
	        	        	if (partyMonsterBattleList.get(b) != null) {
		        	        	Buff newBuff = new Buff(support.name, support.description, support.duration, support.attribute, support.modifier);
		        	        	partyMonsterBattleList.get(b).buffs.put(support.attribute, newBuff);           	        
		        	        	if (support.attribute == 3) {
		        	        		partyMonsterBattleList.get(b).RecalculateSpeed();
		        	        		Log.d("Speed","New Speed Calculated for : " + partyMonsterBattleList.get(b).monster.name + " is " + partyMonsterBattleList.get(b).step + " duration is: " + partyMonsterBattleList.get(b).buffs.get(3).duration);
		            	        }
	        	        	}
	        	        }
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + "!");
	        		} else if (partyMonsterBattleList.get(i).monster.activeAbility.getClass() == DamageAbility.class) {
	        			BackgroundChecker.monsterWasAttacked = true;
		        		int iEnemyAttack = BattleHelper.AIAttack(partyMonsterBattleList.get(i), enemyMonsterBattleList);
		        		if (iEnemyAttack == -1) {
		        			throw new Error("Damage ability attack index is -1, impossible!");
		        		}
		        		DamageAllAbility dAbility = (DamageAllAbility) partyMonsterBattleList.get(i).monster.activeAbility;
	        			double damage = dAbility.damage * partyMonsterBattleList.get(i).monster.attack;
	        			enemyMonsterBattleList.get(iEnemyAttack).currentHp -= damage;
//	            		list.add(partyMonsterBattleList.get(i).monster.name + " Used Ability " +  partyMonsterBattleList.get(i).monster.ability.name + 
//	            				" For " + damage + "!");
	            		
	            		//Checks if all enemies are dead 
	        			checkEnemyDead(iEnemyAttack);
	        		}
	        		if (BackgroundChecker.finishedCurrentBattle)
	        			return;
	        	}
        		
        	}
    	}
    }
    
    /**
     * Brings the party back to life reseting all of their hp and ability steps
     * @param size - the size of the party list (always 5)
     */
    private static void reviveParty(int size) {
		deadPartyMonsters = 0;
		BackgroundChecker.finishedCurrentBattle = true;
		// TODO issue because  they can attack again immediately?
		battleSteps = 0;
		// TODO remove
		if (list.size() < 100)
			list.add("Your party was wiped");
	    for (int i = 0; i < size; i++) {
		    if (partyMonsterBattleList.get(i) != null) {
		    	partyMonsterBattleList.get(i).resetHp();
		    	partyMonsterBattleList.get(i).resetAbilityStep();
		    }
	    }
	}
	
    private static void checkEnemyDead(int iPartyAttack) {
    	Log.d("into check", "checking if " + enemyMonsterBattleList.get(iPartyAttack).monster.name + " is dead");
		if (enemyMonsterBattleList.get(iPartyAttack).currentHp <= 0) {
			Log.d("dead check", enemyMonsterBattleList.get(iPartyAttack).monster.name + " is dead");
			// TODO add to other
			exp += enemyMonsterBattleList.get(iPartyAttack).monster.exp * enemyMonsterBattleList.get(iPartyAttack).monster.level / 2;
    		//list.add(enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been defeated!");
    		deadEnemies++;
    		captureMonster(iPartyAttack);
    		checkEnemyMonsterAllDead();
		}
    }
    
    private static void captureMonster(int iPartyAttack) {
    	//if (!caughtAlready && (double) ((Math.random() * 100.0) + 1) > enemyMonsterBattleList.get(iPartyAttack).monster.capture) {
			Log.d("capture monster", "caught " + enemyMonsterBattleList.get(iPartyAttack).monster.name );
			// TODO remove
			if (found.size() < 10) {
				if (list.size() < 10) 
					list.add(enemyMonsterBattleList.get(iPartyAttack).monster.name + " has been captured!");
				found.add(enemyMonsterBattleList.get(iPartyAttack).monster);
			}
			caughtAlready = true;
		//}
    }
    
    private static void checkEnemyMonsterAllDead() {
    	if (deadEnemies >= enemyPartySize) {
    		// TODO remove
    		if (list.size() < 100)
    			list.add("Defeated all enemies");
			fightObjective++;	
			if (fightObjective > 2) {
				finishEnabled = true;
			}
			generateEnemies();
		}
    }
	
}
