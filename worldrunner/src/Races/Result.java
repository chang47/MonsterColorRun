package Races;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class Result extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.result_activity, container, false);
		Button done = (Button) view.findViewById(R.id.resultDone);
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.backToRace();
				
			}
		});
		
		return view;
	}

}
