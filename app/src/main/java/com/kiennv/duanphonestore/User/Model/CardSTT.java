package com.kiennv.duanphonestore.User.Model;

import java.io.Serializable;
import java.text.DateFormat;

public class CardSTT implements Serializable {
    private int id;
    private int IdUser;
    private long Price;
    private String Date;
    private String Status;
    private String ReceivedDate;

    public CardSTT(int id, int idUser, long price, String date, String status, String receivedDate) {
        this.id = id;
        IdUser = idUser;
        Price = price;
        Date = date;
        Status = status;
        ReceivedDate = receivedDate;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdUser() {
        return IdUser;
    }

    public void setIdUser(int idUser) {
        IdUser = idUser;
    }

    public long getPrice() {
        return Price;
    }

    public void setPrice(long price) {
        Price = price;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getReceivedDate() {
        return ReceivedDate;
    }

    public void setReceivedDate(String receivedDate) {
        ReceivedDate = receivedDate;
    }
}
