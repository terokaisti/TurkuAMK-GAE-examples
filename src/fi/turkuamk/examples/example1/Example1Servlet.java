package fi.turkuamk.examples.example1;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Example1Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/plain");
		resp.getWriter().print("Huomenna on " + tomorrow());
	}

	/**
	 * Returns tomorrow week day name
	 * 
	 * @return String Tomorrow weekday
	 */
	public String tomorrow() {
		
		final String weekDay = "EEEE"; // weekday argument
		
		Calendar cal = Calendar.getInstance(TimeZone
				.getTimeZone("Europe/Helsinki"));
		Date date = cal.getTime();
		System.out.println(date.toString());

		cal.add(Calendar.DAY_OF_MONTH, 1);
		date = cal.getTime();
		System.out.println(date.toString());

		DateFormat dfm = new SimpleDateFormat(weekDay);

		return dfm.format(date).toString();
	}
}
