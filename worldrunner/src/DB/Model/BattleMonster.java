package DB.Model;

import java.util.HashMap;

import Abilities.Buff;


public class BattleMonster {
	
	public Monster monster;
	public int step;
	public HashMap<Integer, Buff> buffs;
	public int hp;
	public int atk;
	public int def;
	public int spd;
	public int currentHp;
	public int abilityStep;
	
	public BattleMonster(Monster monster) {
		this.monster = monster;
		buffs = new HashMap<Integer, Buff>();
		this.hp = monster.hp * 15 * monster.level / 100;
		this.currentHp = this.hp;
		this.atk = monster.attack * 10 * monster.level / 100;
		this.def = monster.defense * 10 * monster.level / 100;
		this.spd = monster.speed * 10 * monster.level / 100;
		this.step = 1000 / this.spd;
		this.abilityStep = monster.activeAbility.steps;
		// real one
		// this.step = 30000 / (5 * this.spd);
	}
	
	public BattleMonster(Monster monster, boolean fake) {
			this.monster = monster;
			buffs = new HashMap<Integer, Buff>();
			this.hp = 1000;
			this.currentHp = 1000;
			this.atk = 300;
			this.def = 300;
			this.spd = 200;
			this.step = 1000 / this.spd;	
			this.abilityStep = monster.activeAbility.steps;
	}
	
	/**
	 * Recalculates the speed for monsters in response to buffs (Or the removal of buffs)
	 */
	public void RecalculateSpeed() {
		if (buffs.containsKey(3)) {
			this.step = (int) (1000.0 / ((double) monster.speed * buffs.get(3).modifier));
			// this.step = (int) (30000.0 / (5 * monster.speed * buffs.get(3).modifier));			
		} else {
			this.step = (int) (1000.0 / ((double) monster.speed));
			// this.step = (int) (30000.0 / (double) (5 * monster.spd)); 
		}
	}
	
	public void resetHp() {
		this.currentHp = this.hp;
	}
	
	public void resetAbilityStep() {
		this.abilityStep = monster.activeAbility.steps;
	}
}
