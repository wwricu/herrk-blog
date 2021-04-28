package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import dao.UserManagerDAO;
import dao.ClassManagerDAO;
import dao.CommentManagerDAO;

import util.ArticleInfo;
import util.ClassInfo;
import util.CommentInfo;
import util.Log;

import java.io.File;
import java.util.Date;
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

        int commentId = 0;
        int autherId = 0;
        int articleId = -1;
        int replyCommentId = -1;

        if (request.getParameter("commentId") != null) {
            autherId = Integer.parseInt(request.getParameter("commentId"));
        }
        if (request.getParameter("autherId") != null) {
            autherId = Integer.parseInt(request.getParameter("autherId"));
        }
        if (request.getParameter("articleId") != null) {
            autherId = Integer.parseInt(request.getParameter("articleId"));
        }
        if (request.getParameter("replyCommentId") != null) {
            replyCommentId = Integer.parseInt(request.getParameter("replyCommentId"));
        }

        String nickName = request.getParameter("nickName");
        String avatarLink = request.getParameter("avatarLink");
        String email = request.getParameter("email");
        String website = request.getParameter("website");
        String body = request.getParameter("body");

        CommentInfo info = new CommentInfo();
        StringBuilder json = new StringBuilder("{");
        
        response.setContentType("text/plain");

        switch (action) {
            case "post":
                /* ?action=post&autherId=0&articleId=0&replyCommentId=0&nickName=ww&avatarLink=ww&email=ww&website=ww&body=ww
                {
                    "commentId": 1,
                }*/
                Log.Info("create a comment");
                info.setValue(0, autherId, articleId, replyCommentId,
                        nickName, avatarLink, website, email, body,
                        new Date().toString(), "");
                ;
                response.getWriter().write(
                    json.append("\"commentId\":")
                        .append(CommentManagerDAO.createComment(info))
                        .toString());
                break;
            case "delete":
                /* ?action=delete&commentId=1
                {
                    "commentId": 1,
                }*/
                if (!isUserAuthorized(session)) {
                    Log.Error("Unauthorized access!");
                }
                Log.Info("delete comment No. " + commentId);
                info.mCommentId = commentId;
                response.getWriter().write(
                        json.append("\"commentId\":")
                            .append(CommentManagerDAO.deleteComment(info, 0))
                            .toString());
                break;
            case "allcomments":
            /* ?action=allcomments
            {
                subComments: [{
                    "commentId": 1,
                    "autherName": "xxx",
                    "avatarLink": "xxx",
                    "email": "xxx",
                    "website": "xxx",
                    "body": "xxx",
                    "articleId": 0,
                    "replyCommentId": 1,
                    subCommments: [
                        {...}, {...}
                    ]
                }, {
                }]
            }*/
            Log.Info("get all comments on board");
            CommentInfo[] comments = getComment(0, 0);
            json.append("\"subComments\":[");
            for (int i = 0; i < comments.length; i++) {
                json.append(info.toJson());
                if (i != comments.length - 1) {                    
                    json.append(",");
                }
            }
            json.append("]}");
            response.getWriter().write(
                json.toString()
                    .replace("\r", "\\r")
                    .replace("\n", "\\n"));
            break;
            default:
                Log.Info("unrecognized action " + action);
        }
    }

    private static CommentInfo[] getComment(int replyCommentId, int articleId) {
        CommentInfo[] comments = CommentManagerDAO.searchComment(replyCommentId, articleId, 2);
        for (var info: comments) {
            info.mSubComments = CommentManagerDAO.searchComment(info.mCommentId, articleId, 2);
        }
        return comments;
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
