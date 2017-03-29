package com.vesper.mobilecare;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vesper.mobilecare.View.SettingItemView;
import com.vesper.mobilecare.utils.ConstantValue;
import com.vesper.mobilecare.utils.SharedPreUtil;

import static com.vesper.mobilecare.R.id.siv_phoneLocal;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        final SettingItemView settingView = (SettingItemView) findViewById(R.id.siv_update);

        settingView.setCheck(SharedPreUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false));
//      监听选中状态一定要设置相关监听
        settingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isCheck = settingView.isCheck();
                settingView.setCheck(!isCheck);
                SharedPreUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE,
                        !isCheck);
            }
        });

        final SettingItemView phoneLocalView = (SettingItemView) findViewById(
                siv_phoneLocal);
        phoneLocalView.setCheck(SharedPreUtil.getBoolean(this, ConstantValue.PHONE_LOCAL, false));

        phoneLocalView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = phoneLocalView.isCheck();
                phoneLocalView.setCheck(!isCheck);
                SharedPreUtil.putBoolean(getApplicationContext(), ConstantValue.PHONE_LOCAL,
                        !isCheck);
            }
        });
    }
}
