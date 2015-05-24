package com.brnleehng.worldrunner;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

// Hub for selecting a race (probably will be modified to be more generic)
public class Race extends Fragment{
	private Button freeRun;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.race_activity, container, false);
		freeRun = (Button) view.findViewById(R.id.freeRun);
		freeRun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Hub.selectFriend();
			}
		});
		return view;
	}  
}
