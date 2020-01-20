package com.example.auction.product.fragment;

import androidx.fragment.app.Fragment;

import com.example.auction.main.Tabs;

public enum ProductFragments {

    PRODUCT_FRAGMENT_ALL(new ProductFragment(Tabs.PRODUCT_FRAGMENT_ALL.getNum())),
    PRODUCT_FRAGMENT_MY(new ProductFragment(Tabs.PRODUCT_FRAGMENT_MY.getNum())),
    PRODUCT_FRAGMENT_REGISTER(new ProductRegisterFragment());

    private final Fragment fragment;

    ProductFragments(Fragment fragment) {
        this.fragment = fragment ;
    }

    public Fragment getFragment() {
        return fragment;
    }
}
