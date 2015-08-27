package Abilities;

public class DamageAllAbility extends Ability {
	public double damage;
	public int attributes;

	public DamageAllAbility(String name, String description, int level, int steps, double damage, int attributes, int abilityId) {
		super(name,description,level,steps, abilityId);
		this.damage = damage;
		this.attributes = attributes;
		// TODO Auto-generated constructor stub
	}

	//@Override
	
	// TODO 
	public double activateAbility() {
		
		
		return damage * level;
	}
	
}