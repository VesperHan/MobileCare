package com.vesper.mobilecare.userSetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vesper.mobilecare.ContactListActivity;
import com.vesper.mobilecare.R;
import com.vesper.mobilecare.utils.ConstantValue;
import com.vesper.mobilecare.utils.SharedPreUtil;
import com.vesper.mobilecare.utils.ToastUtil;

public class Setup3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        initUI();
    }

    private EditText et_number;
    private Button bt_select;

    private void initUI() {

        et_number = (EditText) findViewById(R.id.et_phone_number);
        String phone = SharedPreUtil.getString(this, ConstantValue.CONTACT_PHONE, "");
        et_number.setText(phone);

        bt_select = (Button) findViewById(R.id.bt_select_number);
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            String phone = data.getStringExtra("phone");
            Log.e("ac", phone);

//            特殊字符过滤
            phone = phone.replace("-", "").replace(" ", "").trim();
            et_number.setText(phone);
//            持久化
            SharedPreUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextPage(View view) {
        String phone = et_number.getText().toString();

        if (!TextUtils.isEmpty(phone)) {
            intentSkip(Setup4Activity.class);
        } else {
            ToastUtil.show(this, "请输入电话号码");
        }
    }

    public void prePage(View view) {

        intentSkip(Setup2Activity.class);
    }

    public void intentSkip(Class obj) {
        Intent intent = new Intent(getApplicationContext(), obj);
        startActivity(intent);
        finish();
    }
}
