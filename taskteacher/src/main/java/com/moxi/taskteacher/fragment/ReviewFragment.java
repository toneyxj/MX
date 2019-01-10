package com.moxi.taskteacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.adapter.ReviewWorkAdapter;
import com.moxi.taskteacher.httpconstans.TeaContsans;
import com.moxi.taskteacher.model.ReviewWorkList;
import com.moxi.taskteacher.request.HttpPostRequest;
import com.moxi.taskteacher.request.RequestCallBack;
import com.moxi.taskteacher.request.ReuestKeyValues;
import com.moxi.taskteacher.teainterface.ItemClickListener;
import com.moxi.taskteacher.utils.GetTeacherUserId;
import com.moxi.taskteacher.view.ImageViewProgressArc;
import com.moxi.taskteacher.view.PaintInvalidateRectView;
import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.FileSaveASY;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Log;
import com.mx.mxbase.utils.StringUtils;
import com.mx.mxbase.utils.Toastor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

/**
 * Created by Archer on 2016/12/22.
 */
public class ReviewFragment extends BaseFragment implements View.OnClickListener, RequestCallBack {
    private RecyclerView recyclerReview;
    private LinearLayout llReview;
    private ImageViewProgressArc imageTitle, imageAnswer, imageReview;
    private TextView tvToReview;
    private PaintInvalidateRectView pirReview;
    private LinearLayout llEditReview, llReviewSend;
    private ReviewWorkList.ReviewWork.Review currentReview;
    private TextView tvSend;
    private TextView tvCount;
    private LinearLayout llRight;
    private LinearLayout llLeft;
    private TextView tvTryAgain;

