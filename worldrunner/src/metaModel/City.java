package metaModel;

import java.util.ArrayList;

/**
 * The places that players travel around to. Each city are connected
 * by routes where they meet other monsters and contain their own
 * dungeons where unique monsters can be found
 */
public class City {
	public int cityId;
	public String cityName;
	public String description;
	//public Boss boss;
	
	public City(int id, String name, String description) {
		this.cityId = id;
		this.cityName = name;
		this.description = description;
	}
	
}
