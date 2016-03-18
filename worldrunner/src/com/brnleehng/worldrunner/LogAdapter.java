package com.brnleehng.worldrunner;

import java.util.List;

import DB.Model.Monster;
import DB.Model.RunningLog;
import android.content.Context;
import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LogAdapter extends ArrayAdapter<RunningLog> {
	Context context;
	LayoutInflater inflater;
	List<RunningLog> list;
	
	public LogAdapter(Context context, int resourceID, List<RunningLog> list) {
		super(context, resourceID, list);
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}
	
	private class ViewHolder {
		TextView date;
		TextView steps;
	}
	
	@Override
 	// Creates the actual object that the user sees
 	public View getView(int position, View view, ViewGroup parent) {
 		final ViewHolder holder;
 		
 		if (view == null) {
 			holder = new ViewHolder();
 			// TODO change the list
 			view = inflater.inflate(R.layout.log_adapter_list, null);
 			holder.date = (TextView) view.findViewById(R.id.icon);
 			view.setTag(holder);
 		} else {
 			holder = (ViewHolder) view.getTag();
 		}
		 
		// checks for the null remove sticker case
	
		//holder.txtTitle.setText(monsterList.get(position).name);
		holder.date = (TextView) view.findViewById(R.id.logDate);
		holder.steps = (TextView) view.findViewById(R.id.logSteps);
		RunningLog log = list.get(position);
		holder.date.setText(log.getMonth() + "/" + log.getDay() + "/" + log.getYear());
		holder.steps.setText(log.getSteps());
		return view;
 	}

}
