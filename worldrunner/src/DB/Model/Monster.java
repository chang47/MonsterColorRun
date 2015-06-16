package DB.Model;

// The enemy that are encountered and can be caught
// Different stats from their sticker form
public class Monster {
	public int id;
	public String name;
	public int hp;
	public int attack;
	public int defense;
	public int speed;
	public double capture;
	public int element;
	
	public Monster(int id, String name, int hp, int attack, int defense, int speed, double capture, int element) {
		this.id = id;
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
	}
}
