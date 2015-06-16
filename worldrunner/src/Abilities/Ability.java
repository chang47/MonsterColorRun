package Abilities;

public abstract class Ability {
	public String name;
	public String description;
	public int level;
	public int steps;
	
	public Ability(String name, String description, int level, int steps) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.steps = steps;
	}
	
	public abstract double activateAbility();
}
