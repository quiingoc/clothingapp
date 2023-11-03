package edu.huflit.shoppingonl.Model;

public class Product {
    String proID, proName,  proDes, proCate, proImage;
    int price, proQuan;

    public Product() {
    }

    public Product(String proID, String proName, String proDes, int price, int proQuan, String proCate, String proImage) {
        this.proID = proID;
        this.proName = proName;
        this.proDes = proDes;
        this.price = price;
        this.proQuan = proQuan;
        this.proCate = proCate;
        this.proImage = proImage;
    }

    public String getProID() {
        return proID;
    }

    public void setProID(String proID) {
        this.proID = proID;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProDes() {
        return proDes;
    }

    public void setProDes(String proDes) {
        this.proDes = proDes;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getProQuan() {
        return proQuan;
    }

    public void setProQuan(int proQuan) {
        this.proQuan = proQuan;
    }

    public String getProCate() {
        return proCate;
    }

    public void setProCate(String proCate) {
        this.proCate = proCate;
    }

    public String getProImage() {
        return proImage;
    }

    public void setProImage(String proImage) {
        this.proImage = proImage;
    }
}
