package cn.fuzzlog.android_fuzz_logcat_monitor;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText edit;
    private Button button;
    private void requestPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},1);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.INTERNET},1);
        }
    }
    private void checkRootPermission(){
        boolean rootResult = RootManager.RunRootPerrmisson(getPackageCodePath());
        if(rootResult == false){
            Toast.makeText(this,"需要获取Root权限",Toast.LENGTH_LONG);
            Log.v("log_monitor: ","需要获取Root权限");
            try{
                Thread.sleep(5000);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            this.finish();
        }
    }
    private void startMyService(String ip){
        Intent intent = new Intent(this,MyService.class);
        intent.putExtra("ip", ip);
        startService(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //高版本安卓动态获取权限
        requestPermission();
        //获取root来支持阅读其他的日志
        checkRootPermission();
        edit = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ip = edit.getText().toString();
                Log.v("log_monitor: ",ip);
                startMyService(ip);
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
}
