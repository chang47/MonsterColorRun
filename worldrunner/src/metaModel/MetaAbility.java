package metaModel;

public class MetaAbility {
	public int id;
	public int type;
	public String name;
	public String description;
	public int steps;
	public int attribute;
	public double modifier;
	public int duration;
	
	public MetaAbility(int id, int type, String name, String description, int steps,
			int attribute, double modifier, int duration) {
		this.id = id;
		this.type = type;
		this.name = name;
		this.description = description;
		this.steps = steps;
		this.attribute = attribute;
		this.modifier = modifier;
		this.duration = duration;
	}
}
