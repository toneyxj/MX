package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 商品信息VO
 *
 * @author duxiaojie
 * @date 2018-08-03 20:53
 */
public class MProductVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long cart_product_item_id;
    private Long product_id;
    private Integer product_type;
    private Long main_product_id;
    private String product_name;
    private Integer img_version;
    private String product_image;
    private Integer have_mobile_price;
    private Integer product_count;
    private BigDecimal product_total_money;
    private MProductPriceVO price;
    private Integer stock;
    private Long shop_id;
    private String shop_name;
    private List<MProductSaleAttributeVO> catalog;
    private Integer is_checked;
    private Integer allow_check;
    private Integer is_presale;
    private Integer is_ebook;
    private Integer is_offline;
    private Integer is_deserve;
    private Integer is_outlets;
    private String add_cart_date;
    private String message;
    private List<MProductSinglePromotionVO> single_promotions;
    private Long batch_id;
    private String is_overseas;
    private Integer activity_type;
    private String category_path;
    private List<MProductSubVO> sub_product;
    private Integer spu_limit_buy_num;
    private String limit_scope;
    private Integer over_spu_limit_buy_num;
    private String price_reminder;
    private Integer max_limit_buy;
    private BigDecimal low_price;
    private String show_tag;
    private String low_price_name;
    private BigDecimal prime_price;
    private BigDecimal account_money;
    private Integer seckill_time;
    private Integer is_ele_gift_card;
    private String max_limit_buy_tips;
    private List<MProductLabelInfoVO> label_infos;
    private BigDecimal price_change;
    private Object select_promotions;

    public Long getCart_product_item_id() {
        return cart_product_item_id == null ? 0 : cart_product_item_id;
    }

    public void setCart_product_item_id(Long cart_product_item_id) {
        this.cart_product_item_id = cart_product_item_id;
    }

    public Long getProduct_id() {
        return product_id == null ? 0 : product_id;
    }

    public void setProduct_id(Long product_id) {
        this.product_id = product_id;
    }

    public Integer getProduct_type() {
        return product_type == null ? 0 : product_type;
    }

    public void setProduct_type(Integer product_type) {
        this.product_type = product_type;
    }

    public Long getMain_product_id() {
        return main_product_id == null ? 0 : main_product_id;
    }

    public void setMain_product_id(Long main_product_id) {
        this.main_product_id = main_product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public Integer getHave_mobile_price() {
        return have_mobile_price == null ? 0 : have_mobile_price;
    }

    public void setHave_mobile_price(Integer have_mobile_price) {
        this.have_mobile_price = have_mobile_price;
    }

    public Integer getProduct_count() {
        return product_count == null ? 0 : product_count;
    }

    public void setProduct_count(Integer product_count) {
        this.product_count = product_count;
    }

    public BigDecimal getProduct_total_money() {
        return product_total_money == null ? BigDecimal.ZERO : product_total_money;
    }

    public void setProduct_total_money(BigDecimal product_total_money) {
        this.product_total_money = product_total_money;
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

    public Long getShop_id() {
        return shop_id == null ? 0 : shop_id;
    }

    public void setShop_id(Long shop_id) {
        this.shop_id = shop_id;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public List<MProductSaleAttributeVO> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<MProductSaleAttributeVO> catalog) {
        this.catalog = catalog;
    }

    public Integer getIs_checked() {
        return is_checked == null ? 0 : is_checked;
    }

    public void setIs_checked(Integer is_checked) {
        this.is_checked = is_checked;
    }

    public Integer getAllow_check() {
        return allow_check == null ? 0 : allow_check;
    }

    public void setAllow_check(Integer allow_check) {
        this.allow_check = allow_check;
    }

    public Integer getIs_presale() {
        return is_presale == null ? 0 : is_presale;
    }

    public void setIs_presale(Integer is_presale) {
        this.is_presale = is_presale;
    }

    public Integer getIs_ebook() {
        return is_ebook == null ? 0 : is_ebook;
    }

    public void setIs_ebook(Integer is_ebook) {
        this.is_ebook = is_ebook;
    }

    public Integer getIs_offline() {
        return is_offline == null ? 0 : is_offline;
    }

    public void setIs_offline(Integer is_offline) {
        this.is_offline = is_offline;
    }

    public Integer getIs_deserve() {
        return is_deserve == null ? 0 : is_deserve;
    }

    public void setIs_deserve(Integer is_deserve) {
        this.is_deserve = is_deserve;
    }

    public Integer getIs_outlets() {
        return is_outlets == null ? 0 : is_outlets;
    }

    public void setIs_outlets(Integer is_outlets) {
        this.is_outlets = is_outlets;
    }

    public String getAdd_cart_date() {
        return add_cart_date;
    }

    public void setAdd_cart_date(String add_cart_date) {
        this.add_cart_date = add_cart_date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MProductSinglePromotionVO> getSingle_promotions() {
        return single_promotions;
    }

    public void setSingle_promotions(List<MProductSinglePromotionVO> single_promotions) {
        this.single_promotions = single_promotions;
    }

    public Long getBatch_id() {
        return batch_id == null ? 0 : batch_id;
    }

    public void setBatch_id(Long batch_id) {
        this.batch_id = batch_id;
    }

    public String getIs_overseas() {
        return is_overseas;
    }

    public void setIs_overseas(String is_overseas) {
        this.is_overseas = is_overseas;
    }

    public Integer getActivity_type() {
        return activity_type == null ? 0 : activity_type;
    }

    public void setActivity_type(Integer activity_type) {
        this.activity_type = activity_type;
    }

    public String getCategory_path() {
        return category_path;
    }

    public void setCategory_path(String category_path) {
        this.category_path = category_path;
    }

    public List<MProductSubVO> getSub_product() {
        return sub_product;
    }

    public void setSub_product(List<MProductSubVO> sub_product) {
        this.sub_product = sub_product;
    }

    public Integer getSpu_limit_buy_num() {
        return spu_limit_buy_num == null ? 0 : spu_limit_buy_num;
    }

    public void setSpu_limit_buy_num(Integer spu_limit_buy_num) {
        this.spu_limit_buy_num = spu_limit_buy_num;
    }

    public String getLimit_scope() {
        return limit_scope;
    }

    public void setLimit_scope(String limit_scope) {
        this.limit_scope = limit_scope;
    }

    public Integer getOver_spu_limit_buy_num() {
        return over_spu_limit_buy_num == null ? 0 : over_spu_limit_buy_num;
    }

    public void setOver_spu_limit_buy_num(Integer over_spu_limit_buy_num) {
        this.over_spu_limit_buy_num = over_spu_limit_buy_num;
    }

    public String getPrice_reminder() {
        return price_reminder;
    }

    public void setPrice_reminder(String price_reminder) {
        this.price_reminder = price_reminder;
    }

    public Integer getMax_limit_buy() {
        return max_limit_buy == null ? 0 : max_limit_buy;
    }

    public void setMax_limit_buy(Integer max_limit_buy) {
        this.max_limit_buy = max_limit_buy;
    }

    public BigDecimal getLow_price() {
        return low_price == null ? BigDecimal.ZERO : low_price;
    }

    public void setLow_price(BigDecimal low_price) {
        this.low_price = low_price;
    }

    public String getShow_tag() {
        return show_tag;
    }

    public void setShow_tag(String show_tag) {
        this.show_tag = show_tag;
    }

    public String getLow_price_name() {
        return low_price_name;
    }

    public void setLow_price_name(String low_price_name) {
        this.low_price_name = low_price_name;
    }

    public BigDecimal getPrime_price() {
        return prime_price == null ? BigDecimal.ZERO : prime_price;
    }

    public void setPrime_price(BigDecimal prime_price) {
        this.prime_price = prime_price;
    }

    public BigDecimal getAccount_money() {
        return account_money == null ? BigDecimal.ZERO : account_money;
    }

    public void setAccount_money(BigDecimal account_money) {
        this.account_money = account_money;
    }

    public Integer getSeckill_time() {
        return seckill_time == null ? 0 : seckill_time;
    }

    public void setSeckill_time(Integer seckill_time) {
        this.seckill_time = seckill_time;
    }

    public Integer getIs_ele_gift_card() {
        return is_ele_gift_card == null ? 0 : is_ele_gift_card;
    }

    public void setIs_ele_gift_card(Integer is_ele_gift_card) {
        this.is_ele_gift_card = is_ele_gift_card;
    }

    public String getMax_limit_buy_tips() {
        return max_limit_buy_tips;
    }

    public void setMax_limit_buy_tips(String max_limit_buy_tips) {
        this.max_limit_buy_tips = max_limit_buy_tips;
    }

    public List<MProductLabelInfoVO> getLabel_infos() {
        return label_infos;
    }

    public void setLabel_infos(List<MProductLabelInfoVO> label_infos) {
        this.label_infos = label_infos;
    }

    public BigDecimal getPrice_change() {
        return price_change == null ? BigDecimal.ZERO : price_change;
    }

    public void setPrice_change(BigDecimal price_change) {
        this.price_change = price_change;
    }

    public Object getSelect_promotions() {
        return select_promotions;
    }

    public void setSelect_promotions(Object select_promotions) {
        this.select_promotions = select_promotions;
    }
}
