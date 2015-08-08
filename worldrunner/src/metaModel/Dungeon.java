package metaModel;

import java.util.ArrayList;

import DB.Model.Monster;

// Dungeons are locations in cities that offer
// unique specific monsters that can be grinded and captured.
public class Dungeon {
	public int dungeonId;
	public int cityId;
	public String dungeonName;
	public String description;
	public int min;
	public int max;
	public ArrayList<Monster> monsters;
	
	public Dungeon(int id, int cityId, String name, String description, int min, int max) {
		this.dungeonId = id;
		this.cityId = cityId;
		this.dungeonName = name;
		this.description = description;
		this.min = min;
		this.max = max;
		monsters = new ArrayList<Monster>();
	}
}
