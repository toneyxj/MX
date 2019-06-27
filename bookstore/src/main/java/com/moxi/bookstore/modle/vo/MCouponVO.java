package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 礼券信息
 *
 * @author duxiaojie
 * @date 2018-08-06 15:44
 */
public class MCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    // 礼券活动编码
    private String activity_serial_num;

    // 礼券呈现平台(0 PC；1 Mobile；2 PCor_mobile)
    private String activity_display_platform;

    // 礼券批次号
    private String coupon_apply_id;

    // 礼券面值
    private String coupon_value;

    // 礼券使用的最小限额
    private String coupon_min_use_value;

    // 每个顾客可领取的次数
    private Integer activity_everyone_rec_num;

    // 是否支持前台验证（0 不支持前台展示（包括领券中心，单品页，购物车页面），1 支持前台展示（包括领券中心，单品页，购物车页面））
    private Integer activity_support_foreground_display;

    // 是否支持页面验证黄牛账号（0 不支持页面验证黄牛账号，1 支持页面验证黄牛账号）
    private Integer activity_support_page_validation;

    // 礼券开始时间
    private String coupon_start_date;

    // 礼券结束时间
    private String coupon_end_date;

    // 礼券排序优先级
    private Integer coupon_ordered_priority;

    // 用户等级(0 普通用户；1 金卡用户；2 钻石用户；3 银卡用户) "0,1,2"
    private String activity_received_crowd;

    // 呈现终端(0 Android；2 i_pad；4 Iphone；5 Touch) "0,2,4,5"
    private String activity_display_terminal;

    // 活动支持平台 (0 自营；1 商家；2 全部可用)
    private Integer activity_support_platform;

    // 礼券使用范围描述
    private String coupon_use_scope_desc;

    // h5礼券URL
    private String coupon_h5_url;

    // app礼券URL
    private String coupon_app_url;

    private String coupon_expiry_date;

    private String coupon_limit_amount_str;

    private String coupon_label;

    private String coupon_desc;

    public String getActivity_serial_num() {
        return activity_serial_num;
    }

    public void setActivity_serial_num(String activity_serial_num) {
        this.activity_serial_num = activity_serial_num;
    }

    public String getActivity_display_platform() {
        return activity_display_platform;
    }

    public void setActivity_display_platform(String activity_display_platform) {
        this.activity_display_platform = activity_display_platform;
    }

    public String getCoupon_apply_id() {
        return coupon_apply_id;
    }

    public void setCoupon_apply_id(String coupon_apply_id) {
        this.coupon_apply_id = coupon_apply_id;
    }

    public Integer getActivity_everyone_rec_num() {
        return activity_everyone_rec_num == null ? 0 : activity_everyone_rec_num;
    }

    public void setActivity_everyone_rec_num(Integer activity_everyone_rec_num) {
        this.activity_everyone_rec_num = activity_everyone_rec_num;
    }

    public Integer getActivity_support_foreground_display() {
        return activity_support_foreground_display == null ? 0 : activity_support_foreground_display;
    }

    public void setActivity_support_foreground_display(Integer activity_support_foreground_display) {
        this.activity_support_foreground_display = activity_support_foreground_display;
    }

    public Integer getActivity_support_page_validation() {
        return activity_support_page_validation == null ? 0 : activity_support_page_validation;
    }

    public void setActivity_support_page_validation(Integer activity_support_page_validation) {
        this.activity_support_page_validation = activity_support_page_validation;
    }

    public String getCoupon_start_date() {
        return coupon_start_date;
    }

    public void setCoupon_start_date(String coupon_start_date) {
        this.coupon_start_date = coupon_start_date;
    }

    public String getCoupon_end_date() {
        return coupon_end_date;
    }

    public void setCoupon_end_date(String coupon_end_date) {
        this.coupon_end_date = coupon_end_date;
    }

    public Integer getCoupon_ordered_priority() {
        return coupon_ordered_priority == null ? 0 : coupon_ordered_priority;
    }

    public void setCoupon_ordered_priority(Integer coupon_ordered_priority) {
        this.coupon_ordered_priority = coupon_ordered_priority;
    }

    public String getActivity_received_crowd() {
        return activity_received_crowd;
    }

    public void setActivity_received_crowd(String activity_received_crowd) {
        this.activity_received_crowd = activity_received_crowd;
    }

    public String getActivity_display_terminal() {
        return activity_display_terminal;
    }

    public void setActivity_display_terminal(String activity_display_terminal) {
        this.activity_display_terminal = activity_display_terminal;
    }

    public Integer getActivity_support_platform() {
        return activity_support_platform == null ? 0 : activity_support_platform;
    }

    public void setActivity_support_platform(Integer activity_support_platform) {
        this.activity_support_platform = activity_support_platform;
    }

    public String getCoupon_use_scope_desc() {
        return coupon_use_scope_desc;
    }

    public void setCoupon_use_scope_desc(String coupon_use_scope_desc) {
        this.coupon_use_scope_desc = coupon_use_scope_desc;
    }

    public String getCoupon_h5_url() {
        return coupon_h5_url;
    }

    public void setCoupon_h5_url(String coupon_h5_url) {
        this.coupon_h5_url = coupon_h5_url;
    }

    public String getCoupon_app_url() {
        return coupon_app_url;
    }

    public void setCoupon_app_url(String coupon_app_url) {
        this.coupon_app_url = coupon_app_url;
    }

    public String getCoupon_expiry_date() {
        return coupon_expiry_date;
    }

    public void setCoupon_expiry_date(String coupon_expiry_date) {
        this.coupon_expiry_date = coupon_expiry_date;
    }

    public String getCoupon_limit_amount_str() {
        return coupon_limit_amount_str;
    }

    public void setCoupon_limit_amount_str(String coupon_limit_amount_str) {
        this.coupon_limit_amount_str = coupon_limit_amount_str;
    }

    public String getCoupon_label() {
        return coupon_label;
    }

    public void setCoupon_label(String coupon_label) {
        this.coupon_label = coupon_label;
    }

    public String getCoupon_desc() {
        return coupon_desc;
    }

    public void setCoupon_desc(String coupon_desc) {
        this.coupon_desc = coupon_desc;
    }

    public String getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        this.coupon_value = coupon_value;
    }

    public String getCoupon_min_use_value() {
        return coupon_min_use_value;
    }

    public void setCoupon_min_use_value(String coupon_min_use_value) {
        this.coupon_min_use_value = coupon_min_use_value;
    }
}
