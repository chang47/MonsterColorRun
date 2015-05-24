package DB.Model;

// Dungeons are locations in cities that offer
// unique specific monsters that can be grinded and captured.
public class Dungeon {
	public int dungeonId;
	public String dungeonName;
	public int cityId;
	
	public Dungeon() { }
	
	public Dungeon(int id, String name, int city) {
		this.dungeonId = id;
		this.dungeonName = name;
		this.cityId = city;
	}
}
