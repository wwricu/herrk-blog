import dao.ClickCountDAO;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ClickCountServlet extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        int count = 0;
        ClickCountDAO dao = new ClickCountDAO();
        HttpSession session = request.getSession(false);

        if (null == session) {
            dao.clickPlus();
            count = dao.getCount();

            session = request.getSession(true);
            session.setAttribute("count", String.valueOf(count));
        } else {
            count = Integer.parseInt((String)session.getAttribute("count"));
        }

        try {
            response.getWriter().write(String.valueOf(count));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    }

};
