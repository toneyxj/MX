package com.moxi.bookstore.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.moxi.bookstore.BookstoreApplication;
import com.moxi.bookstore.R;
import com.moxi.bookstore.adapter.CartAdapter;
import com.moxi.bookstore.base.BookStoreBaseActivity;
import com.moxi.bookstore.bean.Cart;
import com.moxi.bookstore.bean.MakeOrderData;
import com.moxi.bookstore.http.HttpManager;
import com.moxi.bookstore.http.NewCartWorkManager;
import com.moxi.bookstore.http.deal.DoStoreDeal;
import com.moxi.bookstore.http.deal.MakeOrderDeal;
import com.moxi.bookstore.http.listener.BackMassage;
import com.moxi.bookstore.http.listener.HttpOnNextListener;
import com.moxi.bookstore.http.subscribers.ProgressSubscriber;
import com.moxi.bookstore.interfacess.ClickPosition;
import com.moxi.bookstore.interfacess.OnFlingListener;
import com.moxi.bookstore.modle.getEbookOrderFlowV2_Data;
import com.moxi.bookstore.modle.vo.MCartListResponseVO;
import com.moxi.bookstore.modle.vo.MCollectionVO;
import com.moxi.bookstore.modle.vo.MProductVO;
import com.moxi.bookstore.modle.vo.MShopVO;
import com.moxi.bookstore.utils.ToolUtils;
import com.moxi.bookstore.view.HSlidableListView;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class CartActivity extends BookStoreBaseActivity implements OnFlingListener, ClickPosition {

    @Bind(R.id.booklist_lv)
    HSlidableListView bookslv;
    @Bind(R.id.title)
    TextView catetory_title;
    @Bind(R.id.price)
    TextView totalPrice;
    @Bind(R.id.edit_end_tv)
    TextView edit_end_tv;
    @Bind(R.id.checkBox)
    CheckBox checkBox;
    @Bind(R.id.sumbit_tv)
    TextView sumbit_tv;
    CartAdapter booksadapter;
    //    List<Cart.ProductsBean> data, pageData;
    List<MProductVO> data, pageData;
    private MCartListResponseVO cartList;
    String type, group, chanelname;
    String cartId, token, deviceNo;
    int currentPage=1, pageCunt, resultCount;
    String productIds, selectTitles;

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_cart;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Intent intent = getIntent();
        type = intent.getStringExtra("chaneltype");
        APPLog.e("channelType" + type);
        group = intent.getStringExtra("group");
        chanelname = intent.getStringExtra("chaneltitle");
        deviceNo = BookstoreApplication.getDeviceNO();
        pageData = new ArrayList<>();
        initView(null);
    }

    private void initView(List<MProductVO> data) {
        if (data == null) {
            currentPage=1;
            bookslv.setDivider(null);
            booksadapter = new CartAdapter(this, this);
            bookslv.setAdapter(booksadapter);
            bookslv.setOnFlingListener(this);

            findViewById(R.id.addfavor_tv).setVisibility(View.GONE);
            findViewById(R.id.del_tv).setVisibility(View.GONE);
            findViewById(R.id.edit_ll).setVisibility(View.VISIBLE);

            findViewById(R.id.edit_end_tv).setVisibility(View.GONE);
            findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);

        } else if (data != null) {

            resultCount = data.size();
            getpageCunt();
            if (currentPage>pageCunt)
                currentPage=pageCunt;
            pageData.clear();
            int size=(currentPage * 4)>resultCount?resultCount:(currentPage * 4);
            for (int i = (currentPage - 1) * 4; i < size; i++) {
                pageData.add(data.get(i));
            }

            booksadapter.setData(pageData);

            if (isAllCheck())
                checkBox.setChecked(true);

            totalPrice.setText("￥：" + cartList.getPrice().getEbook_total());
            catetory_title.setText("购物车（ " + data.size() + " ）" + "共" + currentPage + "/" + pageCunt + " 页");
        }

    }

    public List<MProductVO> getProducts(MCartListResponseVO cartData) {
        List<MProductVO> list = new ArrayList<>();
        if (cartData != null) {
            for (MShopVO shopVo : cartData.getShop_vos()) {
                for (MCollectionVO cliction : shopVo.getCollections()) {
                    for (MProductVO pvos : cliction.getProducts()) {
                        list.add(pvos);
                    }
                }
            }
        }
        APPLog.e("list",list.size());
        return list;
    }

    private void getSelectPrice() {
        if (getSelectData()) {
            makeOrder(true);
        }
//        Double priceDouble = 0.0;
//        for (Cart.ProductsBean bean : data) {
//            if (bean.isChecked()) {
//                Double price = Double.parseDouble(bean.getPrice());
//                priceDouble += price;
//            }
//        }
//       totalPrice.setText("￥：" + ToolUtils.getIntence().formatPrice(priceDouble/100));
    }

    public void editCart(View v) {
        switch (v.getId()) {
            case R.id.edit_tv:
                findViewById(R.id.addfavor_tv).setVisibility(View.VISIBLE);
                findViewById(R.id.del_tv).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_ll).setVisibility(View.INVISIBLE);
                findViewById(R.id.edit_end_tv).setVisibility(View.VISIBLE);
                edit_end_tv.setText("编辑");
                findViewById(R.id.edit_tv).setVisibility(View.GONE);
                break;
            case R.id.edit_end_tv:
                findViewById(R.id.addfavor_tv).setVisibility(View.GONE);
                findViewById(R.id.del_tv).setVisibility(View.GONE);
                findViewById(R.id.edit_ll).setVisibility(View.VISIBLE);
                findViewById(R.id.edit_end_tv).setVisibility(View.GONE);
                findViewById(R.id.edit_tv).setVisibility(View.VISIBLE);
                break;
        }

    }

    private void getToken() {
        if (ToolUtils.getIntence().hasLogin(this)) {
            if (token == null || !ToolUtils.getIntence().getToken(this).getToken().equals(token)) {
                token = ToolUtils.getIntence().getToken(this).getToken();
                getCartId();
            }
        }

    }

    private void getCartId() {
//        HashMap<String, Object> pramers = new ParamsMap(this);
//        //pramers.put("action", "listShoppingCart");
//        pramers.put("deviceType", "Android");
//        pramers.put("token", token);
//        CartListDeal IdDeal = new CartListDeal(new ProgressSubscriber(CartIdListener, this), pramers);
//        HttpManager manager = HttpManager.getInstance();
//        manager.doHttpDeal(IdDeal);
        getSaleData();
        showDialog("加载中...");
    }

    private HttpOnNextListener CartIdListener = new HttpOnNextListener<Cart>() {
        @Override
        public void onNext(Cart cart) {
            if (isfinish) return;
            hideDialog();
            cartId = cart.getCartId();
            getSaleData();
        }

        @Override
        public void onError() {
            if (isfinish) return;
            hideDialog();
            APPLog.e("getCartId failed");
        }
    };

    public void delFromCart(View v) {
        List<MProductVO> checkBeens = booksadapter.getChickItems();
        StringBuilder productIds = new StringBuilder();
        for (int i = 0; i < checkBeens.size(); i++) {
            if (i > 0) {
                productIds.append(",");
            }
            productIds.append(checkBeens.get(i).getCart_product_item_id());
        }
        if (TextUtils.isEmpty(productIds)) {
            ToastUtil("未选中书籍");
//            totalPrice.setText("￥：0.00");
            return;
        }
        NewCartWorkManager.deleteData(token, productIds.toString(), getList);
//        productIds = productIds.substring(1);
//
//        final HashMap<String, Object> params = new ParamsMap(this);
//        //params.put("action", "deleteShoppingCart");
//        //params.put("cartId", "1609271123042346");
//        APPLog.e("cartId:" + cartId);
//        APPLog.e("productIds:" + productIds);
//        APPLog.e("token:" + token);
//        params.put("cartId", cartId);
//        params.put("productIds", productIds);
//        params.put("token", token);
//        HttpManager manager = HttpManager.getInstance();
//        manager.doHttpDeal(new BaseDeal() {
//            @Override
//            public Observable getObservable(HttpService methods) {
//                return methods.deleteShoppingCart(params);
//            }
//
//            @Override
//            public Subscriber getSubscirber() {
//                return new ProgressSubscriber(
//                        new HttpOnNextListener<DeleteShoppingCart>() {
//                            @Override
//                            public void onError() {
//                                hideDialog();
//                            }
//
//                            @Override
//                            public void onNext(DeleteShoppingCart o) {
//                                hideDialog();
//                                APPLog.e("deletcartlist result:" + o.getResult());
//                                List<Cart.ProductsBean> checkBeens = booksadapter.getChickItems();
//                                List<Cart.ProductsBean> newBeans = new ArrayList<Cart.ProductsBean>();
//
//                                for (Cart.ProductsBean bean : data) {
//                                   /* boolean isDel = false;
//                                    for (Cart.ProductsBean delBean : checkBeens) {
//                                        if (bean.getMediaId() == delBean.getMediaId()) {
//                                            isDel = true;
//                                            break;
//                                        }
//                                    }
//                                    if (!isDel) {
//                                        newBeans.add(bean);
//                                    }*/
//                                    if (!bean.isChecked())
//                                        newBeans.add(bean);
//                                }
//                                data = newBeans;
//                                initView(data);
//                                totalPrice.setText("￥：0.00");
//                                checkBox.setChecked(false);
//                                edit_end_tv.setText("完成");
//                            }
//                        }, CartActivity.this);
//            }
//
//        });
        showDialog("加载中...");
    }

    public void allCheck(View v) {//全选
        if (isfinish || data == null) return;
        CheckBox checkBox = (CheckBox) v;
        boolean ischeck = checkBox.isChecked();
        APPLog.e("取消那算=" + ischeck);
        StringBuilder productIds = new StringBuilder();
        List<MProductVO> checkBeens = new ArrayList<>();
        for (MProductVO mp : data) {
            if ((ischeck && mp.getIs_checked() != 1)||(!ischeck&&mp.getIs_checked() == 1)) {
                checkBeens.add(mp);
            }
        }
        if (checkBeens.size()<=0)return;
        for (int i = 0; i < checkBeens.size(); i++) {
            if (i > 0) {
                productIds.append(",");
            }
            productIds.append(checkBeens.get(i).getCart_product_item_id());
        }
        APPLog.e(productIds.toString());
        if (ischeck) {
            NewCartWorkManager.gouxuan(token, productIds.toString(), getList);
        } else {
            NewCartWorkManager.quxiaoGouxuan(token, productIds.toString(), getList);
        }
        showDialog("");
//        booksadapter.setData(data);
//        currentPage = 1;
//        catetory_title.setText("购物车（ " + data.size() + " ）" + "共" + currentPage + "/" + pageCunt + " 页");
//        getSelectPrice();
    }

    private boolean isAllCheck() {
        for (MProductVO bean : data) {
            if (bean.getIs_checked() != 1) {
                checkBox.setChecked(false);
                return false;
            }
        }
        return true;
    }

    /**
     * 提交订单
     *
     * @param v
     */
    public void sumbit(View v) {
        if (getSelectData()) {
            sumbit_tv.setClickable(false);
            makeOrder(false);
        }
    }

    private boolean getSelectData() {
        List<MProductVO> beens = new ArrayList<>();
        for (MProductVO bean : data) {
            if (bean != null && bean.getIs_checked() == 1)
                beens.add(bean);
        }
        if (null == beens || 0 == beens.size()) {
            ToastUtil("未添加书籍!");
            return false;
        } else {
            StringBuilder sbName = new StringBuilder();
            StringBuilder sb = new StringBuilder();
            StringBuilder arraysb = new StringBuilder();
//            sb.append("\"");
            arraysb.append("[");
            for (int i = 0; i < beens.size(); i++) {
                MProductVO bean = beens.get(i);
                String item = "{\"productId\":\"" + bean.getProduct_id() + "\",\"saleId\":\"" + bean.getProduct_id() + "\",\"cId\":\"\"}";
                arraysb.append(item);
                if (i < beens.size() - 1)
                    arraysb.append(",");
                if (i > 0) {
                    sb.append("," + bean.getProduct_id());
                    sbName.append("+" + bean.getProduct_name());
                } else {
                    sb.append(bean.getProduct_id());
                    sbName.append(bean.getProduct_id());
                }
            }
//            sb.append("\"");
            arraysb.append("]");
            productArray = arraysb.toString();
            productIds = sb.toString();
            selectTitles = sbName.toString();
            return true;
        }
    }

    String productArray;

    @Override
    public void onActivityStarted(Activity activity) {
        if (null != cartId && null != token) {
            // getSaleData();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        getToken();

        getSaleData();
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        setResult(MyActivity.CARTL_RES);
    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    private BackMassage getList = new BackMassage() {
        @Override
        public void onSucess(Object obj) {
            if (isFinishing()) return;
            hideDialog();
            cartList = (MCartListResponseVO) obj;
            data = getProducts(cartList);
            initView(data);
            hideDialog();
        }

        @Override
        public void onFail(Exception e) {
            if (isFinishing()) return;
            hideDialog();
        }
    };

    /**
     * 请求购物车列表
     */
    private void getSaleData() {
        NewCartWorkManager.cartList(token, getList);
//        final HashMap<String, Object> params = new ParamsMap(this);
//        //params.put("action", "listShoppingCart");
//        if (StringUtils.isNull(cartId))return;
//        params.put("cartId", cartId);
//        params.put("token", token);
//        HttpManager manager = HttpManager.getInstance();
//        manager.doHttpDeal(new BaseDeal() {
//            @Override
//            public Observable getObservable(HttpService methods) {
//                return methods.listShoppingCart(params);
//            }
//
//            @Override
//            public Subscriber getSubscirber() {
//                return new ProgressSubscriber(
//                        new HttpOnNextListener<Cart>() {
//                            @Override
//                            public void onError() {
//                                if (isfinish) return;
//                                hideDialog();
//                            }
//
//                            @Override
//                            public void onNext(Cart o) {
//                                if (isfinish) return;
//                                hideDialog();
//                                data = o.getProducts();
//                                initView(data);
//                            }
//                        }, CartActivity.this);
//            }
//
//        });
        showDialog("加载购物车...");
    }

    public void addFavor(View v) {
        if (getSelectData()) {
            if (ToolUtils.getIntence().hasLogin(this)) {
                DoStoreDeal storeDeal = new DoStoreDeal(new ProgressSubscriber(addFavorListener, CartActivity.this),
                        productIds, token);
                HttpManager manager = HttpManager.getInstance();
                manager.doHttpDeal(storeDeal);
                APPLog.e("收藏ids:" + productIds);
                showDialog("加载中...");
            }
        }
    }

    private HttpOnNextListener addFavorListener = new HttpOnNextListener() {
        @Override
        public void onNext(Object o) {
            if (isfinish) return;
            hideDialog();
            ToastUtil("收藏成功");
            edit_end_tv.setText("完成");
        }

        @Override
        public void onError() {
            if (isfinish) return;
            hideDialog();
        }
    };

    public void goBack(View v) {
        finish();
    }

    public void searchBook(View v) {
        startActivity(new Intent(CartActivity.this, SearchBookActivity.class));

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    @Override
    public void onLeftFling() {

        APPLog.e("go next page" + currentPage);

        if (currentPage < pageCunt) {
            currentPage++;
            if (pageData.size() != 0) {
                pageData.clear();
            }
            for (int i = (currentPage - 1) * 4; i < (currentPage * 4 > resultCount ?
                    resultCount : currentPage * 4); i++) {
                APPLog.e("i" + currentPage);
                pageData.add(data.get(i));
            }
            APPLog.e(resultCount + "pagedata.size:" + pageData.size());
            booksadapter.setData(pageData);
            catetory_title.setText("购物车（ " + data.size() + " ）" + "共" + currentPage + "/" + pageCunt + " 页");
        } else
            ToastUtil("最后一页");

    }

    @Override
    public void onRightFling() {
        APPLog.e("go last page" + currentPage);

        if (currentPage > 1) {
            currentPage--;
            if (pageData.size() != 0) {
                pageData.clear();
            }
            for (int i = (currentPage - 1) * 4; i < currentPage * 4; i++) {
                pageData.add(data.get(i));
            }
            booksadapter.setData(pageData);
            catetory_title.setText("购物车（ " + data.size() + " ）" + "共" + currentPage + "/" + pageCunt + " 页");
        } else
            ToastUtil("首页");

    }

    public void getpageCunt() {
        if (0 == data.size()) {
            pageCunt = 1;
        } else {
            int n = data.size() % 4;
            if (n == 0)
                pageCunt = data.size() / 4;
            else
                pageCunt = data.size() / 4 + 1;
            APPLog.e("pagecunt:" + pageCunt);
        }
    }

    @Override
    public void click(int position) {
        APPLog.e("勾选选中");
//        getSelectPrice();
        MProductVO item = (MProductVO) booksadapter.getItem(position);
        if (item.getIs_checked() == 1) {
            NewCartWorkManager.quxiaoGouxuan(token, item.getCart_product_item_id() + "", getList);
        } else {
            NewCartWorkManager.gouxuan(token, item.getCart_product_item_id() + "", getList);
        }
        showDialog("");
    }

    private boolean isMathPrice = false;

    private void makeOrder(boolean isMathPrice) {
        if (ToolUtils.getIntence().showBindingDDUser(this)) {
            this.isMathPrice = isMathPrice;
            MakeOrderDeal makeOrderDeal = new MakeOrderDeal(new ProgressSubscriber(orderListener, this),
                    productIds, token, deviceNo, false);
            HttpManager manager = HttpManager.getInstance();
            manager.doHttpDeal(makeOrderDeal);
            showDialog(isMathPrice ? "" : "创建订单");
        }
    }

    String orderId, total, key, payable;
    private HttpOnNextListener orderListener = new HttpOnNextListener<MakeOrderData>() {
        @Override
        public void onNext(MakeOrderData data) {
            sumbit_tv.setClickable(true);
            hideDialog();
            getEbookOrderFlowV2_Data result = data.getResult().getEbookOrderFlowV2.data;
//            OrderResult result = data.getResult().getSubmitOrder().getData().getResult();
            if (null != result) {
//                orderId = result.getOrder_id();
//                total = result.getTotal();
//                payable = result.getPayable();//铃铛数
                int totalPrice = result.totalPrice;
                int payableprice = result.payable;
                total = String.valueOf(totalPrice / 100.0);
                payable = String.valueOf(payableprice / 100.0);
                key = result.key;
                APPLog.e("makeOrder success: " + orderId);
                if (visibale) {
                    if (isMathPrice) {
                        String money = "总计￥：" + ToolUtils.getIntence().formatPrice(payableprice / 100.0);
                        if (totalPrice > payableprice) {
                            money += "    已优惠￥：" + ToolUtils.getIntence().formatPrice((totalPrice - payableprice) / 100.0);
                        }
                        CartActivity.this.totalPrice.setText(money);
                    } else {
                        goPay();
                        finish();
                    }
                }
            } else
                showToast("结算失败,请重试");
        }

        @Override
        public void onError() {
            sumbit_tv.setClickable(true);
            hideDialog();
        }
    };

    private void goPay() {
//        Intent intent=new Intent(this,PayActivity.class);
//        intent.putExtra("orderId",orderId);
//        intent.putExtra("total",total);
//        intent.putExtra("payable",payable);
//        intent.putExtra("key",key);
//        intent.putExtra("token",token);
//        intent.putExtra("CartId",cartId);
//        intent.putExtra("productIds",productIds);
//        intent.putExtra("bookName",selectTitles);
//        intent.putExtra("productArray",productArray);
//        intent.putExtra("deviceNo",deviceNo);
//        startActivity(intent);
        PayActivity.startPayActivity(this, "", productIds, payable, total, key, token, productArray, deviceNo, selectTitles, cartId, 0);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_PAGE_UP) {//上一页
            onRightFling();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {//下一页
            onLeftFling();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

}
