package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 商品礼券信息
 *
 * @author duxiaojie
 * @date 2018-08-06 16:03
 */
public class MProductCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer shop_id;
    private Long product_id;
    private Long item_id;
    private String product_name;
    private Integer item_count;
    private boolean is_checked;
    private String group_info; // 如果有时则是否跨店铺促销
    private BigDecimal price;
    private Long main_product_id;
    private Integer num_images;
    private Integer img_version;
    private String product_image;

    public Integer getShop_id() {
        return shop_id == null ? 0 : shop_id;
    }

    public void setShop_id(Integer shop_id) {
        this.shop_id = shop_id;
    }

    public Long getProduct_id() {
        return product_id == null ? 0 : product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Long getItem_id() {
        return item_id == null ? 0 : item_id;
    }

    public void setItem_id(Long item_id) {
        this.item_id = item_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Integer getItem_count() {
        return item_count == null ? 0 : item_count;
    }

    public void setItem_count(Integer item_count) {
        this.item_count = item_count;
    }

    public boolean isIs_checked() {
        return is_checked;
    }

    public void setIs_checked(boolean is_checked) {
        this.is_checked = is_checked;
    }

    public String getGroup_info() {
        return group_info;
    }

    public void setGroup_info(String group_info) {
        this.group_info = group_info;
    }

    public BigDecimal getPrice() {
        return price == null ? BigDecimal.ZERO : price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Long getMain_product_id() {
        return main_product_id == null ? 0 : main_product_id;
    }

    public void setMain_product_id(Long main_product_id) {
        this.main_product_id = main_product_id;
    }

    public Integer getNum_images() {
        return num_images == null ? 0 : num_images;
    }

    public void setNum_images(Integer num_images) {
        this.num_images = num_images;
    }

    public Integer getImg_version() {
        return img_version == null ? 0 : img_version;
    }

    public void setImg_version(Integer img_version) {
        this.img_version = img_version;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
