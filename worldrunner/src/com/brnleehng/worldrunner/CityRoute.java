package com.brnleehng.worldrunner;

import metaModel.City;
import metaModel.Dungeon;
import metaModel.Route;
import android.app.Fragment;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class CityRoute extends Fragment{
	public City city;
	public LinearLayout layout;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.cityroute_activity, container, false);
		layout = (LinearLayout) view.findViewById(R.id.cityRouteLayout);
		city = Hub.getCurrentCity();
		
		// lists dungeons
		for (final Route route : Hub.refRoutes.get(city.cityId)) {
			Button button = (Button) inflater.inflate(R.layout.template_button, container, false);
			//button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setId(route.id);
			button.setText(route.name);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Hub.partySize() > 0) {
						// RouteRun
						Hub.startRouteRun(route);
					} else {
						Toast.makeText(getActivity(), "Add one monster to your team before you start!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			layout.addView(button);
		}
		return view;
	}
}
