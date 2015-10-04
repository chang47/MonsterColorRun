package intro;

import com.brnleehng.worldrunner.Hub;
import com.brnleehng.worldrunner.R;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.TitlePageIndicator;

import DB.DBManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.GpsStatus.Listener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class WalkThrough extends Activity {
	private static final int MAX_VIEWS = 6;
	private int currentIndex = 0;
	ViewPager mViewPager;
	WalkthroughPageAdapter adapter;
	private Button button;
	private boolean first;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walkthrough_activity);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        int[] imageId = {R.drawable.walkthrough1, R.drawable.walkthrough2, R.drawable.walkthrough3,
        		R.drawable.walkthrough4, R.drawable.walkthrough5, R.drawable.walkthrough6};
       	adapter = new WalkthroughPageAdapter(imageId);
        mViewPager.setAdapter(adapter);
        CirclePageIndicator titleIndicator = (CirclePageIndicator)findViewById(R.id.titlesssss);
        titleIndicator.setViewPager(mViewPager);
        button = new Button(this);
        button.setText("Click to continue");
        button.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        
        button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DBManager db = new DBManager(getApplicationContext());
				db.close();
				Intent intent = new Intent(getApplicationContext(), Hub.class);
				startActivity(intent);
			}
		});
        
        first = true;

        titleIndicator.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int index) {
				currentIndex = index;
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) { }
			
			@Override
			public void onPageScrollStateChanged(int index) {
				// TODO Auto-generated method stub
				if (currentIndex == adapter.getCount() - 1 && first) {
					View showInfo = findViewById(R.id.screen_navigation_button);
					ViewGroup parent = (ViewGroup) showInfo.getParent();
					int viewIndex = parent.indexOfChild(showInfo);
					parent.removeView(showInfo);
					parent.addView(button, viewIndex);
					first = false;
				}
			}
		});
    }
    
    class WalkthroughPageAdapter extends PagerAdapter {
    	int[] imageId;
    	
    	public WalkthroughPageAdapter(int[] imageId) {
    		this.imageId = imageId;
    	}
    	
    	@Override
    	public int getCount() {
    		return MAX_VIEWS;
    	}
    	
    	@Override
    	public boolean isViewFromObject(View view, Object object) {
    		return view == (View) object;
    	}
    	
    	@Override
    	public Object instantiateItem(View container, int position) {
    		 Log.e("walkthrough", "instantiateItem(" + position + ");");
             LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View imageViewContainer = inflater.inflate(R.layout.walkthrough_single_view, null);
             ImageView imageView = (ImageView) imageViewContainer.findViewById(R.id.image_view);
             imageView.setImageResource(imageId[position]);
             ((ViewPager) container).addView(imageViewContainer);
             return imageViewContainer;
    	}
    	
    	@Override
    	public void destroyItem(View container, int position, Object object) {
    		((ViewPager) container).removeView((View) object);
    	}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub
			
		}
    }
    
    
}
