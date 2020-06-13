import dao.UserManagerDAO;
import util.JwtUtils;

import java.io.PrintWriter;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;


public class UserManagerServlet extends HttpServlet {

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        UserManagerDAO dao = new UserManagerDAO();

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String invitation = request.getParameter("invitation");
        String token = request.getParameter("token");

        HttpSession session = request.getSession(false);

        if (null == action) {
            response.getWriter().write("no action!");

            return;
        }

        switch (action) {
            case "login":
                if (0 == dao.authUser(username, password)) {
                    session = request.getSession(true);

                    session.setAttribute("username", username);
                    session.setAttribute("status", "login");

                    String geneToken = JwtUtils.geneJsonWebToken(username);

                    response.getWriter().write(geneToken);
                } else {
                    // fail to log in
                    response.getWriter().write("fail");

                    // request.getRequestDispatcher("fail.html").forward(request, response);
                }
            break;
            case "token":
                String token_username;
                if (true != getLogStatus(username, session)) {
                    token_username = JwtUtils.authJWT(token);
                    if (null != token_username) {
                        session = request.getSession(true);

                        session.setAttribute("username", token_username);
                        session.setAttribute("status", "login");
                        response.getWriter().write(token_username);
                    } else {
                        // fail to log in
                        response.getWriter().write("fail");
                    }
                } else {
                    token_username = (String)session.getAttribute("username");
                    response.getWriter().write(token_username);
                }
                break;

            case "signup":
                if (null == invitation || false == invitation.equals("dayazhuanjia")) {
                    response.getWriter().write("invitation needed");
                }
                if (0 == dao.addUser(username, password)) {
                    session = request.getSession(true);

                    session.setAttribute("username", username);
                    session.setAttribute("status", "login");

                    String geneSignToken = JwtUtils.geneJsonWebToken(username);

                    response.getWriter().write(geneSignToken);
                } else {
                    response.getWriter().write("fail");
                }
                break;

            case "getstatus":
                getLogStatus(username, session);
                break;

            default:
                break;
        }
    }

    protected boolean getLogStatus(String username, HttpSession session) {

        if (null == username || null == session) {
            return false;
        }

        if ((String)session.getAttribute("username") == username
                    && (String)session.getAttribute("status") == "login") {
            return true;
        }

        return false;
    }

};
