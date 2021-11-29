package com.kiennv.duanphonestore.User.Model;

import java.io.Serializable;

public class Favourite implements Serializable {
    private int id;
    private int idSP;
    private int idUS;
    private String name;
    private int price;
    private String image_SP;
    private String mota;

    public Favourite(int idSP, int idUS, String name, int price, String image_SP, String mota) {
        this.idSP = idSP;
        this.idUS = idUS;
        this.name = name;
        this.price = price;
        this.image_SP = image_SP;
        this.mota = mota;
    }

    public Favourite(int id, int idSP, int idUS, String name, int price, String image_SP, String mota) {
        this.id = id;
        this.idSP = idSP;
        this.idUS = idUS;
        this.name = name;
        this.price = price;
        this.image_SP = image_SP;
        this.mota = mota;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSP() {
        return idSP;
    }

    public void setIdSP(int idSP) {
        this.idSP = idSP;
    }

    public int getIdUS() {
        return idUS;
    }

    public void setIdUS(int idUS) {
        this.idUS = idUS;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage_SP() {
        return image_SP;
    }

    public void setImage_SP(String image_SP) {
        this.image_SP = image_SP;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }
}
