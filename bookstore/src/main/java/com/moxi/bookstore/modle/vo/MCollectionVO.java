package com.moxi.bookstore.modle.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 促销商品集合
 *
 * @author duxiaojie
 * @date 2018-08-06 15:12
 */
public class MCollectionVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<MProductVO> products;

    private MCollectionPromotionVO collection_promotion;

    public List<MProductVO> getProducts() {
        return products;
    }

    public void setProducts(List<MProductVO> products) {
        this.products = products;
    }

    public MCollectionPromotionVO getCollection_promotion() {
        return collection_promotion;
    }

    public void setCollection_promotion(MCollectionPromotionVO collection_promotion) {
        this.collection_promotion = collection_promotion;
    }
}
