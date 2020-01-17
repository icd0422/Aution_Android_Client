package com.example.auction.product.item;

import java.sql.Timestamp;

public class Product {
    private int userId;
    private String userName;
    private int id;
    private String name;
    private String description;
    private String category;
    private int startPrice;
    private int immediatePurchasePrice;
    private int status;
    private String image;
    private int term;
    private Timestamp endLine;
    private Timestamp endTime;

    private String userEmail = null;

    private int currentPrice = 0;

    @Override
    public String toString() {
        return "Product{" +
                "userId=" + userId +
                ", userName='" + userName + '\'' +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", startPrice=" + startPrice +
                ", immediatePurchasePrice=" + immediatePurchasePrice +
                ", status=" + status +
                ", image='" + image + '\'' +
                ", term=" + term +
                ", endLine=" + endLine +
                ", endTime=" + endTime +
                ", userEmail='" + userEmail + '\'' +
                ", currentPrice=" + currentPrice +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public int getImmediatePurchasePrice() {
        return immediatePurchasePrice;
    }

    public int getStatus() {
        return status;
    }

    public String getImage() {
        return image;
    }

    public int getTerm() {
        return term;
    }

    public Timestamp getEndLine() {
        return endLine;
    }

    public Timestamp getEndTime() {
        return endTime;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }


    public int getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }
}
