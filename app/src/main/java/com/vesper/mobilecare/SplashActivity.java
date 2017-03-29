package com.vesper.mobilecare;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.vesper.mobilecare.utils.StreamUtil;
import com.vesper.mobilecare.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    protected static final String tag = "SplashActivity";
    static final int UPDAE_VERSION = 100;
    static final int ENTER_HOME = 101;
    static final int URL_ERROR = 102;
    static final int IO_ERROR = 103;
    static final int JSON_ERROR = 104;
    static final String home_url = "http://192.168.61.125:8080";
//    http://10.30.2.37:8080/getVersion

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDAE_VERSION:
                    showUpdateDialog();
                    break;
                case IO_ERROR:
                    ToastUtil.show(getApplicationContext(),"网络连接失败,请检查网络");
                    enterHome();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initUI();
        initData();
//        initAnimation();
    }

    TextView textView;
    int mLocalVersionCode;
    String verName;

    private void initData() {

        verName = getVersionName();
        textView.setText("版本号: " + verName);
//      本地版本号
        mLocalVersionCode = getVersionCode();
//      服务端版本号 版本名称 描述
        checkVersion();
    }

    String versionName;
    String versionCode;
    String versionMsg;

    private void checkVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = Message.obtain();
                try {
                    URL url = new URL(home_url + "/getVersion");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(2000);
                    connection.setReadTimeout(2000);
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == 200) {

                        InputStream stream = connection.getInputStream();
                        String json = StreamUtil.streamToString(stream);
                        JSONObject object = new JSONObject(json);
                        Log.e("E", json);
                        versionName = object.getString("versionName");
                        versionCode = object.getString("versionCode");
                        versionMsg = object.getString("msg");

                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {
                            msg.what = UPDAE_VERSION;
                        } else {
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void showUpdateDialog() {

        AlertDialog.Builder build = new AlertDialog.Builder(this);
        build.setIcon(R.mipmap.ic_launcher);
        build.setTitle("版本更新");
        build.setMessage(versionName + "\n" + versionMsg);
        build.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });
        build.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                enterHome();
            }
        });
//        即使不点击对话框,默认取消的话,也要进入并移除Dialog
        build.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
                dialog.dismiss();
            }
        });
        build.show();
    }

    private void enterHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
//        关闭界面
        finish();
    }

    private ProgressDialog progressDialog;

    private void downLoadApk() {

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File
                    .separator + "更新.txt";
            HttpUtils utils = new HttpUtils();
            utils.download(home_url + "/appDownload", path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
//                    responseInfo.contentLength
                    ToastUtil.show(getApplicationContext(), "下载成功");
                    enterHome();
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }

    private void installApk(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.Default");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        需要回传使用一下方法,当下一个界面finish完成之后回来到该页面
        startActivityForResult(intent, 0);
    }

    private void initUI() {

        textView = (TextView) findViewById(R.id.tv_verName);
    }

    private void initAnimation() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(3000);

        RelativeLayout rlRoot = (RelativeLayout) findViewById(R.id.Sp_root);
        rlRoot.startAnimation(alphaAnimation);
    }

    /**
     * @return 版本号versionName
     */
    public String getVersionName() {
//      获取包管理者对象
        PackageManager manager = getPackageManager();
        try {
//            PackageManager.GET_ACTIVITIES 传0获取基本信息
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @return 版本号versionCode
     */
    public int getVersionCode() {

        PackageManager manager = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = manager.getPackageInfo(getPackageName(),0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }
}
