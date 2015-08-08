package metaModel;

public class DungeonMonsters {
	public int id;
	public int monsterId;
	public int dungeonId;
	public int capture;
	public int level;
	
	public DungeonMonsters(int id, int monsterId, int dungeonId, int capture,
			int level) {
		this.id = id;
		this.monsterId = monsterId;
		this.dungeonId = dungeonId;
		this.capture = capture;
		this.level = level;
	}
}
