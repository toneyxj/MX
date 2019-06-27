package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 店铺促销品VO
 *
 * @author duxiaojie
 * @date 2018-08-06 15:27
 */
public class MShopPromotionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long promotion_id;
    private Integer promotion_type;
    private String promotion_rule;
    private String promotion_tag;
    private String promotion_name;
    private String promotion_title;
    private String promotion_tips;
    private BigDecimal collection_price;
    private String promotion_desc;
    private String shop_msg;
    private Long shop_id;
    private List<MProductGiftVO> gifts;

    public Long getPromotion_id() {
        return promotion_id == null ? 0 : promotion_id;
    }

    public void setPromotion_id(Long promotion_id) {
        this.promotion_id = promotion_id;
    }

    public Integer getPromotion_type() {
        return promotion_type == null ? 0 : promotion_type;
    }

    public void setPromotion_type(Integer promotion_type) {
        this.promotion_type = promotion_type;
    }

    public String getPromotion_rule() {
        return promotion_rule;
    }

    public void setPromotion_rule(String promotion_rule) {
        this.promotion_rule = promotion_rule;
    }

    public String getPromotion_tag() {
        return promotion_tag;
    }

    public void setPromotion_tag(String promotion_tag) {
        this.promotion_tag = promotion_tag;
    }

    public String getPromotion_name() {
        return promotion_name;
    }

    public void setPromotion_name(String promotion_name) {
        this.promotion_name = promotion_name;
    }

    public String getPromotion_title() {
        return promotion_title;
    }

    public void setPromotion_title(String promotion_title) {
        this.promotion_title = promotion_title;
    }

    public String getPromotion_tips() {
        return promotion_tips;
    }

    public void setPromotion_tips(String promotion_tips) {
        this.promotion_tips = promotion_tips;
    }

    public BigDecimal getCollection_price() {
        return collection_price == null ? BigDecimal.ZERO : collection_price;
    }

    public void setCollection_price(BigDecimal collection_price) {
        this.collection_price = collection_price;
    }

    public String getPromotion_desc() {
        return promotion_desc;
    }

    public void setPromotion_desc(String promotion_desc) {
        this.promotion_desc = promotion_desc;
    }

    public String getShop_msg() {
        return shop_msg;
    }

    public void setShop_msg(String shop_msg) {
        this.shop_msg = shop_msg;
    }

    public Long getShop_id() {
        return shop_id == null ? 0 : shop_id;
    }

    public void setShop_id(Long shop_id) {
        this.shop_id = shop_id;
    }

    public List<MProductGiftVO> getGifts() {
        return gifts;
    }

    public void setGifts(List<MProductGiftVO> gifts) {
        this.gifts = gifts;
    }
}
