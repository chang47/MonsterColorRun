package DB.Model;

import Abilities.Ability;

// Stickers are the main fighting force that you the user has
// to fight with other peopl
public class Sticker {
	public int pstid;
	public int pid;
	public int sid;
	public String name;
	public int element;
	public int current_level;
	public int current_exp;
	public int spaid;
	public int saaid;
	public int evolve;
	public int equipped;
	public int position;
	public int hp;
	public int attack;
	public int defense;
	public int speed;
	public double capture;
	
	public Sticker(int pstid, int pid, int sid, String name, int element,
			int current_level, int current_exp, int spaid, int saaid, int evolve, int equipped, int position,
			int hp, int attack, int defense, int speed, double capture) {
		this.pstid = pstid;
		this.pid = pid;
		this.sid = sid;
		this.name = name;
		this.element = element;
		this.current_level = current_level;
		this.current_exp = current_exp;
		this.spaid = spaid;
		this.saaid = saaid;
		this.evolve = evolve;
		this.equipped = equipped;
		this.position = position;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.capture = capture;
	}
	
	public Sticker() { }
	
/*	public Monster convertToMonster() {
		return new Monster(pstid, name, hp, attack, defense, speed, capture, color, ability);
	}*/
	
}
