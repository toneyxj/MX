package com.moxi.bookstore.http.deal;

import com.moxi.bookstore.http.HttpService;
import com.moxi.bookstore.http.entity.BaseDeal;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by xiajun on 2019/6/14.
 */

public class AddShopingCart extends BaseDeal {
    Subscriber msubscriber;
    HashMap<String,Object> map;

    public AddShopingCart(Subscriber msubscriber, HashMap<String, Object> map) {
        this.msubscriber = msubscriber;
        this.map = map;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.addShoppingCart(map);
    }

    @Override
    public Subscriber getSubscirber() {
        return msubscriber;
    }
}
