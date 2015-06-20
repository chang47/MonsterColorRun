package DB.Model;

import java.util.ArrayList;

// Dungeons are locations in cities that offer
// unique specific monsters that can be grinded and captured.
public class Dungeon {
	public int dungeonId;
	public String dungeonName;
	public int cityId;
	public ArrayList<Monster> monsters;
	
	// empty constructors are evil!!!!
	public Dungeon(int id, String name, int city) {
		this.dungeonId = id;
		this.dungeonName = name;
		this.cityId = city;
		monsters = new ArrayList<Monster>();
	}
}
