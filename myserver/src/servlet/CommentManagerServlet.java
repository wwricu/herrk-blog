package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import dao.UserManagerDAO;
import dao.ClassManagerDAO;

import util.ArticleInfo;
import util.ClassInfo;
import util.Log;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

public class CommentManagerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession(true);

        String action = request.getParameter("action");

        int autherId = 0;
        if (request.getParameter("autherId") != null) {
            autherId = request.getParameter("autherId");
        }
        int articleId = -1;
        if (request.getParameter("articleId") != null) {
            autherId = request.getParameter("articleId");
        }
        int replyCommentId = -1;
        if (request.getParameter("replyCommentId") != null) {
            replyCommentId = request.getParameter("replyCommentId");
        }

        String className = request.getParameter("nickName");
        String avatarLink = request.getParameter("avatarLink");
        String website = request.getParameter("website");
        String bodyMD = request.getParameter("bodyMD");

        CommentInfo info = new CommentInfo();
        StringBuilder json = new StringBuilder("{");
        
        response.setContentType("text/plain");

        switch (action) {
            case "post":
                /* ?action=post&autherId=0&articleId=0&replyCommentId=0&nickName=ww&email=ww&website=ww&bodyMD=ww&avatarLink=ww
                {
                    "commentId": 1,
                    "result": "success"
                }*/
                Log.Info("create a comment");
                break;
            case "delete":
                /* ?action=delete&commentId=1
                {
                    "commentId": 1,
                    "result": "success"
                }*/
                if (!isUserAuthorized(session)) {
                    Log.Error("Unauthorized access!");
                }
                Log.Info("delete comment No. ");
                break;
            case "allcomments":
            /* ?action=allcomments
            {
                subComments: [{
                    "commentId": 1,
                    "autherName": "xxx",
                    "email": "xxx",
                    "website": "xxx",
                    "bodyMD": "xxx",
                    "articleId": 0,
                    "replyCommentId": 1,
                    subCommments: [
                        {...}, {...}
                    ]
                }, {
                    
                }]
            }*/
            Log.Info("get all comments");
            break;
            default:
                Log.Info("unrecognized action " + action);
        }
    }

    private boolean isUserAuthorized(HttpSession session) {
        if (session == null) {
            return false;
        }

        String logStatus = (String)session.getAttribute("status");
        if (logStatus == null || !logStatus.equals("login")) {
            Log.Error("not logged in!");
            return false;
        }

        int userGroup = (int)session.getAttribute("userGroup");
        int userId = (int) session.getAttribute("userId");
        if (userGroup < 1) {
            Log.Error("unauthorized group, user is "
                        + userId + " group is " + userGroup);
            return false;
        }

        return true;
    }
}
