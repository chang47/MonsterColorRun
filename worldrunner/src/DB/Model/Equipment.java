package DB.Model;

// Equipment that the character can attach to themselves.
// Improves their stats and other features.
//TODO Needs to be re-adusted but for beta, just keep them out.
public class Equipment implements Comparable<Equipment> {
	private int etid;
	private int pid;
	private int eid;
	private int pstid;
	private String name;
	private int category;
	private int equipped;
	private int leader;
	private int current_level;
	private int current_exp;
	private int current_speed;
	private int current_reach;
	private int eaid;
	
	public Equipment(int etid, int pid, int eid, int pstid, String name,
			int category, int equipped, int leader, int current_level,
			int current_exp, int current_speed, int current_reach, int eaid) {
		super();
		this.etid = etid;
		this.pid = pid;
		this.eid = eid;
		this.pstid = pstid;
		this.name = name;
		this.category = category;
		this.equipped = equipped;
		this.leader = leader;
		this.current_level = current_level;
		this.current_exp = current_exp;
		this.current_speed = current_speed;
		this.current_reach = current_reach;
		this.eaid = eaid;
	}
	
	public Equipment() { }
	
	
	public int getEtid() {
		return etid;
	}
	public void setEtid(String etid) {
		this.etid = Integer.parseInt(etid);
	}
	public int getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = Integer.parseInt(pid);
	}
	public int getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = Integer.parseInt(eid);
	}
	public int getPstid() {
		return pstid;
	}
	public void setPstid(String pstid) {
		this.pstid = Integer.parseInt(pstid);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = Integer.parseInt(category);
	}
	public int getEquipped() {
		return equipped;
	}
	public void setEquipped(String equipped) {
		this.equipped = Integer.parseInt(equipped);
	}
	public int getLeader() {
		return leader;
	}
	public void setLeader(String leader) {
		this.leader = Integer.parseInt(leader);
	}
	public int getCurrent_level() {
		return current_level;
	}
	public void setCurrent_level(String current_level) {
		this.current_level = Integer.parseInt(current_level);
	}
	public int getCurrent_exp() {
		return current_exp;
	}
	public void setCurrent_exp(String current_exp) {
		this.current_exp = Integer.parseInt(current_exp);
	}
	public int getCurrent_speed() {
		return current_speed;
	}
	public void setCurrent_speed(String current_speed) {
		this.current_speed = Integer.parseInt(current_speed);
	}
	public int getCurrent_reach() {
		return current_reach;
	}
	public void setCurrent_reach(String current_reach) {
		this.current_reach = Integer.parseInt(current_reach);
	}
	public int getEaid() {
		return eaid;
	}
	public void setEaid(String eaid) {
		this.eaid = Integer.parseInt(eaid);
	}

	@Override
	public int compareTo(Equipment another) {
		return another.getCategory() - this.getCategory();
	}
	
	@Override
	public boolean equals(Object other) {
		Equipment otherEquipment = (Equipment) other;
		return otherEquipment.etid == this.getEtid();
	}
	
}
