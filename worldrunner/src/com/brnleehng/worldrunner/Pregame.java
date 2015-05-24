package com.brnleehng.worldrunner;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.id;
import com.brnleehng.worldrunner.R.layout;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

// Select friends to run with to enter dungeons/routes
public class Pregame extends Fragment{
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final String Friends = "friendsKey";
    private SharedPreferences sharedpreferences;
    private Button runBtn;
    private CheckBox test1, test2, test3, test4, test5;


    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.pregame_activity, container, false);
		sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        runBtn = (Button) view.findViewById(R.id.run_button);
        test1 = (CheckBox) view.findViewById(R.id.test1);
        test2 = (CheckBox) view.findViewById(R.id.test2);
        test3 = (CheckBox) view.findViewById(R.id.test3);
        test4 = (CheckBox) view.findViewById(R.id.test4);
        test5 = (CheckBox) view.findViewById(R.id.test5);

        if(sharedpreferences.contains(Friends)){
            String  friendsList = sharedpreferences.getString(Friends, "");
        }

        runBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startRun();
               Hub.startRun();
            }
        });
		return view;
	}  

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void startRun(){
        StringBuffer OUTPUT = new StringBuffer();

        if(test1.isChecked()){
            OUTPUT.append(test1.getText());
        }

        if(test2.isChecked()){
            OUTPUT.append(" - " + test2.getText());
        }

        if(test3.isChecked()){
            OUTPUT.append(" - " + test3.getText());
        }

        if(test4.isChecked()){
            OUTPUT.append(" - " + test4.getText());
        }

        if(test5.isChecked()) {
            OUTPUT.append(" - " + test5.getText());
        }

        //Toast.makeText(Pregame.this, OUTPUT.toString(),
        //        Toast.LENGTH_LONG).show();
        //Intent intent = new Intent(getActivity(), Main.class);
        //startActivity(intent);
        //Hub.startRun();
    }
}
