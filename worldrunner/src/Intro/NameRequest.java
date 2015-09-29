package Intro;

import com.brnleehng.worldrunner.R;

import DB.DBManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NameRequest extends Activity {
	private EditText editText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_request_activity);
		editText = (EditText) findViewById(R.id.nameEditText);
		Button button = (Button) findViewById(R.id.nameButton);
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DBManager db = new DBManager(getApplicationContext());
				db.close();
				Intent intent = new Intent(getApplicationContext(), WalkThrough.class);
				intent.putExtra("username", editText.getText());
				startActivity(intent);
			}
		});
	}

}
