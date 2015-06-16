package Model;

import java.util.HashMap;

import Abilities.Buff;
import DB.Model.Monster;


public class BattleMonster {
	
	public Monster monster;
	public double currentHp;
	public int currentStep;
	public HashMap<Integer, Buff> buffs;
	
	public BattleMonster(Monster monster, double currentHp, int currentStep) {
		this.monster = monster;
		this.currentHp = currentHp;
		this.currentStep = currentStep;
		buffs = new HashMap<Integer, Buff>();
	}
	
	public void RecalculateSpeed(int currentStep) {
		if (buffs.containsKey(3)) {
			this.currentStep = (int) (1000.0 / ((double) monster.speed * buffs.get(3).modifier));
		} else {
			this.currentStep = (int) (1000.0 / ((double) monster.speed));
		}
	}
}
