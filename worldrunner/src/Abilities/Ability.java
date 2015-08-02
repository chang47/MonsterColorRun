package Abilities;

public abstract class Ability {
	public String name;
	public String description;
	public int level;
	public int steps;
	public int abilityId;
	
	public Ability(String name, String description, int level, int steps, int abilityId) {
		this.name = name;
		this.description = description;
		this.level = level;
		this.steps = steps;
		this.abilityId = abilityId;
	}
	
	public abstract double activateAbility();
}
