package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import util.ArticleInfo;
import util.Log;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

public class ArticleManagerServlet extends HttpServlet {

    private int mArticleNumber;

    public void init() throws ServletException {
        super.init();
        Log.Info("start Article Servlet");

        NumberCountDAO numberCountDAO = new NumberCountDAO();
        mArticleNumber = numberCountDAO.getArticleCount();

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        Log.Info("ArticleManagerServlet.init() article number = " + mArticleNumber);
    }

    /*
    Functions:
    1. Post an article
    2. Delete an article
    3. Update an article
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String logStatus = (String)session.getAttribute("status");
        if (logStatus == null || true != logStatus.equals("login")) {
            return;
        }
        int userId = (int) session.getAttribute("userid");
        Log.Info("article manager invoked, user is " + userId);

        String action = request.getParameter("action");

        String articleIdS = request.getParameter("articleId");
        String classIdS = request.getParameter("classId");
        String title = request.getParameter("title");
        String summary = request.getParameter("summary");
        String tags = request.getParameter("tags");
        String bodyMD = request.getParameter("bodyMD");
        String permission = request.getParameter("permission");

        // String autherIdS = request.getParameter("autherid");
        // String createTime = request.getParameter("mCreateTime");
        // String lastModifyTime = request.getParameter("mLastModifyTime");

        int articleId = 0;
        int classId = 0;
        if (articleIdS != null && articleIdS.length() != 0) {
            articleId = Integer.parseInt(articleIdS);
        }
        if (classIdS != null && classIdS.length() != 0) {
            classId = Integer.parseInt(classIdS);
        }
        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        NumberCountDAO numberCountDAO = new NumberCountDAO();
        ArticleInfo info = new ArticleInfo();
        java.sql.Date currentTime = new java.sql.Date(System.currentTimeMillis());
        response.setContentType("text/plain");

        switch (action) {
            case "post":
                Log.Info("post an article");
                info.setValue(0, userId, classId, title, summary, tags, bodyMD, currentTime.toString(), currentTime.toString(), 0);
                // articleId, userId, classId, title, summary, tags, body, createtime, lstmodftime, permission
                int createId = articleManagerDAO.createArticle(info);
                if (createId > 0) {
                    Log.Info("article id is " + createId);
                } else {
                    Log.Info("article id is " + createId);
                    response.getWriter().write("fail");
                }
                break;
            case "delete":
                Log.Info("delete article No. " + articleId);
                if (articleManagerDAO.legalAuthor(articleId, userId) != true) {
                    Log.Warn("auther id failure");
                    break;
                }
                articleManagerDAO.deleteArticle(articleId);
                response.getWriter().write("success");
                break;
            case "update":
                /* ?action=update&articleId=1&title=ww&summary=ww&tags=www&bodyMD=www?permission=1 */
                Log.Info("update article No. " + articleId);
                if (articleManagerDAO.legalAuthor(articleId, userId) != true) {
                    Log.Warn("auther id failure");
                    break;
                }
                info.setValue(0, userId, classId, title, summary, tags, bodyMD, null, currentTime.toString(), 0);
                articleManagerDAO.updateArticle(articleId, info);
            default:
                Log.Info("unrecognized action " + action);
                break;
        }
    }
}
