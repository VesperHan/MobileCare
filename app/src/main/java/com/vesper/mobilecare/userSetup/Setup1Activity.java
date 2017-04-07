package com.vesper.mobilecare.userSetup;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vesper.mobilecare.R;

public class Setup1Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup1);
    }
    public void nextPage(View view){
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);

        finish();
    }
}
