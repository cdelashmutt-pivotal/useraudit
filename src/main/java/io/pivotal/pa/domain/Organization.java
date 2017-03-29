package io.pivotal.pa.domain;

import java.util.List;

/**
 * Created by grog on 3/29/2017.
 */
public class Organization {

	private String id;

	private String name;

	private List<User> users;

	private List<Space> spaces;

	public Organization() {

	}

	public Organization(String id, String name) {
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Space> getSpaces() {
		return spaces;
	}

	public void setSpaces(List<Space> spaces) {
		this.spaces = spaces;
	}
}
