package com.unistar.myservice4.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class UserRest {
    // response model should not contain password for security
    // the userId is to retrieve the user info, should not be like 1,2,3... to prevent hacker guess

    private String userId;
    private String firstName;
    private String lastName;
    private String email;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
