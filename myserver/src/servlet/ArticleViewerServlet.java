package servlet;

import dao.NumberCountDAO;
import dao.ArticleManagerDAO;
import dao.UserManagerDAO;

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

        Log.Verbose("article viewer servlet");

        String action = request.getParameter("action");
        if (action == null) {
            Log.Error("action is null");
            return;
        }

        HttpSession session = request.getSession(true);
        String logStatus = (String)session.getAttribute("status");
        int userId = -1;
        if (logStatus != null && logStatus.equals("login")) {
            userId = (int) session.getAttribute("userid");
        }

        String index = request.getParameter("index");
        String order = request.getParameter("order");
        String articleId = request.getParameter("articleId");
        Log.Info("action is " + action);

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        NumberCountDAO numberCountDAO = new NumberCountDAO();
        int articleNum = numberCountDAO.getArticleCount();
        StringBuilder json = new StringBuilder("{");
        response.setContentType("text/plain");

        switch (action) {
            // ?action=getnum
            case "getnum":
                Log.Verbose("article num is " + articleNum);
                response.getWriter().write(String.valueOf(articleNum));
            break;
            /* ?action=preview&index=1&order=last_modify_time
               {
                    "article_id": 0,
                    "auther_id": 0,
                    "auther_name": "",
                    "title": "",
                    "summary": "",
                    "tags": "",
                    "bodyMD": "",
                    "create_time": "",
                    "last_modify_time": "",
                    "permission": 0
                };
            */
            case "preview":
                if (Integer.parseInt(index) >= articleNum) {
                    response.getWriter().write("index exceed");
                    break;
                }
                ArticleInfo[] list = articleManagerDAO.getLatestArticles(
                        Integer.parseInt(index), 1, order);
                if (list == null || list.length == 0) {
                    Log.Error("get article failure");
                    break;
                }
                response.getWriter().write(
                    list[0].toJson(UserManagerDAO.getUserName(list[0].mAutherId))
                        .replace("\r", "\\r").replace("\n", "\\n")
                );
            break;
            // ?action=view&articleId=1
            case "view":
                ArticleInfo info = articleManagerDAO.searchArticle(Integer.parseInt(articleId));
                response.getWriter().write(info.toJson());
            break;
            /* ?action=modify&autherId=1&articleId
             * {
             *     "autherName": "wwr",
             *     "isOwner": true
             * }
             */
            case "modify":
                String autherName = "";
                String autherId = request.getParameter("autherId");
                if (autherId != null && autherId.length() > 0) {
                    autherName = UserManagerDAO.getUserName(Integer.parseInt(autherId));
                }
                json.append("\"autherName\":\"")
                    .append(autherName)
                    .append("\",\"isOwner\":");
                /*
                * here we do not strictly judge whether the user is the owner of the article,
                * but only trust the autherId pass from the front end because this API only control the visibility,
                * It front-end cheated, he can only get a button, our article manager will finally decide his permission.
                */
                if (userId == 1 || userId == Integer.parseInt(autherId)) {
                    json.append("true");
                } else {
                    json.append("false");
                }
                response.getWriter().write(json.append("}").toString());
            break;
            default:
                Log.Warn("unrecognized action " + action);
                break;
        }
    }
}
