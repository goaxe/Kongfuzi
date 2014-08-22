package com.kongfuzi.teacher.activities;

import java.util.ArrayList;
import java.util.List;

import org.holoeverywhere.app.Activity;
import org.holoeverywhere.app.AlertDialog;
import org.holoeverywhere.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.kongfuzi.teacher.R;
import com.kongfuzi.teacher.MainApplication;
import com.kongfuzi.teacher.internal.KFZClient;
import com.kongfuzi.teacher.internal.KFZClient.KFZNetworkError;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MubanListActivity extends Activity {

  public static final String DEBUG_TAG = "MubanListActivity";

  private Activity mActivity;
  private ListView mListView;
  private ListAdapter mListAdapter;
  private List<ListItem> mCurrentList = new ArrayList<ListItem>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.title_bar_bg));
    getSupportActionBar().setTitle("评语模版");
    setContentView(R.layout.activity_muban_list);
    mActivity = this;

    mListView = (ListView) findViewById(R.id.listview);
    mListAdapter = new ListAdapter();
    mListView.setAdapter(mListAdapter);

    new RefreshDataThread().start();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuItem menuItem = menu.add("更多");
    menuItem.setIcon(R.drawable.add_btn);
    menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getTitle().equals("更多")) {
      final EditText editText = new EditText(mActivity);
      DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) {
          // TODO Auto-generated method stub
          new AddMubanThread(editText.getText().toString()).start();
        }
      };
      new AlertDialog.Builder(mActivity).setTitle("请输入评语").setView(editText).setPositiveButton("确定", listener)
          .setNegativeButton("取消", null).show();
    } else {
      finish();
    }
    return true;
  }

  public class ListAdapter extends BaseAdapter {

    @Override
    public int getCount() {
      // TODO Auto-generated method stub
      return mCurrentList.size();
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
      final ListItem item = mCurrentList.get(arg0);
      View view = getLayoutInflater().inflate(R.layout.muban_list_item_layout);
      TextView textView = (TextView) view.findViewById(R.id.textview);
      textView.setText(item.content);
      view.setOnLongClickListener(new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
          // TODO Auto-generated method stub
          DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
              if (which == 0) {
                final EditText editText = new EditText(mActivity);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    item.content = editText.getText().toString();
                    new ModifyMubanThread(item).start();
                  }
                };
                new AlertDialog.Builder(mActivity).setTitle("请输入模板内容").setView(editText).setPositiveButton("确定", listener)
                    .setNegativeButton("取消", null).show();
              } else if (which == 1) {
                mCurrentList.remove(item);
                new DeleteMubanThread(item.commentId).start();
              } else if (which == 2) {
                final EditText editText = new EditText(mActivity);
                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    new AddMubanThread(editText.getText().toString()).start();
                  }
                };
                new AlertDialog.Builder(mActivity).setTitle("请输入评语").setView(editText).setPositiveButton("确定", listener)
                    .setNegativeButton("取消", null).show();
              }
              dialog.dismiss();
            }
          };
          new AlertDialog.Builder(mActivity).setTitle("请选择")
              .setSingleChoiceItems(getResources().getStringArray(R.array.muban_choices), 0, listener).show();
          return false;
        }
      });
      return view;
    }
  }

  public class RefreshDataThread extends Thread {

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      
      
      try {
        JSONObject jsonObject = client.mubanList("exam", 0, 20);
        JSONArray jsonArray = jsonObject.getJSONArray("results");
        mCurrentList = new ArrayList<ListItem>();
        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject object = jsonArray.getJSONObject(i);
          String commentId = object.getString("commentid");
          String content = object.getString("content");
          ListItem item = new ListItem(commentId, content);
          mCurrentList.add(item);
        }
        mActivity.runOnUiThread(new Runnable() {
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
      }

    }
  }

  public class DeleteMubanThread extends Thread {
    private String commentId;

    public DeleteMubanThread(String commentId) {
      this.commentId = commentId;
    }

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      JSONObject jsonObject;
      try {
        jsonObject = client.mubanDelete(new JSONArray().put(commentId));
      } catch (KFZNetworkError e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          mListAdapter.notifyDataSetChanged();
        }
      });

    }
  }

  public class ModifyMubanThread extends Thread {
    private ListItem item;

    public ModifyMubanThread(ListItem item) {
      this.item = item;
    }

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      JSONObject jsonObject;
      try {
        jsonObject = client.mubanModify(item.commentId, item.content);
      } catch (KFZNetworkError e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      mActivity.runOnUiThread(new Runnable() {
        public void run() {
          mListAdapter.notifyDataSetChanged();
        }
      });

    }
  }

  public class AddMubanThread extends Thread {

    private String content;

    public AddMubanThread(String content) {
      this.content = content;
    }

    @Override
    public void run() {
      KFZClient client = new KFZClient();
      client.setSecretKey(MainApplication.secretKey);
      
      try {
        JSONObject jsonObject = client.mubanAdd("exam", content);
        String commentId = jsonObject.getString("commentid");
        ListItem item = new ListItem(commentId, content);
        mCurrentList.add(item);
        mActivity.runOnUiThread(new Runnable() {
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
      }
    }
  }

  public class ListItem {
    String commentId;
    String content;

    public ListItem(String commentId, String content) {
      this.commentId = commentId;
      this.content = content;
    }
  }
}
