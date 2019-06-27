package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 促销单品VO
 *
 * @author duxiaojie
 * @date 2018-08-06 10:55
 */
public class MProductSinglePromotionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long promotion_id;
    private Integer promotion_type;
    private String promotion_rule;
    private String promotion_tag;
    private String promotion_name;
    private List<MProductGiftVO> gifts;
    private String promotion_desc;
    /**
     * 秒杀倒计时
     */
    private Long count_down_time;

    /**
     * N件起购
     */
    private Integer min_limit_buy;
    /**
     * 银铃铛优惠购是否使用银铃铛
     */
    private Boolean is_use_point;

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

    public Long getCount_down_time() {
        return count_down_time == null ? 0 : count_down_time;
    }

    public void setCount_down_time(Long count_down_time) {
        this.count_down_time = count_down_time;
    }

    public Integer getMin_limit_buy() {
        return min_limit_buy == null ? 0 : min_limit_buy;
    }

    public void setMin_limit_buy(Integer min_limit_buy) {
        this.min_limit_buy = min_limit_buy;
    }

    public Boolean getIs_use_point() {
        return is_use_point == null ? false : is_use_point;
    }

    public void setIs_use_point(Boolean is_use_point) {
        this.is_use_point = is_use_point;
    }
}
