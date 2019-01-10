package com.moxi.CPortTeacher.frament.WhiteBoard;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.config.OttoCode;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.OttoBeen;
import com.moxi.CPortTeacher.view.PaintInvalidateRectView;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.HttpUpFileRequest;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.utils.FileSaveASY;
import com.mx.mxbase.utils.StringUtils;
import com.squareup.otto.Subscribe;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 白板区域fragment
 * Created by Administrator on 2016/10/28.
 */
public class WhiteBoardFragment extends CportBaseFragment implements HttpUpFileRequest.UpFileCallBack {
    public final static String code="WhiteBoardFragment";
    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;

    @Bind(R.id.send_white_area)
    TextView send_white_area;

    @Bind(R.id.mian_white_area_layout)
    RelativeLayout mian_white_area_layout;
    private int Viewindex = -1;
    private String saveFilePath = StringUtils.getSDPath() + "save.png";

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_white_area;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        send_white_area.setOnClickListener(this);
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        AddPaintView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.white_area_add_page:
                AddPaintView();
                getView(Viewindex).onResume();
                getView(Viewindex).onPause();
                break;
            case R.id.send_white_area:
                dialogShowOrHide(true, "白板提交中...");
                Bitmap bitmap=PrintScreen();
                if (bitmap==null){
                    com.mx.mxbase.utils.ToastUtils.getInstance().showToastShort("未有绘制内容");
                    return;
                }
                new FileSaveASY(bitmap, saveFilePath, new FileSaveASY.saveSucess() {
                    @Override
                    public void onSaveSucess(boolean is) {
                        if (is) {
                                sendWhiteBorad();
                        }else {
                            dialogShowOrHide(false, "白板提交中...");
                            StringUtils.deleteFile(saveFilePath);
                            ToastUtils.getInstance().showToastShort("图片获取失败！请重试");
                        }
                    }
                }).execute();
                break;
            case R.id.last_page:
                moveLeft();
                break;
            case R.id.next_page:
                moveRight();
                break;
            default:
                break;
        }
    }

    /**
     * 发送图片
     */
    private void sendWhiteBorad(){
        OkHttpUtils.post().url(Connector.getInstance().boardSend).addParams("userId", String.valueOf(UserInformation.getInstance().getID())).addFile("file", "save.png", new File(saveFilePath)).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.getInstance().showToastShort("提交失败");
                dialogShowOrHide(false,"");
                StringUtils.deleteFile(saveFilePath);
            }

            @Override
            public void onResponse(String response, int id) {
                ToastUtils.getInstance().showToastShort("下发完成");
                dialogShowOrHide(false,"");
                StringUtils.deleteFile(saveFilePath);
            }
        });
    }
    /**
     * 截屏
     */
    public Bitmap PrintScreen() {
        //获得截屏图片
        return getView(Viewindex).CurrentBitmap();
    }

    @Override
    public void moveLeft() {
        if (Viewindex > 0) {
            Viewindex--;
            showView();
        }
    }

    @Override
    public void moveRight() {
        if (Viewindex < mian_white_area_layout.getChildCount() - 1) {
            Viewindex++;
            showView();
        }
    }

    private PaintInvalidateRectView newPaintView() {
        PaintInvalidateRectView view = new PaintInvalidateRectView(context,WhiteBoardFragment.code);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);
        return view;
    }

    public void AddPaintView() {
        if (mian_white_area_layout.getChildCount() >= 5) {
            ToastUtils.getInstance().showToastShort("最多创建5张白板！");
            return;
        }
        mian_white_area_layout.addView(newPaintView());
        Viewindex++;
        showView();
    }

    private void showView() {

        setPrevent();
//        getView(Viewindex).setPreventClearScreen(false);
        getView(Viewindex).setVisibility(View.VISIBLE);
        show_index.setText(String.valueOf(Viewindex + 1) + "/" + mian_white_area_layout.getChildCount());
    }

    /**
     * 阻止清屏
     */
    private void setPrevent() {
        for (int i = 0; i < mian_white_area_layout.getChildCount(); i++) {
//            getView(i).setPreventClearScreen(true);
            getView(i).setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 获得指定坐标的view
     *
     * @param index
     * @return
     */
    private PaintInvalidateRectView getView(int index) {
        return ((PaintInvalidateRectView) mian_white_area_layout.getChildAt(index));
    }

    @Override
    public void UpUnderway(String code, int progress) {

    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
//        StringUtils.deleteFile(saveFilePath);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
//        StringUtils.deleteFile(saveFilePath);
    }
    @Override
    public void onResume() {
        super.onResume();
            if (mian_white_area_layout.getChildCount() > 0) {
                getView(Viewindex).onResume();
                getHandler().sendEmptyMessageDelayed(1,50);
            }
    }
    @Subscribe
    public void resume(OttoBeen been){
        if (isfinish||this.isHidden())return;
        if (been.code.equals(OttoCode.RESUME)){
            getView(Viewindex).onResume();
            getView(Viewindex).onPause();
//            getHandler().sendEmptyMessageDelayed(1,50);
        }else if (been.code.equals(OttoCode.STOP)){
            getView(Viewindex).onstop();
        }
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what){
            case 1:
                getView(Viewindex).onPause();
                break;
            default:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mian_white_area_layout.getChildCount()>0){
            getView(Viewindex).onstop();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mian_white_area_layout.getChildCount()>0){
            getView(Viewindex).onPause();
        }
    }
}