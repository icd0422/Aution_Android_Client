package com.example.auction.request;

public enum Url {
    SERVER("http://10.102.61.109:8080"),
    LOGIN(SERVER.getUrl() + "/user/login"),
    PRODUCT_LIST_ALL(SERVER.getUrl() + "/product"),
    PRODUCT_LIST_MY(SERVER.getUrl() + "/user/my/product");

    private final String url;

    Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
