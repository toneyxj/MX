package com.moxi.CPortTeacher.frament.ClassTests;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.weedout.DoProgressAdapter;
import com.moxi.CPortTeacher.adapter.weedout.EndAnswerAdapter;
import com.moxi.CPortTeacher.adapter.weedout.LevelAdapter;
import com.moxi.CPortTeacher.adapter.weedout.StudentAdapter;
import com.moxi.CPortTeacher.adapter.weedout.TitleAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.weedout.ExamsTitleModel;
import com.moxi.CPortTeacher.model.weedout.FallWorkProgressModel;
import com.moxi.CPortTeacher.model.weedout.FallWorkScoreModel;
import com.moxi.CPortTeacher.model.weedout.StudentModel;
import com.moxi.CPortTeacher.utils.MxgsaTagHandler;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.HttpTimer;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.Base64Utils;
import com.mx.mxbase.utils.GsonTools;
import com.mx.mxbase.utils.ListUtils;
import com.mx.mxbase.utils.Toastor;
import com.mx.mxbase.view.AlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;

/**
 * 淘汰答题
 * Created by Administrator on 2016/10/28.
 */
public class WeedOutAnswerFragment extends CportBaseFragment {

    @Bind(R.id.rl_main_layout)
    RelativeLayout rlMainLayout;
    @Bind(R.id.ll_send_to_all_student)
    LinearLayout llSendToALl;
    @Bind(R.id.ll_chose_student_to_send)
    LinearLayout llChoseSend;
    @Bind(R.id.include_two_button_bottom)
    View viewTwoBtn;
    @Bind(R.id.include_one_button_bottom)
    View viewOneBtn;
    @Bind(R.id.tv_two_btn_first)
    TextView tvBtnFirst;
    @Bind(R.id.tv_two_btn_second)
    TextView tvBtnSecond;
    @Bind(R.id.tv_one_btn_button)
    TextView tvBtnOne;
    @Bind(R.id.ll_button_area)
    LinearLayout llBtnOneArea;
    @Bind(R.id.ll_two_page_left)
    LinearLayout llTwoPageLeft;
    @Bind(R.id.ll_two_page_right)
    LinearLayout llTwoPageRight;
    @Bind(R.id.ll_one_page_left)
    LinearLayout llOnePageLeft;
    @Bind(R.id.ll_one_page_right)
    LinearLayout llOnePageRight;
    @Bind(R.id.tv_two_button_bottom_desc)
    TextView tvTwoBtnDesc;
    @Bind(R.id.tv_one_button_bottom_desc)
    TextView tvOneBtnDesc;
    @Bind(R.id.rl_one_button_bottom)
    RelativeLayout rlOneBtnBottom;
    @Bind(R.id.rl_two_button_bottom)
    RelativeLayout rlTwoBtnBottom;
    @Bind(R.id.tv_two_page_count)
    TextView tvTwoBtnCount;
    @Bind(R.id.tv_one_page_count)
    TextView tvOneBtnCount;

    private boolean LOOK_EXAMS = false;
    private HttpTimer httpTimer;
    private int choseExamsIndex = 0;
    private List<String> listChose = new ArrayList<>();
    private List<StudentModel.StudentWeed> listStudent = new ArrayList<>();

