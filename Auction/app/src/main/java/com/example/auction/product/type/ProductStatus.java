package com.example.auction.product.type;

import android.graphics.Color;

public enum ProductStatus {

    PRODUCT_READY(-1, Color.GREEN, "준비중"),
    PRODUCT_ING(0, Color.BLUE, "경매중"),
    PRODUCT_COMPELETE(1, Color.RED, "완료");
    ;

    private final int state ;
    private final int color;
    private final String desc;

    ProductStatus(int state, int color, String desc) {
        this.state = state ;
        this.color = color;
        this.desc = desc;
    }

    public int getState() {
        return this.state;
    }

    public String getDesc() {
        return this.desc;
    }

    public int getColor() {
        return this.color;
    }
}
