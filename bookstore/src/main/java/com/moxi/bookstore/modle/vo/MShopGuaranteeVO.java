package com.moxi.bookstore.modle.vo;

import java.io.Serializable;

/**
 * 店铺商家服务保障
 *
 * @author duxiaojie
 * @date 2018-08-06 15:40
 */
public class MShopGuaranteeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;

    public Integer getId() {
        return id == null ? 0 : id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
