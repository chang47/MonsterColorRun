package Items;

import java.util.ArrayList;

import DB.DBManager;
import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

public class EquipStickers extends Fragment {
	GridView gridview;	
	ArrayList<Sticker> unequippedSticker;
	ArrayList<Sticker> equippedSticker;
	Sticker currentSticker;
	int currentPosition;
	private ImageView sticker1;
	private ImageView sticker2;
	private ImageView sticker3;
	private ImageView sticker4;
	private ImageView sticker5;
	private ImageView[] stickerViews;
	
	/**
	 * Creates the sticker equipment screen. Allows user to equip any stickers that
	 * aren't already equipped. Shows stickers via custom adapter
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.equipsticker_activity, container, false);
		
		// setup stickers
		stickerViews[0] = (ImageView) view.findViewById(R.id.equipSticker1);
		stickerViews[1] = (ImageView) view.findViewById(R.id.equipSticker2);
		stickerViews[2] = (ImageView) view.findViewById(R.id.equipSticker3);
		stickerViews[3] = (ImageView) view.findViewById(R.id.equipSticker4);
		stickerViews[4] = (ImageView) view.findViewById(R.id.equipSticker5);
	/**	sticker1 = (ImageView) view.findViewById(R.id.equipSticker1);
		sticker2 = (ImageView) view.findViewById(R.id.equipSticker2);
		sticker3 = (ImageView) view.findViewById(R.id.equipSticker3);
		sticker4 = (ImageView) view.findViewById(R.id.equipSticker4);
		sticker5 = (ImageView) view.findViewById(R.id.equipSticker5);
		**/
		
		final DBManager db = new DBManager(getActivity());
		
		// get whatever category that the user decides to change
		/*
		currentSticker = Hub.currentSticker;
		currentPosition = Hub.currentStickerPosition;
		unequippedSticker = db.getUnequipedStickers();
		*/
		
		// TODO temp to be fixed because the current one is used
		//equippedSticker = Hub.equippedStickers;
		equippedSticker = Hub.tempEquippedSticker;
		
		// assumes that the stickers can never be put in an incorrect order
		// adds in the picture of the monsters
		for (int i = 0; i < equippedSticker.size(); i++) {
			stickerViews[i].setImageResource(R.drawable.ic_launcher);
		}
		
		// TODO is this the correct adapter? Need to reformat adapters to make sense
		final StickerAdapter adapter = new StickerAdapter(getActivity(), R.layout.mylist, unequippedSticker);
		gridview = (GridView) view.findViewById(R.id.viewGridViewEquip);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// clicked on existing item
				if (position == 1) {
					
				// gets the user's selected equipment
				} else if (equippedSticker.size() < 5) {
					
					Sticker newSticker = adapter.getItem(position);
					
					// updates the sticker list
					if (currentSticker != null) {
						// maybe instead of removing the list itself, just call set and remove methods?
						equippedSticker.remove(currentSticker);
						currentSticker.equipped = 0;
						currentSticker.position = 0;
						
						// TODO instead of relying on database for everything, store it all locally and then send it once it's ready
						db.updateSticker(currentSticker);
					}
					
					newSticker.equipped = 1;
					newSticker.position = equippedSticker.size() + 1;
					equippedSticker.add(newSticker);
					Hub.equippedStickers = equippedSticker;
					
					// TODO - Still need to save everything to the DB, simple loop update
					db.updateSticker(newSticker);
					Hub.equipItems();
					//Goes back to equipped page.
				}
			}
		});
		return view;
	}
}