    private HashMap<String, String> interfaceResult = new HashMap<>();

    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_weed_out_answer;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init();
    }

    /**
     * 初始化视图
     */
    private void init() {
        llChoseSend.setOnClickListener(this);
        llSendToALl.setOnClickListener(this);
        llBtnOneArea.setOnClickListener(this);

        List<ReuestKeyValues> score = new ArrayList<>();
        score.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
        getData(score, "1009", Connector.getInstance().getFallWorkProgess, true);

        addViewToLinearLayout(R.layout.mx_include_weed_out_first_layout, 8, 2, new String[]{"全部发送", "选择发送"}, "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_watch_exams:
                LOOK_EXAMS = true;
                choseExamsIndex = 0;
                getData(null, "1002", Connector.getInstance().getExamsBySubjectId + UserInformation.getInstance().getUserInformation().subjectId, true);
                break;
            case R.id.ll_send_to_all_student:
                switch (tvBtnFirst.getText().toString()) {
                    case "全部发送":
                        progressPage = 0;
                        List<ReuestKeyValues> params = new ArrayList<>();
                        if (interfaceResult.get("1001") == null) {
                            getData(params, "1005", Connector.getInstance().getStudenturl, true);
                        } else {
                            List<ReuestKeyValues> send = new ArrayList<>();
                            send.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                            String tempStudents = "";
                            for (StudentModel.StudentWeed studentWeed : listStudent) {
                                tempStudents += studentWeed.getId() + ";";
                            }
                            send.add(new ReuestKeyValues("studentIds", tempStudents.substring(0, tempStudents.length() - 1)));
                            getData(send, "1003", Connector.getInstance().startFallWork, true);
                        }
                        break;
                    case "结束答题":
                        endPage = 0;
                        List<ReuestKeyValues> end = new ArrayList<>();
                        end.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                        getData(end, "1004", Connector.getInstance().endFallWork, true);
                        break;
                }
                break;
            case R.id.ll_chose_student_to_send:
                switch (tvBtnSecond.getText().toString()) {
                    case "选择发送":
                        List<ReuestKeyValues> params = new ArrayList<>();
                        getData(params, "1001", Connector.getInstance().getStudenturl, true);
                        break;
                    case "重新发送":
                        Toastor.showToast(getActivity(), "重新发送");
                        break;
                }
                break;
            case R.id.ll_button_area:
                switch (tvBtnOne.getText().toString()) {
                    case "发送":
                        progressPage = 0;
                        List<ReuestKeyValues> send = new ArrayList<>();
                        send.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                        String tempStudents = "";
                        if (listChose.size() > 0) {
                            for (String index : listChose) {
                                Integer aaa = Integer.parseInt(index);
                                tempStudents += listStudent.get(aaa).getId() + ";";
                            }
                            send.add(new ReuestKeyValues("studentIds", tempStudents.substring(0, tempStudents.length() - 1)));
                            getData(send, "1003", Connector.getInstance().startFallWork, true);
                        } else {
                            Toastor.showToast(getActivity(), "请选择学生");
                        }
                        break;
                    case "发送结果":
                        Toastor.showToast(getActivity(), "发送结果");
                        break;
                }
                break;
            default:
                break;
        }
    }

    /**
     * 添加第一个布局到fragment
     *
     * @param layoutId   布局id
     * @param btnNumbers 底部按钮个数
     * @param bottomDesc 底部按钮描述
     */
    private void addViewToLinearLayout(int layoutId, int size, int btnNumbers, String[] btnNames, String bottomDesc) {
        rlMainLayout.removeAllViews();
        switch (btnNumbers) {
            case 1://底部只有一个按钮
                viewOneBtn.setVisibility(View.VISIBLE);
                viewTwoBtn.setVisibility(View.GONE);
                break;
            case 2://底部有两个按钮
                viewOneBtn.setVisibility(View.GONE);
                viewTwoBtn.setVisibility(View.VISIBLE);
                tvBtnFirst.setText(btnNames[0]);
                tvBtnSecond.setText(btnNames[1]);
                break;
        }
        if (bottomDesc.equals("")) {
            tvOneBtnDesc.setVisibility(View.GONE);
            tvTwoBtnDesc.setVisibility(View.GONE);
        } else {
            switch (btnNumbers) {
                case 1:
                    tvOneBtnDesc.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvTwoBtnDesc.setVisibility(View.VISIBLE);
                    break;
            }
        }
        rlOneBtnBottom.setVisibility(View.GONE);
        rlTwoBtnBottom.setVisibility(View.GONE);
        View firstView = LayoutInflater.from(getActivity()).inflate(layoutId, rlMainLayout);
        RecyclerView recyclerView = (RecyclerView) firstView.findViewById(R.id.recycler_fragment_weed_out_level);
        TextView tvWatchExams = (TextView) firstView.findViewById(R.id.tv_watch_exams);
        tvWatchExams.setOnClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        LevelAdapter adapter = new LevelAdapter(getActivity(), size);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                LOOK_EXAMS = true;
                getData(null, "1002", Connector.getInstance().getExamsBySubjectId + UserInformation.getInstance().getUserInformation().subjectId, true);
                choseExamsIndex = position;
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

    }

    /**
     * 添加第二个布局到fragment
     *
     * @param layoutId   布局id
     * @param btnNumbers 底部按钮个数
     * @param bottomDesc 底部按钮描述
     * @param result     服务器返回数据
     */
    private void addSecondToLinearLayout(int layoutId, int btnNumbers, String[] btnNames, String bottomDesc, String result) {
        rlMainLayout.removeAllViews();
        switch (btnNumbers) {
            case 1://底部只有一个按钮
                viewOneBtn.setVisibility(View.VISIBLE);
                viewTwoBtn.setVisibility(View.GONE);
                break;
            case 2://底部有两个按钮
                viewOneBtn.setVisibility(View.GONE);
                viewTwoBtn.setVisibility(View.VISIBLE);
                tvBtnFirst.setText(btnNames[0]);
                tvBtnSecond.setText(btnNames[1]);
                break;
        }
        rlOneBtnBottom.setVisibility(View.GONE);
        rlTwoBtnBottom.setVisibility(View.GONE);
        View secondView = LayoutInflater.from(getActivity()).inflate(layoutId, rlMainLayout);
        if (bottomDesc.equals("")) {
            tvOneBtnDesc.setVisibility(View.GONE);
            tvTwoBtnDesc.setVisibility(View.GONE);
        } else {
            switch (btnNumbers) {
                case 1:
                    tvOneBtnDesc.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvTwoBtnDesc.setVisibility(View.VISIBLE);
                    break;
            }
        }
        final ExamsTitleModel etm = GsonTools.getPerson(result, ExamsTitleModel.class);
        final TextView tvExamsDetails = (TextView) secondView.findViewById(R.id.tv_exams_details);
        try {
            setTitle(etm.getResult().get(choseExamsIndex).getTitle(), tvExamsDetails);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RecyclerView recyclerView = (RecyclerView) secondView.findViewById(R.id.recycler_weed_out_exams_index);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 6));
        final TitleAdapter adapter = new TitleAdapter(getActivity(), 0, 8);
        recyclerView.setAdapter(adapter);
        adapter.setCurrentIndex(choseExamsIndex);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                adapter.setCurrentIndex(position);
                choseExamsIndex = position;
                setTitle(etm.getResult().get(position).getTitle(), tvExamsDetails);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    /**
     * 添加做题进度界面到fragment
     *
     * @param layoutId   布局id
     * @param btnNumbers 按钮个数
     * @param btnNames   按钮名称
     * @param bottomDesc 底部区域描述
     */

    private int progressPage = 0;
    private int flag = 0;
    private Chronometer tvTime;

    private void addProgressLayoutToLinearLayout(int layoutId, int btnNumbers, String[] btnNames, String bottomDesc) {
        rlMainLayout.removeAllViews();
        switch (btnNumbers) {
            case 1://底部只有一个按钮
                viewOneBtn.setVisibility(View.VISIBLE);
                viewTwoBtn.setVisibility(View.GONE);
                break;
            case 2://底部有两个按钮
                viewOneBtn.setVisibility(View.GONE);
                viewTwoBtn.setVisibility(View.VISIBLE);
                tvBtnFirst.setText(btnNames[0]);
                tvBtnSecond.setText(btnNames[1]);
                break;
        }
        final FallWorkProgressModel fwpm = GsonTools.getPerson(bottomDesc, FallWorkProgressModel.class);
        View progressView = LayoutInflater.from(getActivity()).inflate(layoutId, rlMainLayout);
        rlOneBtnBottom.setVisibility(View.GONE);
        rlTwoBtnBottom.setVisibility(View.VISIBLE);
        if (bottomDesc.equals("")) {
            tvOneBtnDesc.setVisibility(View.GONE);
            tvTwoBtnDesc.setVisibility(View.GONE);
        } else {
            switch (btnNumbers) {
                case 1:
                    tvOneBtnDesc.setVisibility(View.VISIBLE);
                    tvOneBtnDesc.setText("闯关失败：" + fwpm.getResult().getFailedList());
                    break;
                case 2:
                    if (!fwpm.getResult().getFailedList().equals("")) {
                        tvTwoBtnDesc.setVisibility(View.VISIBLE);
                        tvTwoBtnDesc.setText("闯关失败：" + fwpm.getResult().getFailedList());
                    } else {
                        tvTwoBtnDesc.setVisibility(View.GONE);
                    }
                    break;
            }
        }

        TextView tvTotalPeople = (TextView) progressView.findViewById(R.id.tv_total_people);
        TextView tvWeedOutPeople = (TextView) progressView.findViewById(R.id.tv_weed_out_people);
        TextView tvFinishPeople = (TextView) progressView.findViewById(R.id.tv_finish_people);
        if (flag == 0) {
            tvTime = (Chronometer) progressView.findViewById(R.id.tv_west_time);
        }
        tvTotalPeople.setText("答题人数：" + fwpm.getResult().getCount() + "人");
        tvFinishPeople.setText("已完成：" + fwpm.getResult().getCommitCount() + "人");
        tvWeedOutPeople.setText("已淘汰：" + fwpm.getResult().getFailedCount() + "人");
        RecyclerView recyclerView = (RecyclerView) progressView.findViewById(R.id.recycler_fragment_weed_out_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final DoProgressAdapter doProgressAdapter = new DoProgressAdapter(getActivity(), fwpm.getResult().getProgessList(), progressPage);
        recyclerView.setAdapter(doProgressAdapter);
        final int temp = ListUtils.splitList(fwpm.getResult().getProgessList(), 7).size();
        if (temp > 0) {
            tvTwoBtnCount.setText((progressPage + 1) + "/" + temp);
        } else {
            tvTwoBtnCount.setText("0/0");
        }
        llTwoPageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fwpm.getResult().getProgessList().size() / 7 > progressPage) {
                    progressPage++;
                    doProgressAdapter.setPage(progressPage);
                    tvTwoBtnCount.setText((progressPage + 1) + "/" + temp);
                }
            }
        });
        llTwoPageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (progressPage > 0) {
                    progressPage--;
                    doProgressAdapter.setPage(progressPage);
                    tvTwoBtnCount.setText((progressPage + 1) + "/" + temp);
                }
            }
        });
