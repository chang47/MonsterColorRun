package com.brnleehng.worldrunner;

import DB.Model.City;
import DB.Model.Dungeon;
import DB.Model.Route;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CityRoute extends Fragment{
	public City city;
	public LinearLayout layout;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cityroute_activity, container, false);
		layout = (LinearLayout) view.findViewById(R.id.cityRouteLayout);
		city = Hub.getCurrentCity();
		
		// lists dungeons
		for (final Route route : city.routes) {
			Button button = new Button(getActivity());
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setText(route.routeName);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Hub.startRouteRun(route);
				}
			});
			
			layout.addView(button);
		}
		return view;
	}
}
