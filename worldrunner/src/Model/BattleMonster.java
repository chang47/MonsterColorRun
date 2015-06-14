package Model;

import java.lang.reflect.Field;

import DB.Model.Monster;


public class BattleMonster {
	
	public Monster monster;
	public double currentHp;
	public int currentStep;
	
	public BattleMonster(Monster monster, double currentHp, int currentStep) {
		this.monster = monster;
		this.currentHp = currentHp;
		this.currentStep = currentStep;
	}

}
