package com.kongfuzi.student;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NavigationSlidingMenu extends SlidingMenu {

    public static final String DEBUG_TAG = "NavigationSlidingMenu";
    
    private ImageView mSettingImageView;
    public String[] mHomeTitles;
    private String[] mMineTitles;
    private ListView mHomeListView;
    private ListView mMineListView;
    private ListAdapter mHomeAdapter;
    private ListAdapter mMineAdapter;

    private Callbacks mCallbacks;

    public interface Callbacks {
        public void onNavigationSlidingMenuItemClicked(int position);
    }

    public NavigationSlidingMenu(Context context, int resId) {
        super(context);
        
        setMenu(resId);
        mHomeTitles = getResources().getStringArray(R.array.home_titles);
        mMineTitles = getResources().getStringArray(R.array.mine_titles);
        
        mHomeListView = (ListView) findViewById(R.id.home_listview);
        mHomeAdapter = new ListAdapter();
        mHomeAdapter.setItem(mHomeTitles);
        mHomeListView.setAdapter(mHomeAdapter);
        
        mMineListView = (ListView) findViewById(R.id.mine_listview);
        mMineAdapter = new ListAdapter();
        mMineAdapter.setItem(mMineTitles);
        mMineListView.setAdapter(mMineAdapter);
        
        mSettingImageView = (ImageView) findViewById(R.id.setting_imageview);
        mSettingImageView.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            mCallbacks.onNavigationSlidingMenuItemClicked(4);
          }
        });
        /*        mNavigationTitles = getResources().getStringArray(R.array.nav_array);
        mNavigationImages = new Integer[] {R.drawable.icon_personal_white, R.drawable.icon_groups_white,
                R.drawable.icon_collent_white, R.drawable.icon_update_white,R.drawable.icon_setup_white };
         */
    }
    
    @Override
    public void attachToActivity(Activity activity, int slideStyle) {
        super.attachToActivity(activity, slideStyle);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "the Activity which NavigationSlidingMenu belongs to must implement the Callbacks interface");
        }
        mCallbacks = (Callbacks) activity;
    }
    
    public class ListAdapter extends BaseAdapter {
      
      private String[] currentArray;

      public void setItem(String[] stringArray) {
        this.currentArray = stringArray;
      }
      
      @Override
      public int getCount() {
        // TODO Auto-generated method stub
        return currentArray.length;
      }

      @Override
      public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
      }

      @Override
      public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
      }

      @Override
      public View getView(int arg0, View arg1, ViewGroup arg2) {
        final int positon = arg0;
        View view = LayoutInflater.inflate(getContext(), R.layout.navigation_list_item_layout);
        TextView titleTextView = (TextView) view.findViewById(R.id.title_textview);
        titleTextView.setText(currentArray[arg0]);
        view.setOnClickListener(new OnClickListener() {
          
          @Override
          public void onClick(View v) {
            // TODO Auto-generated method stub
            if (currentArray[0].equals("个人中心")) {
              mCallbacks.onNavigationSlidingMenuItemClicked(5 + positon);
            } else {
              mCallbacks.onNavigationSlidingMenuItemClicked(positon);
            }
            
          }
        });
        return view;
      }
      
    }

}
