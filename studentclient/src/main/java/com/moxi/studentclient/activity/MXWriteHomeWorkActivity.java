//package com.moxi.studentclient.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.os.Bundle;
//import android.os.Message;
//import android.os.PersistableBundle;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.TextUtils;
//import android.text.style.ImageSpan;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.moxi.classRoom.information.UserInformation;
//import com.moxi.studentclient.Presenter.MXWriteHomeWork;
//import com.moxi.studentclient.R;
//import com.moxi.studentclient.cache.ACache;
//import com.moxi.studentclient.model.DBExamsModel;
//import com.moxi.studentclient.model.ExamsDetailsModel;
//import com.moxi.studentclient.utils.MxgsaTagHandler;
//import com.moxi.studentclient.view.PaintInvalidateRectView;
//import com.moxi.studentclient.view.SlideLinerlayout;
//import com.mx.mxbase.base.BaseActivity;
//import com.mx.mxbase.constant.Constant;
//import com.mx.mxbase.http.MXHttpHelper;
//import com.mx.mxbase.model.BaseModel;
//import com.mx.mxbase.utils.Base64Utils;
//import com.mx.mxbase.utils.DensityUtil;
//import com.mx.mxbase.utils.GsonTools;
//import com.mx.mxbase.utils.Log;
//import com.mx.mxbase.utils.MXUamManager;
//import com.mx.mxbase.utils.Toastor;
//import com.mx.mxbase.view.AlertDialog;
//
//import org.litepal.crud.DataSupport;
//
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import butterknife.Bind;
//
///**
// * Created by Archer on 16/10/13.
// */
//public class MXWriteHomeWorkActivity extends BaseActivity implements View.OnClickListener {
//
//    @Bind(R.id.tv_exams_title_view)
//    TextView tvExamsTitle;
//    @Bind(R.id.tv_exams_analysis_view)
//    TextView tvAnalysis;
//    @Bind(R.id.tv_chose_exams_title_view)
//    TextView tvChoseTitle;
//    @Bind(R.id.tv_chose_exams_analysis_view)
//    TextView tvChoseAnalysis;
//    @Bind(R.id.ll_base_back)
//    LinearLayout llBack;
//    @Bind(R.id.tv_base_back)
//    TextView tvBack;
//    @Bind(R.id.tv_base_mid_title)
//    TextView tvMidTitle;
//    @Bind(R.id.slide_liner_layout)
//    SlideLinerlayout slideLinerLayout;
//    @Bind(R.id.slide_chose_liner_layout)
//    SlideLinerlayout slideChoseLayout;
//    @Bind(R.id.pirv_home_work_achace)
//    PaintInvalidateRectView paintInvalidateRectView;
//    @Bind(R.id.img_home_work_left)
//    ImageView imgPageLeft;
//    @Bind(R.id.img_home_work_right)
//    ImageView imgPageRight;
//    @Bind(R.id.tv_home_work_page_count)
//    TextView tvPage;
//    @Bind(R.id.radio_group_write_home)
//    RadioGroup radioGroup;
//    @Bind(R.id.radio_answer_1)
//    RadioButton radioBtn1;
//    @Bind(R.id.radio_answer_2)
//    RadioButton radioBtn2;
//    @Bind(R.id.radio_answer_3)
//    RadioButton radioBtn3;
//    @Bind(R.id.radio_answer_4)
//    RadioButton radioBtn4;
//    @Bind(R.id.ll_base_right)
//    LinearLayout llRight;
//    @Bind(R.id.tv_base_right)
//    TextView tvRight;
//
//    private ExamsDetailsModel edm;
//    private int page = 0;
//    private String url;
//    private String cob_zj_id, cos_sem_id, cob_sub_id, cob_sub_name;
//
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        dialogShowOrHide(false, "");
//        if (msg.arg1 == 102) {
//            if (msg.what == Integer.parseInt(Constant.SUCCESS)) {
//                Toastor.showToast(this, "提交成功");
////                Intent intent = new Intent(this, MXErrorExamsActivity.class);
////                intent.putExtra("cob_exams_title", getIntent().getStringExtra("exams_details_title"));
////                intent.putExtra("cob_zj_id", cob_zj_id);
////                startActivity(intent);
//                this.finish();
//            } else {
//                Toastor.showToast(this, "提交失败，请重试！");
//            }
//        }
//    }
//
//    @Override
//    protected int getMainContentViewId() {
//        return R.layout.mx_activity_write_home_work;
//    }
//
//    @Override
//    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        new MXWriteHomeWork().getExamsDetails(this);
//
//        init();
//    }
//
//    @Override
//    public void onWindowFocusChanged(boolean hasFocus) {
//        super.onWindowFocusChanged(hasFocus);
//    }
//
//    /**
//     * 初始化视图
//     */
//    private void init() {
//        //获取上个界面传递过来的数据
//        String examsDetails;// = getIntent().getStringExtra("exams_details");
//        cob_zj_id = "21038";//getIntent().getStringExtra("cos_zj_id");//章节id
//        cos_sem_id = getIntent().getStringExtra("cos_sem_id");//学期id
//        cob_sub_id = getIntent().getStringExtra("cob_sub_id");//科目id
//        cob_sub_name = getIntent().getStringExtra("cob_sub_name");//科目名称
//
//        tvMidTitle.setText(getIntent().getStringExtra("exams_details_title"));
//
//        llBack.setVisibility(View.VISIBLE);
//        tvBack.setText("选择章节");
//        llRight.setVisibility(View.VISIBLE);
//        List<DBExamsModel> ddd = DataSupport.where("baocunid=?", cob_zj_id).find(DBExamsModel.class);
//        if (ddd.size() > 0) {
//            tvChoseAnalysis.setVisibility(View.VISIBLE);
//            tvAnalysis.setVisibility(View.VISIBLE);
//            tvRight.setText("查看结果");
//        } else {
//            tvChoseAnalysis.setVisibility(View.GONE);
//            tvAnalysis.setVisibility(View.GONE);
//            tvRight.setText("提交");
//        }
//        url = Constant.GET_SYNC_EXAMS + cob_zj_id;
//
//        llBack.setOnClickListener(this);
//        llRight.setOnClickListener(this);
//        imgPageLeft.setOnClickListener(this);
//        imgPageRight.setOnClickListener(this);
//        radioBtn1.setOnClickListener(this);
//        radioBtn2.setOnClickListener(this);
//        radioBtn3.setOnClickListener(this);
//        radioBtn4.setOnClickListener(this);
//
//        examsDetails= ACache.get(this).getAsString(url);
//
//        parseExamsDetails(page, examsDetails);
//    }
//
//    /**
//     * 解析同步练习试题
//     *
//     * @param page         当前页码
//     * @param examsDetails 试题json数据
//     */
//    private void parseExamsDetails(int page, String examsDetails) {
//        edm = GsonTools.getPerson(examsDetails, ExamsDetailsModel.class);
//        if (edm.getResult().get(page).getType() == 6) {
//            //TODO 选择题
//            slideChoseLayout.setVisibility(View.VISIBLE);
//            slideLinerLayout.setVisibility(View.GONE);
//            setTitle((page + 1) + "、" + edm.getResult().get(page).getTitle(), tvChoseTitle);
//            setTitle("\n\n解析：" + edm.getResult().get(page).getAnalysis(), tvChoseAnalysis);
//            resetRadioGroup(page, edm);
//        } else {
//            slideChoseLayout.setVisibility(View.GONE);
//            slideLinerLayout.setVisibility(View.VISIBLE);
//            setTitle((page + 1) + "、" + edm.getResult().get(page).getTitle(), tvExamsTitle);
//            setTitle("\n\n解析：" + edm.getResult().get(page).getAnalysis(), tvAnalysis);
//            int height = measureView(tvExamsTitle) + measureView(tvAnalysis);
//            RelativeLayout.LayoutParams da = (RelativeLayout.LayoutParams) paintInvalidateRectView.getLayoutParams();
//            if (height < DensityUtil.getScreenH(this)) {
//                height = DensityUtil.getScreenH(this);
//            }
//            da.height = height;
//            Log.e("当前绘制区域高度", "height" + height);
//            paintInvalidateRectView.setLayoutParams(da);
//            //TODO
//            String bitUrlCache = url + this.getIntent().getStringExtra("exams_details_title") + "_" + page;
//            if (ACache.get(this).getAsBitmap(bitUrlCache) != null) {
//                paintInvalidateRectView.initBitmap(ACache.get(this).getAsBitmap(bitUrlCache));
//            } else {
//                paintInvalidateRectView.initBitmap(null);
//            }
//        }
//        slideLinerLayout.moveToTop();
//        slideChoseLayout.moveToTop();
//        tvPage.setText((page + 1) + "/" + edm.getResult().size());
//    }
//
//    /**
//     * @param page
//     * @param edm
//     */
//    private void saveDataBaseChose(int page, ExamsDetailsModel edm) {
//        DBExamsModel db = new DBExamsModel();
//        db.setExamsDetails(GsonTools.obj2json(edm));
//        db.setSubjectId(cob_sub_id);//课程id
//        db.setBaocunid(cob_zj_id);//章节id
//        db.setCos_sem_id(cos_sem_id);//年级id
//        db.setSubjectName(cob_sub_name);//课程名称
////        db.setExamsTitle(getIntent().getStringExtra("exams_details_title"));
//        List<DBExamsModel> ddd = DataSupport.where("baocunid=?", cob_zj_id).find(DBExamsModel.class);
//        if (ddd.size() > 0) {
//            submitTongbu();
//        } else {
//            db.saveThrows();
//            submitTongbu();
//        }
//    }
//
//    /**
//     * 重新设置radioGroup
//     *
//     * @param page 题号
//     * @param edm  数据对象
//     */
//    private void resetRadioGroup(int page, ExamsDetailsModel edm) {
//        if (TextUtils.isEmpty(edm.getResult().get(page).getResult())) {
//            radioGroup.clearCheck();
//        } else {
//            switch (edm.getResult().get(page).getResult()) {
//                case "A":
//                    radioBtn1.setChecked(true);
//                    break;
//                case "B":
//                    radioBtn2.setChecked(true);
//                    break;
//                case "C":
//                    radioBtn3.setChecked(true);
//                    break;
//                case "D":
//                    radioBtn4.setChecked(true);
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 让radiogroup失去点击效果
//     *
//     * @param testRadioGroup 目标radiogroup
//     */
//    public void disableRadioGroup(RadioGroup testRadioGroup) {
//        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
//            testRadioGroup.getChildAt(i).setEnabled(false);
//            testRadioGroup.getChildAt(i).setClickable(false);
//        }
//    }
//
//    /**
//     * @param title
//     * @param view
//     */
//    private void setTitle(String title, TextView view) {
//        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"](\\s*)(data:image/)\\S+(base64,)([^'\"]+)['\"][^>]*>");
//        Matcher m = p.matcher(title);
//        while (m.find()) {
//            String str = m.group(4);
//            title = title.replace(m.group(), "#@M#@X@" + str + "#@M#@X@");
//        }
//        String titleResult = title;
//        if (titleResult.indexOf("#@M#@X@") > 0) {
//            String[] s = titleResult.split("#@M#@X@");
//            view.setText("");
//            for (int j = 0; j < s.length; j++) {
//                if (j % 2 == 0) {
//                    view.append(Html.fromHtml(s[j]));
//                } else {
//                    Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
//                    ImageSpan imgSpan = new ImageSpan(this, bitmap);
//                    SpannableString spanString = new SpannableString("icon");
//                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    view.append(spanString);
//                }
//            }
//        } else {
//            view.setText(Html.fromHtml(title, null, new MxgsaTagHandler(this)));
//        }
//    }
//
//    @Override
//    public void onActivityStarted(Activity activity) {
//
//    }
//
//    @Override
//    public void onActivityResumed(Activity activity) {
//        if (UserInformation.getInstance().getID() == -1 || UserInformation.getInstance().getUserInformation() == null) {
//            finish();
//        }
//    }
//
//    @Override
//    public void onActivityPaused(Activity activity) {
//
//    }
//
//    @Override
//    public void onActivityStopped(Activity activity) {
//        dialogShowOrHide(false, "");
//    }
//
//    @Override
//    public void onActivitySaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//
//    }
//
//    @Override
//    public void onActivityRestoreInstanceState(Bundle savedInstanceState) {
//
//    }
//
//    @Override
//    public void onActivityDestroyed(Activity activity) {
//
//    }
//
//    @Override
//    public void onClick(View view) {
//        long currentTime = Calendar.getInstance().getTimeInMillis();
//        switch (view.getId()) {
//            case R.id.ll_base_back:
//                this.finish();
//                break;
//            case R.id.img_home_work_left:
//                if (edm.getResult().get(page).getType() != 6) {
//                    saveBitMap();
//                }
//                if (page > 0) {
//                    page--;
//                    parseExamsDetails(page, ACache.get(this).getAsString(url));
//                }
//                break;
//            case R.id.img_home_work_right:
//                if (edm.getResult().get(page).getType() != 6) {
//                    saveBitMap();
//                }
//                if (page < edm.getResult().size() - 1) {
//                    page++;
//                    parseExamsDetails(page, ACache.get(this).getAsString(url));
//                }
//                break;
//            //提交
//            case R.id.ll_base_right:
//                if (tvRight.getText().toString().equals("提交")) {
//                    new AlertDialog(this).builder().setTitle("提示").setMsg("确认提交?").setCancelable(false).setNegativeButton("确定", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            saveDataBaseChose(page, edm);
//                        }
//                    }).setPositiveButton("取消", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                        }
//                    }).show();
//                } else {
////                    Intent intent = new Intent();
////                    intent.setClass(this, MXErrorExamsActivity.class);
////                    intent.putExtra("cob_zj_id", cob_zj_id);
////                    intent.putExtra("cob_exams_title", getIntent().getStringExtra("exams_details_title"));
////                    startActivity(intent);
//                }
//                break;
//            case R.id.radio_answer_1:
//                updateAnswer(page, "A");
//                break;
//            case R.id.radio_answer_2:
//                updateAnswer(page, "B");
//                break;
//            case R.id.radio_answer_3:
//                updateAnswer(page, "C");
//                break;
//            case R.id.radio_answer_4:
//                updateAnswer(page, "D");
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 提交同步练习
//     */
//    private void submitTongbu() {
//        HashMap<String, String> sub = new HashMap<>();
//        sub.put("semId", cos_sem_id);
//        sub.put("cchId", cob_zj_id);
//        sub.put("appSession", MXUamManager.queryUser(this));
//        MXHttpHelper.getInstance(this).postStringBack(102, Constant.SUBMIT_TB_RESULT, sub, getHandler(), BaseModel.class);
//    }
//
//    /**
//     * 更新缓存结果
//     *
//     * @param page
//     * @param d
//     */
//    private void updateAnswer(int page, String d) {
//        edm.getResult().get(page).setResult(d);
//        ACache.get(this).put(url, GsonTools.obj2json(edm));
//    }
//
//    /**
//     * 替代getDrawingCache方法
//     *
//     * @param v
//     * @return
//     */
//    private Bitmap loadBitmapFromView(View v) {
//        if (v == null) {
//            return null;
//        }
//        Bitmap screenshot;
//        screenshot = Bitmap.createBitmap(v.getWidth(), v.getMeasuredHeight(), Bitmap.Config.RGB_565);
//        Canvas c = new Canvas(screenshot);
//        c.translate(-v.getScrollX(), -v.getScrollY());
//        v.draw(c);
//        return screenshot;
//    }
//
//    /**
//     * 保存缓存图片方便上传图片的时候读取
//     */
//    private void saveBitMap() {
//        String fileId = this.getIntent().getStringExtra("exams_details_title");
////        String strPath = "/mxAcache/" + fileId + "/" + page + ".png";
////        Bitmap bitmap1 = paintInvalidateRectView.getBitmap();
////        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
////            File sdCardDir = Environment.getExternalStorageDirectory();
////            FileOutputStream fos = null;
////            try {
////                File fileBitmap = new File(sdCardDir, strPath);
////                if (!fileBitmap.getParentFile().exists()) {
////                    fileBitmap.getParentFile().mkdirs();
////                }
////                fos = new FileOutputStream(fileBitmap);
////                //当指定压缩格式为PNG时保存下来的图片显示正常
////                bitmap1.compress(Bitmap.CompressFormat.PNG, 100, fos);
////                fos.flush();
////            } catch (Exception e) {
////                e.printStackTrace();
////            } finally {
////                try {
////                    fos.close();
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////            }
////        }
//        String daaa = url + fileId + "_" + page;
//        ACache.get(this).put(daaa, paintInvalidateRectView.getBitmap());
////        slideLinerLayout.setDrawingCacheEnabled(true);
//    }
//
//    /**
//     * 计算view的高度
//     *
//     * @param child
//     * @return
//     */
//    private int measureView(View child) {
//        ViewGroup.LayoutParams lp = child.getLayoutParams();
//        if (lp == null) {
//            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        }
//        int childMeasureWidth = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
//        int childMeasureHeight;
//        if (lp.height > 0) {
//            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(lp.height, View.MeasureSpec.EXACTLY);
//        } else {
//            childMeasureHeight = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);//未指定
//        }
//        child.measure(childMeasureWidth, childMeasureHeight);
//        int aaa = child.getMeasuredHeight();
//        return aaa;
//    }
//}
