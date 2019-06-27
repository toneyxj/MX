package com.moxi.bookstore.modle.vo;

import java.io.Serializable;

/**
 * 商品销售基本属性
 *
 * @author duxiaojie
 * @date 2018-08-06 11:26
 */
public class MProductSaleAttributeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 属性名称
     */
    private String attribute_name;
    /**
     * 属性值
     */
    private String attribute_value;
    /**
     * 自定义属性值
     */
    private String self_attribute_value;

    public String getAttribute_name() {
        return attribute_name;
    }

    public void setAttribute_name(String attribute_name) {
        this.attribute_name = attribute_name;
    }

    public String getAttribute_value() {
        return attribute_value;
    }

    public void setAttribute_value(String attribute_value) {
        this.attribute_value = attribute_value;
    }

    public String getSelf_attribute_value() {
        return self_attribute_value;
    }

    public void setSelf_attribute_value(String self_attribute_value) {
        this.self_attribute_value = self_attribute_value;
    }
}
