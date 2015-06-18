package Other;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;
import Model.BattleMonster;

public class BattleHelper {
	public static int Attack(BattleMonster attacker, BattleMonster defender) {
		int damage = attacker.monster.attack;
		int defense = defender.monster.defense;

		Log.d("Damage", "Damage: " + attacker.monster.name + " And has a attack of " + damage);
		Log.d("Defense", "Defender: " + defender.monster.name + " And has a defense of " + defense);
		
		if (attacker.buffs.containsKey(1)) {
			damage *= attacker.buffs.get(1).modifier;
			Log.d("Damage", "Calculated: " + damage);
		}
		
		if (defender.buffs.containsKey(2)) {
			//NOTE: Does not work for some reason. Troubleshoot it later.;
			defense *= defender.buffs.get(2).modifier;
			Log.d("Defense", "Calculated: " + defense);
		}
		
		if ((attacker.monster.element == 1  && defender.monster.element == 3)    || 
				(attacker.monster.element == 2 && defender.monster.element == 1) ||
				(attacker.monster.element == 3 && defender.monster.element == 2) ||
				(attacker.monster.element == 4 && defender.monster.element == 5) ||
				(attacker.monster.element == 5 && defender.monster.element == 4)) {
			//If the attacker is strong vs. the defender
			damage = (damage * 2) - defense;
			
		} else if ((attacker.monster.element == 3  && defender.monster.element == 1)    || 
				(attacker.monster.element == 1 && defender.monster.element == 2) ||
				(attacker.monster.element == 2 && defender.monster.element == 3)) {
			//If the defender is strong vs. the attacker
			damage = (damage / 2) - defense;
		} else {
			//If there is no strength/weakness
			damage = damage - defense;
		}


		Log.d("Damage", "Attacker: " + attacker.monster.name + " has a total damage of " + damage);
		Log.d("Defense", "Defender: " + defender.monster.name + " has a defense of " + defense);
		
		
		//If the damage is less than 0, set it to 0.
		if (damage < 1) {
			damage = 1;
		}
			
		return damage; 
	}
	
	public static int AIAttack(BattleMonster enemy,ArrayList<BattleMonster> party)
	{
		double largest = (double)(party.get(0).monster.hp/BattleHelper.Attack(enemy, party.get(0)));
		double tempSize = 0;
		int largestIndex = 0;
		
		for (int a = 1; a < party.size(); a++) {
			if (party.get(a).currentHp > 0	) {
				
				tempSize = (double)(party.get(a).monster.hp/BattleHelper.Attack(enemy, party.get(a)));
				
				if (largest > tempSize) {
					largest = tempSize;
					largestIndex = a;
				}
			}
		}

        Log.d("Final Comparison","Final Index: " + largestIndex + " With a max size of: " + largest);
		return largestIndex;
	}

}
