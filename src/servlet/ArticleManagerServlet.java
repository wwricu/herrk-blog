package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import util.ArticleInfo;
import util.JwtUtils;

import java.io.IOException;
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

        String action = request.getParameter("action");

        if ("login" != session.getAttribute("status")) {
            response.getWriter().write("no login");
            return;
        }

        switch (action) {
            case "post":
            case "delete":
            case "update":
            default:
        }

        return;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            return;
        }

}
