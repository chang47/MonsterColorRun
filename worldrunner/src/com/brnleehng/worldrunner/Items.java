package com.brnleehng.worldrunner;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

// Framgent to display user's sticker/equipment options
public class Items extends Fragment {
	Button mViewEquipment;
	Button mEquipEquipment;
	Button mSellEquipment;
	Button mViewSticker;
	Button mEquipSticker;
	Button mSellSticker;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.items_activity, container, false);
		mViewEquipment = (Button) view.findViewById(R.id.viewEquipment);
		mEquipEquipment = (Button) view.findViewById(R.id.equipEquipment);
		mSellEquipment = (Button) view.findViewById(R.id.sellEquipment);
		mViewSticker = (Button) view.findViewById(R.id.viewSticker);
		mEquipSticker = (Button) view.findViewById(R.id.equipSticker);
		mSellSticker = (Button) view.findViewById(R.id.sellSticker);
		
		mViewEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.viewEquipment();
			}
		});
		
		mViewSticker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.viewSticker();
			}
		});
		
		mSellEquipment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.sellEquipment();
			}
		});
		
		mSellSticker.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.sellSticker();
			}
		});
		
		mEquipEquipment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.equipItems();
			}
		});
		
		return view;
	}  
}
