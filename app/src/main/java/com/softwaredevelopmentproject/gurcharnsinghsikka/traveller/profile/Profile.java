package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import java.util.ArrayList;
import java.util.List;

public class Profile {

    private String userId;
    private String firstName;
    private String lastName;
    private String age;
    private String gender;
    private String bio;
    private String email;
    private String phone;
    private String facebook;
    private List<String> likes;

    public Profile(String userId, String firstName, String lastName, String age, String gender, String bio, String email, String phone, String facebook, List<String> likes) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        this.likes = likes;
    }

    public Profile(String userId, String firstName, String lastName, String age, String gender, String bio, String email, String phone, String facebook, String likes) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.bio = bio;
        this.email = email;
        this.phone = phone;
        this.facebook = facebook;
        setLikes(likes);
    }

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getLikesString() {
        return String.join(", ", likes);
    }

    public List<String> getLikesList() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = new ArrayList<>();
        String[] string = likes.split(", ");
        for (String s : string)
            this.likes.add(s);
    }

    public void setLikes(List<String> likes){
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if(this==null && o==null) return true;
        if(this!=null && o==null) return false;
        if(this==null && o!=null) return false;
        if (!(o instanceof Profile)) return false;
        Profile profile = (Profile) o;
        return getUserId().equals(profile.getUserId());
    }

    @Override
    public int hashCode() {
        return (userId != null ? userId.hashCode() : 0);
    }
}
