package com.kiennv.duanphonestore.User.Model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String name;
    private int price;
    private int id;
    private String image_SP;
    private String Motasanpham;

    public SanPham(){

    }

    public SanPham(int id, String name, int price, String image_SP, String motasanpham) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.image_SP = image_SP;
        this.Motasanpham = motasanpham;
    }

    public String getMotasanpham() {
        return Motasanpham;
    }

    public void setMotasanpham(String motasanpham) {
        Motasanpham = motasanpham;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
