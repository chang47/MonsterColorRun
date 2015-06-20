package DB.Model;

import java.util.ArrayList;

/**
 * The places that players travel around to. Each city are connected
 * by routes where they meet other monsters and containt their own
 * dungeons where unique monsters can be found
 */
public class City {
	public int cityId;
	public String cityName;
	public ArrayList<Route> routes;
	public ArrayList<Dungeon> dungeons;
	//public Boss boss;
	
	public City(int id, String name) {
		cityId = id;
		cityName = name;
		this.routes = new ArrayList<Route>();
		this.dungeons = new ArrayList<Dungeon>();
	}
	
}
