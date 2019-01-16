package cn.fuzzlog.android_fuzz_logcat_monitor;

/**
 * Created by xcy_m on 2019/1/15.
 */

import java.util.List;

public interface LogReader {

    interface UpdateHandler {
        boolean isCancelled();
        void update(int status, List<String> lines);
    }

    void read(UpdateHandler updateHandler);

}