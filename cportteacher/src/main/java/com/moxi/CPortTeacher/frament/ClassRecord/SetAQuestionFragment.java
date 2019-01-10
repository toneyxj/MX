package com.moxi.CPortTeacher.frament.ClassRecord;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.adapter.SetSubjectAdapter;
import com.moxi.CPortTeacher.config.Connector;
import com.moxi.CPortTeacher.dbUtils.JsonAnalysis;
import com.moxi.CPortTeacher.frament.CportBaseFragment;
import com.moxi.CPortTeacher.model.SetSubjectModel;
import com.moxi.classRoom.information.UserInformation;
import com.moxi.classRoom.request.ReuestKeyValues;
import com.moxi.classRoom.utils.ToastUtils;
import com.mx.mxbase.view.LinerlayoutInter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 出题记录
 * Created by Administrator on 2016/10/28.
 */
@SuppressLint("ValidFragment")
public class SetAQuestionFragment extends CportBaseFragment implements LinerlayoutInter.LinerLayoutInter ,AdapterView.OnItemClickListener{
    @Bind(R.id.main_layout_inter)
    LinerlayoutInter main_layout_inter;
    @Bind(R.id.show_hitn)
    TextView show_hitn;
    @Bind(R.id.record_list)
    ListView record_list;
    @Bind(R.id.up_file)//一键上传
    Button up_file;

    @Bind(R.id.last_page)
    ImageButton last_page;
    @Bind(R.id.show_index)
    TextView show_index;
    @Bind(R.id.next_page)
    ImageButton next_page;
    private List<SetSubjectModel> listData=new ArrayList<>();
    private List<SetSubjectModel> listMiddle=new ArrayList<>();
    private SetSubjectAdapter adapter;
    private int List_item_height=0;

    /**
     * 当前显示页数
     */
    private int CurrentIndex = 1;
    /**
     * 总页数
     */
    private int totalIndex = 1;
    private int pageSize = 10;
    private  SubjectModelListener listener;
    @Override
    protected int getMainContentViewId() {
        return R.layout.fragment_set_question;
    }

    public SetAQuestionFragment(SubjectModelListener listener){
        this.listener=listener;
    }

    @Override
    protected void onFragmentCreated(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        last_page.setOnClickListener(this);
        next_page.setOnClickListener(this);
        show_hitn.setOnClickListener(this);

        main_layout_inter.setLayoutInter(this);
        record_list.setOnItemClickListener(this);

        up_file.setOnClickListener(this);
    }
    private void getAllData(){
        List<ReuestKeyValues> values = new ArrayList<>();
        values.add(new ReuestKeyValues("rows", String.valueOf(pageSize)));
        values.add(new ReuestKeyValues("page", String.valueOf(CurrentIndex)));
        values.add(new ReuestKeyValues("userId", String.valueOf(UserInformation.getInstance().getID())));
        dialogShowOrHide(true,"课堂记录获取中...");
        getData(values,"课堂记录", Connector.getInstance().teacherClassWorkList,true);
    }

    @Override
    public void onSuccess(String result, String code) {
        super.onSuccess(result, code);
        if (code.equals("课堂记录")) {
            try {
                JSONObject object = new JSONObject(result);
                JSONObject resultData = object.getJSONObject("result");
                JSONArray array = resultData.getJSONArray("list");
                //解析列表
                listData.addAll(JsonAnalysis.getInstance().getSetSubjectModels(array.toString()));
                CurrentIndex = resultData.getInt("page");
                totalIndex = resultData.getInt("pageCount");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (listData.size() == 0) {
                show_hitn.setText("没有记录哟！！！");
                show_hitn.setVisibility(View.VISIBLE);
            } else {
                show_hitn.setVisibility(View.GONE);
            }
            initSonData();
        }
    }

    @Override
    public void onFail(String code, boolean showFail, int failCode, String msg) {
        super.onFail(code, showFail, failCode, msg);
        if (code.equals("课堂记录")) {
            if (CurrentIndex > 1)
                CurrentIndex--;
            if (listData.size() == 0) {
                show_hitn.setText("请求出错\n点击重试");
                show_hitn.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.last_page:
                moveLeft();
                break;
            case R.id.next_page:
                moveRight();
                break;
            case R.id.up_file://一键上传
                ToastUtils.getInstance().showToastShort("该功能还未开发");
                break;
            case R.id.show_hitn://重新加载
                if (listData.size()==0){
                    getAllData();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onViewInitChange() {
        if (record_list.getHeight()==0)return;
        if (List_item_height==0) {
            List_item_height = (record_list.getHeight() - 10) / (pageSize);
            getAllData();
        }
    }

    /**
     * 分配下面数据以刷新
     */
    public void initSonData() {
        if (pageSize*CurrentIndex>listData.size()&&CurrentIndex<totalIndex){
            getAllData();
            return;
        }
        if (listData.size() == 0) {
            adapterItems(listData);
        } else if (totalIndex == CurrentIndex) {
            adapterItems(listData.subList((CurrentIndex-1) * pageSize, listData.size()));
        } else {
            adapterItems(listData.subList((CurrentIndex-1) * pageSize, (CurrentIndex ) * pageSize));
        }
        setShowText();
    }
    private void adapterItems(List<SetSubjectModel> List) {
        listMiddle.clear();
        listMiddle.addAll(List);
        if (adapter == null) {
            adapter = new SetSubjectAdapter(context,listMiddle,List_item_height);
            record_list.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }
    /**
     * 设置显示当前显示页数
     */
    public void setShowText() {
        show_index.setText(String.valueOf(CurrentIndex) + "/" + totalIndex);
    }
    @Override
    public void moveLeft() {
        if (CurrentIndex>1){
            CurrentIndex--;
            initSonData();
        }
    }

    @Override
    public void moveRight() {
        if (CurrentIndex<totalIndex){
            CurrentIndex++;
            if ((CurrentIndex*pageSize>listData.size())&&(listData.size()%pageSize==0)&&(listData.size()/10!=totalIndex)){
                getAllData();
                return;
            }
                initSonData();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        listener.onSubjectModel(listMiddle.get(position));
    }

    public interface SubjectModelListener{
        void onSubjectModel(SetSubjectModel model);
    }
}
