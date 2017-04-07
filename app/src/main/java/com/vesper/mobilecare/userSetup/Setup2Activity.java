package com.vesper.mobilecare.userSetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.vesper.mobilecare.R;
import com.vesper.mobilecare.View.SettingItemView;
import com.vesper.mobilecare.utils.ConstantValue;
import com.vesper.mobilecare.utils.SharedPreUtil;
import com.vesper.mobilecare.utils.ToastUtil;

public class Setup2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        initUI();
    }

    private SettingItemView sv_sim_bound;

    private void initUI() {

        sv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        String sim_number = SharedPreUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (TextUtils.isEmpty(sim_number)) {
            sv_sim_bound.setCheck(false);
        } else {
            sv_sim_bound.setCheck(true);
        }

        sv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = sv_sim_bound.isCheck();
                sv_sim_bound.setCheck(!isCheck);

                if (!isCheck) {
                    TelephonyManager manager = (TelephonyManager) getSystemService
                            (TELEPHONY_SERVICE);
//                    获取sim卡号
                    String sn = manager.getSimSerialNumber();
                    SharedPreUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, sn);
                } else {
                    SharedPreUtil.remove(getApplicationContext(), ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    public void nextPage(View view) {

        String sn = SharedPreUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(sn)) {
            intentSkip(Setup3Activity.class);
        } else {
            ToastUtil.show(this, "请绑定sim卡");
        }
    }

    public void prePage(View view) {

        intentSkip(Setup1Activity.class);
    }

    public void intentSkip(Class obj) {
        Intent intent = new Intent(getApplicationContext(), obj);
        startActivity(intent);
        finish();
    }
}
