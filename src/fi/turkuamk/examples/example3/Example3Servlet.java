package fi.turkuamk.examples.example3;

import javax.servlet.http.*;
import java.io.IOException;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

@SuppressWarnings("serial")
public class Example3Servlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException {
		System.out.println("test");
		resp.setContentType("text/html");
	}
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		System.out.println("test");
		resp.setContentType("text/html");
		String action = req.getParameter("action").toString();

		String firstName = req.getParameter("firstName").toString();
		String lastName = req.getParameter("lastName").toString();
		
		if(action.equals("store"))
			store_TwigPersist(firstName, lastName);
		resp.sendRedirect("/example3.jsp");
	}
	
	private void store_TwigPersist(String firstName, String lastName) {
		ObjectDatastore ds = new AnnotationObjectDatastore();
		ds.store(new UserEntity(firstName, lastName, "M"));
	}
	/**
	 * Stores instance to Datastore LowLevel
	 * @param req
	 */
	private void store_LowLevel(String firstName, String lastName) {
		
		// http://ikaisays.com/2010/06/03/introduction-to-working-with-app-engine%E2%80%99s-low-level-datastore-api/
		// http://ikaisays.com/2010/07/13/issuing-app-engine-datastore-queries-with-the-low-level-api/

		// Entity creation
		Entity user = new Entity("UserEntry");
		user.setProperty("firstName", firstName);
		user.setProperty("lastName", lastName);
		
		// Stores entity to Datastore
		DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
		ds.put(user);
	}
	
}
