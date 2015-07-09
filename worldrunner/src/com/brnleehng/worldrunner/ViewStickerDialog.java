package com.brnleehng.worldrunner;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ViewStickerDialog extends DialogFragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Bundle bundle = this.getArguments();
		View view = inflater.inflate(R.layout.viewstickerdialog_activity, container, false);
        
        Button exitButton = (Button) view.findViewById(R.id.viewExitButton);
        
        exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
        });
		return view;
	}
}
