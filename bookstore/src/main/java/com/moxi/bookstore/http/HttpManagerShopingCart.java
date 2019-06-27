package com.moxi.bookstore.http;

import com.moxi.bookstore.http.entity.BaseDeal;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by xiajun on 2019/6/14.
 */
public class HttpManagerShopingCart {

    public static final String BASE_URL = "http://api.dangdang.com";
    private final static int CONNECT_TIMEOUT =30;
    private final static int READ_TIMEOUT=30;
    private final static int WRITE_TIMEOUT=30;
    private HttpService httpService;
    private volatile static HttpManagerShopingCart INSTANCE;

    //构造方法私有
    private HttpManagerShopingCart() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT,TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS);
        Retrofit retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();
        httpService = retrofit.create(HttpService.class);
    }

    //获取单例
    public static HttpManagerShopingCart getInstance() {
        if (INSTANCE == null) {
            synchronized (HttpManagerShopingCart.class) {
                if (INSTANCE == null) {
                    INSTANCE = new HttpManagerShopingCart();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 处理http请求
     *
     * @param basePar 封装的请求数据
     */
    public void doHttpDeal(BaseDeal basePar) {
        Observable observable = basePar.getObservable(httpService)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(basePar);
        observable.subscribe(basePar.getSubscirber());
    }

}
