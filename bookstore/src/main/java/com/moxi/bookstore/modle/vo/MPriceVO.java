package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 价格对象OV
 *
 * @author duxiaojie
 * @date 2018-08-03 17:54
 */
public class MPriceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 总价
     */
    private BigDecimal total;
    /**
     * 电子书总价
     */
    private BigDecimal ebook_total;
    /**
     * 除去电子书之后的价格
     */
    private BigDecimal except_ebook_total;
    /**
     * 电子书总价对应铃铛数
     */
    private Integer ebook_total_amount;
    /**
     * 集合促销的优惠之和
     */
    private BigDecimal total_favor;

    public BigDecimal getTotal() {
        return total == null ? BigDecimal.ZERO : total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getEbook_total() {
        return ebook_total == null ? BigDecimal.ZERO : ebook_total;
    }

    public void setEbook_total(BigDecimal ebook_total) {
        this.ebook_total = ebook_total;
    }

    public BigDecimal getExcept_ebook_total() {
        return except_ebook_total == null ? BigDecimal.ZERO : except_ebook_total;
    }

    public void setExcept_ebook_total(BigDecimal except_ebook_total) {
        this.except_ebook_total = except_ebook_total;
    }

    public Integer getEbook_total_amount() {
        return ebook_total_amount == null ? 0 : ebook_total_amount;
    }

    public void setEbook_total_amount(Integer ebook_total_amount) {
        this.ebook_total_amount = ebook_total_amount;
    }

    public BigDecimal getTotal_favor() {
        return total_favor == null ? BigDecimal.ZERO : total_favor;
    }

    public void setTotal_favor(BigDecimal total_favor) {
        this.total_favor = total_favor;
    }

    @Override
    public String toString() {
        return "MPriceVO{" +
                "total=" + total +
                ", ebook_total=" + ebook_total +
                ", except_ebook_total=" + except_ebook_total +
                ", ebook_total_amount=" + ebook_total_amount +
                ", total_favor=" + total_favor +
                '}';
    }
}
