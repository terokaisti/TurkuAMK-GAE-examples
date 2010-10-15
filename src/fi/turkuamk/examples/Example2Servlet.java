package fi.turkuamk.examples;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import javax.servlet.http.*;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class Example2Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/html");
		
        UserService userService = UserServiceFactory.getUserService();

        String thisURL = req.getRequestURI();
        String msg = "";
        if (req.getUserPrincipal() != null) {
        	User user = userService.getCurrentUser();
            msg = "<pre>";
            msg += "Name: " +req.getUserPrincipal().getName()+"\n";
            msg += "Nickname: "+user.getNickname()+"\n";
            msg += "Email: "+user.getEmail()+"\n";
            msg += "User id: "+user.getUserId()+"\n";
            msg += "Auth domain: "+user.getAuthDomain()+"\n";
            msg += "Is user admin? "+(userService.isUserAdmin() ? "yes" : "no") +"\n";
            msg += "\nYou can <a href=\""+
            	userService.createLogoutURL(thisURL) +
                "\">sign out</a>.</pre>";
        } else {
            resp.getWriter().println("<pre>Please <a href=\"" +
                                         userService.createLoginURL(thisURL) +
                                         "\">sign in</a>.</pre>");
        }
        resp.getWriter().println(msg);
	}
}
