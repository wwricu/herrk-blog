package util;

public class Log {
    private static LogLevel logLevel = LogLevel.DEBUG;

    private static String WWR   = "WWR_LOG";
    private static String FATAL = "_FATAL";
    private static String ERROR = "_ERROR";
    private static String WARN = "_WARN";
    private static String INFO = "_INFO";
    private static String DEBUG = "_DEBUG";
    private static String VERBOSE= "_VERBOSE";

    private static StringBuffer getAncestorName() {

        int depth = 3;
        StringBuffer buf = new StringBuffer();
        StackTraceElement[] elementStack = new Throwable().getStackTrace();

        if (depth > elementStack.length) {
            depth = elementStack.length;
        }

        for (int i = 0; i < depth; i++) {

            // buf.append("file: " + elementStack[i].getFileName() + "\t");
            // buf.append("class: " + elementStack[i].getClassName() + "\t");
            buf.append("line: " + elementStack[i].getLineNumber() + "\t");
            buf.append("method: " + elementStack[i].getClassName() + "." + elementStack[i].getMethodName() + "()");
            if (depth - 1 == i) {
                buf.append("\t");
            } else {
                buf.delete(0, buf.length());
            }
        }

        return buf;
    }

    public static <T> void Fatal(T parameter) {

        StringBuffer prefix = getAncestorName();

        System.out.println(WWR + FATAL + "\t" + prefix.toString() + parameter);
    }

    public static <T> void Error(T parameter) {
        StringBuffer prefix = getAncestorName();
        System.out.println(WWR + ERROR + "\t" + prefix.toString() + parameter);
    }

    public static <T> void Warn(T parameter) {
        if (logLevel == LogLevel.WARN ||
            logLevel == LogLevel.INFO ||
            logLevel == LogLevel.DEBUG ||
            logLevel == LogLevel.VERBOSE) {

                StringBuffer prefix = getAncestorName();
                System.out.println(WWR + WARN + "\t" + prefix.toString() + parameter);
        }
    }

    public static <T> void Info(T parameter) {
        if (logLevel == LogLevel.INFO ||
            logLevel == LogLevel.DEBUG ||
            logLevel == LogLevel.VERBOSE) {
            StringBuffer prefix = getAncestorName();
            System.out.println(WWR + INFO + "\t" + prefix.toString() + parameter);
        }
    }

    public static <T> void Debug(T parameter) {
        if (logLevel == LogLevel.DEBUG || logLevel == LogLevel.VERBOSE) {
            StringBuffer prefix = getAncestorName();
            System.out.println(WWR + DEBUG + "\t" + prefix.toString() + parameter);
        }
    }

    public static <T> void Verbose(T parameter) {
        if (logLevel == LogLevel.VERBOSE) {
            StringBuffer prefix = getAncestorName();
            System.out.println(WWR + VERBOSE + "\t" + prefix.toString() + parameter);
        }
    }
};

enum LogLevel {
    VERBOSE,
    DEBUG,
    INFO,
    WARN,
}