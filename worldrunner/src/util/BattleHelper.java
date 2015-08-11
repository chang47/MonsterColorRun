package util;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;
import DB.Model.BattleMonster;

/**
 * A class that offers helpful methods for monsters to fight
 * each other
 */
public class BattleHelper {

	/**
	 * Decides damage dealt from one target or another
	 * @param attacker who is attacking
	 * @param defender who is defending
	 * @return how much damage is done
	 */
	public static int Attack(BattleMonster attacker, BattleMonster defender) {
		int attack = attacker.atk;
		int defense = defender.def;
		int damage = 0;
		//Log.d("Damage", "Damage: " + attacker.monster.name + " And has a attack of " + damage);
		//Log.d("Defense", "Defender: " + defender.monster.name + " And has a defense of " + defense);
		
		if (attacker.buffs.containsKey(1)) {
			attack *= attacker.buffs.get(1).modifier;
			//Log.d("Damage", "Calculated: " + damage);
		}
		
		if (defender.buffs.containsKey(2)) {
			//NOTE: Does not work for some reason. Troubleshoot it later.;
			defense *= defender.buffs.get(2).modifier;
			//Log.d("Defense", "Calculated: " + defense);
		}
		
		damage =  (int) (2.5 * attack * (attack / defense));
		
		if ((attacker.monster.element == 1  && defender.monster.element == 3)    || 
				(attacker.monster.element == 2 && defender.monster.element == 1) ||
				(attacker.monster.element == 3 && defender.monster.element == 2) ||
				(attacker.monster.element == 4 && defender.monster.element == 5) ||
				(attacker.monster.element == 5 && defender.monster.element == 4)) {
			//If the attacker is strong vs. the defender
			damage *= 2;
			
		} else if ((attacker.monster.element == 3  && defender.monster.element == 1)    || 
				(attacker.monster.element == 1 && defender.monster.element == 2) ||
				(attacker.monster.element == 2 && defender.monster.element == 3)) {
			//If the defender is strong vs. the attacker
			damage /= 2;
		}


		//Log.d("Damage", "Attacker: " + attacker.monster.name + " has a total damage of " + damage);
		//Log.d("Defense", "Defender: " + defender.monster.name + " has a defense of " + defense);
		
		
		//If the damage is less than 0, set it to 0.
		if (damage < 1) {
			damage = 1;
		}
			
		return damage; 
	}
	
	/**
	 * Decides which target from the list if being attacked. Selects the monster that the attacker
	 * will deal the highest damage to.
	 * @param enemy Who is attacking
	 * @param party The list of people who are attacking
	 * @return The index the monster that is going to be attacked is at, -1 if all monsters are dead
	 */
	public static int AIAttack(BattleMonster enemy,ArrayList<BattleMonster> party)
	{
		//Sets the AI of what monsters attack
		double largest = -1;
		double tempSize = -1;
		int largestIndex = -1;
		
		for (int a = 0; a < party.size(); a++) {
			if (party.get(a) != null && party.get(a).currentHp > 0	) {
				tempSize = (double)(party.get(a).hp / BattleHelper.Attack(enemy, party.get(a)));
				
				if (largest < tempSize) {
					largest = tempSize;
					largestIndex = a;
				}
			}
		}

        Log.d("Final Comparison","Final Index: " + largestIndex + " With a max size of: " + largest);
		return largestIndex;
	}

}
