package Items.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.brnleehng.worldrunner.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
// Not Used?
public class EquipmentViewAdapter extends ArrayAdapter<String> {
 
	private final Activity context;
	private final String[] itemname;
	private final List<String> list;
 
	public EquipmentViewAdapter(Activity context, String[] itemname, ArrayList<String> list) {
		super(context, R.layout.mylist, list);
		// TODO Auto-generated constructor stub
		 
		this.context = context;
		this.itemname = itemname;
		this.list = list;
	}
 	@Override
 	public View getView(int position,View view,ViewGroup parent) {
 		View rowView = view;

 		if (rowView == null) {
 			LayoutInflater inflater = context.getLayoutInflater();
 			rowView = inflater.inflate(R.layout.mylist, null);
 		}
		 
 		//TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
		//TextView lvl = (TextView) rowView.findViewById(R.id.textView1);
		//TextView spd = (TextView) rowView.findViewById(R.id.textView2);
		//TextView rch = (TextView) rowView.findViewById(R.id.textView3);
		
		//txtTitle.setText(itemname[position]);
		//@todo get data from array list or 2D array
		imageView.setImageResource(R.drawable.ic_launcher); // have the pictured ordered correctly
		//lvl.setText("lvl: " + list.get(position));
		//spd.setText("lvl: " + list.get(position));
		//rch.setText("lvl: " + list.get(position));
		return rowView;
 	}
 	
 	public void delete(int position) {
 		list.remove(position);
 	}
}