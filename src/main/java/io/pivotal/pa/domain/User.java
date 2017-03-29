package io.pivotal.pa.domain;

import java.util.List;

/**
 * Created by grog on 3/29/2017.
 */
public class User {
	private String name;
	private List<String> roles;

	public User(String name, List<String> roles) {
		this.name = name;
		this.roles = roles;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
