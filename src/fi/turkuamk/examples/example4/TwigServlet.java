package fi.turkuamk.examples.example4;

import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

@SuppressWarnings("serial")
public class TwigServlet extends HttpServlet {
	ObjectDatastore ds = new AnnotationObjectDatastore();
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		Person entry = new Person();
		entry.name = req.getParameter("name");
		entry.age = new Integer(req.getParameter("age"));
		
		ds.store(entry);
		
		resp.sendRedirect("/store_and_view");
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		System.out.println("test");
		resp.setContentType("text/html");
		
		List<Person> list = ds.find().type(Person.class).returnAll().now();
		for( Person p : list ) {
			resp.getWriter().print("Id: "+p.id+"<br/>Name: "+p.name+"<br/>Age: "+p.age+"<hr/>");
		}
	}
	
}
