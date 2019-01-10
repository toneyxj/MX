package com.moxi.copybook.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.moxi.copybook.FileCopy;
import com.moxi.copybook.R;
import com.mx.mxbase.base.BaseApplication;
import com.mx.mxbase.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 设置框
 * Created by Administrator on 2016/9/22.
 */
public class SettingDialog extends Dialog  {
    private String fontPath = "font/";
    //字体文件
    private String sdFilePath= StringUtils.getFilePath("font/");
    private List<String> fonts=new ArrayList<>();
    private List<String> assetsFonts=new ArrayList<>();
    /**
     * 字体设置
     */
    //标准
    private LinearLayout standard_click;
    private ImageView standard_select;
    //圆体
    private LinearLayout arcfont_click;
    private ImageView arcfont_select;
    //黑体
    private LinearLayout blackfont_click;
    private ImageView blackfont_select;
    //隶书
    private LinearLayout lishu_click;
    private ImageView lishu_select;
    //楷体
    private LinearLayout kaiti_click;
    private ImageView kaiti_select;
private SettingInterface anInterface;


    public SettingDialog(Context context, int theme, SettingInterface anInterface) {
        super(context, theme);
        this.anInterface = anInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_setting);
        fonts.add(null);
        fonts.add(sdFilePath + "zhun_yuan.TTF");
        fonts.add(sdFilePath + "hei_ti.TTF");
        fonts.add(sdFilePath + "fang_son.TTF");
        fonts.add(sdFilePath + "kai_ti.TTF");

        assetsFonts.add(null);
        assetsFonts.add(fontPath+"zhun_yuan.TTF");
        assetsFonts.add(fontPath+"hei_ti.TTF");
        assetsFonts.add(fontPath+"fang_son.TTF");
        assetsFonts.add(fontPath+"kai_ti.TTF");

        initfontSize();
    }

    private boolean IsfontRefuresh = true;

    private void initfontSize() {
        //标准
        standard_click = (LinearLayout) findViewById(R.id.standard_click);
        standard_select = (ImageView) findViewById(R.id.standard_select);
        //圆体
        arcfont_click = (LinearLayout) findViewById(R.id.arcfont_click);
        arcfont_select = (ImageView) findViewById(R.id.arcfont_select);
        //黑体
        blackfont_click = (LinearLayout) findViewById(R.id.blackfont_click);
        blackfont_select = (ImageView) findViewById(R.id.blackfont_select);
        //隶书/仿宋
        lishu_click = (LinearLayout) findViewById(R.id.lishu_click);
        lishu_select = (ImageView) findViewById(R.id.lishu_select);
        //楷体
        kaiti_click = (LinearLayout) findViewById(R.id.kaiti_click);
        kaiti_select = (ImageView) findViewById(R.id.kaiti_select);

        standard_click.setOnClickListener(fontClick);
        arcfont_click.setOnClickListener(fontClick);
        blackfont_click.setOnClickListener(fontClick);
        lishu_click.setOnClickListener(fontClick);
        kaiti_click.setOnClickListener(fontClick);

        selectIndex(getFontIndex());

    }

    View.OnClickListener fontClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.standard_click) {
                selectIndex(0);
            } else if (i == R.id.arcfont_click) {
                selectIndex(1);
            } else if (i == R.id.blackfont_click) {
                selectIndex(2);
            } else if (i == R.id.lishu_click) {
                selectIndex(3);
            } else if (i == R.id.kaiti_click) {
                selectIndex(4);
            }
        }
    };
    private int currentFontIndex=-1;

    private void selectIndex(final int index) {
        if (index==currentFontIndex)return;

        nonSelect();
        String path = null;
        String fontName = "";
        String toSdPath=null;
        switch (index) {
            case 0:
                standard_select.setImageResource(R.mipmap.select_have);
                fontName = "系统字体";
                break;
            case 1:
//                path = fontPath + "zhun_yuan.TTF";
//                toSdPath=sdFilePath+"zhun_yuan.TTF";
                fontName = "圆体";
                arcfont_select.setImageResource(R.mipmap.select_have);
                break;
            case 2:
//                path = fontPath + "hei_ti.TTF";
//                toSdPath=sdFilePath+"hei_ti.TTF";
                fontName = "黑体";
                blackfont_select.setImageResource(R.mipmap.select_have);
                break;
            case 3:
//                path = fontPath + "fang_son.TTF";
//                toSdPath=sdFilePath+"fang_son.TTF";
                fontName = "仿宋";
                lishu_select.setImageResource(R.mipmap.select_have);
                break;
            case 4:
//                path = fontPath + "kai_ti.TTF";
//                toSdPath=sdFilePath+"kai_ti.TTF";
                fontName = "楷体";
                kaiti_select.setImageResource(R.mipmap.select_have);
                break;
            default:
                break;
        }

        if (!IsfontRefuresh) {
            new FileCopy(getContext(), assetsFonts.get(index), fonts.get(index), fontName, new FileCopy.CopyListener() {
                @Override
                public void CopyListener(boolean results, String path, String name) {
                    if (results){
                        SharedPreferences.Editor editor=BaseApplication.editor;
                        editor.putString("fontPath",path);
                        editor.commit();
                        anInterface.settingFont(path);
                        SettingDialog.this.dismiss();
                    }else {
                        Toast.makeText(getContext(),"字体准备失败",Toast.LENGTH_SHORT).show();
                    }

                }
            }).execute();
        }
        currentFontIndex=index;
        IsfontRefuresh = false;
    }

    private int getFontIndex() {
        String savePath=BaseApplication.preferences.getString("fontPath",null);
        if (savePath==null)return 0;
        for (int i = 1; i < fonts.size(); i++) {
            if (fonts.get(i).equals(savePath)){
                return i;
            }
        }
        return 0;
    }

    private void nonSelect() {
        standard_select.setImageResource(R.mipmap.select_non);
        arcfont_select.setImageResource(R.mipmap.select_non);
        blackfont_select.setImageResource(R.mipmap.select_non);
        lishu_select.setImageResource(R.mipmap.select_non);
        kaiti_select.setImageResource(R.mipmap.select_non);
    }

    public static void getDialog(Context context, SettingInterface anInterface) {
        SettingDialog dialog = new SettingDialog(context, R.style.AlertDialogStyle, anInterface);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.getDecorView().setPadding(BaseApplication.ScreenWidth / 6, 0, BaseApplication.ScreenWidth / 6, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        window.setAttributes(lp);
        dialog.show();
    }

}
