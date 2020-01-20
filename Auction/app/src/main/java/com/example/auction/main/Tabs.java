package com.example.auction.main;

public enum Tabs {

    PRODUCT_FRAGMENT_ALL(0, "전체 상품"),
    PRODUCT_FRAGMENT_MY(1, "내가 올린 상품"),
    BID_MY(2, "나의 입찰 내역"),
    PRODUCT_FRAGMENT_REGISTER(3, "상품 등록");

    private final int num;
    private final String text;

    Tabs(int num, String text) {
        this.num = num;
        this.text = text;
    }

    public int getNum() {
        return num;
    }

    public String getText() {
        return text;
    }
}
