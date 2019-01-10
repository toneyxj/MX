package com.moxi.handwritten;


import com.moxi.handwritten.model.WriteFileBeen;

/**
 * Created by Administrator on 2016/8/2.
 */
public interface WriteMainInterface {
    /**
     * 选择的主文件
     * @param titleName
     * @param dataName
     */
    public  void selectItem(String titleName, String dataName);//

    /**
     * 显示当前文件夹浏览路径
     * @param index
     */
    public  void ShowIndex(String index);

    /**
     * 点击进入具体文件夹
     * @param data 获得数据
     */
    public void commingFloder(WriteFileBeen data);

    /**
     * 点击进入具体文件
     * @param data
     */
    public void checkDetailFile(String tableName, WriteFileBeen data);

    /**
     * 创建文件
     */
    public void createNewFile(String tableName);
    /**
     * 修改文件介素调用
     */
    public void alterFinish();
    public void HideShow();
}
