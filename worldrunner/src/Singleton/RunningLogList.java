package Singleton;

import java.util.List;

import DB.Model.RunningLog;

public class RunningLogList {
	private static List<RunningLog> list = null;
	
	public RunningLogList(List<RunningLog> list) {
		RunningLogList.list = list;
	}
	
	public static List<RunningLog> getList() {
		return list;
	}
	
}
