package com.brnleehng.worldrunner;

import intro.NameRequest;

import com.brnleehng.worldrunner.R;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


// Fragment for Admin database control (drop and create a db)
public class Database extends Fragment {
	private static final String DATABASE_NAME = "Player";
	private static final String DATABASE_REFERENCE_NAME = "reference";
	private static SharedPreferences pref;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.database_activity, container, false);
		Button createDB = (Button) view.findViewById(R.id.createDB);

		pref = getActivity().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		createDB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// DB.CreateDB
				Hub.createDB();
				//Hub.createChanges();
			}
		});
		
		Button deleteDB = (Button) view.findViewById(R.id.deleteDB);
		deleteDB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().deleteDatabase(DATABASE_NAME);
				getActivity().deleteDatabase(DATABASE_REFERENCE_NAME);
				Toast.makeText(getActivity(), "deleted database", Toast.LENGTH_LONG).show();
			}
		});
		
		Button freeRun = (Button) view.findViewById(R.id.freeRunTest);
		freeRun.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (Hub.partySize() > 0) {
				// TestRun.java
					Hub.startRun();
				}
				
			}
		});
		
		Button resetNew = (Button) view.findViewById(R.id.makeNewPlayer);
		resetNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				SharedPreferences.Editor editor = pref.edit();
				editor.putBoolean(getString(R.string.firstTime), true);
				editor.commit();
			}
		});
		return view;
	}  
}
