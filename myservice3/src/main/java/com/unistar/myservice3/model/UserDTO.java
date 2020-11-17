package com.unistar.myservice3.model;

public class UserDTO {
	private Integer id;
	private String name;
	private String email;
	private boolean disabled;

	public UserDTO(){

	}

	public UserDTO(Integer id, String name, String email, boolean disabled){
		this.id = id;
		this.name = name;
		this.email = email;
		this.disabled = disabled;
	}

	public UserDTO(User user)
	{
		this.id = user.getId();
		this.name = user.getName();
		this.email = user.getEmail();
		this.disabled = user.isDisabled();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}
}
