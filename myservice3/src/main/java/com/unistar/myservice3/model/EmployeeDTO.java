package com.unistar.myservice3.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
		property = "@id", scope = EmployeeDTO.class)
public class EmployeeDTO {
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private final TimeZone defaultTimeZone = TimeZone.getTimeZone("GMT-5");

	private Integer employeeID;
	private String lastName;
	private String firstName;
	private String title;
	private String titleOfCourtesy;
	private String birthDate;
	private String hireDate;
	private String address;
	private String city;
	private String region;
	private String postalCode;
	private String country;
	private String homePhone;
	private String extension;
	private String photo;
	private String notes;
	private Integer reportsTo;
	private String photoPath;

	// Empty constructor is needed for Jackson to recreate the object from JSON
	public EmployeeDTO() {
		this.firstName = "";
		this.lastName = "";
	}

	public EmployeeDTO(Integer employeeID, String firstName, String lastName) {
		this.employeeID = employeeID;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public EmployeeDTO(Employee entity){
		this.employeeID = entity.getEmployeeID();
		this.lastName = entity.getLastName();
		this.firstName = entity.getFirstName();
		this.title = entity.getTitle();
		this.titleOfCourtesy = entity.getTitleOfCourtesy();
		this.birthDate = this.dateToString(entity.getBirthDate());
		this.hireDate = this.dateToString(entity.getHireDate());
		this.address = entity.getAddress();
		this.city = entity.getCity();
		this.region = entity.getRegion();
		this.postalCode = entity.getPostalCode();
		this.country = entity.getCountry();
		this.homePhone = entity.getHomePhone();
		this.extension = entity.getExtension();
		this.photo = entity.getPhoto();
		this.notes = entity.getNotes();
		this.reportsTo = entity.getReportsTo();
		this.photoPath = entity.getPhotoPath();
	}

	public Employee toEntity() throws ParseException{
		Employee employee = new Employee(this.employeeID, this.firstName, this.lastName);
		employee.setTitle(this.title);
		employee.setTitleOfCourtesy(this.titleOfCourtesy);
		employee.setBirthDate(this.stringToDate(this.birthDate));
		employee.setHireDate(this.stringToDate(this.hireDate));
		employee.setAddress(this.address);
		employee.setCity(this.city);
		employee.setRegion(this.region);
		employee.setPostalCode(this.postalCode);
		employee.setCountry(this.country);
		employee.setHomePhone(this.homePhone);
		employee.setExtension(this.extension);
		employee.setPhoto(this.photo);
		employee.setNotes(this.notes);
		employee.setReportsTo(this.reportsTo);
		employee.setPhotoPath(this.photoPath);
		return employee;
	}

	@Override
	public String toString() {
		return "Employee [employeeID=" + employeeID + ", firstName=" + firstName
				+ ", lastName=" + lastName
				+ "]";
	}

	// custom date related methods handle the date conversion back and forth between the client and the server
	// converts date String into a Date in server's timezone
	public Date stringToDate(String strDate, String timezone) throws ParseException {
		if(strDate == null || strDate.isEmpty())
			return null;

		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.parse(strDate);
	}

	// to set DTO's date to the date String in current user timezone
	public String dateToString(Date date, String timezone) {
		if(date == null)
			return null;

		dateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return dateFormat.format(date);
	}

	public Date stringToDate(String strDate) throws ParseException {
		if(strDate == null || strDate.isEmpty())
			return null;

		dateFormat.setTimeZone(defaultTimeZone);
		return dateFormat.parse(strDate);
	}

	// to set DTO's date to the date String in current user timezone
	public String dateToString(Date date) {
		if(date == null)
			return null;

		dateFormat.setTimeZone(defaultTimeZone);
		return dateFormat.format(date);
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

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
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
