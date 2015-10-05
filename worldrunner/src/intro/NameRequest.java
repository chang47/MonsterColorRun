package intro;

import com.brnleehng.worldrunner.R;

import DB.DBManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class NameRequest extends Activity {
	private EditText editText;
	private NumberPicker weight;
	private NumberPicker feet;
	private NumberPicker inch;
	private SharedPreferences pref;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.name_request_activity);
		editText = (EditText) findViewById(R.id.nameEditText);
		Button button = (Button) findViewById(R.id.nameButton);
		pref = getApplication().getSharedPreferences("MonsterColorRun", Context.MODE_PRIVATE);
		
		weight = (NumberPicker) findViewById(R.id.weightPicker);
		weight.setMinValue(50);
		weight.setMaxValue(400);
		
		feet= (NumberPicker) findViewById(R.id.feetPicker);
		feet.setMinValue(3);
		feet.setMaxValue(8);
		
		inch = (NumberPicker) findViewById(R.id.inchPicker);
		inch.setMinValue(0);
		inch.setMaxValue(11);
		
		button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String username = editText.getText().toString();
				if (!username.isEmpty()) {
					DBManager db = new DBManager(getApplicationContext());
					db.close();
					
					SharedPreferences.Editor editor = pref.edit();
					editor.putInt(getString(R.string.weight), weight.getValue());
					editor.putInt(getString(R.string.feet), feet.getValue());
					editor.putInt(getString(R.string.inch), inch.getValue());
					editor.commit();
					
					Intent intent = new Intent(getApplicationContext(), SelectMonster.class);
					intent.putExtra("username", username);
					startActivity(intent);
				} else {
					Toast.makeText(getApplicationContext(), "Please enter a username", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

}
