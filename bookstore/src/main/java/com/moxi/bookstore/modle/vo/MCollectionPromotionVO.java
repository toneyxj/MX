package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 促销品集合促销对象
 *
 * @author duxiaojie
 * @date 2018-08-06 15:17
 */
public class MCollectionPromotionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long promotion_id;
    private Integer promotion_type;
    private String promotion_rule;
    private String promotion_tag;
    private String promotion_name;
    private String promotion_title;
    private String promotion_tips;
    private String promotion_new_tips;
    private BigDecimal collection_price;
    private Integer promotion_gift_sign;
    private BigDecimal promotion_arrive_price;
    private List<MProductGiftVO> gifts;
    private String promotion_desc;
    private Integer meet_buy_total_num;
    private BigDecimal favor_price;
    private BigDecimal buy_again;
    private BigDecimal next_favore_price;
    private Integer max_limit_buy;
    private Integer buy_total_num;
    private BigDecimal special_price;
    private BigDecimal next_favor;
    private BigDecimal next_dis;
    private BigDecimal dis_price;

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

    public String getPromotion_new_tips() {
        return promotion_new_tips;
    }

    public void setPromotion_new_tips(String promotion_new_tips) {
        this.promotion_new_tips = promotion_new_tips;
    }

    public BigDecimal getCollection_price() {
        return collection_price == null ? BigDecimal.ZERO : collection_price;
    }

    public void setCollection_price(BigDecimal collection_price) {
        this.collection_price = collection_price;
    }

    public Integer getPromotion_gift_sign() {
        return promotion_gift_sign == null ? 0 : promotion_gift_sign;
    }

    public void setPromotion_gift_sign(Integer promotion_gift_sign) {
        this.promotion_gift_sign = promotion_gift_sign;
    }

    public BigDecimal getPromotion_arrive_price() {
        return promotion_arrive_price == null ? BigDecimal.ZERO : promotion_arrive_price;
    }

    public void setPromotion_arrive_price(BigDecimal promotion_arrive_price) {
        this.promotion_arrive_price = promotion_arrive_price;
    }

    public List<MProductGiftVO> getGifts() {
        return gifts;
    }

    public void setGifts(List<MProductGiftVO> gifts) {
        this.gifts = gifts;
    }

    public String getPromotion_desc() {
        return promotion_desc;
    }

    public void setPromotion_desc(String promotion_desc) {
        this.promotion_desc = promotion_desc;
    }

    public Integer getMeet_buy_total_num() {
        return meet_buy_total_num == null ? 0 : meet_buy_total_num;
    }

    public void setMeet_buy_total_num(Integer meet_buy_total_num) {
        this.meet_buy_total_num = meet_buy_total_num;
    }

    public BigDecimal getFavor_price() {
        return favor_price == null ? BigDecimal.ZERO : favor_price;
    }

    public void setFavor_price(BigDecimal favor_price) {
        this.favor_price = favor_price;
    }

    public BigDecimal getBuy_again() {
        return buy_again == null ? BigDecimal.ZERO : buy_again;
    }

    public void setBuy_again(BigDecimal buy_again) {
        this.buy_again = buy_again;
    }

    public void setNext_favor(BigDecimal next_favor) {
        this.next_favor = next_favor;
    }

    public BigDecimal getNext_favore_price() {
        return next_favore_price == null ? BigDecimal.ZERO : next_favore_price;
    }

    public void setNext_favore_price(BigDecimal next_favore_price) {
        this.next_favore_price = next_favore_price;
    }

    public Integer getMax_limit_buy() {
        return max_limit_buy == null ? 0 : max_limit_buy;
    }

    public void setMax_limit_buy(Integer max_limit_buy) {
        this.max_limit_buy = max_limit_buy;
    }

    public Integer getBuy_total_num() {
        return buy_total_num == null ? 0 : buy_total_num;
    }

    public void setBuy_total_num(Integer buy_total_num) {
        this.buy_total_num = buy_total_num;
    }

    public BigDecimal getSpecial_price() {
        return special_price == null ? BigDecimal.ZERO : special_price;
    }

    public void setSpecial_price(BigDecimal special_price) {
        this.special_price = special_price;
    }

    public BigDecimal getNext_favor() {
        return next_favor == null ? BigDecimal.ZERO : next_favor;
    }

    public BigDecimal getNext_dis() {
        return next_dis == null ? BigDecimal.ZERO : next_dis;
    }

    public void setNext_dis(BigDecimal next_dis) {
        this.next_dis = next_dis;
    }

    public BigDecimal getDis_price() {
        return dis_price == null ? BigDecimal.ZERO : dis_price;
    }

    public void setDis_price(BigDecimal dis_price) {
        this.dis_price = dis_price;
    }
}
