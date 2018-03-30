package com.maning.mnupdateapk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.maning.updatelibrary.InstallUtils;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "InstallUtils";

    //    public static final String APK_URL = "http://download.fir.im/v2/app/install/56dd4bb7e75e2d27f2000046?download_token=e415c0fd1ac3b7abcb65ebc6603c59d9&source=update";
    public static final String APK_URL = "http://download.fir.im/v2/app/install/5a52e936ca87a8600e0002f9?download_token=cd8662357947f151de92975b46082ba6&source=update";
    //    public static final String APK_URL = "https://www.pgyer.com/apiv2/app/install?appKey=e6fcefdffc8c0ef2d7700e867f3b9685&_api_key=ae839fd4e088946dc307140042b97e17";

    public static final String APK_NAME = "update";
    public static final String APK_SAVE_PATH = Environment.getExternalStorageDirectory() + "/MNUpdateAPK";


    private Context context;

    private TextView tv_progress;
    private TextView tv_info;
    private Button btnDownload;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        initViews();

    }

    private void initViews() {
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        tv_info = (TextView) findViewById(R.id.tv_info);
        btnDownload = (Button) findViewById(R.id.btnDownload);
    }

    public void otherPage(View view) {
        startActivity(new Intent(this, OtherActivity.class));
    }

    public void download2(View view) {
        //通过浏览器去下载APK
        InstallUtils.installAPKWithBrower(this, APK_URL);
    }

    public void download(View view) {
        btnDownload.setClickable(false);
        new InstallUtils(context, APK_URL, APK_NAME,new InstallUtils.DownloadCallBack() {
            @Override
            public void onStart() {
                Log.i(TAG, "InstallUtils---onStart");
                tv_progress.setText("0%");
            }

            @Override
            public void onComplete(final String path) {
                Log.i(TAG, "InstallUtils---onComplete:" + path);
                InstallUtils.installAPK(context, path, new InstallUtils.InstallCallBack() {
                    @Override
                    public void onSuccess() {
                        Toast.makeText(context, "正在安装程序", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFail(Exception e) {
                        tv_info.setText("安装失败:" + e.toString());
                    }
                });
                tv_progress.setText("100%");
                tv_info.setText("下载成功");
                btnDownload.setClickable(true);
            }

            @Override
            public void onLoading(long total, long current) {
                Log.i(TAG, "InstallUtils----onLoading:-----total:" + total + ",current:" + current);
                tv_progress.setText((int) (current * 100 / total) + "%");
            }

            @Override
            public void onFail(Exception e) {
                Log.i(TAG, "InstallUtils---onFail:" + e.getMessage());
                tv_info.setText("下载失败:" + e.toString());
                btnDownload.setClickable(true);
            }

        }).downloadAPK();
    }

}
