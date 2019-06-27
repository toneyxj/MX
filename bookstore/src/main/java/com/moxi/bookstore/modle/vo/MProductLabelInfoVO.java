package com.moxi.bookstore.modle.vo;

import java.io.Serializable;

/**
 * 商品标签信息VO
 *
 * @author duxiaojie
 * @date 2018-10-16 19:46
 */
public class MProductLabelInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String atmosphere_image_180x180;
    private String atmosphere_image_56x56;

    public String getAtmosphere_image_180x180() {
        return atmosphere_image_180x180;
    }

    public void setAtmosphere_image_180x180(String atmosphere_image_180x180) {
        this.atmosphere_image_180x180 = atmosphere_image_180x180;
    }

    public String getAtmosphere_image_56x56() {
        return atmosphere_image_56x56;
    }

    public void setAtmosphere_image_56x56(String atmosphere_image_56x56) {
        this.atmosphere_image_56x56 = atmosphere_image_56x56;
    }
}
