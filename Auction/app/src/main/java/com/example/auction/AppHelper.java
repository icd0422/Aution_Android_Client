package com.example.auction;

import com.example.auction.product.fragment.ProductFragment;
import com.example.auction.product.fragment.ProductRegisterFragment;

public class AppHelper {
    public static final boolean DEBUG = false ;

    public static final String URL = "http://10.102.61.107:8080";

    public static final int PRODUCT_VIEW_ALL = 1 ;
    public static final int PRODUCT_VIEW_MY = 2 ;

    public static final ProductFragment PRODUCT_FRAGMENT_ALL = new ProductFragment(AppHelper.PRODUCT_VIEW_ALL);
    public static final ProductFragment PRODUCT_FRAGMENT_MY = new ProductFragment(AppHelper.PRODUCT_VIEW_MY);
    public static final ProductRegisterFragment PRODUCT_FRAGMENT_REGISTER = new ProductRegisterFragment();

    public static String TOKEN = null ;

    public static final int GET_IMAGE_REQUEST = 0 ;

}

