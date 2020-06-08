import dao.UserManagerDAO;

import java.io.PrintWriter;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

 
public class UserManagerServlet extends HttpServlet {
 
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
 
        UserManagerDAO dao = new UserManagerDAO();

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String invitation = request.getParameter("invitation");

        String html = null;
        int failCode = -1;

        if (null == action || null == username || null == password) {
            html = "<div style='color:red'>failed</div>";
            PrintWriter pw = response.getWriter();
            pw.println(html);

            return;
        } else if (action.equals("signup") && !invitation.equals("dayazhuanjia")) {
            html = "<div style='color:red'>wrong invitation code</div>";
            PrintWriter pw = response.getWriter();
            pw.println(html);

            return;
        } else {
            //log
        }

        if (true == action.equals("signup")) {
            failCode = dao.addUser(username, password);
            if (0 == failCode) {
                html = "<div style='color:green'>successfully signed in</div>";
            } else if (-2 == failCode) {
                html = "<div style='color:red'>failed to signed in, invalid user name!</div>";
            } else if (-3 == failCode) {
                html = "<div style='color:red'>failed to signed in, invalid password!</div>";
            } else if (-5 == failCode) {
                html = "<div style='color:red'>failed to signed in, user name existed</div>";
            } else {
                html = "<div style='color:red'>failed to signed in, failure code is " + failCode + " </div>";
            }
        } else if (true == action.equals("login")) {
            failCode = dao.authUser(username, password);
            if (0 == failCode) {
                String passwd = dao.getPasswd(username);

                request.getSession(true).setAttribute("login_username", username);
                request.getSession(true).setAttribute("login_password", passwd);
                String a = (String)request.getSession(true).getAttribute("login_password");

                html = "<div style='color:green'>successfully loged in " + a + " </div>";
            } else if (-1 == failCode) {
                html = "<div style='color:red'>failed to signed in,invalid user name!</div>";
            } else if (-2 == failCode) {
                html = "<div style='color:red'>failed to signed in,invalid password!</div>";
            } else if (-3 == failCode) {
                html = "<div style='color:red'>failed to signed in, password mismatched!</div>";
            } else {
                html = "<div style='color:red'>failed 2</div>";
            }
        }

        PrintWriter pw = response.getWriter();
        pw.println(html);

        return;
    }
 
};  
