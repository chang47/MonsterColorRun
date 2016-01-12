package DB.Model;

public class RunningLog implements Comparable<RunningLog> {
	private int steps;
	private int day;
	private int month;
	private int year;
	
	public RunningLog(int steps, int day, int month, int year) {
		this.steps = steps;
		this.day = day;
		this.month = month;
		this.year = year;
	}
	
	public int getSteps() {
		return steps;
	}

	public void setSteps(int steps) {
		this.steps = steps;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}
	
	@Override
	public int compareTo(RunningLog other) {
		if (this.year > other.year) {
			return 1;
		} else if (this.month > other.month) {
			return 1;
		} else if (this.day > other.day) {
			return 1;
		} else if (this.day == other.day && this.month == other.month && this.year == other.year) {
			return 0;
		} else {
			return -1;
		}
	}
	
}
