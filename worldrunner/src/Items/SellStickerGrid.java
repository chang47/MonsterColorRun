package Items;

import java.util.ArrayList;
import java.util.List;

import DB.DBManager;
import DB.Model.Monster;
import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

public class SellStickerGrid extends Fragment {
	GridView gridview;

	List<Monster> list;
	private StickerAdapter adapter;
	private ArrayList<Monster> sellList;
	DBManager db;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.sellequipmentgrid_activity, container, false);
		list = Hub.stickerList;
		db = new DBManager(getActivity());
		
		sellList = new ArrayList<Monster>();
		adapter = new StickerAdapter(getActivity(), R.layout.mylist, list);
		
		gridview = (GridView) view.findViewById(R.id.gridview);
		gridview.setAdapter(adapter);
		gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				boolean checked = adapter.toggleSelection(position);
				Monster monster = adapter.getItem(position);
				sellList.add(monster);
				if (checked) {
					//view.setBackgroundColor(Color.TRANSPARENT);
					sellList.remove(monster);
				} else {
					//view.setBackgroundColor(Color.BLUE);
					sellList.add(monster);
				}
			}
		});
		
		
		Button sellBtn = (Button) view.findViewById(R.id.sellBtn2);
		
		sellBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SparseBooleanArray selected = adapter.getSelectedIds();
				for (int i = (selected.size() - 1); i >= 0; i--) {
					if (selected.valueAt(i)) {
						Monster selecteditem = adapter.getItem(selected.keyAt(i));
						// Remove selected items following the ids
						adapter.remove(selecteditem);
						db.deleteSticker(selecteditem);
						// reloads the user party and all items
						DBManager db = new DBManager(getActivity().getApplicationContext());
						Hub.getPlayerData(db);
						db.close();
					}
				}
				adapter.removeSelection();
				//@TODO make the grid max size be the total # of items user can have
				// with the new sparse position check, is this even necessary?
				/*
				for (int i = 0; i < gridview.getCount() * 3; i++) {
					View item = gridview.getChildAt(i);
					if (item != null) {
						item.setBackgroundColor(Color.TRANSPARENT);
					}
				}*/
			}
		});
		
		return view;
	}  

}
