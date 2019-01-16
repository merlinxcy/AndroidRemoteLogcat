package cn.fuzzlog.android_fuzz_logcat_monitor;

import java.io.DataOutputStream;

/**
 * Created by xcy_m on 2019/1/16.
 */

public class RootManager {
    public static boolean RunRootPerrmisson(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777" + pkgCodePath;
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();

            }catch (Exception e){

            }
        }
    return true;
    }

}
