package Items;

import java.util.ArrayList;

import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;

// Not used?
public class ViewSticker extends Fragment {
	GridView gridview;
	ArrayList<Sticker> list;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		list = Hub.getStickers();
		StickerAdapter adapter = new StickerAdapter(getActivity(), R.layout.mylist, list);
		
		gridview = (GridView) view.findViewById(R.id.viewGridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Sticker SelectedItem = list.get(position);
				Toast.makeText(getActivity().  getApplicationContext(), SelectedItem.name, Toast.LENGTH_SHORT).show();
			}
		});
		return view;
	}  
}