//            tvTime.setBase(SystemClock.elapsedRealtime());//计时器清零
        if (flag == 0) {
            try {
                tvTime.start();
                flag = 2;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 结束答题查看结果界面
     *
     * @param layoutId   布局id
     * @param btnNumbers 按钮个数
     * @param btnNames   按钮名称
     * @param bottomDesc 底部区域描述
     */
    int endPage = 0;

    private void addEndAnswerLayoutToLinearLayout(int layoutId, int btnNumbers, String[] btnNames, String bottomDesc) {
        rlMainLayout.removeAllViews();
        final FallWorkScoreModel fwcm = GsonTools.getPerson(bottomDesc, FallWorkScoreModel.class);
        switch (btnNumbers) {
            case 1://底部只有一个按钮
                viewOneBtn.setVisibility(View.VISIBLE);
                viewTwoBtn.setVisibility(View.GONE);
                tvBtnOne.setText(btnNames[0]);
                break;
            case 2://底部有两个按钮
                viewOneBtn.setVisibility(View.GONE);
                viewTwoBtn.setVisibility(View.VISIBLE);
                tvBtnFirst.setText(btnNames[0]);
                tvBtnSecond.setText(btnNames[1]);
                break;
        }
        rlOneBtnBottom.setVisibility(View.VISIBLE);
        rlTwoBtnBottom.setVisibility(View.GONE);
        View endAnswerView = LayoutInflater.from(getActivity()).inflate(layoutId, rlMainLayout);
        if (bottomDesc.equals("")) {
            tvOneBtnDesc.setVisibility(View.GONE);
            tvTwoBtnDesc.setVisibility(View.GONE);
        } else {
            switch (btnNumbers) {
                case 1:
                    if (!fwcm.getResult().getFailedList().equals("")) {
                        tvOneBtnDesc.setVisibility(View.VISIBLE);
                        tvOneBtnDesc.setText("闯关失败：" + fwcm.getResult().getFailedList());
                    } else {
                        tvOneBtnDesc.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    tvTwoBtnDesc.setVisibility(View.VISIBLE);
                    break;
            }
        }

        TextView tvTotalPeople = (TextView) endAnswerView.findViewById(R.id.tv_total_people);
        TextView tvWeedOutPeople = (TextView) endAnswerView.findViewById(R.id.tv_weed_out_people);
        TextView tvFinishPeople = (TextView) endAnswerView.findViewById(R.id.tv_finish_people);
        tvTotalPeople.setText("答题人数：" + fwcm.getResult().getCount() + "人");
        tvFinishPeople.setText("闯关成功：" + (fwcm.getResult().getCount() - fwcm.getResult().getFailedCount()) + "人");
        tvWeedOutPeople.setText("淘汰：" + fwcm.getResult().getFailedCount() + "人");

        RecyclerView recyclerView = (RecyclerView) endAnswerView.findViewById(R.id.recycler_fragment_weed_out_progress);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final EndAnswerAdapter endAnswerAdapter = new EndAnswerAdapter(getActivity(), fwcm.getResult().getScoreList(), endPage);
        recyclerView.setAdapter(endAnswerAdapter);
        final int temp = ListUtils.splitList(fwcm.getResult().getScoreList(), 7).size();
        llOnePageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fwcm.getResult().getScoreList().size() / 7 > endPage) {
                    endPage++;
                    endAnswerAdapter.setPage(endPage);
                    if (temp > 0) {
                        tvOneBtnCount.setText((endPage + 1) + "/" + temp);
                    } else {
                        tvOneBtnCount.setText("0/0");
                    }
                }
            }
        });
        llOnePageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (endPage > 0) {
                    endPage--;
                    endAnswerAdapter.setPage(endPage);
                    if (temp > 0) {
                        tvOneBtnCount.setText((endPage + 1) + "/" + temp);
                    } else {
                        tvOneBtnCount.setText("0/0");
                    }
                }
            }
        });
        if (temp > 0) {
            tvOneBtnCount.setText((endPage + 1) + "/" + temp);
        } else {
            tvOneBtnCount.setText("0/0");
        }
        tvOneBtnDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog(getActivity()).builder().setTitle("闯关结果").setMsg(fwcm.getResult().getFailedList()).setCancelable(true).setNegativeButton("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
            }
        });
    }

    /**
     * 添加学生发送界面到当前fragment
     *
     * @param sendId     布局id
     * @param btnNumbers 底部按钮个数
     * @param bottomDesc 底部按钮描述
     * @param result     接口返回数据
     */
    private void addSendToStudentLayout(int sendId, int btnNumbers, String[] btnNames, String bottomDesc, String result) {
        rlMainLayout.removeAllViews();
        listChose.clear();
        switch (btnNumbers) {
            case 1://底部只有一个按钮
                viewOneBtn.setVisibility(View.VISIBLE);
                viewTwoBtn.setVisibility(View.GONE);
                tvBtnOne.setText(btnNames[0]);
                break;
            case 2://底部有两个按钮
                viewOneBtn.setVisibility(View.GONE);
                viewTwoBtn.setVisibility(View.VISIBLE);
                break;
        }
        View sendView = LayoutInflater.from(getActivity()).inflate(sendId, rlMainLayout);
        if (bottomDesc.equals("")) {
            tvOneBtnDesc.setVisibility(View.GONE);
            tvTwoBtnDesc.setVisibility(View.GONE);
        } else {
            switch (btnNumbers) {
                case 1:
                    tvOneBtnDesc.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    tvTwoBtnDesc.setVisibility(View.VISIBLE);
                    break;
            }
        }
        RecyclerView sendRecyclerView = (RecyclerView) sendView.findViewById(R.id.recycler_fragment_weed_out_students);
        TextView tvStudentCount = (TextView) sendView.findViewById(R.id.tv_fragment_weed_out_student);
        sendRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 7));
        final StudentModel sm = GsonTools.getPerson(result, StudentModel.class);
        try {
            if (sm.getResult().size() > 0) {
                tvStudentCount.setText("学生名单(" + sm.getResult().size() + "人)");
                listStudent = sm.getResult();
                final StudentAdapter adapter = new StudentAdapter(getActivity(), sm.getResult(), 0);
                sendRecyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if (listChose.contains(position + "")) {
                            listChose.remove(position + "");
                        } else {
                            listChose.add(position + "");
                        }
                        adapter.setListChose(listChose);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                });
            } else {
                tvStudentCount.setText("学生名单(0人)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        interfaceResult.put(code, result);
        switch (code) {
            case "1001"://获取淘汰答题学生名单
                this.setTitle("学生名单");
                addSendToStudentLayout(R.layout.mx_include_weed_out_send_layout, 1, new String[]{"发送"}, "", result);
                break;
            case "1002"://获取淘汰答题试题
                this.setTitle("方程式闯关答题比赛");
                addSecondToLinearLayout(R.layout.mx_include_weed_out_second_layout, 2, new String[]{"全部发送", "选择发送"}, "", result);
                break;
            case "1003"://获取淘汰答题进度,这里应该循环获取
                List<ReuestKeyValues> progress = new ArrayList<>();
                progress.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                httpTimer = new HttpTimer(getActivity(), progress, this, "1006", Connector.getInstance().getFallWorkProgess);
                break;
            case "1004"://结束淘汰答题
                this.setTitle("闯关答题统计结果");
                if (httpTimer != null) {
                    httpTimer.stopTimer();
                }
                List<ReuestKeyValues> score = new ArrayList<>();
                score.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                getData(score, "1007", Connector.getInstance().getFallWorkScore, true);
                break;
            case "1005"://点击全部发送同样是获取学生列表接口返回之后再调用全部发送接口
                StudentModel sm = GsonTools.getPerson(result, StudentModel.class);
                List<ReuestKeyValues> send = new ArrayList<>();
                send.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                String tempStudents = "";
                listStudent = sm.getResult();
                for (StudentModel.StudentWeed studentWeed : listStudent) {
                    tempStudents += studentWeed.getId() + ";";
                }
                send.add(new ReuestKeyValues("studentIds", tempStudents.substring(0, tempStudents.length() - 1)));
                getData(send, "1003", Connector.getInstance().startFallWork, true);
                break;
            case "1006":
                this.setTitle("答题进度");
                addProgressLayoutToLinearLayout(R.layout.mx_include_weed_out_progress_layout, 2, new String[]{"结束答题", "重新发送"}, result);
                break;
            case "1007":
                addEndAnswerLayoutToLinearLayout(R.layout.mx_include_weed_out_progress_layout, 1, new String[]{"发送结果"}, result);
                break;
            case "1009":
                List<ReuestKeyValues> aaa = new ArrayList<>();
                aaa.add(new ReuestKeyValues("userId", UserInformation.getInstance().getUserInformation().myid + ""));
                httpTimer = new HttpTimer(getActivity(), aaa, this, "1006", Connector.getInstance().getFallWorkProgess);
                break;
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
    }

    @Override
    public void onclickBack() {
        super.onclickBack();
        switch (this.getTitle()) {
            case "互动课堂":
                break;
            case "方程式闯关答题比赛":
                this.setTitle("");
                addViewToLinearLayout(R.layout.mx_include_weed_out_first_layout, 8, 2, new String[]{"全部发送", "选择发送"}, "");
                break;
            case "学生名单":
                if (LOOK_EXAMS) {
                    this.setTitle("方程式闯关答题比赛");
                    rlMainLayout.removeAllViews();
                    getData(null, "1002", Connector.getInstance().getExamsBySubjectId + UserInformation.getInstance().getUserInformation().subjectId, true);
                    LOOK_EXAMS = false;
                } else {
                    this.setTitle("");
                    rlMainLayout.removeAllViews();
                    addViewToLinearLayout(R.layout.mx_include_weed_out_first_layout, 8, 2, new String[]{"全部发送", "选择发送"}, "");
                }
                break;
            case "答题进度":
                if (httpTimer != null) {
                    httpTimer.stopTimer();
                }
                if (interfaceResult.get("1001") != null) {
                    getData(null, "1001", Connector.getInstance().getStudenturl, true);
                } else {
                    this.setTitle("学生名单");
                    addSendToStudentLayout(R.layout.mx_include_weed_out_send_layout, 1, new String[]{"发送"}, "", interfaceResult.get("1001"));
                }
                break;
            case "闯关答题统计结果":
                this.setTitle("");
                addViewToLinearLayout(R.layout.mx_include_weed_out_first_layout, 8, 2, new String[]{"全部发送", "选择发送"}, "");
                break;
        }
    }

    /**
     * 设置题目信息
     *
     * @param title 题目内容
     * @param view  目标view设置
     */
    private void setTitle(String title, TextView view) {
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"](\\s*)(data:image/)\\S+(base64,)([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(title);
        while (m.find()) {
            String str = m.group(4);
            title = title.replace(m.group(), "#@M#@X@" + str + "#@M#@X@");
        }
        String titleResult = title;
        if (titleResult.indexOf("#@M#@X@") > 0) {
            String[] s = titleResult.split("#@M#@X@");
            view.setText("");
            for (int j = 0; j < s.length; j++) {
                if (j % 2 == 0) {
                    view.append(Html.fromHtml(s[j]));
                } else {
                    Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
                    ImageSpan imgSpan = new ImageSpan(getActivity(), bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.append(spanString);
                }
            }
        } else {
            view.setText(Html.fromHtml(title, null, new MxgsaTagHandler(getActivity())));
        }
    }
}
