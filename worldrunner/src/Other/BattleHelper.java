package Other;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.util.Log;
import Model.BattleMonster;

public class BattleHelper {
	public static int Attack(BattleMonster attacker, BattleMonster defender) {
		int damage = 0;
		
		if ((attacker.monster.element == 1  && defender.monster.element == 3)    || 
				(attacker.monster.element == 2 && defender.monster.element == 1) ||
				(attacker.monster.element == 3 && defender.monster.element == 2) ||
				(attacker.monster.element == 4 && defender.monster.element == 5) ||
				(attacker.monster.element == 5 && defender.monster.element == 4)) {
			//If the attacker is strong vs. the defender
			damage = (attacker.monster.attack * 2) - defender.monster.defense;
			
		} else if ((attacker.monster.element == 3  && defender.monster.element == 1)    || 
				(attacker.monster.element == 1 && defender.monster.element == 2) ||
				(attacker.monster.element == 2 && defender.monster.element == 3)) {
			//If the defender is strong vs. the attacker
			damage = (attacker.monster.attack / 2) - defender.monster.defense;
		} else {
			//If there is no strength/weakness
			damage = attacker.monster.attack - defender.monster.defense;
		}
		
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

	        Log.d("Comparing ",largest + " with " + tempSize);
			
			tempSize = (double)(party.get(a).monster.hp/BattleHelper.Attack(enemy, party.get(a)));
			
			if (largest > tempSize) {
		        Log.d("Replacing ", largest + " with " + tempSize);
				largest = tempSize;
				largestIndex = a;
			}
		}

        Log.d("Final Comparison","Final Index: " + largestIndex + " With a max size of: " + largest);
		return largestIndex;
	}

}
