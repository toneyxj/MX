package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 赠品VO
 *
 * @author duxiaojie
 * @date 2018-08-06 11:06
 */
public class MProductGiftVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cart_gift_item_id;
    private Long gift_id;
    private String gift_name;
    private String gift_image;
    private MProductPriceVO price;
    private Integer stock;
    private Integer is_selected;
    private Integer allow_select;
    private Integer spu;
    private List<MProductSaleAttributeVO> sale_attributes;
    private Integer is_ebook;
    private String buy_total_money_can_select;

    public Long getCart_gift_item_id() {
        return cart_gift_item_id == null ? 0 : cart_gift_item_id;
    }

    public void setCart_gift_item_id(Long cart_gift_item_id) {
        this.cart_gift_item_id = cart_gift_item_id;
    }

    public Long getGift_id() {
        return gift_id == null ? 0 : gift_id;
    }

    public void setGift_id(Long gift_id) {
        this.gift_id = gift_id;
    }

    public String getGift_name() {
        return gift_name;
    }

    public void setGift_name(String gift_name) {
        this.gift_name = gift_name;
    }

    public String getGift_image() {
        return gift_image;
    }

    public void setGift_image(String gift_image) {
        this.gift_image = gift_image;
    }

    public MProductPriceVO getPrice() {
        return price;
    }

    public void setPrice(MProductPriceVO price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock == null ? 0 : stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getIs_selected() {
        return is_selected == null ? 0 : is_selected;
    }

    public void setIs_selected(Integer is_selected) {
        this.is_selected = is_selected;
    }

    public Integer getAllow_select() {
        return allow_select == null ? 0 : allow_select;
    }

    public void setAllow_select(Integer allow_select) {
        this.allow_select = allow_select;
    }

    public Integer getSpu() {
        return spu == null ? 0 : spu;
    }

    public void setSpu(Integer spu) {
        this.spu = spu;
    }

    public List<MProductSaleAttributeVO> getSale_attributes() {
        return sale_attributes;
    }

    public void setSale_attributes(List<MProductSaleAttributeVO> sale_attributes) {
        this.sale_attributes = sale_attributes;
    }

    public Integer getIs_ebook() {
        return is_ebook == null ? 0 : is_ebook;
    }

    public void setIs_ebook(Integer is_ebook) {
        this.is_ebook = is_ebook;
    }

    public String getBuy_total_money_can_select() {
        return buy_total_money_can_select;
    }

    public void setBuy_total_money_can_select(String buy_total_money_can_select) {
        this.buy_total_money_can_select = buy_total_money_can_select;
    }
}
