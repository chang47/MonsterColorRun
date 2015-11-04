package intro;

import java.util.List;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import DB.DBManager;
import DB.Model.Player;
import DB.Model.Sticker;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SelectMonster extends Activity {

	private String username;
	private DBManager db;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		db = new DBManager(getApplicationContext());
		setContentView(R.layout.select_monster_activity);
		ImageView monster1 = (ImageView) findViewById(R.id.chooseMonster1);
		ImageView monster2 = (ImageView) findViewById(R.id.chooseMonster2);
		ImageView monster3 = (ImageView) findViewById(R.id.chooseMonster3);
		
		Intent intent = getIntent();
		username = intent.getStringExtra("username");
		monster1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Player> players = db.getPlayer();
				players.get(0).username = username;
				db.updatePlayer(players.get(0));
				db.addStickerDirectly(new Sticker(-1, players.get(0).pid, 1, "Firtin", 0, 1, 0, 1, 1, 0, 0, 0, 45, 60, 50, 55, 0));
				db.close();
				Intent intent = new Intent(getApplicationContext(), Hub.class);
				startActivity(intent);
			}
		});
		
		monster2.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				List<Player> players = db.getPlayer();
				players.get(0).username = username;
				db.updatePlayer(players.get(0));
				db.addStickerDirectly(new Sticker(-1, players.get(0).pid, 2, "Artabbit", 1, 1, 0, 2, 2, 0, 0, 0, 50, 55, 55, 60, 0));
				db.close();
				Intent intent = new Intent(getApplicationContext(), Hub.class);
				startActivity(intent);
			}
		});
		
		monster3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				List<Player> players = db.getPlayer();
				players.get(0).username = username;
				db.updatePlayer(players.get(0));
				db.addStickerDirectly(new Sticker(-1, players.get(0).pid, 3, "Roseer", 2, 1, 0, 3, 3, 0, 0, 0, 55, 50, 60, 50, 0));
				db.close();
				Intent intent = new Intent(getApplicationContext(), Hub.class);
				startActivity(intent);
			}
		});
	}

}
