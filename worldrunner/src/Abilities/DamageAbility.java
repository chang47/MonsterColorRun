package Abilities;

public class DamageAbility extends Ability {
	public double damage;
	public int attributes;

	public DamageAbility(String name, String description, int level, int steps, double damage, int attributes) {
		super(name,description,level,steps);
		this.damage = damage;
		this.attributes = attributes;
		// TODO Auto-generated constructor stub
	}

	@Override
	public double activateAbility() {
		
		
		return damage * level;
	}
	
}