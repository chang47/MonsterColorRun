package com.brnleehng.worldrunner;

import java.util.ArrayList;

import metaModel.City;
import metaModel.Route;
import DB.Model.MapGraph;
import android.app.Fragment;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
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
		final SoundPool sp = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
		final int soundIds[] = new int[2];
		soundIds[0] = sp.load(getActivity().getBaseContext(), R.raw.click, 1);
		soundIds[1] = sp.load(getActivity().getBaseContext(), R.raw.enterbattle, 1);
		
		View view = inflater.inflate(R.layout.cityhub_activity, container, false);
		dungeon = (Button) view.findViewById(R.id.dungeonBut);
		move = (Button) view.findViewById(R.id.moveBut);
		// receives the current city ID
		City currentCity = Hub.currentCity;
		Log.d("current city", "" + currentCity.cityName + " description " + currentCity.description );
		TextView cityName = (TextView) view.findViewById(R.id.currentCity);
		cityName.setText("Welcome to: " + currentCity.cityName);

		// moves to screen with multiple dungeons
		dungeon.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sp.play(soundIds[0], 1, 1, 1, 0, 1);
				Hub.selectDungeons();
			}
		});
		
		// moves to screen with multiple routes to city
		move.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sp.play(soundIds[0], 1, 1, 1, 0, 1);
				Hub.selectRoute();
			}
		});
		return view;
	}  
	
}
