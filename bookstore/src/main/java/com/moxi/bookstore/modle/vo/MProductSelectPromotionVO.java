package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 一品多促 选择促销
 *
 * @author duxiaojie
 * @date 2018-12-20 15:12
 */
public class MProductSelectPromotionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String checked_promotion_tag;
    private String checked_promotion_desc;
    private List<SelectPromotionVO> promotion_list;

    public String getChecked_promotion_tag() {
        return checked_promotion_tag;
    }

    public void setChecked_promotion_tag(String checked_promotion_tag) {
        this.checked_promotion_tag = checked_promotion_tag;
    }

    public String getChecked_promotion_desc() {
        return checked_promotion_desc;
    }

    public void setChecked_promotion_desc(String checked_promotion_desc) {
        this.checked_promotion_desc = checked_promotion_desc;
    }

    public List<SelectPromotionVO> getPromotion_list() {
        return promotion_list;
    }

    public void setPromotion_list(List<SelectPromotionVO> promotion_list) {
        this.promotion_list = promotion_list;
    }

    public static class SelectPromotionVO{

    private Long promotion_id;
    private Integer promotion_type;
    private String promotion_name;
    private Integer is_checked;
    private String promotion_tag;
    private String promotion_desc;

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

    public String getPromotion_name() {
        return promotion_name;
    }

    public void setPromotion_name(String promotion_name) {
        this.promotion_name = promotion_name;
    }

    public Integer getIs_checked() {
        return is_checked == null ? 0 : is_checked;
    }

    public void setIs_checked(Integer is_checked) {
        this.is_checked = is_checked;
    }

    public String getPromotion_tag() {
        return promotion_tag;
    }

    public void setPromotion_tag(String promotion_tag) {
        this.promotion_tag = promotion_tag;
    }

    public String getPromotion_desc() {
        return promotion_desc;
    }

    public void setPromotion_desc(String promotion_desc) {
        this.promotion_desc = promotion_desc;
    }

    }
}
