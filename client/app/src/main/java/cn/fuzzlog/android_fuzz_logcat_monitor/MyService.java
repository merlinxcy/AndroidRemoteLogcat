package cn.fuzzlog.android_fuzz_logcat_monitor;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import javax.net.ssl.HandshakeCompletedListener;

public class MyService extends Service {
    private String ip;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags,int startid){
//        super.onStartCommand()
//        intent.get
        ip = intent.getStringExtra("ip");
        mThread obj = new mThread();
        obj.start();
//        obj.run(); //run会阻塞ui
        CheckThread obj2 = new CheckThread();
        obj2.start();
        return START_NOT_STICKY;
    }
    //共享信号
    private List list = new ArrayList();
    private boolean pushFlag = false;
    private String buffer = new String();
    private ReentrantReadWriteLock locker = new ReentrantReadWriteLock();
    //共享信号
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            System.out.print("%%called");
            if(msg.what == 0){
                System.out.print("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                System.out.print(list);
            }
        }
    };
    ///////////////////////////////////////////////
    private class CheckThread extends Thread{
        @Override
        public void run(){
            long start = System.currentTimeMillis();
            long now;
            while(true){
                now = System.currentTimeMillis();
                if((now-start) >= 1000){
                    start = System.currentTimeMillis();
                    locker.writeLock().lock();
                    pushFlag = true;
                    locker.writeLock().unlock();
//                    System.out.println("******CheckThread 切换pushflag");
                }
            }
        }
    }
    private class mThread extends Thread{
        @Override
        public void run(){
            BufferedReader bufferedReader = null;

            try {
                String[] command = new String[] { "su","-c", "logcat", "-v", "time" };
                Process process = Runtime.getRuntime().exec(command);
                bufferedReader = new BufferedReader(
                        new InputStreamReader(process.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            while(true){
                //in this thread get the log continuously
                try{
//                    list.addAll(Collections.singletonList(bufferedReader.readLine()));
                    buffer = buffer + bufferedReader.readLine().toString() + "\n";
                }
                catch (IOException e){
                    e.printStackTrace();
                }
                locker.readLock().lock();
                if(pushFlag){
//                    System.out.println("******pushflag变换");
                    MyHttp obj  = new MyHttp("http://"+ ip +"/log",buffer);
                    try{
                        obj.okpost();
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                    buffer = "";
                    pushFlag = false;
                }
                locker.readLock().unlock();
            }
        }
    }
}
