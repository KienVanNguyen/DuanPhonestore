package com.kiennv.duanphonestore.User.Model;

public class User {
    private int id;
    private String phone, password, fullName, email, address;
    private String images;


    public User(int id,String fullName, String email,String address, String phone, String password, String images) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.fullName = fullName;
        this.email = email;
        this.images = images;

        this.password=password;
    }

    public User(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String address) {
        this.email = address;
    }


    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", image='" + images + '\'' +
                '}';
    }
}
