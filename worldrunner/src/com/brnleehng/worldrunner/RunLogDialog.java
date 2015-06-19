package com.brnleehng.worldrunner;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
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
		
		return view;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog  = super.onCreateDialog(savedInstanceState);
		
		
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		return dialog;
	}

}
