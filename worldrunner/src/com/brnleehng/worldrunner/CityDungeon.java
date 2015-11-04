package com.brnleehng.worldrunner;

import util.TutorialTest;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import metaModel.City;
import metaModel.Dungeon;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class CityDungeon extends Fragment {
	public City city;
	public LinearLayout layout;
	private boolean firstTime;
	private SharedPreferences pref;
	private ShowcaseView showInfo;
	private ShowcaseView showSelectDungeon;
	private View view;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.citydungeon_activity, container, false);
		layout = (LinearLayout) view.findViewById(R.id.cityDungeonLayout);
		city = Hub.getCurrentCity();
		
		// lists dungeons
		for (final Dungeon dungeon : Hub.refDungeons.get(city.cityId)) {
			Log.v("showInfoStuff", "city Id" + city.cityId + " dungeon id " + dungeon.dungeonId + " dungeon name " + dungeon.dungeonName);
			Button button = new Button(getActivity());
			button.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			button.setId(dungeon.dungeonId);
			button.setText(dungeon.dungeonName);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (Hub.partySize() > 0) {
						Hub.startDungeonRun(dungeon);
					} else {
						Toast.makeText(getActivity(), "Add one monster to your team before you start!", Toast.LENGTH_SHORT).show();
					}
				}
			});
			
			layout.addView(button);
			
		}
		
		// firstTime = pref.getBoolean(getString(R.string.showDungeon), true);
		firstTime = TutorialTest.showDungeon;
		view.post(new Runnable() {

			@Override
			public void run() {
				if (firstTime && Hub.currentCity.cityId == 1 && Hub.partySize() > 0) {
					showInfo = new ShowcaseView.Builder(getActivity())
					.setTarget(new ViewTarget(view.findViewById(1)))
					.setContentText("Showcase View")
					.setContentText("Click edit party to edit your party")
					.build();
					
					showInfo.overrideButtonClick(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showInfo.hide();
							commonHide(showInfo);
							((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showInfo);

							showSelectDungeon = new ShowcaseView.Builder(getActivity())
							.setTarget(new ViewTarget(view.findViewById(1)))
							.setContentText("Showcase View")
							.setContentText("Click edit party to edit your party")
							.build();
							
							showSelectDungeon.hideButton();
							showSelectDungeon.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									showSelectDungeon.hide();
									commonHide(showSelectDungeon);
									((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showSelectDungeon);
									TutorialTest.showDungeon = false;
									Hub.startDungeonRun(Hub.refDungeons.get(1).get(0));
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
