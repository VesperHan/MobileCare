package com.vesper.mobilecare;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactListActivity extends AppCompatActivity {

    private ListView lv_contact;
    private CusAdapter cusAdapter;
    private List<HashMap<String, String>> contactList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            cusAdapter = new CusAdapter();
            lv_contact.setAdapter(cusAdapter);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        customView();
        customData();
    }

    //  获取系统联系人
    private void customData() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                ContentResolver resolver = getContentResolver();
                Cursor cursor = resolver.query(
                        Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"},
                        null, null, null);
                contactList.clear();

                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Cursor indexCursor = resolver.query(
                            Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"},
                            "raw_contact_id = ?", new String[]{id}, null);

                    HashMap<String, String> hashMap = new HashMap<>();
                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        Log.e("aa", data + type);
                        if (type.equals("vnd.android.cursor.item/phone_v2")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("phone", data);
                            }
                        } else if (type.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
//                    通知主线程可以使用填充好的数据集合
                handler.sendEmptyMessage(0);
            }
        }).start();

    }

    private void customView() {

        lv_contact = (ListView) findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (cusAdapter != null) {
                    HashMap<String, String> hashMap = (HashMap<String, String>) cusAdapter.getItem(position);
                    Log.e("ac",hashMap.toString());
                    String phone = hashMap.get("phone");

                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }

    class CusAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public Object getItem(int position) {
            return contactList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);

            TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
            TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);

            HashMap<String, String> tmp = (HashMap<String, String>) getItem(position);

            tv_name.setText(tmp.get("name"));
            tv_phone.setText(tmp.get("phone"));
            return view;
        }
    }
}
