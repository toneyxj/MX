package com.moxi.handwritten;

import android.app.Activity;
import android.graphics.Path;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.handwritten.dialog.SaveDrawDialog;
import com.moxi.handwritten.model.ClickPoint;
import com.moxi.handwritten.model.TextPath;
import com.moxi.handwritten.model.ZoomTextPageBeen;
import com.moxi.handwritten.view.CursorView;
import com.moxi.handwritten.view.DrawTextZoomView;
import com.moxi.handwritten.view.TextZoomShowView;
import com.moxi.handwritten.view.ZoomRelativeLayout;
import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.constant.APPLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class TextZoomActivity extends BaseActivity implements DrawTextZoomView.ZoomListener, View.OnClickListener ,ZoomRelativeLayout.ZoomPointListener{
    public static final int horizonInterspace=10;
    @Bind(R.id.draw_text)
    DrawTextZoomView draw_text;
    //放置显示文字
    @Bind(R.id.relative_room)
    ZoomRelativeLayout relative_room;
    //添加文本信息
    @Bind(R.id.add_text)
    RelativeLayout add_text;

    @Bind(R.id.delete)
    TextView delete;

    @Bind(R.id.complete)
    TextView complete;
    @Bind(R.id.cursor)
    CursorView cursor;//游标view
    /**
     * 相对布局集合
     */
    private List<RelativeLayout> relatives = new ArrayList<>();
    /**
     * 横排当前宽度
     */
    private int horizontal=5;
    /**
     * 当前排数
     */
    private int vertical = 0;
    /**
     * 当前显示文字的位置
     */
    private int currentPosition=0;
    /**
     * 当前显示页数
     */
    private int CurrentPage = 0;
    /**
     * 总排数
     */
    private final int totalVer = 18;

    /**
     * 控件宽度
     */
    private int ketWidth;
    /**
     * 控件高度
     */
    private int ketHeight=0;
    /**
     * 控件显示文字高度
     */
    private int textHeight;
    /**
     * 空隙高度
     */
    private int interspace;


    /**
     * 装载绘制文字数据集合
     */
    private ArrayList<ZoomTextPageBeen> mainData = new ArrayList<>();
    /**
     * 显示文字的布局集合
     */
    private ArrayList<TextZoomShowView> zoomViews = new ArrayList<>();


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        complete.setOnClickListener(this);
        //设置手指点击光标监听
        relative_room.setListener(this);

        delete.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ketWidth = relative_room.getWidth();
            ketHeight = relative_room.getHeight() / totalVer;

            textHeight = (int) (ketHeight * 0.8);

            interspace = (int) (ketHeight * 0.2);

            draw_text.initZoom(textHeight, this);
            relative_room.setline(ketHeight, totalVer, ketWidth);

            cursor.setCursorHeight(ketHeight);
            addRelative(false);
        }
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {//避免返回时出现白色阴影
            TextZoomActivity.this.finish();
        }else if (msg.what==horizonInterspace) {//写字完成刷新
            Path path = draw_text.getPath();
            if (path != null) {
                int width = draw_text.getDrawWidth();
                APPLog.e("控件宽度" + width);
                TextZoomShowView showView = new TextZoomShowView(this,width,textHeight+horizonInterspace);
//                showView.setBackgroundColor(getResources().getColor(R.color.colorGray));
                if ((horizontal + horizonInterspace + width) > ketWidth) {
                    horizontal = 5;
                    vertical++;
                }
                int leftMargin = horizontal;
                int topMargin = (vertical * ketHeight + interspace);
                zoomViews.add(currentPosition, showView);
                relatives.get(CurrentPage).addView(showView);
                //添加文字保存
                mainData.get(CurrentPage).settexts(currentPosition,new TextPath(draw_text.getListData(), vertical, leftMargin, topMargin, width));
                //增加绘制宽度
                horizontal += width + horizonInterspace;
                //设置光标移动
                cursor.setMagin(horizontal - 4, vertical * ketHeight);

                showView.setPath(path);
                showView.setMagin(leftMargin, topMargin);

                currentPosition++;
                changeCursorLastView(width+horizonInterspace);
            }
            draw_text.clearDraw();
        }
    }

    /**
     * 移动光标后面的文字
     */
    private void changeCursorLastView(int textWidth){
        int middleWidth=5;//临时展示宽度
        int middleVer=vertical;//临时展示第几行
        boolean isfirst=true;
        int lastWidth=0;
        //有需要移动的文字
            for (int i = currentPosition; i < zoomViews.size(); i++) {
                //设置字体新位置信息
//                mainData.get(CurrentPage).changPosition(i, textWidth);
                TextPath textPath=mainData.get(CurrentPage).getPageTexts().get(i);
                int width=textPath.textWidth;
                //初始化赋值
                if (isfirst){
                    isfirst=false;
                    middleWidth=textPath.leftpoint;
                    middleWidth+=textWidth;
                }else{
                    middleWidth+=lastWidth+horizonInterspace;
                }
                lastWidth=width;

                if ((middleWidth + width) > ketWidth) {
                    middleWidth = 5;
                    middleVer++;
                }

                int leftMargin = middleWidth;
                int topMargin = (middleVer * ketHeight + interspace);


                textPath.setPoint(middleVer, leftMargin, topMargin);

//                TextPath been = mainData.get(CurrentPage).getPageTexts().get(i);

                zoomViews.get(i).setMagin(textPath.leftpoint, textPath.toppoint);

            }
    }

    /**
     * 删除文字
     */
    private void deleteText(){
        //当前删除文字坐标
         currentPosition=currentPosition-1;
        if (currentPosition<0)return;

        //获得删除的对象
        TextZoomShowView showView =zoomViews.get(currentPosition);
       //获得要删除的文字对象
        TextPath textmiddlePath=mainData.get(CurrentPage).getPageTexts().get(currentPosition);
        boolean isfirst=true;
        int middleWidth=textmiddlePath.leftpoint;//临时展示宽度
        int middleVer=vertical;//临时展示第几行
        int textWidth=textmiddlePath.textWidth;//字体宽度
        //删除对象view
        relatives.get(CurrentPage).removeView(showView);
        //删除保存在集合中的view
        zoomViews.remove(currentPosition);
        mainData.get(CurrentPage).getPageTexts().remove(currentPosition);
        //设置光标位置信息
        horizontal=middleWidth;
        cursor.setMagin(horizontal - 4, vertical * ketHeight);
        //有需要移动的文字
        for (int i = currentPosition; i < zoomViews.size(); i++) {
            //设置字体新位置信息
            TextPath textPath=mainData.get(CurrentPage).getPageTexts().get(i);
            int width=textPath.textWidth;
            //初始化赋值
            if (isfirst){
                isfirst=false;
            }

            if ((middleWidth +horizonInterspace+ width) > ketWidth) {
                middleWidth = 5;
                middleVer++;
            }

            int leftMargin = middleWidth;
            int topMargin = (middleVer * ketHeight + interspace);

            textPath.setPoint(middleVer, leftMargin, topMargin);

            zoomViews.get(i).setMagin(textPath.leftpoint, textPath.toppoint);
            //增加绘制宽度
            middleWidth += width + horizonInterspace;
        }

    }


    private void addRelative(boolean is) {
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        relatives.add(layout);
        add_text.addView(layout);
        if (is) {
            horizontal = 5;
            vertical = 0;
            CurrentPage++;
        }
        if (mainData.size() != CurrentPage + 1) {
            mainData.add(new ZoomTextPageBeen(ketWidth,relative_room.getHeight(),ketHeight,interspace));
            mainData.get(CurrentPage).setPage(CurrentPage);
        }
        hideLayout();
    }

    private void hideLayout() {
        for (int i = 0; i < relatives.size(); i++) {
            if (CurrentPage != i) {
                relatives.get(i).setVisibility(View.GONE);
            } else {
                relatives.get(i).setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void endDown() {
        getHandler().removeMessages(10);
    }

    @Override
    public void startUp() {
        getHandler().sendEmptyMessageDelayed(10, 500);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.complete:
                onBackPressed();
                break;
            case R.id.delete:
                deleteText();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        SaveDrawDialog.getdialog(this, "", saveDrawListener);
    }
    /**
     * 保存绘制提示框
     */
    View.OnClickListener saveDrawListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SaveDrawDialog dialog = (SaveDrawDialog) v.getTag();
            dialog.dismiss();

            switch (v.getId()) {
                case R.id.qiut:
                    break;
                case R.id.insure:
                    getHandler().sendEmptyMessageDelayed(0, 200);
                    break;
                case R.id.discard:
                    getHandler().sendEmptyMessageDelayed(0, 200);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected int getMainContentViewId() {
        return R.layout.activity_text_zoom;
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {

    }

    @Override
    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }

    /**
     * 点击屏幕某个位置
     * @param x
     * @param eventy
     */
    @Override
    public void ZoomPoint(float x, float eventy) {
        int curVer=getPointVertical(eventy);
        if (curVer==-1){
            return;
        }

        //计算获得当前输入
        ClickPoint textPoint=getPointText(curVer,x);

        if (textPoint!=null){
            int position=textPoint.position;
            //获得文字光标显示的坐标点
            vertical=curVer;
            if (textPoint.isStart){
                currentPosition=textPoint.position;
                horizontal=mainData.get(CurrentPage).getPageTexts().get(position).leftpoint;
            }else{
                currentPosition=textPoint.position+1;
                horizontal=mainData.get(CurrentPage).getPageTexts().get(position).gettextDrawWid();
            }
            cursor.setMagin(horizontal-4,vertical*ketHeight);
        }

    }

    /**
     * 获得当前点击的排数
     * @param eventy
     * @return
     */
    private int getPointVertical(float eventy){
        float ver=  (eventy/ketHeight);
        int pintVer= (int) ver;
        if (((ver*1000)%1000)==0){
            pintVer--;
        }
        //获得最大排数
        int maxver=mainData.get(CurrentPage).getMaxVertical();
        if (maxver>=pintVer){
           return  pintVer;
        }
        return -1;
    }
    private ClickPoint getPointText(int ver,float eventX){
        ClickPoint clickPoint=null;
        int hr=0;
        for (TextPath been:mainData.get(CurrentPage).getPageTexts()){

            if (been.lineNumber==ver){
                if (eventX<been.gettextDrawWid()){
                    clickPoint=new ClickPoint();
                    clickPoint.position=hr;
                    if (eventX<(been.gettextDrawWid()/2)){
                        clickPoint.isStart=true;
                    }else{
                        clickPoint.isStart=false;
                    }
                   return clickPoint;
                }
            }
            hr++;
        }
        return clickPoint;
    }
}
