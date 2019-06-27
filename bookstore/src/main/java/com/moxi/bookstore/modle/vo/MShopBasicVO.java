package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 店铺基本信息VO
 *
 * @author duxiaojie
 * @date 2018-08-06 15:40
 */
public class MShopBasicVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MShopGuaranteeVO> shop_guarantees;
    private String shop_name;
    private Integer shop_id;
    private Integer shop_type;
    private String shop_url;

    public List<MShopGuaranteeVO> getShop_guarantees() {
        return shop_guarantees;
    }

    public void setShop_guarantees(List<MShopGuaranteeVO> shop_guarantees) {
        this.shop_guarantees = shop_guarantees;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public Integer getShop_id() {
        return shop_id == null ? 0 : shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public Integer getShop_type() {
        return shop_type == null ? 0 : shop_type;
    }

    public void setShop_type(Integer shop_type) {
        this.shop_type = shop_type;
    }

    public String getShop_url() {
        return shop_url;
    }

    public void setShop_url(String shop_url) {
        this.shop_url = shop_url;
    }
}
