package com.brnleehng.worldrunner;

import java.util.ArrayList;

import util.TutorialTest;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import metaModel.City;
import metaModel.Route;
import DB.Model.MapGraph;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;

public class CityHub extends Fragment {
	public SparseArray<ArrayList<Route>> cityRoutes;
	private Button dungeon;
	private Button move;
	private SharedPreferences pref;
	private boolean firstTime; 
	private boolean secondTime;
	private ShowcaseView showMoveout;
	private ShowcaseView showDungeon;
	private ShowcaseView showTeam;
	
	private ShowcaseView showExplain;
	private ShowcaseView showPickDungeon;
	
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
		
		
		// first time interactive tutorial
		pref = getActivity().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		firstTime = pref.getBoolean(getString(R.string.firstTime), true);
		secondTime = pref.getBoolean(getString(R.string.secondTime), false);
		
		// needed so that the app won't crash when SV targets something that 
		// doesn't exist
		view.post(new Runnable() {
			@Override
			public void run() {
				if (firstTime) {
					ViewTarget target = new ViewTarget(R.id.moveBut, getActivity());
					showMoveout = new ShowcaseView.Builder(getActivity())
						.setTarget(target)
						.setContentText("Showcase View")
						.setContentText("this is highlighting the home button")
						.build();
					
					showMoveout.hideButton();
				
					showMoveout.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showMoveout.hide();
							commonHide(showMoveout);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showMoveout);
							ViewTarget target = new ViewTarget(R.id.dungeonBut, getActivity());
							showDungeon = new ShowcaseView.Builder(getActivity())
								.setTarget(target)
								.setContentText("Showcase View")
								.setContentText("this is highlighting the home button")
								.build();
							
							showDungeon.hideButton();
							
							showDungeon.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showDungeon.hide();
									commonHide(showDungeon);
									((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showDungeon);
									ViewTarget target = new ViewTarget(R.id.menuItems, getActivity());
									showTeam = new ShowcaseView.Builder(getActivity())
										.setTarget(target)
										.setContentText("Showcase View")
										.setContentText("this is highlighting the home button")
										.build();
									
									showTeam.hideButton();
									
									showTeam.setOnClickListener(new View.OnClickListener() {
										
										@Override
										public void onClick(View v) {
											showTeam.hide();
											commonHide(showTeam);
											((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showTeam);
											pref.edit().putBoolean(getString(R.string.firstTime), false).apply();
											TutorialTest.cityHub = false;
											Hub.items();
										}
									});
								}
							});
						}
					});
				} else if (secondTime && Hub.partySize() > 0) {
					showExplain = new ShowcaseView.Builder(getActivity())
					.setTarget(new ViewTarget(R.id.dungeonBut, getActivity()))
					.setContentText("Showcase View")
					.setContentText("this is highlighting the home button")
					.build();
					
					showExplain.overrideButtonClick(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showExplain.hide();
							commonHide(showExplain);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showExplain);
							showPickDungeon = new ShowcaseView.Builder(getActivity())
							.setTarget(new ViewTarget(R.id.dungeonBut, getActivity()))
							.setContentText("Showcase View")
							.setContentText("this is highlighting the home button")
							.build();
							
							showPickDungeon.hideButton();
							
							showPickDungeon.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showPickDungeon.hide();
									commonHide(showPickDungeon);
									((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showPickDungeon);
									pref.edit().putBoolean(getString(R.string.secondTime), false).apply();
									pref.edit().putBoolean(getString(R.string.showDungeon), true).apply();
									Hub.selectDungeons();
								}
							});
						}
					});
				}
			}
		});
		
		
		return view;
	}  
	
	public static void commonHide(ShowcaseView scv) {
		scv.setOnClickListener(null);
		scv.setOnShowcaseEventListener(null);
		scv.setOnTouchListener(null);
	}
	
}
