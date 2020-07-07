package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import util.ArticleInfo;

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
    private ArticleInfo[] mArticleInfos;

    public void init() throws ServletException {
        super.init();
        System.out.println("wangweiran start Article Servlet");

        NumberCountDAO numberCountDAO = new NumberCountDAO();
        mArticleNumber = numberCountDAO.getArticleCount();

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        mArticleInfos = articleManagerDAO.getLatestArticles(0, mArticleNumber);
        System.out.println("ArticleManagerServlet.init() article number = " + mArticleNumber);

        return;
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
        int userId = (int)session.getAttribute("userid");

        String action = request.getParameter("action");

        String articleId = request.getParameter("articleId");
        String autherId = request.getParameter("autherid");
        String title = request.getParameter("title");
        String summary = request.getParameter("mSummary");
        String tags = request.getParameter("mTags");
        // String createTime = request.getParameter("mCreateTime");
        // String lastModifyTime = request.getParameter("mLastModifyTime");
        String permission = request.getParameter("mPermission");

        String article = request.getParameter("article");

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        NumberCountDAO numberCountDAO = new NumberCountDAO();

        ArticleInfo info = new ArticleInfo();
        java.sql.Date currentTime = new java.sql.Date(System.currentTimeMillis());

        switch (action) {
            case "post":
                info.setValue(0, userId, title, summary, tags, currentTime.toString(), null, 0);
                articleManagerDAO.createArticle(info);
            case "delete":
            case "update":
            default:
                break;
        }

        return;
    }
}
