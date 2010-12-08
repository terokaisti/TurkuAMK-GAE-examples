package fi.turkuamk.examples.taskqueue;

import java.net.URL;
import java.util.Iterator;
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
		
		migrate(req.getParameter("to"));
		resp.sendRedirect("/jsp/schemamigration.jsp");
	}
	private void migrate(String to) {
		Queue queue = QueueFactory.getDefaultQueue();
		
		TaskOptions options = url("/worker/migrate");
		options.method(Method.POST);
		options.param("to", to);
		
		queue.add(options);
	}
	ObjectDatastore ds; 
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws IOException {
		
		ds = new AnnotationObjectDatastore();
		boolean retry = false;
		if(req.getParameter("to").equals("p1")) {
			retry = workP1();
		}
		else
			retry = workP2();

		if(retry == true)
			migrate(req.getParameter("to"));
	}
	private boolean workP1() {
		System.out.println("-> p1");
		Iterator<Person2> p2i = ds.find().type(Person2.class).now();
		
		if(p2i.hasNext()) {
			Person2 p2 = p2i.next();
			Person1 p1 = new Person1();
			p1.name = p2.firstName+" "+p2.lastName;
			ds.store(p1);
			ds.delete(p2);
		}
		return p2i.hasNext();
	
	}
	private boolean workP2() {
		System.out.println("-> p2");
		Iterator<Person1> p1i = ds.find().type(Person1.class).now();
		
		if(p1i.hasNext()) {
			Person1 p1 = p1i.next();
			String[] name = p1.name.split(" ");
			Person2 p2 = new Person2();
			p2.firstName = name[0];
			p2.lastName = name[1];
			ds.store(p2);
			ds.delete(p1);
		}
		return p1i.hasNext();
	}
	
	
}