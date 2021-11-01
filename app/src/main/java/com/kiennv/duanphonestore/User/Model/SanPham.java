package com.kiennv.duanphonestore.User.Model;

import java.io.Serializable;

public class SanPham implements Serializable {
    private String name;
    private int price;
    private int id;
    private String image_SP;
    private String category_id;
    private String Motasanpham;

    public SanPham(){

    }

    public SanPham(int id, String name, int price, String image_SP,String category_id, String motasanpham) {
        this.name = name;
        this.id = id;
        this.price = price;
        this.category_id = category_id;
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
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
