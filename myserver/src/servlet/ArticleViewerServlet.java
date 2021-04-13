package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import util.ArticleInfo;
import util.Log;

import java.io.File;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

public class ArticleViewerServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String logStatus = (String)session.getAttribute("status");
        if (logStatus == null || true != logStatus.equals("login")) {
            return;
        }
        int userId = (int) session.getAttribute("userid");

        String action = request.getParameter("action");
        if (action == null) {
            Log.Error("action is null");
            return;
        }
        String start = request.getParameter("start");
        String num = request.getParameter("num");
        String order = request.getParameter("order");
        String articleId = request.getParameter("articleId");

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        NumberCountDAO numberCountDAO = new NumberCountDAO();
        StringBuilder json = new StringBuilder("{")

        switch (action) {
            case "getnum":
                response.getWriter().write(numberCountDAO.getArticleCount());
            break;
            case "preview":
                ArticleInfo[] list = articleManagerDAO.getLatestArticles(
                        Integer.parseInt(start), Integer.parseInt(num), order);
                json.append("\"num\":").append(String.valueOf(list.length()).append("\"list\":[");
                for (ArticleInfo info : list) {
                    json.append(info.toJson()).append(",");
                }
                json.append("]}");
                response.getWriter().write(json.toString());
            break;
            case "view":
                ArticleInfo info = articleManagerDAO.searchArticle(Integer.parseInt(articleId));
                response.getWriter().write(info.toJson());
            break;
            default:
                Log.Warn("unrecognized action " + action);
                break;
        }
    }
}
