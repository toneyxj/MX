package com.moxi.handwritten.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.moxi.handwritten.R;
import com.moxi.handwritten.WriteDrawActivity;
import com.moxi.handwritten.WriteMainInterface;
import com.moxi.handwritten.adapter.WriteItemAdapter;
import com.moxi.handwritten.dialog.WriteMoveDialog;
import com.moxi.handwritten.model.FloderBeen;
import com.moxi.handwritten.model.WriteFileBeen;
import com.moxi.handwritten.utils.WriteDataUtils;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.base.baseFragment;
import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.dialog.InputDialog;
import com.mx.mxbase.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 写字板进入碎片
 * Created by Administrator on 2016/8/2.
 */
public class WriteFragment extends baseFragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {
    private String titleName = "";
    private String dataName = "";
    public String floderPath = "";

    public String getFloderPath() {
        return floderPath;
    }

    public String getTitleName() {
        return titleName;
    }

    public String getDataName() {
        return dataName;
    }//所属表名

    /**
     * 设置事件监听
     */
    private WriteMainInterface listener;
    private List<WriteFileBeen> listData = new ArrayList<>();

    @Bind(R.id.write_item)
    GridView write_item;
    private WriteItemAdapter adapter;
    private List<WriteFileBeen> sonList = new ArrayList<>();
    private int CurrentIndex = 0;
    private int totalIndex = 1;
    private boolean isSelect = false;

    public void setIsSelect(boolean isSelect) {
        this.isSelect = isSelect;
        adapter.setShowAelect(isSelect);
        adapter.notifyDataSetChanged();
        if (!isSelect) {
            for (int i = 0; i < listData.size(); i++) {
                listData.get(i).isSelect = false;
            }
        }
    }
    /**
     * 注册广播
     */
    public BroadcastReceiver receiver;

    /**
     * 设置广播
     */
    public void setBrodcast() {
        receiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction()
                        .equals(dataName)) {
                    reInit();
                }
            }

        };

        IntentFilter intentFilter = new IntentFilter(dataName);
        getActivity().registerReceiver(receiver, intentFilter);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (WriteMainInterface) context;
        } catch (Exception e) {
        }
    }

    //    @Bind(R.id.write_viewpager)
