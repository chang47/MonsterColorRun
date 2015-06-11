package com.brnleehng.worldrunner;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

// Initial start page
public class Register extends Activity {
    SharedPreferences sp;
	private Button submitNameButton;
	private EditText usernameBox;
	private EditText firstnameBox;
	private EditText lastnameBox;
    private Random r;
    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String ID = "idKey";
    public static final String NAME = "nameKey";
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        if(sp.contains(ID) && sp.contains(NAME)){
            Intent goHome = new Intent(Register.this, FreeRun.class);
            Register.this.startActivity(goHome);
        }

        setContentView(R.layout.register_activity);
        usernameBox = (EditText) findViewById(R.id.txtUsername);
        firstnameBox = (EditText) findViewById(R.id.txtFName);
        lastnameBox = (EditText) findViewById(R.id.txtLName);
        submitNameButton = (Button) findViewById(R.id.submitNameButton);
        submitNameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = usernameBox.getText().toString();
				if (username.equals("")) {
					Toast.makeText(getApplicationContext(), 
							"Please enter a name", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(Register.this, Register2.class);
					intent.putExtra("username", username);
					Register.this.startActivity(intent);
				}
				
				String firstname = firstnameBox.getText().toString();
				if (username.equals("")) {
					Toast.makeText(getApplicationContext(), 
							"Please enter a name", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(Register.this, Register2.class);
					intent.putExtra("firstname", username);
					Register.this.startActivity(intent);
				}
				
				String lastname = lastnameBox.getText().toString();
				if (username.equals("")) {
					Toast.makeText(getApplicationContext(), 
							"Please enter a name", Toast.LENGTH_LONG).show();
				} else {
					Intent intent = new Intent(Register.this, Register2.class);
					intent.putExtra("lastname", username);
					Register.this.startActivity(intent);
				}
			}
		});
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
