package fi.turkuamk.examples;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Turkuamk_gae_examplesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().println("Hello, world");
	}
}
