package servlet;

import dao.InitDAO;
import util.*;

import java.io.PrintWriter;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

public class InitServlet extends HttpServlet {
    @Override
    public void init() throws ServletException {
        super.init();
        Log.Info("start init servlet");
        InitDAO.init();
        Daemon.startDaemon();
    }
}
