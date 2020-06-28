package filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {
    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession(true);

        String ip = request.getRemoteAddr();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = new Date();
        String date = sdf.format(d);

        System.out.println("visitor info:" + date + " " + ip);

        String url = request.getRequestURI();
        String action = request.getParameter("action");

        /* here add filter url! */
        switch (url) {
            case "/articlemanager":
                if ("login" != session.getAttribute("status")) {
                    response.sendRedirect("/login.html");
                }
                break;
            default:
                break;
        }

        chain.doFilter(request, response);
        return;
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        System.out.println("Login Filter init()");
    }
};
