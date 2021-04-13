package util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class SystemInfo implements Runnable {
    private static final String UPTIME_PATH = "/home/proc/uptime";
    private static long mUpTime = 0;
    public int mThrottle = 2000;

    public static long getUptime() {

        long upTime = 0;
        String line = null;
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;

        try {
            fileInputStream = new FileInputStream(UPTIME_PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            while ((line = bufferedReader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == '.') {
                        upTime = Long.parseLong(line.substring(0, i));
                        break;
                    }
                }
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mUpTime = upTime;
        return upTime;
    }

    public void run() {
        Log.Info("start system info daemon");
        while(true) {
            try {
                Thread.sleep(mThrottle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getUptime();
            Log.Verbose("uptime is " + mUpTime);
        }
    }
}

