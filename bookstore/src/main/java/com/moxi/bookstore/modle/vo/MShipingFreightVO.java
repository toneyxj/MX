package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 运费计算VO
 *
 * @author duxiaojie
 * @date 2018-08-17 11:05
 */
public class MShipingFreightVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 当当自营免运费 and 凑单免运费
     */
    private String shiping_freight;

    /**
     * 当当自营店铺旁边展示凑单免运费入口时的文案
     */
    private String shop_shiping_freight;

    /**
     * 购物车上方是否有凑单免运费入口
     * 1有，0没有
     */
    private Integer is_show_cart_fight_entrance;

    /**
     * 当当自营店铺旁边是否有凑单免运费入口
     * 1有，0没有
     */
    private Integer is_show_shop_fight_entrance;

    /**
     * 满多少钱可以免运费
     */
    private BigDecimal free_shipping_money;

    /**
     * 还差多少钱可以免运费
     */
    private BigDecimal free_shipping_lack_money;

    /**
     * 运费默认文案
     */
    private String shipping_freight_default_tips;

    public MShipingFreightVO() {
    }

    public MShipingFreightVO(
            String shiping_freight, String shop_shiping_freight, Integer is_show_cart_fight_entrance,
            Integer is_show_shop_fight_entrance, BigDecimal free_shipping_money, BigDecimal free_shipping_lack_money) {
        this.shiping_freight = shiping_freight;
        this.shop_shiping_freight = shop_shiping_freight;
        this.is_show_cart_fight_entrance = is_show_cart_fight_entrance;
        this.is_show_shop_fight_entrance = is_show_shop_fight_entrance;
        this.free_shipping_money = free_shipping_money;
        this.free_shipping_lack_money = free_shipping_lack_money;
    }

    public String getShiping_freight() {
        return shiping_freight;
    }

    public void setShiping_freight(String shiping_freight) {
        this.shiping_freight = shiping_freight;
    }

    public String getShop_shiping_freight() {
        return shop_shiping_freight;
    }

    public void setShop_shiping_freight(String shop_shiping_freight) {
        this.shop_shiping_freight = shop_shiping_freight;
    }

    public Integer getIs_show_cart_fight_entrance() {
        return is_show_cart_fight_entrance == null ? 0 : is_show_cart_fight_entrance;
    }

    public void setIs_show_cart_fight_entrance(Integer is_show_cart_fight_entrance) {
        this.is_show_cart_fight_entrance = is_show_cart_fight_entrance;
    }

    public Integer getIs_show_shop_fight_entrance() {
        return is_show_shop_fight_entrance == null ? 0 : is_show_shop_fight_entrance;
    }

    public void setIs_show_shop_fight_entrance(Integer is_show_shop_fight_entrance) {
        this.is_show_shop_fight_entrance = is_show_shop_fight_entrance;
    }

    public BigDecimal getFree_shipping_money() {
        return free_shipping_money == null ? BigDecimal.ZERO : free_shipping_money;
    }

    public void setFree_shipping_money(BigDecimal free_shipping_money) {
        this.free_shipping_money = free_shipping_money;
    }

    public BigDecimal getFree_shipping_lack_money() {
        return free_shipping_lack_money == null ? BigDecimal.ZERO : free_shipping_lack_money;
    }

    public void setFree_shipping_lack_money(BigDecimal free_shipping_lack_money) {
        this.free_shipping_lack_money = free_shipping_lack_money;
    }

    public String getShipping_freight_default_tips() {
        return shipping_freight_default_tips;
    }

    public void setShipping_freight_default_tips(String shipping_freight_default_tips) {
        this.shipping_freight_default_tips = shipping_freight_default_tips;
    }
}
