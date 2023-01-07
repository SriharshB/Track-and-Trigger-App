package com.aditi.reform2;

public class UserHelperClass {

    String username,phoneNo,email,profession,password;

    public UserHelperClass() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserHelperClass(String username, String phoneNo, String email, String profession, String password) {
        this.username = username;
        this.phoneNo = phoneNo;
        this.email = email;
        this.profession = profession;
        this.password = password;
    }
}
