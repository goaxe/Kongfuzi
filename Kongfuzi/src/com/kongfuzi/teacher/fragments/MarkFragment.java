package com.kongfuzi.teacher.fragments;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;
import org.holoeverywhere.preference.PreferenceManager;
import org.holoeverywhere.widget.ListView;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.MyDialog;
import com.kongfuzi.teacher.activities.MarkDateListActivity;
import com.kongfuzi.teacher.internal.Constants;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

public class MarkFragment extends Fragment {
  
  public static final String DEBUG_TAG = "MarkFragment";

  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_mark);
  }

  @Override
  public void onViewCreated(View view, Bundle savedInstanceState) {
    mListView = (ListView) view.findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);
    readCache();
    new RefreshDataThread().start();
  }
  
  public void readCache() {
    try {
      JSONObject jsonObject = new JSONObject(MainApplication.prefs.getString(Constants.MarkFragment, ""));
      JSONArray jsonArray = jsonObject.getJSONArray("classes");
      mCurrentList = new ArrayList<ListItem>();
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject object = jsonArray.getJSONObject(i);
        String name = object.getString("name");
        String id = object.getString("id");
        Integer stuCount = object.getInt("stucount");
        ListItem item = new ListItem(name, id, stuCount);
        mCurrentList.add(item);
      }
      mListAdapter.notifyDataSetChanged();
    } catch (JSONException e) {
      // TODO: handle exception
      e.printStackTrace();
    }
    

  }

  public class ListAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mCurrentList.size();
    }

    @Override
    public Object getItem(int position) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    public long getItemId(int position) {
      // TODO Auto-generated method stub
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      final ListItem item = mCurrentList.get(position);
      View view = getLayoutInflater().inflate(R.layout.class_list_item_layout);
      TextView nameTextView = (TextView) view.findViewById(R.id.name_textview);
      TextView countTextView = (TextView) view.findViewById(R.id.count_textview);
      nameTextView.setText(item.name);
      countTextView.setText("(" + item.stuCount + "人)");
      view.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
          Intent intent = new Intent(getSupportActivity(), MarkDateListActivity.class);
          intent.putExtra(Constants.ID, item.id);
          startActivity(intent);
        }
      });
      return view;
    }

  }

  public class RefreshDataThread extends Thread {
    private MyDialog myDialog;
    @Override
    public void run() {
      getActivity().runOnUiThread(new Runnable() {
        public void run() {
          myDialog = new MyDialog(getSupportActivity());
          myDialog.show();
        }
      });
      KFZClient client = new KFZClient();
      String secretKey = MainApplication.prefs.getString(Constants.SECRET_KEY, "");
      client.setSecretKey(secretKey);
      
      //Log.e(DEBUG_TAG, jsonObject.toString());
      try {
        JSONObject jsonObject = client.queryClassesList();
        MainApplication.prefs.edit().putString(Constants.MarkFragment, jsonObject.toString()).commit();
        JSONArray jsonArray = jsonObject.getJSONArray("classes");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String name = object.getString("name");
          String id = object.getString("id");
          Integer stuCount = object.getInt("stucount");
          ListItem item = new ListItem(name, id, stuCount);
          mCurrentList.add(item);
        }

        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            mListAdapter.notifyDataSetChanged();
          }
        });
      } catch (KFZNetworkError e) {
        // TODO: handle exception
        e.printStackTrace();
      } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } finally {
        getActivity().runOnUiThread(new Runnable() {
          public void run() {
            myDialog.dismiss();
          }
        });
      }

    }
  }

  public class ListItem {
    public String name = null;
    public String id = null;
    public Integer stuCount = null;

    public ListItem(String name, String id, Integer stuCount) {
      this.name = name;
      this.id = id;
      this.stuCount = stuCount;
    }
  }

}
