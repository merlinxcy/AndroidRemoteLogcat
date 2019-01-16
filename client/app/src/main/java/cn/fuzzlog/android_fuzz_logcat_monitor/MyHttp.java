package cn.fuzzlog.android_fuzz_logcat_monitor;

import android.provider.Settings;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//import
import android.util.Base64;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by xcy_m on 2019/1/15.
 */

public class MyHttp {
    private String path;
    private String logdata;
    public MyHttp(String url,String logdata){
        this.path = url;
        this.logdata = logdata;

    }
    public int okpost()throws Exception{
//        FormBody.
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        formBodyBuilder.add("logdata", Base64.encodeToString(logdata.getBytes(),Base64.DEFAULT));
        FormBody formBody = formBodyBuilder.build();
        Request.Builder builder = new Request.Builder();
        builder = builder.url(path);
        builder = builder.post(formBody);
        Request request = builder.build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        return response.code();

    }
    public int send()throws Exception{
        //put log data
        StringBuffer buffer = new StringBuffer();
        buffer.append("logdata=").append(new String(Base64.encodeToString(logdata.getBytes(),Base64.DEFAULT)));
        System.out.println(path);
        URL url = new URL(path);
        byte[] data = buffer.toString().getBytes();
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(3000);
        connection.setReadTimeout(8000);
//        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");
        connection.setUseCaches(false);
        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        connection.setRequestProperty("Content-Length",String.valueOf(data.length));
//        connection.connect();
        OutputStream outputPost = new BufferedOutputStream(connection.getOutputStream());
//        OutputStream outputStream = connection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(outputPost, "UTF-8"));
        writer.write("asd");
        writer.flush();
        writer.close();
        outputPost.close();
        connection.disconnect();
        System.out.println("oooooooooooooooooooooooo");
        return 1;
    }
    public void run() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL("http://192.168.0.13:12345/as");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            InputStream in = connection.getInputStream();
            //对获取到的输入流进行读取
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                response.append(line);
            }
//            showResponse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null){
                connection.disconnect();
            }
        }
    }

}
