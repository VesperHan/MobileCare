package com.vesper.mobilecare.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vesper.mobilecare.R;

/**
 * Created by vesperhan on 2017/3/23.
 */
//无论调哪个方法初始化都会调第三个,super改为this
public class SettingItemView extends RelativeLayout {
    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.vesper" +
            ".mobilecare";
    TextView tv_des;
    CheckBox checkBox;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//      将界面一个条目转换成view对象,直接添加到当前SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view, this);

        TextView titleView = (TextView) findViewById(R.id.tv_title);
        tv_des = (TextView) findViewById(R.id.tv_des);
        checkBox = (CheckBox) findViewById(R.id.cb_box);

        initAttrs(attrs);
        titleView.setText(mDestitle);
    }

    private void initAttrs(AttributeSet attributeSet) {

        mDestitle = attributeSet.getAttributeValue(NAMESPACE,"destitle");
        mDesoff = attributeSet.getAttributeValue(NAMESPACE,"desoff");
        mDeson = attributeSet.getAttributeValue(NAMESPACE,"deson");
    }

    public boolean isCheck(){

        return checkBox.isChecked();
    }
    /**
     * @param isCheck	是否作为开启的变量,由点击过程中去做传递
     */
    public void setCheck(boolean isCheck){
        //当前条目在选择的过程中,cb_box选中状态也在跟随(isCheck)变化
        checkBox.setChecked(isCheck);
        if(isCheck){
            //开启
            tv_des.setText(mDeson);
        }else{
            //关闭
            tv_des.setText(mDesoff);
        }
    }
}
