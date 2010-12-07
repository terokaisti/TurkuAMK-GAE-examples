package fi.turkuamk.examples.taskqueue;

import java.net.URL;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.labs.taskqueue.Queue;
import com.google.appengine.api.labs.taskqueue.QueueFactory;
import com.google.appengine.api.labs.taskqueue.TaskOptions;
import com.google.code.twig.ObjectDatastore;
import com.google.code.twig.annotation.AnnotationObjectDatastore;

import static com.google.appengine.api.labs.taskqueue.TaskOptions.Builder.*; // important
import static com.google.appengine.api.labs.taskqueue.TaskOptions.*;


public class SchemaWorkerServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Queue queue = QueueFactory.getDefaultQueue();
		
		TaskOptions options = url("/worker/migrate");
		options.method(Method.POST);
		options.param("to", req.getParameter("to"));
		
		queue.add(options);
		resp.sendRedirect("/jsp/schemamigration.jsp");
	}
	
	ObjectDatastore ds; 
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		
		ds = new AnnotationObjectDatastore();
		if(req.getParameter("to").equals("p1")) {
			workP1();
		}
		else
			workP2();
		
	}
	private void workP1() {
		System.out.println("-> p1");
		for( Person2 p2 : ds.find().type(Person2.class).returnAll().now() ) {
			Person1 p1 = new Person1();
			p1.name = p2.firstName+" "+p2.lastName;
			ds.store(p1);
		}
		ds.deleteAll(Person2.class);

	}
	private void workP2() {
		System.out.println("-> p2");
		for( Person1 p1 : ds.find().type(Person1.class).returnAll().now() ) {
			String[] name = p1.name.split(" ");
			Person2 p2 = new Person2();
			p2.firstName = name[0];
			p2.lastName = name[1];
			ds.store(p2);
		}
		ds.deleteAll(Person1.class);
	}
	
	
}