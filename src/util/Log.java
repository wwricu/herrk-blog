package util;

public class Log {
    private static LogLevel logLevel = LogLevel.INFO;

    private static String WWR   = "WWR_LOG";
    private static String FATAL = "FATAL";
    private static String ERROR = "ERROR";
    private static String WARN = "WARN";
    private static String INFO = "INFO";
    private static String DEBUG = "DEBUG";

    private static StringBuffer getAncestorName(StackTraceElement[] elementStack, int depth) {
        if (depth > elementStack.length) {
            depth = elementStack.length;
        }

        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < depth; i++) {

            buf.delete(0, buf.length());
            buf.append("file:\t" + elementStack[i].getFileName() + "\t");
            buf.append("class:\t" + elementStack[i].getClassName() + "\t");
            buf.append("line:\t" + elementStack[i].getLineNumber() + "\t");
            buf.append("method:\t" + elementStack[i].getMethodName());
            if (1 == depth) {
                buf.append("\t");
            } else {
                buf.append("\n");
            }
        }

        return buf;
    }

    public static <T> void Fatal(T parameter) {

        StackTraceElement[] stack = new Throwable().getStackTrace();
        StringBuffer prefix = getAncestorName(stack, 1000);

        System.out.println(WWR + "\t" + prefix.toString() + parameter);
    }
    public static <T> void Error(T parameter) {
        System.out.println(parameter);
    }
    public static <T> void Warn(T parameter) {
        if (logLevel == LogLevel.WARN ||
            logLevel == LogLevel.INFO ||
            logLevel == LogLevel.DEBUG) {
                System.out.println(parameter);
        }
    }
    public static <T> void Info(T parameter) {
        if (logLevel == LogLevel.INFO || logLevel == LogLevel.DEBUG) {
            System.out.println(parameter);
        }
    }
    public static <T> void Debug(T parameter) {
        if (logLevel == LogLevel.DEBUG) {
            System.out.println(parameter);
        }
    }
};

enum LogLevel {
    DEBUG,
    INFO,
    WARN,
}