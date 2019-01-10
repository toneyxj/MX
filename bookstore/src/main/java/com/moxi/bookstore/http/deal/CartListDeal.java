package com.moxi.bookstore.http.deal;

import com.moxi.bookstore.http.HttpService;
import com.moxi.bookstore.http.entity.BaseDeal;

import java.util.HashMap;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/10/14.
 */
public class CartListDeal extends BaseDeal {
    Subscriber msubscriber;
    HashMap<String,Object> map;

    public CartListDeal(Subscriber msubscriber, HashMap<String, Object> map) {
        this.msubscriber = msubscriber;
        this.map = map;
    }

    @Override
    public Observable getObservable(HttpService methods) {
        return methods.listShoppingCart(map);
    }

    @Override
    public Subscriber getSubscirber() {
        return msubscriber;
    }
}
