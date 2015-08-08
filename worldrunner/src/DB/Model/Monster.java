package DB.Model;

import Abilities.Ability;

// The enemy that are encountered and can be caught
// Different stats from their sticker form
public class Monster {
	public int uid;
	public String name;
	public int hp;
	public int attack;
	public int defense;
	public int speed;
	public double capture;
	public int element;
	public Ability activeAbility;
	public int position;
	public int equipped;
	public int exp;
	public int level;
	public int evolve;
	public int monsterId;
	//public Ability passiveAbility;
	
	public Monster(int uid, String name, int hp, int attack, int defense, int speed, double capture, int element, Ability activeAbility, 
			int position, int equipped, int exp, int level, int evolve, int monsterId/*, Ability passiveAbility*/) {
		this.uid = uid;
		this.name = name;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.capture = capture;
		this.element = element;
		//0 = Fire
		//1 = Water
		//2 = Grass
		//3 = Light
		//4 = Dark
		this.activeAbility = activeAbility;
		this.position = position;
		this.equipped = equipped;
		this.exp = exp;
		this.level = level;
		this.evolve = evolve;
		this.monsterId = monsterId;
		//this.passiveAbility = passiveAbility;
	}
}
