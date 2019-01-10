package com.moxi.taskteacher.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.adapter.SendHomeAdapter;
import com.moxi.taskteacher.adapter.StudentAdapter;
import com.moxi.taskteacher.cache.ACache;
import com.moxi.taskteacher.httpconstans.TeaContsans;
import com.moxi.taskteacher.model.HomeWork;
import com.moxi.taskteacher.model.StudentModel;
import com.moxi.taskteacher.request.HttpPostRequest;
import com.moxi.taskteacher.request.RequestCallBack;
import com.moxi.taskteacher.request.ReuestKeyValues;
import com.moxi.taskteacher.teainterface.ItemClickListener;
import com.moxi.taskteacher.utils.GetTeacherUserId;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Toastor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Archer on 2016/12/22.
 */
public class SendWorkFragment extends BaseFragment implements View.OnClickListener, RequestCallBack {

    private RecyclerView recyclerSend;
    private SendHomeAdapter adapter;
    private StudentAdapter stuAdapter;
    private LinearLayout llAllStu;
    private RecyclerView recyclerStus;
    private List<String> listChose = new ArrayList<>();

    private TextView tvChoseSend, tvAllSend;
    private TextView tvCount;
    private int page = 0, stuPage = 0;
    private LinearLayout llRight, llLeft;
    private StudentModel studentModel;
    private HomeWork.HomeWorkModel.WorkModel current;
    private TextView tvNumber;
    private TextView tvNoData;
    private int step;

    private LinearLayout llStuLast, llStuNext;
    private TextView tvStuPage;

