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
        String userName = request.getParameter("userName");
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
            /*
             * Request: usermanager?action=login&username=string&password=string
             * Response: {
             *  "userId": digit,
             *  "userName": "string",
             *  "userGroup": digit
             *  "token": "string"
             * }
             */
            case "login":
                retureCode = dao.authUser(userName, password);
                if (0 < retureCode) {
                    session = request.getSession(true);

                    info = dao.getUserInfo(info.mUserId);

                    session.setAttribute("userId", retureCode);
                    session.setAttribute("userName", userName);
                    session.setAttribute("userGroup", info.mGroup);
                    session.setAttribute("status", "login");

                    json.append("\"userId\":\"").append(retureCode)
                        .append("\",\"userName\":\"").append(userName)
                        .append("\",\"userGroup\":").append(info.mGroup)
                        .append(",\"token\":\"").append(JwtUtils.geneJsonWebToken(userName))
                        .append("\"}");
                    response.getWriter().write(json.toString());
                } else {
                    // fail to log in
                    Log.Info("login fail code is " + retureCode);
                    response.getWriter().write("failure");
                }
            break;
            /*
             * Request: usermanager?action=token&token=string
             * Response: {
             *  "userId": digit,
             *  "userName": "string",
             *  "userGroup": digit,
             *  "token": "string"
             * }
             */
            case "token":
                info = JwtUtils.authJWT(token);
                if (info == null || info.mUserId <= 0) {
                    response.getWriter().write("failure");
                    break;
                }
                info = dao.getUserInfo(info.mUserId);
                session = request.getSession(true);

                session.setAttribute("userId", info.mUserId);
                session.setAttribute("userName", info.mUserName);
                session.setAttribute("userGroup", info.mGroup);
                session.setAttribute("status", "login");
                json.append("\"userId\":\"").append(info.mUserId) // int
                    .append("\",\"userName\":\"").append(info.mUserName) // string
                    .append("\",\"userGroup\":").append(info.mGroup) // int
                    .append("}");
                response.getWriter().write(json.toString());
            break;
            /*
             * Request: ?action=notoken
             * Response: {
             *  "userId": digit,
             *  "userName": "string",
             *  "userGroup": digit,
             *  "token": "string"
             * }
             */
            case "notoken":
                if (null == session) {
                    response.getWriter().write("failure");
                }

                int userId = (int)session.getAttribute("userId");
                userName = (String)session.getAttribute("userName");
                logStatus = (String)session.getAttribute("status");
                String userGroup = (String)session.getAttribute("userGroup");

                if (logStatus.equals("login") && null != userName && userId > 0) {
                    geneSignToken = JwtUtils.geneJsonWebToken(userName);
                    json.append("\"userId\":").append(userId)
                        .append("\"userName\":\"").append(userName)
                        .append("\"userGroup\":\"").append(userGroup)
                        .append("\",\"token\":\"").append(geneSignToken)
                        .append("\"}");
                    response.getWriter().write(json.toString());
                }
            break;

            case "signup":
                if (null == invitation || false == invitation.equals("dayazhuanjia")) {
                    response.getWriter().write("invitation needed");
                    return;
                }
                info = dao.addUser(userName, password);
                String failMsg = null;
                if (info.mUserId > 0) {
                    session = request.getSession(true);

                    session.setAttribute("userId", info.mUserId);
                    session.setAttribute("userName", userName);
                    session.setAttribute("userGroup", info.mGroup);
                    session.setAttribute("status", "login");

                    geneSignToken = JwtUtils.geneJsonWebToken(userName);

                    response.getWriter().write(geneSignToken);
                } else {
                    switch (info.mUserId) {
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
            case "logout":
                session.setAttribute("status", "logout");
                session.invalidate();
            break;
            default:
                Log.Info("unrecognized action");
        }
    }

    protected boolean getLogStatus(String username, HttpSession session) {

        if (null == username || null == session) {
            return false;
        }

        if ((String)session.getAttribute("userName") == username
                    && (String)session.getAttribute("status") == "login") {
            return true;
        }

        return false;
    }

};
