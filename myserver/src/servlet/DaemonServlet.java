package servlet;

import util.Log;
import util.NetSpeed;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;


public class DaemonServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Log.Info("Daemon servlet");

        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        StringBuilder json = new StringBuilder("{");

        if (null == action) {
            Log.Error("action is null");
            return;
        }

        switch (action) {
            case "netspeed":
                json.append("\"inbound\"").append(":").append(NetSpeed.getInSpeed());
                json.append(",");
                json.append("\"outbound\"").append(":").append(NetSpeed.getOutSpeed());
                json.append("}");
                response.getWriter().write(json.toString());
                break;
            default:
                break;
        }
    }
};