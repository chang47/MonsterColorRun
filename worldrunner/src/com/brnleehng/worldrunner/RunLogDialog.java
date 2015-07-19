package com.brnleehng.worldrunner;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class RunLogDialog extends DialogFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Bundle bundle = this.getArguments();
		ArrayList<String> bundlelist = bundle.getStringArrayList("Log");

		View view = inflater.inflate(R.layout.runlog_activity, container, false);
        ArrayList<String> list = new ArrayList<String>();
        
        list.addAll(bundlelist);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        ListView listView = (ListView) view.findViewById(R.id.RunLogList);
        listView.setAdapter(adapter);
        
        Button exitButton = (Button) view.findViewById(R.id.exitButton);
        
        exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
        	
        });
		
		return view;
	}
	



}
