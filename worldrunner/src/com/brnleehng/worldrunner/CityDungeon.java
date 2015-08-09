package com.brnleehng.worldrunner;

import metaModel.City;
import metaModel.Dungeon;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class CityDungeon extends Fragment {
	public City city;
	public LinearLayout layout;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.citydungeon_activity, container, false);
		layout = (LinearLayout) view.findViewById(R.id.cityDungeonLayout);
		city = Hub.getCurrentCity();
		
		
		// lists dungeons
		for (final Dungeon dungeon : city.dungeons) {
			Button button = new Button(getActivity());
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setText(dungeon.dungeonName);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Hub.partySize() > 0)
						Hub.startDungeonRun(dungeon);
				}
			});
			
			layout.addView(button);
		}
		return view;
	}
}
