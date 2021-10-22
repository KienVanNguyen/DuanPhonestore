package com.kiennv.duanphonestore.User.Model;

public class Card {
    private int idGH;
    private String NameSPGH;
    private  long PriceSP;
    private String ImageSPGH;
    private int quantitySPGH;

    public Card(int idGH, String nameSPGH, long priceSP, String imageSPGH, int quantitySPGH) {
        this.idGH = idGH;
        NameSPGH = nameSPGH;
        PriceSP = priceSP;
        ImageSPGH = imageSPGH;
        this.quantitySPGH = quantitySPGH;
    }

    public int getIdGH() {
        return idGH;
    }

    public void setIdGH(int idGH) {
        this.idGH = idGH;
    }

    public String getNameSPGH() {
        return NameSPGH;
    }

    public void setNameSPGH(String nameSPGH) {
        NameSPGH = nameSPGH;
    }

    public long getPriceSP() {
        return PriceSP;
    }

    public void setPriceSP(long priceSP) {
        PriceSP = priceSP;
    }

    public String getImageSPGH() {
        return ImageSPGH;
    }

    public void setImageSPGH(String imageSPGH) {
        ImageSPGH = imageSPGH;
    }

    public int getQuantitySPGH() {
        return quantitySPGH;
    }

    public void setQuantitySPGH(int quantitySPGH) {
        this.quantitySPGH = quantitySPGH;
    }
}
