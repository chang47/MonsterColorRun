package DB;

import java.io.File;
import java.util.List;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

import DB.Model.Player;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CreateDB extends Fragment {
	private static final String DATABASE_NAME = "Player";
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.createdb_activity, container, false);
		DBManager db = new DBManager(getActivity().getApplicationContext());
		Hub.getPlayerData(db);
		List<Player> list = db.getPlayer();
		Player player= list.get(0);
		db.close();
		TextView tv = (TextView) view.findViewById(R.id.dbView);
		tv.setText("pid:" + player.pid + " username " + player.username + " fname " + player.fname + " lname " + player.lname
				+ " level " + player.level);
		// because the onCreate doesn't get called for the fragment we would have to manually reset the whole thing
		return view;
	}  
}
