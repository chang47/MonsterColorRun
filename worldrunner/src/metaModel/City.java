package metaModel;

import java.util.ArrayList;

/**
 * The places that players travel around to. Each city are connected
 * by routes where they meet other monsters and containt their own
 * dungeons where unique monsters can be found
 */
public class City {
	public int cityId;
	public String cityName;
	public String description;
	public ArrayList<Route> routes;
	public ArrayList<Dungeon> dungeons;
	//public Boss boss;
	
	public City(int id, String name, String description) {
		this.cityId = id;
		this.cityName = name;
		this.description = description;
		this.routes = new ArrayList<Route>();
		this.dungeons = new ArrayList<Dungeon>();
	}
	
}
