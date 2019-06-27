package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品价格VO
 *
 * @author duxiaojie
 * @date 2018-08-03 20:55
 */
public class MProductPriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private BigDecimal original_price;
    private BigDecimal dangdang_price;
    private BigDecimal promotion_price;
    private BigDecimal mobile_exclusive_price;
    private BigDecimal vip_price;

    public BigDecimal getOriginal_price() {
        return original_price == null ? BigDecimal.ZERO : original_price;
    }

    public void setOriginal_price(BigDecimal original_price) {
        this.original_price = original_price;
    }

    public BigDecimal getDangdang_price() {
        return dangdang_price == null ? BigDecimal.ZERO : dangdang_price;
    }

    public void setDangdang_price(BigDecimal dangdang_price) {
        this.dangdang_price = dangdang_price;
    }

    public BigDecimal getPromotion_price() {
        return promotion_price == null ? BigDecimal.ZERO : promotion_price;
    }

    public void setPromotion_price(BigDecimal promotion_price) {
        this.promotion_price = promotion_price;
    }

    public BigDecimal getMobile_exclusive_price() {
        return mobile_exclusive_price == null ? BigDecimal.ZERO : mobile_exclusive_price;
    }

    public void setMobile_exclusive_price(BigDecimal mobile_exclusive_price) {
        this.mobile_exclusive_price = mobile_exclusive_price;
    }

    public BigDecimal getVip_price() {
        return vip_price == null ? BigDecimal.ZERO : vip_price;
    }

    public void setVip_price(BigDecimal vip_price) {
        this.vip_price = vip_price;
    }
}
