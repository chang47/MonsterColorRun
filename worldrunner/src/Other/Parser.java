package Other;

public class Parser {

	public String getPlayerName(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return arr[0];
	}
	
	public int getMoney(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[1]);
	}
	
	public int getGem(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[2]);
	}
	
	public String[] getEquipment(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] equipment = arr[3].split(",");
		if (equipment.length > 6) {
			throw new IllegalArgumentException("Equipment can't be greater than 5");
		}
		return equipment;
	}
	
	public String[] getInventory(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] inventory = arr[4].split(",");
		return inventory;
	}
	
	public String[] getFriends(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		String[] inventory = arr[5].split(",");
		return inventory;
	}
	
	public int getLevel(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[6]);
	}
	
	public int getExp(String str) {
		String[] arr = str.split("$");
		checkError(arr);
		return Integer.parseInt(arr[7]);
	}
	
	private void checkError(String[] arr) {
		if (arr.length == 0) {
			throw new IllegalArgumentException("Give no elements");
		}
		if (arr.length != 5) {
			throw new IllegalArgumentException("List has incorrect # of elements");
		}
	}
}
