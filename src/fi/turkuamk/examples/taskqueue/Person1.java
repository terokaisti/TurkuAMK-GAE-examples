package fi.turkuamk.examples.taskqueue;

public class Person1 {
	public String name;
	public Person1() {}
	public Person1(Person2 p2) {
		name = p2.firstName+" "+p2.lastName;
	}
}
