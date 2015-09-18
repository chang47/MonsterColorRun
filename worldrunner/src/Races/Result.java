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
import android.widget.RelativeLayout;

public class Result extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.result_activity, container, false);
		RelativeLayout done = (RelativeLayout) view.findViewById(R.id.overallResultContainer);
		done.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Hub.backToCity();				
			}
		});
		
		return view;
	}

}
