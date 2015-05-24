package DB.Model;

// The enemy that are encountered and can be caught
// Different stats from their sticker form
public class Monster {
	public int id;
	public int hp;
	public int attack;
	public int defense;
	public int speed;
	public double capture;
	
	public Monster(int id, int hp, int attack, int defense, int speed, double capture) {
		this.id = id;
		this.hp = hp;
		this.attack = attack;
		this.defense = defense;
		this.speed = speed;
		this.capture = capture;
	}
}
