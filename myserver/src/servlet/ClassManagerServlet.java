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

public class ClassManagerServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        String logStatus = (String)session.getAttribute("status");
        if (logStatus == null ||
                true != logStatus.equals("login")) {
            return;
        }
        int userId = (int) session.getAttribute("userid");
        String action = request.getParameter("action");

        String classIdS = request.getParameter("classId");
        String className = request.getParameter("class_name");
        String fatherIdS = request.getParameter("father_id");
        String groupS = request.getParameter("group");

        int classId = 0;
        int fatherId = 0;
        int group = -1;

        if (fatherIdS != null && fatherIdS.length() != 0) {
            fatherId = Integer.parseInt(fatherIdS);
        }
        if (classIdS != null && classIdS.length() != 0) {
            classId = Integer.parseInt(classIdS);
        }
        if (groupS != null && groupS.length() != 0) {
            group = Integer.parseInt(groupS);
        }

        ArticleManagerDAO articleManagerDAO = new ArticleManagerDAO();
        NumberCountDAO numberCountDAO = new NumberCountDAO();
        ClassManagerDAO classManagerDAO = new ClassManagerDAO();
        ClassInfo info = new ClassInfo();
        StringBuilder json = new StringBuilder("{");
        response.setContentType("text/plain");

        switch (action) {
            case "create":
                Log.Info("create a class");
                info.setValue(0, className, fatherId, group);
                // classId, className, fatherId, group
                int createId = classManagerDAO.createClass(info);
                if (createId > 0) {
                    Log.Info("class id is " + createId);
                } else {
                    Log.Info("class id is " + createId);
                    response.getWriter().write("fail");
                }
                break;
            case "delete":
                Log.Info("delete class No. " + classId);
                articleManagerDAO.deleteArticle(classId);
                break;
            case "update":
                /* ?action=update&articleId=1&title=ww&summary=ww&tags=www&bodyMD=www?permission=1 */
                Log.Info("update article No. " + classId);
                info.setValue(classId, className, fatherId, group);
                classManagerDAO.updateClass(info);
            break;
            /* ?action=allclasses */
            case "allclasses":
                ClassInfo[] allClasses = classManagerDAO.allTopClasses();
                if (allClasses == null || allClasses.length == 0) {
                    response.getWriter().write("failure");
                }
                json.append("\"list\":[");
                for (int i = 0; i < allClasses.length; i++) {
                    json.append(allClasses[i].toJson());
                    if (i != allClasses.length - 1) {
                        json.append(",");
                    }
                }
                json.append("]}");
                response.getWriter().write(json.toString());
            break;
            case "subclasses":
                ClassInfo[] subClasses = classManagerDAO.subClasses(classId);
                if (subClasses == null || subClasses.length == 0) {
                    response.getWriter().write("failure");
                }
                json.append("\"list\":[");
                for (int i = 0; i < subClasses.length; i++) {
                    String fatherName = classManagerDAO.searchClass(subClasses[i].mFatherId).mClassName;
                    json.append(subClasses[i].toJson(fatherName));
                    if (i != subClasses.length - 1) {
                        json.append(",");
                    }
                }
                json.append("]}");
                response.getWriter().write(json.toString());
            break;
            default:
                Log.Info("unrecognized action " + action);
        }
    }
}