    private int page = 0;
    private String allReviewListResponce = "";
    private List<List<ReviewWorkList.ReviewWork.Review>> listReview = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_review, container, false);
        //初始化view的各控件
        isPrepared = true;
        recyclerReview = (RecyclerView) view.findViewById(R.id.home_work_send);
        llReview = (LinearLayout) view.findViewById(R.id.ll_review_see);
        imageTitle = (ImageViewProgressArc) view.findViewById(R.id.work_title);
        imageAnswer = (ImageViewProgressArc) view.findViewById(R.id.work_answer);
        tvToReview = (TextView) view.findViewById(R.id.tv_to_review);
        imageReview = (ImageViewProgressArc) view.findViewById(R.id.image_review);
        pirReview = (PaintInvalidateRectView) view.findViewById(R.id.pir_review);
        llEditReview = (LinearLayout) view.findViewById(R.id.ll_review_edit_text);
        llReviewSend = (LinearLayout) view.findViewById(R.id.ll_review_send);
        tvSend = (TextView) view.findViewById(R.id.tv_review_send);
        tvCount = (TextView) view.findViewById(R.id.tv_one_page_count);
        llRight = (LinearLayout) view.findViewById(R.id.ll_one_page_right);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_one_page_left);
        tvTryAgain = (TextView) view.findViewById(R.id.no_data_try_again);

        tvToReview.setOnClickListener(this);
        llReviewSend.setOnClickListener(this);
        llRight.setOnClickListener(this);
        llLeft.setOnClickListener(this);
        tvTryAgain.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    protected void loadData() {
        if (!isPrepared || !isVisible()) {
            return;
        }
        getAllReview();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 获取批复列表
     */
    private void getAllReview() {
        String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
        if (teaId.equals("")) {
            Toastor.showToast(getActivity(), "检测到未登录，请先登录");
            return;
        }
        recyclerReview.setVisibility(View.VISIBLE);
        llReview.setVisibility(View.GONE);
        llEditReview.setVisibility(View.GONE);
        List<ReuestKeyValues> review = new ArrayList<>();
        review.add(new ReuestKeyValues("userId", teaId));
        review.add(new ReuestKeyValues("page", "1"));
        review.add(new ReuestKeyValues("rows", "1000"));
        recyclerReview.setLayoutManager(new LinearLayoutManager(getActivity()));
        dialogShowOrHide(true, "加载中请稍后...");
        new HttpPostRequest(getActivity(), this, review, "1002", TeaContsans.GET_REVIEW_WORK, true).execute();
    }

    /**
     * 解析批复作业数据
     *
     * @param result
     */
    private void parseReviewWork(String result) {
        final ReviewWorkList workList = GsonTools.getPerson(result, ReviewWorkList.class);
        if (workList != null) {
            listReview.clear();
            tvTryAgain.setVisibility(View.GONE);
            listReview = ListUtils.splitList(workList.getResult().getList(), 8);
            showReviewByPage(page);
        } else {
            tvTryAgain.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据页码显示
     *
     * @param page
     */
    private void showReviewByPage(final int page) {
        ReviewWorkAdapter reviewAdapter = new ReviewWorkAdapter(getActivity(), listReview, page);
        recyclerReview.setAdapter(reviewAdapter);
        int temp = listReview.size();
        if (temp > 0) {
            tvCount.setText((page + 1) + "/" + temp);
        } else {
            tvCount.setText(page + "/" + temp);
        }
        reviewAdapter.setOnItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View view, int position) {
                if (NetWorkUtil.isNetworkAvailable(getActivity())) {
                    showReviewDetails(listReview.get(page).get(position));
                } else {
                    Toastor.showToast(getActivity(), "请检测网络是否可用");
                }
            }
        });
    }

    /**
     * 显示批复详情
     *
     * @param review
     */
    private void showReviewDetails(ReviewWorkList.ReviewWork.Review review) {
        currentReview = review;
        recyclerReview.setVisibility(View.GONE);
        llReview.setVisibility(View.VISIBLE);
        llEditReview.setVisibility(View.GONE);
        imageTitle.loadImage(TeaContsans.TITLE_IMAGE_PATH + review.getFile());
        imageAnswer.loadImage(TeaContsans.ANSWER_IMAGE_PATH + review.getAnswerfile());
        if (review.getReplyFile().equals("")) {
            tvToReview.setText("批复");
        } else {
            tvToReview.setText("查看批复");
        }
    }

    @Override
    public void onSuccess(String result, String code) {
        dialogShowOrHide(false, "加载中请稍后...");
        switch (code) {
            case "1002":
                allReviewListResponce = result;
                parseReviewWork(result);
                break;
            default:
                break;
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        dialogShowOrHide(false, "加载中请稍后...");
        tvTryAgain.setVisibility(View.VISIBLE);
        switch (code) {
            case "1002":
                if (!allReviewListResponce.equals("")) {
                    parseReviewWork(allReviewListResponce);
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_to_review:
                if (NetWorkUtil.isNetworkAvailable(getActivity())) {
                    if (tvToReview.getText().toString().equals("批复")) {
                        recyclerReview.setVisibility(View.GONE);
                        llReview.setVisibility(View.GONE);
                        llEditReview.setVisibility(View.VISIBLE);
                        imageReview.setVisibility(View.GONE);
                        pirReview.setVisibility(View.VISIBLE);
                        if (currentReview.getReplyFile().equals("")) {
                            pirReview.clearScreen();
                        }
                        tvSend.setVisibility(View.VISIBLE);
                    } else {
                        recyclerReview.setVisibility(View.GONE);
                        llReview.setVisibility(View.GONE);
                        llEditReview.setVisibility(View.VISIBLE);
                        pirReview.setVisibility(View.GONE);
                        imageReview.setVisibility(View.VISIBLE);
                        imageReview.loadImage(TeaContsans.TEACHER_REVIEW_PATH + currentReview.getReplyFile());
                        tvSend.setVisibility(View.GONE);
                    }
                } else {
                    Toastor.showToast(getActivity(), "请检查网络是否可用");
                }
                break;
            case R.id.ll_review_send:
                final String savePath = StringUtils.getSDPath() + "review.png";
                dialogShowOrHide(true, "批复提交中");
                new FileSaveASY(pirReview.CurrentBitmap(), savePath, new FileSaveASY.saveSucess() {
                    @Override
                    public void onSaveSucess(boolean is) {
                        if (!is) {
                            dialogShowOrHide(false, "");
                            Toastor.showToast(getActivity(), "保存图片失败");
                            return;
                        } else {
                            OkHttpUtils.post().url(TeaContsans.REVIEW_HOME_WORK)
                                    .addParams("id", currentReview.getId() + "").
                                    addFile("file", "review.png", new File(savePath)).build().execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    StringUtils.deleteFile(savePath);
                                    dialogShowOrHide(false, "");
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    StringUtils.deleteFile(savePath);
                                    Log.e("onResponse", response);
                                    dialogShowOrHide(false, "");
                                    try {
                                        JSONObject object = new JSONObject(response);
                                        int code = object.getInt("code");
                                        if (code == 0) {
                                            getAllReview();
                                        } else {
                                            Toastor.showToast(getActivity(), "发送批复失败，请重试");
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    }
                }).execute();
                break;
            case R.id.ll_one_page_left:
                if (page > 0) {
                    page--;
                    showReviewByPage(page);
                }
                break;
            case R.id.ll_one_page_right:
                if (page < listReview.size() - 1) {
                    page++;
                    showReviewByPage(page);
                }
                break;
            case R.id.no_data_try_again:
                getAllReview();
                break;
            default:
                break;
        }
    }

    /**
     * 返回监听
     */
    public void onKeyPressDown() {
        if (recyclerReview.getVisibility() == View.VISIBLE) {
            getActivity().finish();
        } else if (llReview.getVisibility() == View.VISIBLE) {
            getAllReview();
        } else if (llEditReview.getVisibility() == View.VISIBLE) {
            showReviewDetails(currentReview);
        }
    }

    public void onPageUp() {
        if (page > 0) {
            page--;
            showReviewByPage(page);
        }
    }

    public void onPageDown() {
        if (page < listReview.size() - 1) {
            page++;
            showReviewByPage(page);
        }
    }
}
