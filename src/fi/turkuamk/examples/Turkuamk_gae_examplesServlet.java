package fi.turkuamk.examples;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Turkuamk_gae_examplesServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().print("Tomorrow is " + tomorrow());
	}

	public String tomorrow() {
		Calendar cal = Calendar.getInstance(TimeZone
				.getTimeZone("Europe/Helsinki"));
		Date date = cal.getTime();
		System.out.println(date.toString());

		cal.add(Calendar.DAY_OF_MONTH, 1);
		date = cal.getTime();
		System.out.println(date.toString());

		DateFormat dfm = new SimpleDateFormat("EEEE");

		return dfm.format(date).toString();
	}
}
