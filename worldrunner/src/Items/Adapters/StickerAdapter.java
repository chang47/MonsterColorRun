package Items.Adapters;

import java.util.ArrayList;
import java.util.List;

import com.brnleehng.worldrunner.R;

import DB.Model.Equipment;
import DB.Model.Monster;
import DB.Model.Sticker;
import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
/**
 * Adapter used to show Stickers in a grid view. Used for viewing and equipping
 * stickers. Similar to equipmentAdapter
 */
public class StickerAdapter extends ArrayAdapter<Monster> {
 
	Context context;
	LayoutInflater inflater;
	List<Monster> monsterList;
	private SparseBooleanArray mSelectedItemsIds;
 
	// Constructor that takes in the list that is being used to read the 
	// whole grid
	public StickerAdapter(Context context, int resourceID, List<Monster> list) {
		super(context, resourceID, list);
		mSelectedItemsIds = new SparseBooleanArray();
		this.context = context;
		this.monsterList = list;
		inflater = LayoutInflater.from(context);
	}
	
	// private class to hold the Sticker. It's more efficient
	// to store everything inside a class as oppose to calling
	// them out individually
	private class ViewHolder {
		//TextView txtTitle;
		ImageView imageView;
		TextView lvl;
		//TextView exp;
		//TextView rch;
	}
	
 	@Override
 	// Creates the actual object that the user sees
 	public View getView(int position,View view,ViewGroup parent) {
 		final ViewHolder holder;
 		
 		// TODO what's the point of this?
 		if (view == null) {
 			holder = new ViewHolder();
 			view = inflater.inflate(R.layout.mylist, null);
 			
 			//holder.txtTitle = (TextView) view.findViewById(R.id.item);
 			holder.imageView = (ImageView) view.findViewById(R.id.icon);
 			//holder.exp = (TextView) view.findViewById(R.id.textView2);
 			//holder.rch = (TextView) view.findViewById(R.id.textView3);
 			view.setTag(holder);
 		} else {
 			holder = (ViewHolder) view.getTag();
 		}
		 
		// checks for the null remove sticker case
		if (getItem(position) == null) {
			holder.imageView.setImageResource(R.drawable.icon);
		} else {
			//holder.txtTitle.setText(monsterList.get(position).name);
			int resId = context.getResources().getIdentifier("head" + monsterList.get(position).monsterId, "drawable", context.getPackageName());
 			holder.lvl = (TextView) view.findViewById(R.id.listLevel);
 			holder.lvl.setText("" + monsterList.get(position).level);
			if (resId != 0) {
				holder.imageView.setImageResource(resId);
			} else {
				holder.imageView.setImageResource(R.drawable.ic_launcher); // have the pictured ordered correctly
			}
			/*holder.lvl.setText("lvl: " + monsterList.get(position).level);
			holder.exp.setText("exp: " + monsterList.get(position).exp);
			holder.rch.setText("rch: " + monsterList.get(position).speed);
			*/if (mSelectedItemsIds.get(position)) {
				view.setBackgroundColor(Color.BLUE);
			} else {
				view.setBackgroundColor(Color.TRANSPARENT);
			}
		}
		return view;
 	}
 	
	@Override
	// removes the sticker from the grid view list. Used for selling
	public void remove(Monster object) {
		monsterList.remove(object);
		notifyDataSetChanged();
	}
 
	// Gets the list that represents the list of stickers
	public List<Monster> getArray() {
		return monsterList;
	}
 
	// toggles the selected item. Selects or un-selects them based off of it's
	// position in the list
	public boolean toggleSelection(int position) {
		return selectView(position, !mSelectedItemsIds.get(position));
	}
	
	// Removes all selected items in the list
	public void removeSelection() {
		mSelectedItemsIds = new SparseBooleanArray();
		notifyDataSetChanged();
	}
 	
	/**
	 * Selects or de-selects the items in the list
	 * @param position - the item to be selected or deselected
	 * @param value - whether the item has not been selected already or not
	 * @return false if added, true if removed
	 */
	public boolean selectView(int position, boolean value) {
		if (value) {
			mSelectedItemsIds.put(position, value);
			notifyDataSetChanged();
			return false;
		} else {
			mSelectedItemsIds.delete(position);
			notifyDataSetChanged();
			return true;
		}
	}
	
	/**
	 * Returns an array of items that were selected by the user
	 * @return list of items selected by the user
	 */
	public SparseBooleanArray getSelectedIds() {
		return mSelectedItemsIds;
	}
	
}