package com.kiennv.duanphonestore.User.Model;

import java.io.Serializable;

public class OrderDetail implements Serializable {
    private int idOrderDetail;
    private int priceOrderDetail;
    private String nameOrderDetail;
    private String image_SPOrderDetail;
    private int quantityOrderDetail;
    private int mahoadonOrderDetail;

    public OrderDetail(int idOrderDetail, int priceOrderDetail, String nameOrderDetail, String image_SPOrderDetail, int quantityOrderDetail, int mahoadonOrderDetail) {
        this.idOrderDetail = idOrderDetail;
        this.priceOrderDetail = priceOrderDetail;
        this.nameOrderDetail = nameOrderDetail;
        this.image_SPOrderDetail = image_SPOrderDetail;
        this.quantityOrderDetail = quantityOrderDetail;
        this.mahoadonOrderDetail = mahoadonOrderDetail;
    }

    public int getIdOrderDetail() {
        return idOrderDetail;
    }

    public void setIdOrderDetail(int idOrderDetail) {
        this.idOrderDetail = idOrderDetail;
    }

    public int getPriceOrderDetail() {
        return priceOrderDetail;
    }

    public void setPriceOrderDetail(int priceOrderDetail) {
        this.priceOrderDetail = priceOrderDetail;
    }

    public String getNameOrderDetail() {
        return nameOrderDetail;
    }

    public void setNameOrderDetail(String nameOrderDetail) {
        this.nameOrderDetail = nameOrderDetail;
    }

    public String getImage_SPOrderDetail() {
        return image_SPOrderDetail;
    }

    public void setImage_SPOrderDetail(String image_SPOrderDetail) {
        this.image_SPOrderDetail = image_SPOrderDetail;
    }

    public int getQuantityOrderDetail() {
        return quantityOrderDetail;
    }

    public void setQuantityOrderDetail(int quantityOrderDetail) {
        this.quantityOrderDetail = quantityOrderDetail;
    }

    public int getMahoadonOrderDetail() {
        return mahoadonOrderDetail;
    }

    public void setMahoadonOrderDetail(int mahoadonOrderDetail) {
        this.mahoadonOrderDetail = mahoadonOrderDetail;
    }
}
