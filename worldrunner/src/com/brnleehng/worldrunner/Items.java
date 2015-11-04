package com.brnleehng.worldrunner;

import util.TutorialTest;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

// Framgent to display user's sticker/equipment options
public class Items extends Fragment {
	Button mViewEquipment;
	Button mEquipItems;
	Button mSellEquipment;
	Button mViewSticker;
	Button mEquipSticker;
	Button mSellSticker;
	private SharedPreferences pref;
	private boolean firstTime;
	private ShowcaseView showItems;
	private ShowcaseView showEquip;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.items_activity, container, false);
		// mViewEquipment = (Button) view.findViewById(R.id.viewEquipment);
		mEquipItems = (Button) view.findViewById(R.id.equipEquipment);
		// mSellEquipment = (Button) view.findViewById(R.id.sellEquipment);
		mViewSticker = (Button) view.findViewById(R.id.viewSticker);
		// mEquipSticker = (Button) view.findViewById(R.id.equipSticker);
		mSellSticker = (Button) view.findViewById(R.id.sellSticker);
		
/*		mViewEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.viewEquipment();
			}
		});
		*/
		mViewSticker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.viewSticker();
			}
		});
		
/*		mSellEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.sellEquipment();
			}
		});*/
		
		mSellSticker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.sellSticker();
			}
		});
		
		mEquipItems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipItems();
			}
		});
		
		pref = getActivity().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		//firstTime = pref.getBoolean(getString(R.string.viewMonster), true);
		firstTime = TutorialTest.items;

		showItems = new ShowcaseView.Builder(getActivity())
			.setTarget(new ViewTarget(view.findViewById(R.id.sellSticker)))
			.setContentText("Showcase View")
			.setContentText("Click edit party to edit your party")
			.build();
		
		showItems.overrideButtonClick(new View.OnClickListener() {
			 
			@Override
			public void onClick(View v) {
				showItems.hide();
				commonHide(showItems);
				((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showItems);
				ViewTarget target = new ViewTarget(R.id.equipEquipment, getActivity());
				showEquip = new ShowcaseView.Builder(getActivity())
				.setTarget(target)
				.setContentText("Showcase View")
				.setContentText("Equip party")
				.build();
				
				showEquip.hideButton();
				
				showEquip.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showEquip.hide();
						commonHide(showEquip);
						((ViewGroup)getActivity().getWindow().getDecorView()).removeView(showEquip);
						//pref.edit().putBoolean(getString(R.string.viewMonster), false).apply();
						TutorialTest.items = false;
						Hub.equipItems();
					}
				});
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
