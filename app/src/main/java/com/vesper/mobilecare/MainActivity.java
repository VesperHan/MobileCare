package com.vesper.mobilecare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.vesper.mobilecare.utils.ConstantValue;
import com.vesper.mobilecare.utils.SharedPreUtil;
import com.vesper.mobilecare.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private String[] listTitleName;
    private int[] imgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initData();
    }

    private void initData() {

        listTitleName = new String[]{
                "手机防盗", "通信卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心"
        };

        imgList = new int[]{
                R.drawable.home_safe, R.drawable.home_callmsgsafe,
                R.drawable.home_apps, R.drawable.home_taskmanager,
                R.drawable.home_netmanager, R.drawable.home_trojan,
                R.drawable.home_sysoptimize, R.drawable.home_tools, R.drawable.home_settings
        };

        gridView.setAdapter(new MyAdapter());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
//                  手机防盗
                    case 0:
                        showDialog();
                        break;

                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    private void showDialog() {

//        首先判断是否有存储
        String psd = SharedPreUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            showInitPsdDialog();
        } else {
            showCurrentPsdDialog();
        }
    }

    private void showInitPsdDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        final View view = View.inflate(this, R.layout.dialog_init_pwd, null);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        final EditText firRow = (EditText) view.findViewById(R.id.et_initFirst);
        final EditText secRow = (EditText) view.findViewById(R.id.et_initConfirm);

        //点击模块之后直接弹出键盘
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) firRow.getContext().getSystemService
                        (Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 300);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                firRow.setFocusable(true);
                firRow.setFocusableInTouchMode(true);

                String psd = firRow.getText().toString();
                String confirm = secRow.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirm)) {
                    if (psd.equals(confirm)) {
                        dialog.dismiss();
                        SharedPreUtil.putString(getApplicationContext(), ConstantValue
                                .MOBILE_SAFE_PSD, psd);
                        enterPhoneSecurity();
                    } else {
                        ToastUtil.show(getApplicationContext(), "密码输入不一致");
                    }
                } else {
                    ToastUtil.show(getApplicationContext(), "密码输入不为空");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void showCurrentPsdDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_init_pwd, null);
        final EditText firRow = (EditText) view.findViewById(R.id.et_initFirst);
        final TextView title = (TextView) view.findViewById(R.id.alert_title);
        title.setText("密码校验");
        final EditText secRow = (EditText) view.findViewById(R.id.et_initConfirm);
        secRow.setVisibility(view.GONE);
        dialog.setView(view);
        dialog.show();

        Button bt_submit = (Button) view.findViewById(R.id.bt_confirm);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd = SharedPreUtil.getString(getApplicationContext(), ConstantValue
                        .MOBILE_SAFE_PSD, "");
                if (firRow.getText().toString().equals(psd)) {
                    ToastUtil.show(getApplicationContext(), "密码校验成功");
                    enterPhoneSecurity();
                    dialog.dismiss();
                } else {
                    ToastUtil.show(getApplicationContext(), "密码输入错误");
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void enterPhoneSecurity() {
        Intent intent = new Intent(getApplicationContext(),SetupOverActivity.class);
        startActivity(intent);
    }

    private void initUI() {
        gridView = (GridView) findViewById(R.id.gv_list);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listTitleName.length;
        }

        @Override
        public Object getItem(int position) {
            return listTitleName[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getApplicationContext(), R.layout.gridview_item, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_modTitle);
            ImageView imageView = (ImageView) view.findViewById(R.id.iv_icon);

            textView.setText(listTitleName[position]);
            imageView.setBackgroundResource(imgList[position]);
            return view;
        }
    }
}
