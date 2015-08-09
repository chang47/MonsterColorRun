package metaModel;

public class RouteMonsters {
	public int id;
	public int monsterId;
	public int routeId;
	public int capture;
	public int level;
	public int monsterRouteId;
	
	public RouteMonsters(int id, int monsterId, int routeId, int capture,
			int level, int monsterRouteId) {
		this.id = id;
		this.monsterId = monsterId;
		this.routeId = routeId;
		this.capture = capture;
		this.level = level;
		this.monsterRouteId = monsterRouteId;
	}
	
}
