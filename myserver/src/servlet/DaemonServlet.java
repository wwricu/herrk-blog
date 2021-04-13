package servlet;

import util.Log;
import util.NetSpeed;
import util.SystemInfo;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;


public class DaemonServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        StringBuilder json = new StringBuilder("{");
        response.setContentType("text/plain");

        if (null == action) {
            Log.Error("action is null");
            return;
        }

        switch (action) {
            case "netspeed":
                json.append("\"inbound\":").append(NetSpeed.getInSpeed());
                json.append(",\"outbound\":").append(NetSpeed.getOutSpeed());
                json.append("}");
                response.getWriter().write(json.toString());
                break;
            case "uptime":
                response.getWriter().write(String.valueOf(SystemInfo.getUptime()));
                break;
            default:
                Log.Info("unrecognized action " + action);
                break;
        }
    }
};