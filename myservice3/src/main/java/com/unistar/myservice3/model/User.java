package com.unistar.myservice3.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name="Users")
public class User
{
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(nullable=false)
	private String name;
	
	@Column(nullable=false, unique=true)
	private String email;
	
	private boolean disabled;
	
	@OneToMany(mappedBy="user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Address> addresses = new HashSet<>();
	
	public User()
	{
	}

	public User(String name, String email)
	{
		this.name = name;
		this.email = email;
		this.disabled = false;
	}

	public User(Integer id, String name, String email, boolean disabled)
	{
		this.id = id;
		this.name = name;
		this.email = email;
		this.disabled = disabled;
	}

	public User(UserDTO dto)
	{
		this.id = dto.getId();
		this.name = dto.getName();
		this.email = dto.getEmail();
		this.disabled = dto.isDisabled();
	}

	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public boolean isDisabled()
	{
		return disabled;
	}

	public void setDisabled(boolean disabled)
	{
		this.disabled = disabled;
	}

	public Set<Address> getAddresses()
	{
		return addresses;
	}

	public void setAddresses(Set<Address> addresses)
	{
		this.addresses = addresses;
	}
}
