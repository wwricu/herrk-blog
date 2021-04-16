package servlet;

import dao.UserManagerDAO;
import util.JwtUtils;
import util.Log;
import util.UserInfo;

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

        // declare variable together
        int retureCode; //login

        int tokenUserId;
        String tokenUsername; // token
        UserInfo info = new UserInfo();
        String logStatus; // notoken
        String geneSignToken; // signup

        UserManagerDAO dao = new UserManagerDAO();

        String action = request.getParameter("action");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String invitation = request.getParameter("invitation");
        String token = request.getParameter("token");

        StringBuilder json = new StringBuilder("{");
        HttpSession session = request.getSession(false);
        response.setContentType("text/plain");

        Log.Debug("call user manager servlet, action = " + action);

        if (null == action) {
            response.getWriter().write("no action!");
            return;
        }

        switch (action) {
            case "login":
                retureCode = dao.authUser(username, password);
                if (0 < retureCode) {
                    session = request.getSession(true);

                    session.setAttribute("userid", retureCode);
                    session.setAttribute("username", username);
                    session.setAttribute("status", "login");

                    json.append("\"userId\":\"").append(retureCode)
                        .append("\",\"userName\":\"").append(username)
                        .append("\",\"token\":\"").append(JwtUtils.geneJsonWebToken(username)).append("\"}");
                    response.getWriter().write(json.toString());
                } else {
                    // fail to log in
                    Log.Info("login fail code is " + retureCode);
                    response.getWriter().write("fail");
                }
            break;
            case "token":
                if (true != getLogStatus(username, session)) {
                    info = JwtUtils.authJWT(token);
                    if (null != info) {
                        session = request.getSession(true);

                        session.setAttribute("username", info.mUserName);
                        session.setAttribute("userid", info.mUserId);
                        session.setAttribute("status", "login");
                        json.append("\"userId\":\"").append(info.mUserId)
                            .append("\",\"userName\":\"").append(info.mUserName)
                            .append("\"}");
                        response.getWriter().write(json.toString());
                    } else {
                        response.getWriter().write("fail");
                    }
                } else {
                    tokenUsername = (String)session.getAttribute("username");
                    response.getWriter().write(tokenUsername);
                }
            break;

            case "notoken":
                if (null == session) {
                    response.getWriter().write("fail");
                }

                username = (String)session.getAttribute("username");
                logStatus = (String)session.getAttribute("status");

                if (logStatus.equals("login") && null != username) {
                    geneSignToken = JwtUtils.geneJsonWebToken(username);
                    String result = "{username: \'" + username + "\', token: \'" + geneSignToken + "\'}";
                    response.getWriter().write(result);
                }
            break;

            case "signup":
                if (null == invitation || false == invitation.equals("dayazhuanjia")) {
                    response.getWriter().write("invitation needed");
                    return;
                }
                int ret = dao.addUser(username, password);
                String failMsg = null;
                if (0 == ret) {
                    session = request.getSession(true);

                    session.setAttribute("username", username);
                    session.setAttribute("status", "login");

                    geneSignToken = JwtUtils.geneJsonWebToken(username);

                    response.getWriter().write(geneSignToken);
                } else {
                    switch (ret) {
                    case -1:
                        failMsg = "connect error";
                        break;
                    case -2:
                        failMsg = "invalid username";
                        break;
                    case -3:
                        failMsg = "invalid password";
                        break;
                    default:
                        failMsg = "internal error";
                    }
                    response.getWriter().write(failMsg);
                }
            break;

            case "getstatus":
                getLogStatus(username, session);
            break;

            default:
                Log.Info("unrecognized action");
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
