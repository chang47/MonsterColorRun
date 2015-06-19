package Abilities;

public class Buff {
	
	public String name;
	public String description;
	public int duration;
	public int effect;
	public double modifier;
	
	public Buff(String name, String description, int duration, int effect, double modifier) {
	this.name = name;
	this.description = description;
	this.duration = duration;
	this.effect = effect;
	this.modifier = modifier;
	}

}
