package com.moxi.bookstore.http.listener;

/**
 * Created by xiajun on 2019/6/17.
 */

public interface BackMassage {

    void onSucess(Object obj);
    void onFail(Exception e);
}
