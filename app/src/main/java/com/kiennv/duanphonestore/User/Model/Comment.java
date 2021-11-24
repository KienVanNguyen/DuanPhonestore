package com.kiennv.duanphonestore.User.Model;

public class Comment {
    private int id;
    private String nameUS;
    private String imageUS;
    private String status;
    private int idUS;
    private int idSP;

    public Comment(int id, String nameUS, String imageUS, String status, int idUS, int idSP) {
        this.id = id;
        this.nameUS = nameUS;
        this.imageUS = imageUS;
        this.status = status;
        this.idUS = idUS;
        this.idSP = idSP;
    }
    public Comment(String nameUS, String imageUS, String status, int idUS, int idSP) {
        this.nameUS = nameUS;
        this.imageUS = imageUS;
        this.status = status;
        this.idUS = idUS;
        this.idSP = idSP;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameUS() {
        return nameUS;
    }

    public void setNameUS(String nameUS) {
        this.nameUS = nameUS;
    }

    public String getImageUS() {
        return imageUS;
    }

    public void setImageUS(String imageUS) {
        this.imageUS = imageUS;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getIdUS() {
        return idUS;
    }

    public void setIdUS(int idUS) {
        this.idUS = idUS;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }
}