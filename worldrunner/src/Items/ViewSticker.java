package Items;

import java.util.ArrayList;
import java.util.List;

import DB.Model.Monster;
import DB.Model.Sticker;
import Items.Adapters.StickerAdapter;
import android.app.DialogFragment;
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
import com.brnleehng.worldrunner.ViewStickerDialog;

// Not used?
public class ViewSticker extends Fragment {
	GridView gridview;
	List<Monster> list;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View view = inflater.inflate(R.layout.viewitems_activity, container, false);
		list = Hub.stickerList;
		final StickerAdapter adapter = new StickerAdapter(getActivity(), R.layout.mylist, list);
		
		gridview = (GridView) view.findViewById(R.id.viewGridView);
		gridview.setAdapter(adapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (adapter.getItem(position) != null) {
					Toast.makeText(getActivity(), adapter.getItem(position).name, Toast.LENGTH_LONG).show();
			
					Hub.viewSticker = adapter.getItem(position);
					ViewStickerDialog newFragment = new ViewStickerDialog();
					//newFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.ViewStickerDialog);
					newFragment.setStyle(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
					newFragment.show(getFragmentManager(), "View Sticker");
				}
			}
		});
		
		
		return view;
	}  
}
