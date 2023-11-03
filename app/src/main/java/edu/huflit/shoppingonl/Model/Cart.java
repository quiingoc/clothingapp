package edu.huflit.shoppingonl.Model;

public class Cart {
    String userName, proID, proName,  proDes, proCate, proImage;
    int quantity, price, proQuan;

    public Cart() {
    }

    public Cart(String userName, String proID, String proName, String proDes, int price, int proQuan, String proCate, String proImage, int quantity) {
        this.userName = userName;
        this.proID = proID;
        this.proName = proName;
        this.proDes = proDes;
        this.price = price;
        this.proQuan = proQuan;
        this.proCate = proCate;
        this.proImage = proImage;
        this.quantity = quantity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
