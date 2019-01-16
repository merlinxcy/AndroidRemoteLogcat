package cn.fuzzlog.android_fuzz_logcat_monitor;

/**
 * Created by xcy_m on 2019/1/15.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
public class LocalLogReader implements LogReader {
    @Override
    public void read(UpdateHandler updateHandler) {

        try {

            String[] command = new String[] { "su","-c", "logcat", "-v", "time" };

//            updateHandler.update(R.string.status_opening, null);

            Process process = Runtime.getRuntime().exec(command);

            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

//            updateHandler.update(R.string.status_active, null);

            while (!updateHandler.isCancelled()) {
                updateHandler.update(0, Collections.singletonList(bufferedReader.readLine()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
