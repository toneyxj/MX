package com.mx.teacher.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.PersistableBundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.mxbase.base.BaseActivity;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.ImageLoaderManager;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;
import com.mx.teacher.R;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.constant.TestConstant;
import com.mx.teacher.entity.Test;
import com.mx.teacher.util.DialogOnClickListener;
import com.mx.teacher.util.MxgsaTagHandler;
import com.mx.teacher.view.AutoAdaptTextView;
import com.mx.teacher.view.CheckReViewDialog;
import com.mx.teacher.view.PaintInvalidateRectView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import okhttp3.Call;

/**
 * 主观题显示UI视图
 * Created by Archer on 16/9/20.
 */
public class MXSubjectiveActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.ll_base_back)
    LinearLayout llBack;
    @Bind(R.id.tv_base_back)
    TextView tvBaseBack;
    @Bind(R.id.tv_base_mid_title)
    TextView tvMidTitle;
    @Bind(R.id.img_home_work_left)
    ImageView imgLeft;
    @Bind(R.id.tv_base_right)
    TextView tvBaseRight;
    @Bind(R.id.ll_base_right)
    LinearLayout llBaseRight;
    @Bind(R.id.img_home_work_right)
    ImageView imgRight;
    @Bind(R.id.tv_home_work_subjective_title)
    AutoAdaptTextView tvHWTitle;
    @Bind(R.id.rl_shot_screen)
    RelativeLayout rlShotScreen;
    @Bind(R.id.img_home_work_done)
    ImageView imgDone;
    @Bind(R.id.pirv_home_work_achace)
    PaintInvalidateRectView paintInvalidateRectView;
    @Bind(R.id.selet_paint)
    RadioGroup selet_paint;
    @Bind(R.id.rubber_draft)
    ImageView rubber_draft;
    @Bind(R.id.ll_submit_view)
    LinearLayout llSubmitView;
    @Bind(R.id.draft_show)
    TextView tvSubmit;
    @Bind(R.id.img_zhan_wei)
    ImageView imgZw;

    private String homeWork = "";
    private Test test;
    private int state;
    private String url;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.arg1 == 100) {
            if (msg.what == 0) {
                Toastor.showToast(this, "提交成功");
            } else {
                Toastor.showToast(this, "提交失败");
            }
        }
    }

    @Override
    protected int getMainContentViewId() {
        //获取课后作业数据
        homeWork = this.getIntent().getStringExtra("home_work_json");
        state = this.getIntent().getIntExtra("home_work_state", -3);
        url = this.getIntent().getStringExtra("home_work_url");
        return R.layout.mx_activity_subjective;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        //初始化view
        llBack.setVisibility(View.VISIBLE);
        tvBaseBack.setText("课后作业");
        tvMidTitle.setText("重庆市初三下学期其中测试");
        rlShotScreen.setDrawingCacheEnabled(true);
        llBaseRight.setVisibility(View.VISIBLE);
        tvBaseRight.setText("批阅");

        //设置点击事件监听
        llBack.setOnClickListener(this);
        imgLeft.setOnClickListener(this);
        imgRight.setOnClickListener(this);
        llBaseRight.setOnClickListener(this);
        selet_paint.setOnCheckedChangeListener(this);
        rubber_draft.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        parseHomeWorkJson(homeWork);
    }

    /**
     * 解析数据
     *
     * @param homeWork
     */
    private void parseHomeWorkJson(String homeWork) {
        test = GsonTools.getPerson(homeWork, Test.class);
        if (state != -1) {
            if (state == 0) {//等待老师批复
                if (TestConstant.IS_TEACHER) {
                    tvBaseRight.setVisibility(View.VISIBLE);
                    tvBaseRight.setText("现在批复");
                } else {
                    tvBaseRight.setVisibility(View.GONE);
                }
            } else {//老师已批复
                tvBaseRight.setText("查看批复");
            }
            rlShotScreen.setVisibility(View.GONE);
            imgDone.setVisibility(View.VISIBLE);
            String url = TestConstant.HOME_WORK_SUBJECTIVE_IMG.replace("?", test.getResult().getExamList().get(6).getHowId() + "");
            llSubmitView.setVisibility(View.GONE);
            imgZw.setVisibility(View.VISIBLE);
            ImageLoaderManager.getInstance().loadImageUrl(imgDone, url);
        } else if (ACache.get(this).getAsBitmap(url + "subjective") != null) {
            rlShotScreen.setVisibility(View.VISIBLE);
            paintInvalidateRectView.setBitmap(ACache.get(this).getAsBitmap(url + "subjective"));
            imgDone.setVisibility(View.GONE);
            tvBaseRight.setVisibility(View.GONE);
            tvHWTitle.setText(Html.fromHtml(test.getResult().getExamList().get(6).getTitle(), null, new MxgsaTagHandler(this)));
        } else {
            rlShotScreen.setVisibility(View.VISIBLE);
            imgDone.setVisibility(View.GONE);
            tvBaseRight.setVisibility(View.GONE);
            tvHWTitle.setText(Html.fromHtml(test.getResult().getExamList().get(6).getTitle(), null, new MxgsaTagHandler(this)));
        }
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
        Bitmap bitmap = rlShotScreen.getDrawingCache();
        ACache.get(this).put(url + "subjective", bitmap);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            this.setResult(-1);
            this.finish();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_PAGE_UP) {
            //上一页
            this.setResult(0);
            this.finish();
            return true;
        } else if ( keyCode == KeyEvent.KEYCODE_PAGE_DOWN) {
            //下一页
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_base_back:
                this.setResult(-1);
                this.finish();
                break;
            case R.id.img_home_work_left:
                this.setResult(0);
                this.finish();
                break;
            case R.id.img_home_work_right:
                break;
            case R.id.ll_base_right:
                if (state == 0) {
                    new CheckReViewDialog(this).builder().setTitle("批复内容").setNegativeButton("确定", new DialogOnClickListener() {
                        @Override
                        public void onclick(View view, String content) {
                            dialogShowOrHide(true, "正在提交...");
                            String checkUrl = TestConstant.TEACHER_CHECK_HOME_WORK.replace("?", test.getResult().getExamList().get(6).getHowId() + "");
                            OkHttpUtils.post().url(checkUrl).addParams("comment", content).build().execute(new StringCallback() {

                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    dialogShowOrHide(false, "正在提交...");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    dialogShowOrHide(false, "正在提交...");
                                    ACache.get(MXSubjectiveActivity.this).put(url, "");
                                    Toastor.showToast(getBaseContext(), "批复成功");
                                    MXSubjectiveActivity.this.setResult(-1);
                                    MXSubjectiveActivity.this.finish();
                                }
                            });
                        }
                    }).setPositiveButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    }).show();
                } else {
                    Intent intent = new Intent(this, ReplyDetailsActivity.class);
                    intent.putExtra("home_work_json", homeWork);
                    startActivity(intent);
                }
                break;
            case R.id.draft_show:
                new AlertDialog(MXSubjectiveActivity.this).builder().setTitle("提示").setMsg("提交后将无法修改，确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        submitHomeWork();
                    }
                }).setPositiveButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                }).show();
                break;
            case R.id.rubber_draft:
                paintInvalidateRectView.setPaint(4);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (checkedId == R.id.bottom_1) {// 笔触最小
            paintInvalidateRectView.setPaint(0);
        } else if (checkedId == R.id.bottom_2) {//笔触中等
            paintInvalidateRectView.setPaint(1);
        } else if (checkedId == R.id.bottom_3) {// 笔触最大
            paintInvalidateRectView.setPaint(2);
        }
    }

    //提交课后作业
    private void submitHomeWork() {
        String strPath = "/mxSaveView/" + test.getResult().getExamList().get(6).getHowId() + ".png";
        Bitmap bitmap = rlShotScreen.getDrawingCache();
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File sdCardDir = Environment.getExternalStorageDirectory();
            FileOutputStream fos = null;
            try {
                File file = new File(sdCardDir, strPath);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                fos = new FileOutputStream(file);
                //当指定压缩格式为PNG时保存下来的图片显示正常
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        File imgFile = new File(Environment.getExternalStorageDirectory(), strPath);
        if (imgFile != null) {
            String sum = "";
            for (int i = 0; i < 6; i++) {
                sum += test.getResult().getExamList().get(i).getResult() + ",";
            }
            String url = TestConstant.SUBMIT_ANSWER_BY_ID.replace("?", test.getResult().getExamList().get(6).getHowId() + "");
            OkHttpUtils.post().url(url).addParams("choiceAnswer", sum).addFile("file", "/mxSaveView/", imgFile).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    Toastor.showToast(MXSubjectiveActivity.this, "请求失败，请重试");
                }

                @Override
                public void onResponse(String response, int id) {
                    Toastor.showToast(MXSubjectiveActivity.this, "提交成功");
                }
            });
        }
    }
}
