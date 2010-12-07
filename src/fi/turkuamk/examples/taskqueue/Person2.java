package fi.turkuamk.examples.taskqueue;

public class Person2 {
	public String firstName;
	public String lastName;
	public Person2() {}
	public Person2(Person1 p1) {
		String[] name = p1.name.split(" ");
		firstName = name[0];
		lastName = name[1];
	}
}
