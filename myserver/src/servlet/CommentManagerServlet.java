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
import java.sql.Date;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

public class CommentManagerServlet extends HttpServlet {

    private int mCommentNumber;

    public void init() throws ServletException {
        super.init();
        Log.Info("start Article Servlet");
        mCommentNumber = CommentManagerDAO.getCommentNum(0);
        Log.Info("CommentManagerServlet.init() Comment number = " + mCommentNumber);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String action = request.getParameter("action");

        int commentId = 0;
        int autherId = 0;
        int articleId = -1;
        int replyCommentId = -1;
        int index = 0;
        int num = 0;
        int replyId = 0;

        if (request.getParameter("commentId") != null) {
            commentId = Integer.parseInt(request.getParameter("commentId"));
        }
        if (request.getParameter("autherId") != null) {
            autherId = Integer.parseInt(request.getParameter("autherId"));
        }
        if (request.getParameter("articleId") != null) {
            articleId = Integer.parseInt(request.getParameter("articleId"));
        }
        if (request.getParameter("replyCommentId") != null) {
            replyCommentId = Integer.parseInt(request.getParameter("replyCommentId"));
        }
        if (request.getParameter("index") != null) {
            index = Integer.parseInt(request.getParameter("index"));
        }
        if (request.getParameter("num") != null) {
            num = Integer.parseInt(request.getParameter("num"));
        }
        if (request.getParameter("replyid") != null) {
            replyId = Integer.parseInt(request.getParameter("replyid"));
        }

        String nickName = request.getParameter("nickName");
        String avatarLink = request.getParameter("avatarLink");
        String email = request.getParameter("email");
        String website = request.getParameter("website");
        String body = request.getParameter("body");

        CommentInfo info = new CommentInfo();
        CommentInfo[] infoArray = null;
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
                        new java.sql.Date(System.currentTimeMillis()).toString(), "");
                Log.Info(info.toJson().toString());
                if (articleId == 0) {
                    mCommentNumber++;
                }
                response.getWriter().write(
                    json.append("\"commentId\":")
                        .append(CommentManagerDAO.createComment(info))
                        .append("}")
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
                if (articleId == 0) {
                    mCommentNumber--;
                }
                info.mCommentId = commentId;
                response.getWriter().write(
                        json.append("\"commentId\":")
                            .append(CommentManagerDAO.deleteComment(info, 0))
                            .toString());
                break;
            case "maincommentnum":
            // ?action=maincommentnum&articleId=0
            int count = CommentManagerDAO.getCommentNum(articleId);
            Log.Info("article" + articleId + " have " + count + " comments");
            response.getWriter().write(String.valueOf(count));
            break;
            case "getlatest":
            /* ?action=latestcomments&articleId=0&replyid&index=0&num=5
            {
                subComments: [{
                    "commentId": 1,
                    "autherId": 11,
                    "articleId": 111,
                    "replyCommentId": 1111,
                    "nickname": "xxx"
                    "avatarLink": "xxx",
                    "email": "xxx",
                    "website": "xxx",
                    "body": "xxx",
                    "createdTime": "xxx",
                    subComments: [
                        {...}, {...}
                    ]
                }, {
                }]
            }*/
            infoArray = CommentManagerDAO.getLatestComments(0, 5, articleId, replyId, "created_time");
            if (infoArray == null) {
                response.getWriter().write("failure");
            }
            for (var comInfo: infoArray) {
                comInfo.mSubComments = getSubComment(comInfo.mCommentId, comInfo.mArticleId);
            }
            json.append("\"subComments\":[");
            for (int i = 0; i < infoArray.length; i++) {
                json.append(infoArray[i].toJson());
                if (i != infoArray.length - 1) {
                    json.append(",");
                }
            }

            response.getWriter().write(
                    json.append("]}")
                    .toString()
                    .replace("\r", "\\r")
                    .replace("\n", "\\n"));
            break;
            case "allcomments":
            /* ?action=allcomments
            {
                subComments: [{
                    "commentId": 1,
                    "autherId": 11,
                    "articleId": 111,
                    "replyCommentId": 1111,
                    "nickname": "xxx"
                    "avatarLink": "xxx",
                    "email": "xxx",
                    "website": "xxx",
                    "body": "xxx",
                    "createdTime": "xxx",
                    subComments: [
                        {...}, {...}
                    ]
                }, {
                }]
            }*/
            Log.Info("get all comments on board");
            CommentInfo[] comments = getSubComment(0, 0);
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

    private static CommentInfo[] getSubComment(int replyCommentId, int articleId) {
        CommentInfo[] comments = CommentManagerDAO.searchComment(replyCommentId, articleId, 2);
        for (var info: comments) {
            info.mSubComments = getSubComment(info.mCommentId, articleId);
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
