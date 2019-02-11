package com.example.imyasfinal;

import android.net.Uri;

public class Artist {
    public String password;
    public String email;
    public String firstname;
    public String lastname;
    public String contact;
    public String location;
    public String description;
    public String image;

    public Artist() {
    }

    public Artist(String s, String toString) {
    }

    public Artist(String firstname, String lastname, String contact, String location, String description, String password, String email, String image) {
        this.password = password;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
        this.contact = contact;
        this.location = location;
        this.description = description;
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

