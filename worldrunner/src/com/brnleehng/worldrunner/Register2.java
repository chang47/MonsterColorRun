package com.brnleehng.worldrunner;

import DB.DBManager;
import android.os.AsyncTask;

import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.R.id;
import com.brnleehng.worldrunner.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Random;

// Selecting new user page
public class Register2 extends Activity {
	private TextView greeting;
	private Button roadGear;
	private Button hillGear;
	private Button trailGear;
	public static final String PREFS_NAME = "MyPrefsFile";
    public static final String ID = "idKey";
    public static final String NAME = "nameKey";
    private static final String DATABASE_NAME = "Player";
    private Random r;

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
        setContentView(R.layout.register2_activity);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
        	throw new IllegalArgumentException("No data passed in user creation");
        }
        final String username = extras.getString("username");
        final String firstName = extras.getString("firstname");
        final String lastName = extras.getString("lastname");
        greeting = (TextView) findViewById(R.id.placeholder);
        greeting.setText("Hi " + username);
        
        roadGear = (Button) findViewById(R.id.roadStarter);
        hillGear = (Button) findViewById(R.id.hillStarter);
        trailGear = (Button) findViewById(R.id.trailStarter);
        roadGear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//makeUser(username, firstName, lastName);
				//deleteDatabase(DATABASE_NAME);
				DBManager db = new DBManager(getApplicationContext());
				db.close();
				Intent intent = new Intent(Register2.this, Hub.class);
				Register2.this.startActivity(intent);
			}
		});
        
        hillGear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "not avilable yet", 
						Toast.LENGTH_LONG).show();
			}
		});
        
        trailGear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Toast.makeText(getApplicationContext(), "not avilable yet", 
						Toast.LENGTH_LONG).show();
			}
		});
        
        
    }
    
    private void makeUser(String userName, String firstName, String lastName){
    	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
        Toast.makeText(getBaseContext(), "Make User function " + userName, Toast.LENGTH_SHORT).show();
        try{
            String URL = "http://54.148.77.196/phptest.php?action=register" +
                    "&firstname=" + firstName + "&lastname=" + lastName + "&username=" + userName;
            CompleteRegister task = new CompleteRegister();
            task.execute(new String[] {URL});
            Toast.makeText(getBaseContext(), URL, Toast.LENGTH_SHORT).show();
        }
        catch(Exception ex){
            Toast.makeText(getBaseContext(), "Response Failed.", Toast.LENGTH_SHORT).show();
        }

		//@TODO: make list of string commits
		//editor.putString("first_time", false);
		//editor.pushString("player", name + "$0$0$1,2,3,4,5$1,2,3,4,5$$1$0"
		// friends: name$money$runningspeed$level$exp
        //editor.putInt(ID, userId);
		editor.putString(NAME, userName);
		editor.commit();
		//Intent intent = new Intent(Register2.this, Race.class);
		//Register2.this.startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private class CompleteRegister extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            greeting.setText(result);
            Toast.makeText(getBaseContext(), "onPostExecute " + result, Toast.LENGTH_SHORT).show();
        }
    }

}
