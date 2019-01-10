package com.moxi.handwritten.fragment;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.moxi.handwritten.R;
import com.moxi.handwritten.adapter.WriteItemAdapter;
import com.mx.mxbase.base.baseFragment;
import com.moxi.handwritten.model.WriteFileBeen;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2016/8/2.
 */
@SuppressLint("ValidFragment")
public class WriteSonFragment extends baseFragment implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    @Bind(R.id.write_item)
    GridView write_item;
    private WriteItemAdapter adapter;
    private List<WriteFileBeen> list;
//    public static WriteFragment newInstance(List<WriteFileBeen> list) {
//        WriteFragment diFragment = new WriteFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("list", (Serializable) list);
//        diFragment.setArguments(bundle);
//        return diFragment;
//    }
    public WriteSonFragment(List<WriteFileBeen> list){
        this.list=list;
    }
    public void setSelect(boolean is){
        adapter.setShowAelect(is);
        adapter.notifyDataSetChanged();
    }
    @Override
    public void initFragment(View view) {
        adapter=new WriteItemAdapter(getActivity(),list);
        write_item.setAdapter(adapter);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_write_son;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (list.get(position).type.equals("-1"))return false;



        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
if (list.get(position).type.equals("-1"))return;
    }
}
