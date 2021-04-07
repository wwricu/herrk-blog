package util;

public class Daemon {
    public static void startDaemon() {
        Log.Info("daemon start");
        NetSpeed netSpeed = new NetSpeed();
        new Thread(netSpeed).start();
    }
};