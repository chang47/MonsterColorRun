package com.brnleehng.worldrunner;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.id;
import com.brnleehng.worldrunner.R.layout;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class FooterBar extends Fragment {
	private Button race;
	private Button items;
	private Button store;
	private Button friends;
	private Button db;
	private Button city;
	
	
	@Override
	public LinearLayout onCreateView(LayoutInflater inflate, ViewGroup container,
			Bundle savedInstance) {
		final LinearLayout view = (LinearLayout) 
				inflate.inflate(R.layout.footer_activity, container, false);
		//final LinearLayout layout = (LinearLayout) inflate.inflate(resource, root)
		race = (Button) view.findViewById(R.id.menuRace);
		race.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.race();
			}
		});
		
		items = (Button) view.findViewById(R.id.menuItems);
		items.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.items();
			}
		});
		
		store = (Button) view.findViewById(R.id.menuStore);
		store.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.store();
			}
		});
		
		friends = (Button) view.findViewById(R.id.menuFriends);
		friends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.friends();
			}
		});
		
		db = (Button) view.findViewById(R.id.menuDB);
		db.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.databaseOptions();
			}
		});
		
		city = (Button) view.findViewById(R.id.city);
		city.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.cityHub();
			}
		});
		return view;
	}
}
