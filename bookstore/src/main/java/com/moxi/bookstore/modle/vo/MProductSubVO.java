package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 虚拟捆绑-子品VO
 *
 * @author duxiaojie
 * @date 2018-08-06 11:45
 */
public class MProductSubVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long product_id;
    private String product_name;
    private Integer item_count;
    private BigDecimal sale_price;
    private String product_image;

    public Long getProduct_id() {
        return product_id == null ? 0 : product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
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

    public BigDecimal getSale_price() {
        return sale_price == null ? BigDecimal.ZERO : sale_price;
    }

    public void setSale_price(BigDecimal sale_price) {
        this.sale_price = sale_price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
