package Items;

import java.util.ArrayList;

import DB.DBManager;
import DB.Model.Equipment;
import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.brnleehng.worldrunner.RunLogDialog;
import com.brnleehng.worldrunner.ViewStickerDialog;

/**
 * Fragment to equip stickers. Very similar to EquipEquipment
 */
public class EquipSticker extends Fragment {
	GridView gridview;	
	ArrayList<Sticker> unequippedSticker;
	ArrayList<Sticker> equippedSticker;
	Sticker currentSticker;
	int currentPosition;
	
	/**
	 * Creates the sticker equipment screen. Allows user to equip any stickers that
	 * aren't already equipped. Shows stickers via custom adapter
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		final DBManager db = new DBManager(getActivity());
		// get whatever category that the user decides to change
		currentSticker = Hub.currentSticker;
		currentPosition = Hub.currentStickerPosition;
		
		// Note: the first value is set to be null so that we can make the remove
		// button in the gridview.
		unequippedSticker = db.getUnequipedStickers();
		
		// TODO temp that needs to be replace soon
		equippedSticker = Hub.tempEquippedSticker;
		//equippedSticker = Hub.equippedStickers;
		
		for (Sticker sticker : unequippedSticker) {
			if (sticker != null) {
				Log.d("unequipped sticker", sticker.name);
			} else {
				Log.d("unequipped sticker", "empty");
			}
		}
		// TODO is this the correct adapter? Need to reformat adapters to make sense
		final StickerAdapter adapter = new StickerAdapter(getActivity(), R.layout.mylist, unequippedSticker);
		gridview = (GridView) view.findViewById(R.id.viewGridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				// gets the user's selected equipment
				Sticker newSticker = adapter.getItem(position);
				
				// checks for the event of no monsters
				if (currentSticker != null) {
					equippedSticker.remove(currentSticker);
					currentSticker.equipped = 0;
					currentSticker.position = 0;
					// TODO instead of relying on database for everything, store it all locally and then send it once it's ready
					db.updateSticker(currentSticker);
				}
				
				// if the user got another sticker that wasn't the
				// remove sticker
				if (newSticker != null) {
					newSticker.equipped = 1;
					newSticker.position = Hub.currentStickerPosition;
					equippedSticker.set(newSticker.position - 1, newSticker);
					Hub.equippedStickers = equippedSticker;
					
					// TODO - Still need to save everything to the DB, simple loop update
					db.updateSticker(newSticker);
				}
				Hub.equipItems();
				//Goes back to equipped page.
			}
		});
		
		gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getItem(position) != null) {
					Toast.makeText(getActivity(), adapter.getItem(position).name, Toast.LENGTH_LONG).show();
			
					Hub.viewSticker = adapter.getItem(position);
					ViewStickerDialog newFragment = new ViewStickerDialog();
					//newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.ViewStickerDialog);
					newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
					newFragment.show(getFragmentManager(), "View Sticker");
				}
				return true;
			}
		});


		return view;
	}
}
