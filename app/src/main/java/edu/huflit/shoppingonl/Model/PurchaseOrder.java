package edu.huflit.shoppingonl.Model;

public class PurchaseOrder {
    String userName, orderID, datetime, fullName, phoneNumber, address, detailID;
    Double totalPrice;

    public PurchaseOrder() {
    }

    public PurchaseOrder(String userName, String orderID, String datetime, String fullName, String phoneNumber, String address, String detailID, Double totalPrice) {
        this.userName = userName;
        this.orderID = orderID;
        this.datetime = datetime;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.detailID = detailID;
        this.totalPrice = totalPrice;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailID() {
        return detailID;
    }

    public void setDetailID(String detailID) {
        this.detailID = detailID;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