//    WriteViewpager write_viewpager;
//    private List<baseFragment> fragments;

    public static WriteFragment newInstance(
            String titleName, String dataName, String floderPath) {
        WriteFragment diFragment = new WriteFragment();
        Bundle bundle = new Bundle();
        bundle.putString("titleName", titleName);
        bundle.putString("dataName", dataName);
        bundle.putString("floderPath", floderPath);
        diFragment.setArguments(bundle);
        return diFragment;
    }

    public void MoveClick() {
        final List<WriteFileBeen> listC = getSelectItem();
        if (listC.size() == 0) return;
        List<FloderBeen> listFloders = WriteDataUtils.getInstance().getAllFloderTable("writemain", "");

        List<FloderBeen> floders = getAllMoveFloaderName(listC);

        List<Integer> listInt=new ArrayList<>();
        int index=0;
        for(FloderBeen been:listFloders){
            String filename=been.fileName;
            for (FloderBeen floderBeen:floders){

                if (filename.contains(floderBeen.fileName)){
                   String[] files=filename.split("/");
                    String[] folderss=floderBeen.fileName.split("/");
                    int size=folderss.length-1;

                    if (files.length==folderss.length&&(folderss[size].equals(files[size]))) {
                        listInt.add(index);
                    }

                    break;
                }
            }
            index++;
        }
        int reduce=0;
        APPLog.e(listInt);
        for (int i = 0; i < listInt.size(); i++) {
            int indexss=listInt.get(i)-reduce;
            listFloders.remove(indexss);
            reduce++;
        }
        if (!dataName.equals("writemain")){
            listFloders.add(0,new FloderBeen("/","writemain"));
        }
        //获得最终文件夹路径
        if (listFloders.size()!=0){
            WriteMoveDialog.getdialog(getActivity(), listFloders, new WriteMoveDialog.selectDialogListenr() {
                @Override
                public void selectItem(FloderBeen floder) {
                    List<Integer> lists=new ArrayList<Integer>();
                    for (int i = 0; i < listC.size(); i++) {
                        if (WriteDataUtils.getInstance().judgeExist(floder.dbTableName,listC.get(i).fileName)){
                            lists.add(i);
                        }
                    }
                    for (int i = 0; i < lists.size(); i++) {
                        listC.remove(lists.get(i)-i);
                    }
                        //移动去的文件夹名称和所在数据库名称
                    WriteDataUtils.getInstance().deleteLinesData(getDataName(),getAllFileName(listC));
                    WriteDataUtils.getInstance().insertData(floder.dbTableName, listC);

                    reInit();
                    listener.alterFinish();
                    getActivity().sendBroadcast(new Intent(floder.dbTableName));
                }
            });
        }
    }

    public List<FloderBeen> getAllMoveFloaderName(List<WriteFileBeen> list) {
        List<FloderBeen> listS = new ArrayList<>();
        for (WriteFileBeen been : list) {
            if (been.type.equals("0")) {
                listS.add(new FloderBeen(floderPath + "/" + been.fileName, been.PathsOrName));
            }
        }
        return listS;
    }

    public void deleteClick() {
        List<WriteFileBeen> listC = getSelectItem();
        if (listC.size() != 0) {
            WriteDataUtils.getInstance().deleteLinesData(getDataName(), getAllFileName(listC));
            //删除图片文件
            List<WriteFileBeen> files=getAllFileContentName(listC);
            for (WriteFileBeen been:files){
                String[] images=been.PathsOrName.split(",");
                for (int i = 0; i < images.length; i++) {
                    StringUtils.deleteFile(images[i]);
                }
            }

            List<FloderBeen> lists = getAllFloaderName(listC);
            for (FloderBeen been : lists) {
                WriteDataUtils.getInstance().getAllFloderTableDelete(been.dbTableName);
            }

            reInit();
            listener.alterFinish();
        }
    }

    public List<String> getAllFileName(List<WriteFileBeen> list) {
        List<String> listS = new ArrayList<>();
        for (WriteFileBeen been : list) {
            listS.add(been.fileName);
        }
        return listS;
    }

    public List<FloderBeen> getAllFloaderName(List<WriteFileBeen> list) {
        List<FloderBeen> listS = new ArrayList<>();
        for (WriteFileBeen been : list) {
            if (been.type.equals("0")) {
                listS.add(new FloderBeen(been.fileName, been.PathsOrName));
            }
        }
        return listS;
    }
    public List<WriteFileBeen> getAllFileContentName(List<WriteFileBeen> list) {
        List<WriteFileBeen> listS = new ArrayList<>();
        for (WriteFileBeen been : list) {
            if (been.type.equals("1")) {
                listS.add(been);
            }
        }
        return listS;
    }

    private List<WriteFileBeen> getSelectItem() {
        List<WriteFileBeen> beens = new ArrayList<>();
        for (WriteFileBeen been : listData) {
            if (been.isSelect)
                beens.add(been);
        }
        return beens;
    }

    @Override
    public void initFragment(View view) {
        init();
        write_item.setOnItemClickListener(this);
        write_item.setOnItemLongClickListener(this);
        write_item.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);//返回手势识别触发的事件
            }
        });
        setBrodcast();
    }

    private void init() {
        titleName = getArguments().getString("titleName");
        dataName = getArguments().getString("dataName");
        floderPath = getArguments().getString("floderPath");

        reInit();
    }

    @Override
    public void moveRight() {
        super.moveRight();
        if (CurrentIndex >= totalIndex - 1) {
            return;
        } else {
            CurrentIndex++;
            initAdapterData();
        }
    }

    @Override
    public void moveLeft() {
        super.moveLeft();
        if (CurrentIndex > 0 && (CurrentIndex <= totalIndex - 1)) {
            CurrentIndex--;
            initAdapterData();
        }
    }

    public void reInit() {
        listData.clear();
        listData.addAll(WriteDataUtils.getInstance().getDatas(dataName, getResources().getString(R.string.new_file)));
        //计算需要多少个fragment
        int size = listData.size() / 12;
        size += listData.size() % 12 == 0 ? 0 : 1;
        totalIndex = size;
        if (totalIndex <= CurrentIndex) {
            CurrentIndex = totalIndex - 1;
        }
        initAdapterData();

        GCFragment();
    }

    private void initAdapterData() {
        if (totalIndex - 1 == CurrentIndex) {
            adapterItems(listData.subList(CurrentIndex * 12, listData.size()));
        } else {
            adapterItems(listData.subList(CurrentIndex * 12, (CurrentIndex + 1) * 12));
        }

        listener.ShowIndex((CurrentIndex + 1) + "/" + totalIndex);
    }

    private void adapterItems(List<WriteFileBeen> List) {
        sonList.clear();
        sonList.addAll(List);
        APPLog.e(sonList.toString());
        if (adapter == null) {
            adapter = new WriteItemAdapter(getActivity(), sonList);
            write_item.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }

    }

    public void initTitle() {
        if (listener != null)
            listener.selectItem(titleName, dataName);
    }

    @Override
    public void onResume() {
        super.onResume();
        initTitle();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_write;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        listData.clear();
        getActivity(). unregisterReceiver(receiver);
        listData = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (sonList.get(position).type.equals("-1")) {
            if (!isSelect) {
                listener.createNewFile(getDataName());
            }
        } else {
            if (isSelect) {
                sonList.get(position).changeSelect();
                adapter.updateSelect(position, write_item);
            } else {
                if (sonList.get(position).type.equals("0")) {
                    listener.commingFloder(sonList.get(position));
                } else {
                    listener.checkDetailFile(getDataName(),sonList.get(position));
                }
            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (sonList.get(position).type.equals("-1")) return false;

        inputTitleDialog(position);
        return false;
    }

    private void inputTitleDialog(final int index) {
        InputDialog.getdialog(getActivity(), getString(R.string.re_name), "请输入新文件名", new InputDialog.InputListener() {
            @Override
            public void quit() {
//                listener.HideShow();
                GCFragment();
            }

            @Override
            public void insure(String name) {
                String olderName = sonList.get(index).fileName;
                if (sonList.get(index).type.equals("1"))
                name= WriteDrawActivity.FileLast+name;

                if ( name.equals(olderName)) {
                    BaseApplication.Toast("输入文件名与原文件名重复");
                    GCFragment();
                    return;
                }
                    if (WriteDataUtils.getInstance().judgeExist(getDataName(), name)) {
                        BaseApplication.Toast("该文件名已存在");
                        GCFragment();
                        return;
                    }
                    APPLog.e(name);
                    APPLog.e(olderName);
                    WriteDataUtils.getInstance().updateDowlaod(getDataName(), name, olderName);
//                    sonList.get(index).fileName = name;
//                    adapter.updateFileName(index, write_item);
                    reInit();
            }
        });
    }
public void GCFragment(){
//    EpdController.invalidate(write_item, EpdController.UpdateMode.GC);
}

}
