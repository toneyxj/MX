package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

/**
 * 店铺礼券信息VO
 *
 * @author duxiaojie
 * @date 2018-08-06 15:52
 */
public class MShopCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private MCouponVO coupon_model;

    private List<MProductCouponVO> coupon_product_list;

    private Integer checked_count;

    private BigDecimal calculate_price;

    private BigDecimal buyAgain;

    private String corresponding_good_massage;

    private List<String> product_id_set;

    private HashMap<String,Object> main_product_id_map;

    private HashMap<String,Object> product_image_num_map;

    private HashMap<String,Object> product_image_version_map;

    public MCouponVO getCoupon_model() {
        return coupon_model;
    }

    public void setCoupon_model(MCouponVO coupon_model) {
        this.coupon_model = coupon_model;
    }

    public List<MProductCouponVO> getCoupon_product_list() {
        return coupon_product_list;
    }

    public void setCoupon_product_list(List<MProductCouponVO> coupon_product_list) {
        this.coupon_product_list = coupon_product_list;
    }

    public Integer getChecked_count() {
        return checked_count == null ? 0 : checked_count;
    }

    public void setChecked_count(Integer checked_count) {
        this.checked_count = checked_count;
    }

    public BigDecimal getCalculate_price() {
        return calculate_price == null ? BigDecimal.ZERO : calculate_price;
    }

    public void setCalculate_price(BigDecimal calculate_price) {
        this.calculate_price = calculate_price;
    }

    public BigDecimal getBuyAgain() {
        return buyAgain == null ? BigDecimal.ZERO : buyAgain;
    }

    public void setBuyAgain(BigDecimal buyAgain) {
        this.buyAgain = buyAgain;
    }

    public String getCorresponding_good_massage() {
        return corresponding_good_massage;
    }

    public void setCorresponding_good_massage(String corresponding_good_massage) {
        this.corresponding_good_massage = corresponding_good_massage;
    }

    public List<String> getProduct_id_set() {
        return product_id_set;
    }

    public void setProduct_id_set(List<String> product_id_set) {
        this.product_id_set = product_id_set;
    }

    public HashMap<String, Object> getMain_product_id_map() {
        return main_product_id_map;
    }

    public void setMain_product_id_map(HashMap<String, Object> main_product_id_map) {
        this.main_product_id_map = main_product_id_map;
    }

    public HashMap<String, Object> getProduct_image_num_map() {
        return product_image_num_map;
    }

    public void setProduct_image_num_map(HashMap<String, Object> product_image_num_map) {
        this.product_image_num_map = product_image_num_map;
    }

    public HashMap<String, Object> getProduct_image_version_map() {
        return product_image_version_map;
    }

    public void setProduct_image_version_map(HashMap<String, Object> product_image_version_map) {
        this.product_image_version_map = product_image_version_map;
    }
}
