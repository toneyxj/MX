package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 购物车列表接口返回基类
 * 按照mapi接口json定制
 *
 * @author duxiaojie
 * @date 2018-08-03 17:51
 */
public class MCartListResponseVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * code码
     * 默认code值0，无错误
     */
    private Integer errorCode;

    /**
     * 消息描述
     * 默认msg值success
     */
    private String errorMsg;

    /**
     * 价格对象
     */
    private MPriceVO price;

    /**
     * 店铺类表
     */
    private List<MShopVO> shop_vos;

    /**
     * spu限购品列表
     * <p>
     * 完全参照mapi结构定义，后期可以改善
     * key：main_product_id
     * value：List<MProductVO>
     */
    private List<Map<Long, List<MProductVO>>> spu_products;

    /**
     * 失效品列表
     */
    private List<MProductVO> invalid_products;

    /**
     * 商品PID字符串
     * 逗号分隔
     */
    private String m_pids;

    /**
     * 商品总数
     */
    private Integer product_total;
    private Integer product_selected_total;

    /**
     * 电子书总数
     */
    private Integer ebook_total;

    /**
     * 增加用户属性字段，如用户是否是vip，VIP的等级等等
     * viptype = 0-普通用户，1-黄金卡用户，2-钻石卡用户，3-白银卡用户，10-企业用户
     */
    private Integer user_viptype;

    /**
     * 当前时间
     */
    private Long current_time;

    /**
     * 尾品汇、最值当品进入购物车倒计时时间
     */
    private Long time_pass;

    /**
     * 降价商品总数
     */
    private Integer reduce_price_product_num;

    /**
     * 降价促销提示语
     */
    private String reduce_price_tips;

    /**
     * 增加可变页头页尾信息
     */
    private Integer show_head_tail;

    private Map<String, String> page_head;
    private Map<String, String> page_tail;

    /**
     * 运费信息vo
     */
    private MShipingFreightVO mShipingFreightVO;

    /**
     * 纸赠电
     */
    private MPrintBookDonateVO print_book_donate;

    /**
     * 降价商品
     */
    private MProductReduceVO reduce_product;


    /**
     * 礼券未领取的提示语
     */
    private String coupon_not_take_tips;



    // 处于选中状态的免邮自营品
    private List<Long> self_free_shipping_fee_products;


    public Integer getErrorCode() {
        return errorCode == null ? 0 : errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public MPriceVO getPrice() {
        return price;
    }

    public void setPrice(MPriceVO price) {
        this.price = price;
    }

    public List<MShopVO> getShop_vos() {
        return shop_vos;
    }

    public void setShop_vos(List<MShopVO> shop_vos) {
        this.shop_vos = shop_vos;
    }

    public List<Map<Long, List<MProductVO>>> getSpu_products() {
        return spu_products;
    }

    public void setSpu_products(List<Map<Long, List<MProductVO>>> spu_products) {
        this.spu_products = spu_products;
    }

    public List<MProductVO> getInvalid_products() {
        return invalid_products;
    }

    public void setInvalid_products(List<MProductVO> invalid_products) {
        this.invalid_products = invalid_products;
    }

    public String getM_pids() {
        return m_pids;
    }

    public void setM_pids(String m_pids) {
        this.m_pids = m_pids;
    }

    public Integer getProduct_total() {
        return product_total == null ? 0 : product_total;
    }

    public void setProduct_total(Integer product_total) {
        this.product_total = product_total;
    }

    public Integer getEbook_total() {
        return ebook_total == null ? 0 : ebook_total;
    }

    public void setEbook_total(Integer ebook_total) {
        this.ebook_total = ebook_total;
    }

    public Integer getUser_viptype() {
        return user_viptype == null ? 0 : user_viptype;
    }

    public void setUser_viptype(Integer user_viptype) {
        this.user_viptype = user_viptype;
    }

    public Long getCurrent_time() {
        return current_time == null ? 0 : current_time;
    }

    public void setCurrent_time(Long current_time) {
        this.current_time = current_time;
    }

    public Long getTime_pass() {
        return time_pass == null ? 0 : time_pass;
    }

    public void setTime_pass(Long time_pass) {
        this.time_pass = time_pass;
    }

    public Integer getReduce_price_product_num() {
        return reduce_price_product_num == null ? 0 : reduce_price_product_num;
    }

    public void setReduce_price_product_num(Integer reduce_price_product_num) {
        this.reduce_price_product_num = reduce_price_product_num;
    }

    public String getReduce_price_tips() {
        return reduce_price_tips;
    }

    public void setReduce_price_tips(String reduce_price_tips) {
        this.reduce_price_tips = reduce_price_tips;
    }

    public Integer getShow_head_tail() {
        return show_head_tail == null ? 0 : show_head_tail;
    }

    public void setShow_head_tail(Integer show_head_tail) {
        this.show_head_tail = show_head_tail;
    }

    public Map<String, String> getPage_head() {
        return page_head;
    }

    public void setPage_head(Map<String, String> page_head) {
        this.page_head = page_head;
    }

    public Map<String, String> getPage_tail() {
        return page_tail;
    }

    public void setPage_tail(Map<String, String> page_tail) {
        this.page_tail = page_tail;
    }

    public MShipingFreightVO getmShipingFreightVO() {
        return mShipingFreightVO;
    }

    public void setmShipingFreightVO(MShipingFreightVO mShipingFreightVO) {
        this.mShipingFreightVO = mShipingFreightVO;
    }

    public MPrintBookDonateVO getPrint_book_donate() {
        return print_book_donate;
    }

    public void setPrint_book_donate(MPrintBookDonateVO print_book_donate) {
        this.print_book_donate = print_book_donate;
    }

    public MProductReduceVO getReduce_product() {
        return reduce_product;
    }

    public void setReduce_product(MProductReduceVO reduce_product) {
        this.reduce_product = reduce_product;
    }

    public Integer getProduct_selected_total() {
        return product_selected_total == null ? 0 : product_selected_total;
    }

    public void setProduct_selected_total(Integer product_selected_total) {
        this.product_selected_total = product_selected_total;
    }

    public String getCoupon_not_take_tips() {
        return coupon_not_take_tips;
    }

    public void setCoupon_not_take_tips(String coupon_not_take_tips) {
        this.coupon_not_take_tips = coupon_not_take_tips;
    }

    public List<Long> getSelf_free_shipping_fee_products() {
        return self_free_shipping_fee_products;
    }

    public void setSelf_free_shipping_fee_products(List<Long> self_free_shipping_fee_products) {
        this.self_free_shipping_fee_products = self_free_shipping_fee_products;
    }
}
