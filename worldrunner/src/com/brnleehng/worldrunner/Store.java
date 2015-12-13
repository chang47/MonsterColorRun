package com.brnleehng.worldrunner;

import com.brnleehng.worldrunner.R.id;

import DB.DBManager;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.Button;
import android.widget.Toast;

// The store to buy equipment and stickers
public class Store extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.store_activity, container, false);
		
		Button buy = (Button) view.findViewById(id.buyMonster);
		buy.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				DBManager db = new DBManager(getActivity());
				int[] res = db.buyMonster(Hub.player);
				if (res[0] == 0) {
					Toast.makeText(getActivity(), "illegal transaction hacker!", Toast.LENGTH_SHORT).show();
				} else if (res[0] == 1) {
					Hub.player = db.getPlayer().get(0); //TODO code needs to be changed
					Toast.makeText(getActivity(), "purchase passed, you have " + Hub.player.gem +
							" gems monster returned is " + res[1], Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "Problem! Multiple transactions were made", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}  
}
