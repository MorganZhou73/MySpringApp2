package com.unistar.myservice2.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.*;

import java.util.Date;

public class Employees {
	@Id private String id;

	@Field("EmployeeID")
	private Integer employeeID;

	@Field("LastName")
	private String lastName;

	@Field("FirstName")
	private String firstName;

	@Field("Title")
	private String title;

	@Field("TitleOfCourtesy")
	private String titleOfCourtesy;

	@Field("BirthDate")
	private Date birthDate;

	@Field("HireDate")
	private Date hireDate;

	@Field("Address")
	private String address;

	@Field("City")
	private String city;

	@Field("Region")
	private String region;

	@Field("PostalCode")
	private String postalCode;

	@Field("Country")
	private String country;

	@Field("HomePhone")
	private String homePhone;

	@Field("Extension")
	private String extension;

	@Field("Photo")
	private String photo;

	@Field("Notes")
	private String notes;

	@Field("ReportsTo")
	private Integer reportsTo;

	@Field("PhotoPath")
	private String photoPath;

	// Empty constructor is needed for Jackson to recreate the object from JSON
	public Employees() {
	}

	public Employees(Integer employeeID, String firstName, String lastName) {
		this.employeeID = employeeID;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Employees(Integer employeeID, String firstName, String lastName,
			String title, Date birthDate, Date hireDate, String address, String city, Integer reportsTo) {
		this.employeeID = employeeID;
		this.firstName = firstName;
		this.lastName = lastName;
		this.title = title;
		this.birthDate = birthDate;
		this.hireDate = hireDate;
		this.address = address;
		this.city = city;
		this.reportsTo = reportsTo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Employees employee = (Employees) o;

		if (employeeID != employee.employeeID ||
				!isEqual(firstName, employee.firstName) ||
				!isEqual(lastName, employee.lastName) ||
				!isEqual(title, employee.title) ||
				!isEqual(titleOfCourtesy, employee.titleOfCourtesy) ||
				!isEqual(birthDate, employee.birthDate) ||
				!isEqual(hireDate, employee.hireDate) ||
				!isEqual(address, employee.address) ||
				!isEqual(city, employee.city) ||
				!isEqual(region, employee.region) ||
				!isEqual(postalCode, employee.postalCode) ||
				!isEqual(country, employee.country) ||
				!isEqual(homePhone, employee.homePhone) ||
				!isEqual(extension, employee.extension) ||
				!isEqual(photo, employee.photo) ||
				!isEqual(notes, employee.notes) ||
				reportsTo != employee.reportsTo ||
				!isEqual(photoPath, employee.photoPath))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (birthDate != null ? birthDate.hashCode() : 0);

		return result;
	}

	private boolean isEqual(String a, String b){
		return a != null ? a.equals(b) : b == null;
	}

	private boolean isEqual(Date a, Date b){
		return a != null ? a.equals(b) : b == null;
	}

	public void copyValues(Employees employee){
		this.employeeID = employee.employeeID;
		this.lastName = employee.lastName;
		this.firstName = employee.firstName;
		this.title = employee.title;
		this.titleOfCourtesy = employee.titleOfCourtesy;
		this.birthDate = employee.birthDate;
		this.hireDate = employee.hireDate;
		this.address = employee.address;
		this.city = employee.city;
		this.region = employee.region;
		this.postalCode = employee.postalCode;
		this.country = employee.country;
		this.homePhone = employee.homePhone;
		this.extension = employee.extension;
		this.photo = employee.photo;
		this.notes = employee.notes;
		this.reportsTo = employee.reportsTo;
		this.photoPath = employee.photoPath;
	}

	public void copyAvailableValues(Employees employee){
		if(employee.employeeID > 0)
			this.employeeID = employee.employeeID;
		if(employee.lastName != null && !employee.lastName.isEmpty())
			this.lastName = employee.lastName;
		if(employee.firstName != null && !employee.firstName.isEmpty())
			this.firstName = employee.firstName;
		if(employee.title != null)
			this.title = employee.title;
		if(employee.titleOfCourtesy != null)
			this.titleOfCourtesy = employee.titleOfCourtesy;
		if(employee.birthDate != null)
			this.birthDate = employee.birthDate;
		if(employee.hireDate != null)
			this.hireDate = employee.hireDate;
		if(employee.address != null)
			this.address = employee.address;
		if(employee.city != null)
			this.city = employee.city;
		if(employee.region != null)
			this.region = employee.region;
		if(employee.postalCode != null)
			this.postalCode = employee.postalCode;
		if(employee.country != null)
			this.country = employee.country;
		if(employee.homePhone != null)
			this.homePhone = employee.homePhone;
		if(employee.extension != null)
			this.extension = employee.extension;
		if(employee.photo != null)
			this.photo = employee.photo;
		if(employee.notes != null)
			this.notes = employee.notes;
		if(employee.reportsTo > 0)
			this.reportsTo = employee.reportsTo;
		if(employee.photoPath != null)
			this.photoPath = employee.photoPath;
	}

	public String getName() {
		return this.firstName + " " + this.lastName;
	}

	public void setName(String name) {
		String[] parts = name.split(",");
		if(parts.length > 1){
			this.firstName = parts[1];
			this.lastName = parts[0];
		}
		else{
			parts = name.split(" ");
			this.firstName = parts[0];
			this.lastName = parts.length > 1 ? parts[1] : "";
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleOfCourtesy() {
		return titleOfCourtesy;
	}

	public void setTitleOfCourtesy(String titleOfCourtesy) {
		this.titleOfCourtesy = titleOfCourtesy;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public String getExtension() {
		return extension;
	}

	public void setExtension(String extension) {
		this.extension = extension;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getReportsTo() {
		return reportsTo;
	}

	public void setReportsTo(Integer reportsTo) {
		this.reportsTo = reportsTo;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
}
