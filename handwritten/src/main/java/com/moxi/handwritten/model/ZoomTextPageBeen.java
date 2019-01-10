package com.moxi.handwritten.model;

import java.util.ArrayList;

/**
 * 缩放保存文件类型当页显示数据
 * Created by Administrator on 2016/8/22.
 */
public class ZoomTextPageBeen {
    /**
     * 页面基本信息
     */
    /**
     * 绘制界面宽度
     */
    private int screenWidth;
    /**
     * 绘制界面高度
     */
    private int screenHeight;
    /**
     * 控件高度
     */
    private int ketHeight;
    private int interspace;

    public ZoomTextPageBeen(int screenWidth, int screenHeight, int ketHeight,int interspace) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.ketHeight = ketHeight;
        this.interspace=interspace;
    }

    /**
     * 显示文字
     */
    private ArrayList<TextPath> pageTexts = new ArrayList<>();
    private int page;//当前显示页数


    public ArrayList<TextPath> getPageTexts() {
        return pageTexts;
    }

    public void setPageTexts(ArrayList<TextPath> pageTexts) {
        this.pageTexts.clear();
        this.pageTexts.addAll(pageTexts);
    }

    /**
     * 设置基本文字信息
     *
     * @param paths
     */
    public void settexts(TextPath paths) {
        pageTexts.add(paths);
    }

    /**
     * 设置基本文字信息
     *
     * @param paths
     */
    public void settexts(int index,TextPath paths) {
        pageTexts.add(index,paths);
    }

    /**
     * 获得当页最大的行数
     *
     * @return
     */
    public int getMaxVertical() {
        if (pageTexts.size()!=0)
        return pageTexts.get(getSize()-1).lineNumber;
        else return -1;
    }

    /**
     * 获得当页字体个数
     * @return
     */
    public int getSize() {
        return pageTexts.size();
    }

    /**
     * 返回
     */
    public void backtext() {
        pageTexts.remove(pageTexts.size() - 1);
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
    public void changPosition(int index,int textWidth){
//        if(pageTexts.get(index).gettextDrawWid()>screenWidth){
//            pageTexts.get(index).lineNumber++;
//            pageTexts.get(index).changeTop(ketHeight,interspace);
//            pageTexts.get(index).leftpoint=0;
//        }else{
//            pageTexts.get(index).leftpoint+=textWidth;
//        }
        pageTexts.get(index).changePosition(screenWidth,ketHeight,interspace,textWidth);
    }
}
