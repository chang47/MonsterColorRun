package Intro;

import com.brnleehng.worldrunner.R;

import DB.DBManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SelectMonster extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.select_monster_activity);
		ImageView monster1 = (ImageView) findViewById(R.id.chooseMonster1);
		ImageView monster2 = (ImageView) findViewById(R.id.chooseMonster2);
		ImageView monster3 = (ImageView) findViewById(R.id.chooseMonster3);
		// get the username
		// select the monster
		// save it to the db
		// load the hub
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
	
	public  
}
