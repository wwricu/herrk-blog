package servlet;

import util.Log;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class FileManagerServlet extends HttpServlet {

    private String getFileList(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        StringBuilder fileInfo = new StringBuilder("{files:[");
        try {
            File file = new File(dirPath);
            if (!file.isDirectory()) {
                return null;
            }
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                String isDir = fileList[i].isDirectory() == true ? "true": "false";
                fileInfo.append("{name:\'")
                        .append(fileList[i].getName())
                        .append("\', size:")
                        .append(fileList[i].length())
                        .append(", isDir:\'")
                        .append(isDir)
                        .append("\'},");
            }
            fileInfo.append("]}");
            Log.Debug(fileInfo);
        } catch (Exception e) {
            Log.Error("Exception:" + e.getMessage());
            fileInfo = null;
        }
        return fileInfo.toString();
    }

    private boolean isDirectory(String path) {
        try {
            File file = new File(path);
            if (file.isDirectory()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            Log.Error("Exception:" + e.getMessage());
        }
        return false;
    }

    public void service(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        String path = request.getParameter("path");
    /* {
            files: [
                {
                    name: "xxx",
                    size:  xxx,
                    isDir:false
                },
                {
                    name: "xxx",
                    size:  xxx,
                    isDir: true
                }
            ]
        } */
        String result = null;

        switch (action) {
        case "getfilelist":
            result = getFileList(path);
            break;
        default:
            Log.Info("unrecognized action " + action);
            result = "failure";
            break;
        }
        try {
            response.getWriter().write(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
};
