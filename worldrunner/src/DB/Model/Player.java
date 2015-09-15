package DB.Model;

// Model representing the players
public class Player {
	public int pid;
	public String username;
	public String fname;
	public String lname;
	public int level;
	public int exp;
	public int coin;
	public int gem;
	public int currentSticker;
	public int maxSticker;
	public int city;
	
	public Player() { }
	
	public Player(int pid, String username, String fname, String lname,
			int level, int exp, int coin, int gem, int currentSticker, int maxSticker,
			int city) {
		super();
		this.pid = pid;
		this.username = username;
		this.fname = fname;
		this.lname = lname;
		this.level = level;
		this.exp = exp;
		this.coin = coin;
		this.gem = gem;
		this.currentSticker = currentSticker;
		this.maxSticker = maxSticker;
		this.city = city;
	}
	/*
	public int getPid() {
		return pid;
	}
	public void setPid(String pid) {
		this.pid = Integer.parseInt(pid);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = Integer.parseInt(level);
	}
	public int getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = Integer.parseInt(exp);
	}
	public int getCoin() {
		return coin;
	}
	public void setCoin(String coin) {
		this.coin = (coin);
	}
	public int getGem() {
		return gem;
	}
	public void setGem(int gem) {
		this.gem = gem;
	}
	public int getCurrentSticker() {
		return currentSticker;
	}
	public void setCurrentSticker(int currentSticker) {
		this.currentSticker = currentSticker;
	}
	public int getMaxSticker() {
		return maxSticker;
	}
	public void setMaxSticker(int maxSticker) {
		this.maxSticker = maxSticker;
	}
	
	public void setCity(int city) {
		this.city = city;
	}
	
	public int getCity() {
		return city;
	}
	*/
	
}