    private List<List<HomeWork.HomeWorkModel.WorkModel>> listValue = new ArrayList<>();
    private List<List<StudentModel.StudentWeed>> listStu = new ArrayList<>();
    private String sendWorkResponce = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tea_fragment_send_work, container, false);
        //初始化view的各控件
        isPrepared = true;
        recyclerSend = (RecyclerView) view.findViewById(R.id.home_work_send);
        llAllStu = (LinearLayout) view.findViewById(R.id.ll_all_student);
        recyclerStus = (RecyclerView) view.findViewById(R.id.recycler_fragment_all_students);
        tvChoseSend = (TextView) view.findViewById(R.id.tv_two_btn_second);
        tvAllSend = (TextView) view.findViewById(R.id.tv_two_btn_first);
        tvCount = (TextView) view.findViewById(R.id.tv_one_page_count);
        llLeft = (LinearLayout) view.findViewById(R.id.ll_one_page_left);
        llRight = (LinearLayout) view.findViewById(R.id.ll_one_page_right);
        tvNumber = (TextView) view.findViewById(R.id.tv_fragment_weed_out_student);
        tvNoData = (TextView) view.findViewById(R.id.no_data_try_again);
        llStuLast = (LinearLayout) view.findViewById(R.id.ll_stu_page_last);
        llStuNext = (LinearLayout) view.findViewById(R.id.ll_stu_page_next);
        tvStuPage = (TextView) view.findViewById(R.id.tv_stu_page_index);

        tvAllSend.setOnClickListener(this);
        tvChoseSend.setOnClickListener(this);
        llLeft.setOnClickListener(this);
        llRight.setOnClickListener(this);
        tvNoData.setOnClickListener(this);
        llStuLast.setOnClickListener(this);
        llStuNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getAllHomeWork();
        }
    }

    @Override
    protected void loadData() {
        if (!isPrepared || !isVisible()) {
            return;
        }
        getAllHomeWork();
    }

    /**
     * 获取可发送的所有作业
     */
    private void getAllHomeWork() {
        step = 0;
        String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
        if (teaId.equals("")) {
            Toastor.showToast(getActivity(), "检测到未登录，请先登录");
            return;
        }
        llAllStu.setVisibility(View.GONE);
        recyclerSend.setVisibility(View.VISIBLE);
        List<ReuestKeyValues> valuePairs = new ArrayList<>();
        valuePairs.add(new ReuestKeyValues("userId", teaId));
        valuePairs.add(new ReuestKeyValues("page", "1"));
        valuePairs.add(new ReuestKeyValues("rows", "1000"));
        recyclerSend.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        dialogShowOrHide(true, "加载中请稍后...");
        new HttpPostRequest(getActivity(), this, valuePairs, "1001", TeaContsans.GET_ALL_WORK, true).execute();
    }

    /**
     * 解析返回结果
     *
     * @param result 返回数据
     */
    private void parseHomeworkResult(String result) {
        HomeWork homeWork = GsonTools.getPerson(result, HomeWork.class);
        if (homeWork != null) {
            listValue = ListUtils.splitList(homeWork.getResult().getList(), 12);
            adapter = new SendHomeAdapter(getActivity(), listValue, page);
            int temp = listValue.size();
            if (temp > 0) {
                tvCount.setText((page + 1) + "/" + temp);
            } else {
                tvCount.setText(page + "/" + temp);
            }
            recyclerSend.setAdapter(adapter);
            adapter.setOnItemClickListener(new ItemClickListener() {
                @Override
                public void onItemClickListener(View view, int position) {
                    String states = ACache.get(getActivity()).getAsString(TeaContsans.GET_ALL_WORK + listValue.get(page).get(position).getId());
                    if (states != null) {
                        switch (states) {
                            case "发送":
                                recyclerSend.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                                current = listValue.get(page).get(position);
                                List<ReuestKeyValues> stu = new ArrayList<>();
                                String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
                                if (teaId.equals("")) {
                                    Toastor.showToast(getActivity(), "检测到未登录，请先登录");
                                    return;
                                }
                                dialogShowOrHide(true, "加载中请稍后...");
                                new HttpPostRequest(getActivity(), SendWorkFragment.this, stu, "1003", TeaContsans.GET_ALL_STUDENT, true).execute();
                                break;
                            default:
                                Toastor.showToast(getActivity(), "已发送");
                                break;
                        }
                    } else {
                        Toastor.showToast(getActivity(), "下载成功");
                    }
                }
            });
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            llAllStu.setVisibility(View.GONE);
            recyclerSend.setVisibility(View.GONE);
        }
    }

    /**
     * 解析学生
     *
     * @param result
     */
    private void parseStudentResult(String result) {
        step = 1;
        stuPage = 0;
        listChose.clear();
        studentModel = GsonTools.getPerson(result, StudentModel.class);
        recyclerStus.setLayoutManager(new GridLayoutManager(getActivity(), 8));
        if (studentModel != null) {
            tvNumber.setText("学生名单（" + studentModel.getResult().size() + "人）");
            listStu = ListUtils.splitList(studentModel.getResult(), 40);
            stuAdapter = new StudentAdapter(getActivity(), listStu, stuPage);
            recyclerStus.setAdapter(stuAdapter);
            stuAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (listChose.contains(position + "")) {
                        listChose.remove(position + "");
                    } else {
                        listChose.add(position + "");
                    }
                    stuAdapter.setListChose(listChose);
                }

                @Override
                public void onItemLongClick(View view, int position) {

                }
            });
            int temp = listStu.size();
            if (temp > 0) {
                tvStuPage.setText((stuPage + 1) + "/" + temp);
            } else {
                tvStuPage.setText(stuPage + "/" + temp);
            }
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            llAllStu.setVisibility(View.GONE);
            recyclerSend.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccess(String result, String code) {
        dialogShowOrHide(false, "加载中请稍后...");
        tvNoData.setVisibility(View.GONE);
        switch (code) {
            case "1001":
                sendWorkResponce = result;
                parseHomeworkResult(result);
                break;
            case "1003":
                llAllStu.setVisibility(View.VISIBLE);
                recyclerSend.setVisibility(View.GONE);
                parseStudentResult(result);
                break;
            case "1004":
                String states = ACache.get(getActivity()).getAsString(TeaContsans.GET_ALL_WORK + current.getId());
                if (states != null) {
                    ACache.get(getActivity()).put(TeaContsans.GET_ALL_WORK + current.getId(), "已发送");
                } else {
                    ACache.get(getActivity()).put(TeaContsans.GET_ALL_WORK + current.getId(), "发送");
                }
                Toastor.showToast(getActivity(), "发送成功");
                break;
            default:
                break;
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        dialogShowOrHide(false, "加载中请稍后...");
        switch (code) {
            case "1001":
                if (!sendWorkResponce.equals("")) {
                    parseHomeworkResult(sendWorkResponce);
                } else {
                    tvNoData.setVisibility(View.VISIBLE);
                    llAllStu.setVisibility(View.GONE);
                    recyclerSend.setVisibility(View.GONE);
                }
                break;
            default:
                tvNoData.setVisibility(View.VISIBLE);
                llAllStu.setVisibility(View.GONE);
                recyclerSend.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_two_btn_first:
                //全部发送
                List<ReuestKeyValues> sendAll = new ArrayList<>();
                sendAll.add(new ReuestKeyValues("homeWorkId", current.getId() + ""));
                String teaId = GetTeacherUserId.getTeaUserId(getActivity(), true);
                if (teaId.equals("")) {
                    Toastor.showToast(getActivity(), "检测到未登录，请先登录");
                    return;
                }
                dialogShowOrHide(true, "加载中请稍后...");
                new HttpPostRequest(getActivity(), this, sendAll, "1004", TeaContsans.SEND_TO_ALL, true).execute();
                break;
            case R.id.tv_two_btn_second:
                if (listChose.size() > 0) {
                    List<ReuestKeyValues> send = new ArrayList<>();
                    send.add(new ReuestKeyValues("homeWorkId", current.getId() + ""));
                    String ids = "";
                    for (int i = 0; i < listChose.size(); i++) {
                        ids += studentModel.getResult().get(Integer.parseInt(listChose.get(i))).getId() + ";";
                    }
                    send.add(new ReuestKeyValues("studentIds", ids));
                    new HttpPostRequest(getActivity(), this, send, "1004", TeaContsans.SEND_TO_ALL, true).execute();
                } else {
                    Toastor.showToast(getActivity(), "请先选择学生才能发送");
                }
                break;
            case R.id.ll_one_page_left:
                if (page > 0) {
                    page--;
                    adapter.setPage(page);
                    int temp = listValue.size();
                    if (temp > 0) {
                        tvCount.setText((page + 1) + "/" + temp);
                    } else {
                        tvCount.setText(page + "/" + temp);
                    }
                }
                break;
            case R.id.ll_one_page_right:
                if (page < listValue.size() - 1) {
                    page++;
                    adapter.setPage(page);
                    int temp = listValue.size();
                    if (temp > 0) {
                        tvCount.setText((page + 1) + "/" + temp);
                    } else {
                        tvCount.setText(page + "/" + temp);
                    }
                }
                break;
            case R.id.no_data_try_again:
                if (step == 0) {
                    getAllHomeWork();
                } else if (step == 1) {
                    List<ReuestKeyValues> stu = new ArrayList<>();
                    String aaa = GetTeacherUserId.getTeaUserId(getActivity(), true);
                    if (aaa.equals("")) {
                        Toastor.showToast(getActivity(), "检测到未登录，请先登录");
                        return;
                    }
                    dialogShowOrHide(true, "加载中请稍后...");
                    new HttpPostRequest(getActivity(), SendWorkFragment.this, stu, "1003", TeaContsans.GET_ALL_STUDENT, true).execute();
                }
                break;
            case R.id.ll_stu_page_last:
                if (stuPage > 0) {
                    stuPage--;
                    stuAdapter.setPage(stuPage);
                    int temp = listStu.size();
                    if (temp > 0) {
                        tvStuPage.setText((stuPage + 1) + "/" + temp);
                    } else {
                        tvStuPage.setText(stuPage + "/" + temp);
                    }
                }
                break;
            case R.id.ll_stu_page_next:
                if (stuPage < listStu.size() - 1) {
                    stuPage++;
                    stuAdapter.setPage(stuPage);
                    int temp = listStu.size();
                    if (temp > 0) {
                        tvStuPage.setText((stuPage + 1) + "/" + temp);
                    } else {
                        tvStuPage.setText(stuPage + "/" + temp);
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 返回监听
     */
    public void onKeyPressDown() {
        if (llAllStu.getVisibility() == View.VISIBLE) {
            getAllHomeWork();
        } else {
            getActivity().finish();
        }
    }

    public void onPageUp() {
        if (page > 0) {
            page--;
            adapter.setPage(page);
        }
    }

    public void onPageDown() {
        if (page < listValue.size() - 1) {
            page++;
            adapter.setPage(page);
        }
    }
}
