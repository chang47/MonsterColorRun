package Abilities;

public class SupportAbility extends Ability {
	
	public double modifier;
	public int attribute;
	public int duration;

	public SupportAbility(String name, String description, int level, int steps, double modifer, int attribute,int duration, int abilityId) {
		super(name, description, level, steps, abilityId);
		// TODO Auto-generated constructor stub
		this.modifier = modifer;
		this.attribute = attribute;
		this.duration = duration;
		
		//1 = Attack
		//2 = Defense
		//3 = Speed
	}

	@Override
	public double activateAbility() {
		// TODO Auto-generated method stub
		return modifier * level;
	}
	
}