import dao.ClickCountDAO;

import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HomeServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		ClickCountDAO dao = new ClickCountDAO();

		dao.clickPlus();

		int count = dao.getCount();
		
		try {
			response.getWriter().println("<h1 align=\"center\"> total click count is " + count + "</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return;
	}

};
