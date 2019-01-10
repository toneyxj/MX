package com.mx.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mx.mxbase.netstate.NetWorkUtil;
import com.mx.mxbase.utils.Toastor;
import com.mx.teacher.R;
import com.mx.teacher.activity.MXWriteHomeWorkActivity;
import com.mx.teacher.cache.ACache;
import com.mx.teacher.constant.TestConstant;
import com.mx.teacher.entity.TeacherGridEntry;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by zhengdelong on 16/9/20.
 */

public class TeacherGridFixAdapter extends BaseAdapter {

    private List<TeacherGridEntry> data = new ArrayList<TeacherGridEntry>();
    private Context context;

    public TeacherGridFixAdapter(Context context, List<TeacherGridEntry> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_teacher_grid, null);
            viewHolder.home_rel = (RelativeLayout) view.findViewById(R.id.home_rel);
            viewHolder.title = (TextView) view.findViewById(R.id.title);
            viewHolder.status = (TextView) view.findViewById(R.id.status);
            viewHolder.time = (TextView) view.findViewById(R.id.time);
            viewHolder.distribute = (TextView) view.findViewById(R.id.distribute);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (data.get(position).getReplyStatus() == 0) {
            //未分发
            viewHolder.distribute.setVisibility(View.VISIBLE);
            viewHolder.time.setVisibility(View.GONE);
            viewHolder.distribute.setText("批复");
            viewHolder.status.setText("未批复");
            viewHolder.home_rel.setBackgroundResource(R.drawable.back_grid_f2);
        } else {
            //已分发
            viewHolder.home_rel.setBackgroundResource(R.drawable.back_grid);
            viewHolder.distribute.setVisibility(View.GONE);
            viewHolder.time.setVisibility(View.VISIBLE);
            viewHolder.status.setText("已批复");
            viewHolder.time.setText(data.get(position).getArrTime());
        }
        viewHolder.title.setText(data.get(position).getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //批复
                getHomeWorkDetails(data.get(position));
            }
        });
        return view;
    }

    static class ViewHolder {
        RelativeLayout home_rel;
        TextView title;
        TextView status;
        TextView time;
        TextView distribute;
    }

    private void getHomeWorkDetails(final TeacherGridEntry tge) {
        final String url = TestConstant.GET_HOME_WORK_BY_ID + tge.getId();
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            if (ACache.get(context).getAsString(url) == null) {
                Toastor.showToast(context, "网络不可用，请检查网络连接");
            } else {
                Intent intent = new Intent(context, MXWriteHomeWorkActivity.class);
                intent.putExtra("home_work_state", tge.getReplyStatus());
                intent.putExtra("home_work_url", url);
                intent.putExtra("home_work_json", ACache.get(context).getAsString(url));
                context.startActivity(intent);
            }
        } else if (ACache.get(context).getAsString(url) != null) {
            Intent intent = new Intent(context, MXWriteHomeWorkActivity.class);
            intent.putExtra("home_work_state", tge.getReplyStatus());
            intent.putExtra("home_work_url", url);
            intent.putExtra("home_work_json", ACache.get(context).getAsString(url));
            context.startActivity(intent);
        } else {
            OkHttpUtils.post().url(url).build().connTimeOut(10000).execute(new StringCallback() {

                @Override
                public void onBefore(Request request, int id) {
                    super.onBefore(request, id);
                }

                @Override
                public void onError(Call call, Exception e, int id) {
                    Toastor.showToast(context, "请求网络失败，请检查网络链接");
                }

                @Override
                public void onResponse(String response, int id) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        int code = jsonObject.getInt("code");
                        if (code == 0) {
                            ACache.get(context).put(url, response);
                            Intent intent = new Intent(context, MXWriteHomeWorkActivity.class);
                            intent.putExtra("home_work_state", tge.getReplyStatus());
                            intent.putExtra("home_work_url", url);
                            intent.putExtra("home_work_json", response);
                            context.startActivity(intent);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
