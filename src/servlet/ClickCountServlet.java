package servlet;

import dao.NumberCountDAO;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClickCountServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        int count = 0;
        NumberCountDAO dao = new NumberCountDAO();
        HttpSession session = request.getSession(false);

        if (null == session) {
            dao.clickCountIncrement();
            count = dao.getClickCount();

            session = request.getSession(true);
            session.setAttribute("count", String.valueOf(count));
        } else {
            count = Integer.parseInt((String)session.getAttribute("count"));
        }

        try {
            if (0 == count) {
                count++;
            }
            response.getWriter().write(String.valueOf(count));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

};
