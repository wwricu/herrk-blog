package util;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class NetSpeed implements Runnable {
    private static final String PATH = "/home/proc/net/dev";
    private static final String ETH  = "eth0:";
    private static final String ENP = "enp2s0:";
    private static long inSpeed;
    private static long outSpeed;

    private NetDevInfo mInfo;
    private long mUpdateTime;
    public int mThrottle;

    NetSpeed() {
        mInfo = null;
        inSpeed = 0;
        outSpeed = 0;
        mThrottle = 2000;
    }

    private void updateNetStat() {

        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        String line = null;
        NetDevInfo info = null;

        try {
            fileInputStream = new FileInputStream(PATH);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));

            while ((line = bufferedReader.readLine()) != null) {
                NetDevInfo n = getNetInfo(line);
                if (n.name.equals(ETH) || n.name.equals(ENP)) {
                    info = n;
                    break;
                }
                Log.Error("Did not found net");
            }
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (info == null) {
            Log.Error("info is null");
            return;
        }

        long curTime = new Date().getTime();

        synchronized (NetSpeed.class) {
            if (mInfo != null) {
                inSpeed = minusStr(info.receive.bytes, mInfo.receive.bytes) / (curTime - mUpdateTime);
                outSpeed = minusStr(info.transmit.bytes, mInfo.transmit.bytes) / (curTime - mUpdateTime); // (byte per millionsecond)
            }
        }

        mUpdateTime = curTime;
        mInfo = info;
    }

    public static long minusStr(String str1, String str2) {
        long res = 0;
        String big = null, lit = null;
        int i = 0, j = 0, carry = 0, pow = 1;

        if (str1.length() == 0 || str2.length() == 0 || str1.equals(str2)) {
            return res;
        }

        if (str1.length() > str2.length()) {
            big = str1;
            lit = str2;
        } else if (str1.length() < str2.length()) {
            big = str2;
            lit = str1;
        } else {
            while (i < str1.length()) {
                if (str1.charAt(i) > str2.charAt(i)) {
                    big = str1;
                    lit = str2;
                    break;
                } else if (str1.charAt(i) < str2.charAt(i)) {
                    big = str2;
                    lit = str1;
                    break;
                }
                i++;
            }
        }

        i = big.length() - 1;
        j = lit.length() - 1;

        while (j >= 0) {
            if (big.charAt(i) - carry >= lit.charAt(j)) {
                res += pow * (big.charAt(i) - carry - lit.charAt(j));
                carry = 0;
            } else {
                res += pow * (10 + big.charAt(i) - carry - lit.charAt(j));
                carry = 1;
            }
            pow *= 10;
            i--;
            j--;
        }

        while (i >= 0) {
            res += pow * (big.charAt(i) - '0' - carry);
            carry = 0;
            pow *= 10;
            i--;
        }

        return res;
    }

    private static NetDevInfo getNetInfo(String line) {
        NetDevInfo info = new NetDevInfo();
        if (line == null || line.length() == 0) {
            return info;
        }

        String[] data = new String[17];

        int s = 0, e = 0;
        for (int i = 0; i < 17; i++) {
            while (s < line.length() && line.charAt(s) == ' ') {
                s++;
            }
            e = s;
            while (e < line.length() && line.charAt(e) != ' ') {
                e++;
            }
            data[i] = line.substring(s, e);
            s = e;
        }

        s = 0;
        info.name = data[s++];
        if (info.name.charAt(info.name.length() - 1) != ':') {
            Log.Info("Not a valid net name");
            return info;
        }

        info.receive.bytes = data[s++];
        info.receive.packets = data[s++];
        info.receive.errs = data[s++];
        info.receive.drop = data[s++];
        info.receive.fifo = data[s++];
        info.receive.frame = data[s++];
        info.receive.compress = data[s++];
        info.receive.multicast = data[s++];

        info.transmit.bytes = data[s++];
        info.transmit.packets = data[s++];
        info.transmit.errs = data[s++];
        info.transmit.drop = data[s++];
        info.transmit.fifo = data[s++];
        info.transmit.frame = data[s++];
        info.transmit.compress = data[s++];
        info.transmit.multicast = data[s++];

        return info;
    }

    public void run() {
        Log.Info("start running net speed");
        while(true) {
            try {
                Thread.sleep(mThrottle);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            updateNetStat();
            Log.Debug("Inbound Speed: " + inSpeed + " Outbound Speed: " + outSpeed);
        }
    }

    public static synchronized long getInSpeed() {
        return inSpeed;
    }

    public static synchronized long getOutSpeed() {
        return outSpeed;
    }

}

class NetDevInfo {
    NetDevInfo() {
        receive = new NetInfo();
        transmit = new NetInfo();
    }
    public String name;
    public NetInfo receive;
    public NetInfo transmit;
}

class NetInfo {
    public String bytes;
    public String packets;
    public String errs;
    public String drop;
    public String fifo;
    public String frame;
    public String compress;
    public String multicast;
}