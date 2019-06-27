package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 降价商品VO
 *
 * @author duxiaojie
 * @date 2018-09-14 17:12
 */
public class MProductReduceVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 降价提示语
     */
    private String reduce_tips;

    /**
     * 降价商品列表
     * 暂时去掉，后期按需增加
     */
    //private List<MProductVO> reduce_product_list;

    public String getReduce_tips() {
        return reduce_tips;
    }

    public void setReduce_tips(String reduce_tips) {
        this.reduce_tips = reduce_tips;
    }
}
