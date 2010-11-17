package fi.turkuamk.examples.example3;

import javax.persistence.Id;

public class UserEntity {
	@Id
	public Long id;
	public String firstName;
	public String lastName;
	public String gender;
	public UserEntity() {}
	public UserEntity(String firstName, String lastName, String g) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.gender = g;
	}
}
