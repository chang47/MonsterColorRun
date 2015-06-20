package com.brnleehng.worldrunner;

import java.util.ArrayList;

import DB.Model.City;
import DB.Model.MapGraph;
import DB.Model.Route;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CityHub extends Fragment {
	public SparseArray<ArrayList<Route>> cityRoutes;
	private Button dungeon;
	private Button move;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.cityhub_activity, container, false);
		dungeon = (Button) view.findViewById(R.id.dungeonBut);
		move = (Button) view.findViewById(R.id.moveBut);
		// receives the current city ID
		City currentCity = Hub.currentCity;
		Log.d("current city", "" + currentCity.cityName);
		TextView cityName = (TextView) view.findViewById(R.id.currentCity);
		cityName.setText("Welcome to: " + currentCity.cityName);
		
		// finds the nearest city to travel to
		// TODO crashes probably MapGraph not used anymore
		//ArrayList<Integer> cityList = MapGraph.nearbyCities(currentCityID);
		
		// moves to screen with multiple dungeons
		dungeon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.selectDungeons();
			}
		});
		
		// moves to screen with multiple routes to city
		move.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.selectRoute();
			}
		});
		/**
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.cityContainer);
		for (int i : cityList) {
			Button btn = new Button(getActivity());
			btn.setId(i);
			final int id_ = btn.getId();
			btn.setText("Go to " + id_);
			btn.setBackgroundColor(Color.rgb(70, 80, 90));
			layout.addView(btn);
			
			// TODO: See if necessary to recall the button
			Button btn1 = (Button) view.findViewById(id_);
			btn1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Hub.changeCity(v.getId());
				}
			});
		}
		*/
		return view;
	}  
	
}
