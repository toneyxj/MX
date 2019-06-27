package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺VO
 *
 * @author duxiaojie
 * @date 2018-08-03 17:54
 */
public class MShopVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Integer> shop_ids;
    private String show_type;
    private List<MProductVO> products;
    private List<MCollectionVO> collections;
    private List<MShopPromotionVO> shop_promotions;
    private List<MShopBasicVO> shop_basics;
    private BigDecimal shop_total_money;
    private boolean is_all_check;
    private List<MShopCouponVO> coupon_vo_list;
    private List<MProductVO> shop_stockout_products;
    private boolean all_coupon_not_take = false;
    public List<Integer> getShop_ids() {
        return shop_ids;
    }

    public void setShop_ids(List<Integer> shop_ids) {
        this.shop_ids = shop_ids;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    public List<MProductVO> getProducts() {
        return products;
    }

    public void setProducts(List<MProductVO> products) {
        this.products = products;
    }

    public List<MCollectionVO> getCollections() {
        return collections;
    }

    public void setCollections(List<MCollectionVO> collections) {
        this.collections = collections;
    }

    public List<MShopPromotionVO> getShop_promotions() {
        return shop_promotions;
    }

    public void setShop_promotions(List<MShopPromotionVO> shop_promotions) {
        this.shop_promotions = shop_promotions;
    }

    public List<MShopBasicVO> getShop_basics() {
        return shop_basics;
    }

    public void setShop_basics(List<MShopBasicVO> shop_basics) {
        this.shop_basics = shop_basics;
    }

    public BigDecimal getShop_total_money() {
        return shop_total_money == null ? BigDecimal.ZERO : shop_total_money;
    }

    public void setShop_total_money(BigDecimal shop_total_money) {
        this.shop_total_money = shop_total_money;
    }

    public boolean isIs_all_check() {
        return is_all_check;
    }

    public void setIs_all_check(boolean is_all_check) {
        this.is_all_check = is_all_check;
    }

    public List<MShopCouponVO> getCoupon_vo_list() {
        return coupon_vo_list;
    }

    public void setCoupon_vo_list(List<MShopCouponVO> coupon_vo_list) {
        this.coupon_vo_list = coupon_vo_list;
    }

    public List<MProductVO> getShop_stockout_products() {
        return shop_stockout_products;
    }

    public void setShop_stockout_products(List<MProductVO> shop_stockout_products) {
        this.shop_stockout_products = shop_stockout_products;
    }

    public boolean isAll_coupon_not_take() {
        return all_coupon_not_take;
    }

    public void setAll_coupon_not_take(boolean all_coupon_not_take) {
        this.all_coupon_not_take = all_coupon_not_take;
    }
}
