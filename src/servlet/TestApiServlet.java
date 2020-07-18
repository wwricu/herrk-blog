package servlet;

import util.Log;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;


public class TestApiServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");
        if (null == action) {
            return;
        }

        switch (action) {
            case "destroysession":
                session.invalidate();
                // response.sendRedirect("/");
                break;
            case "logtest":
                Log.Info("1111");
                break;
            default:
                break;
        }

        return;
    }
};