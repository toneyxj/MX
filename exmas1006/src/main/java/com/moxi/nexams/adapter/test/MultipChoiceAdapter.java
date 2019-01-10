package com.moxi.nexams.adapter.test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.moxi.nexams.R;
import com.moxi.nexams.model.papermodel.DetailsTestModel;
import com.moxi.nexams.model.papermodel.OnlyChoiceModel;
import com.moxi.nexams.utils.TitleUtils;
import com.mx.mxbase.interfaces.OnItemClickListener;
import com.mx.mxbase.utils.GsonTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 完形填空选项适配器
 * Created by Archer on 16/8/10.
 */
public class MultipChoiceAdapter extends RecyclerView.Adapter {

    private Context context;
    private List<TextView> listOptions = new ArrayList<>();
    private List<ImageView> listImg = new ArrayList<>();
    private List<OnlyChoiceModel.PersonsBean> listX = new ArrayList<>();
    private String title;
    private List<String> result;
    private List<View> listChoice = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MultipChoiceAdapter(Context context, String title, String strOpints, List<String> result) {
        this.context = context;
        this.title = title;
        this.result = result;
        OnlyChoiceModel ocm = GsonTools.getPerson("{persons: " + strOpints + "}", OnlyChoiceModel.class);
        listX = ocm.getPersons();
    }

    public void setResult(List<String> result) {
        this.result = result;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_view_item_cloze_three, null);
                holder = new ThreeViewHolder(view);
                break;
            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_view_item_cloze_test, null);
                holder = new FourViewHolder(view);
                break;
            case 5:
                view = LayoutInflater.from(context).inflate(R.layout.mx_recycler_view_item_cloze_five, null);
                holder = new FiveViewHolder(view);
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 3:
                TitleUtils.setTestTitle(title, ((ThreeViewHolder) holder).tvTtitle, context);
                break;
            case 4:
                TitleUtils.setTestTitle(title, ((FourViewHolder) holder).tvTtitle, context);
                break;
            case 5:
                TitleUtils.setTestTitle(title, ((FiveViewHolder) holder).tvTtitle, context);
                break;
        }
        for (int i = 0; i < listX.size(); i++) {
            if (result.contains(i + "")) {
                listImg.get(i).setImageResource(R.mipmap.mx_img_check_box_chosed);
            } else {
                listImg.get(i).setImageResource(R.mipmap.mx_img_check_box_normal);
            }
//            if (result == i) {
//                listImg.get(i).setImageResource(R.mipmap.mx_img_check_box_chosed);
//            } else {
//                listImg.get(i).setImageResource(R.mipmap.mx_img_check_box_normal);
//            }
            TitleUtils.setTestTitle(listX.get(i).getDesc(), listOptions.get(i), context);
            if (onItemClickListener != null) {
                final int finalI = i;
                listChoice.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onItemClickListener.onItemClick(listChoice.get(finalI), finalI);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (listX != null) {
            if (listX.size() == 3) {
                return 3;
            } else if (listX.size() == 4) {
                return 4;
            } else if (listX.size() == 5) {
                return 5;
            } else {
                return 4;
            }
        } else {
            return 4;
        }
    }

    class FourViewHolder extends RecyclerView.ViewHolder {
        TextView tvTtitle, tvADesc, tvBDesc, tvCDesc, tvDDesc;
        ImageView imgA, imgB, imgC, imgD;
        LinearLayout llChoiceA, llChoiceB, llChoiceC, llChoiceD;

        public FourViewHolder(View itemView) {
            super(itemView);
            tvTtitle = (TextView) itemView.findViewById(R.id.tv_cloze_title);
            tvADesc = (TextView) itemView.findViewById(R.id.tv_index_a_desc);
            tvBDesc = (TextView) itemView.findViewById(R.id.tv_index_b_desc);
            tvCDesc = (TextView) itemView.findViewById(R.id.tv_index_c_desc);
            tvDDesc = (TextView) itemView.findViewById(R.id.tv_index_d_desc);

            listOptions.add(tvADesc);
            listOptions.add(tvBDesc);
            listOptions.add(tvCDesc);
            listOptions.add(tvDDesc);

            imgA = (ImageView) itemView.findViewById(R.id.img_index_a);
            imgB = (ImageView) itemView.findViewById(R.id.img_index_b);
            imgC = (ImageView) itemView.findViewById(R.id.img_index_c);
            imgD = (ImageView) itemView.findViewById(R.id.img_index_d);

            llChoiceA = (LinearLayout) itemView.findViewById(R.id.ll_choice_A);
            llChoiceB = (LinearLayout) itemView.findViewById(R.id.ll_choice_B);
            llChoiceC = (LinearLayout) itemView.findViewById(R.id.ll_choice_C);
            llChoiceD = (LinearLayout) itemView.findViewById(R.id.ll_choice_D);

            listChoice.add(llChoiceA);
            listChoice.add(llChoiceB);
            listChoice.add(llChoiceC);
            listChoice.add(llChoiceD);

            listImg.add(imgA);
            listImg.add(imgB);
            listImg.add(imgC);
            listImg.add(imgD);
        }
    }

    class ThreeViewHolder extends RecyclerView.ViewHolder {
        TextView tvTtitle, tvADesc, tvBDesc, tvCDesc;
        ImageView imgA, imgB, imgC;
        LinearLayout llChoiceA, llChoiceB, llChoiceC;

        public ThreeViewHolder(View itemView) {
            super(itemView);
            tvTtitle = (TextView) itemView.findViewById(R.id.tv_cloze_title);
            tvADesc = (TextView) itemView.findViewById(R.id.tv_index_a_desc);
            tvBDesc = (TextView) itemView.findViewById(R.id.tv_index_b_desc);
            tvCDesc = (TextView) itemView.findViewById(R.id.tv_index_c_desc);
            listOptions.add(tvADesc);
            listOptions.add(tvBDesc);
            listOptions.add(tvCDesc);

            imgA = (ImageView) itemView.findViewById(R.id.img_index_a);
            imgB = (ImageView) itemView.findViewById(R.id.img_index_b);
            imgC = (ImageView) itemView.findViewById(R.id.img_index_c);

            listImg.add(imgA);
            listImg.add(imgB);
            listImg.add(imgC);

            llChoiceA = (LinearLayout) itemView.findViewById(R.id.ll_choice_A);
            llChoiceB = (LinearLayout) itemView.findViewById(R.id.ll_choice_B);
            llChoiceC = (LinearLayout) itemView.findViewById(R.id.ll_choice_C);

            listChoice.add(llChoiceA);
            listChoice.add(llChoiceB);
            listChoice.add(llChoiceC);
        }
    }

    class FiveViewHolder extends RecyclerView.ViewHolder {
        TextView tvTtitle, tvADesc, tvBDesc, tvCDesc, tvDDesc, tvEDesc;
        ImageView imgA, imgB, imgC, imgD, imgE;
        LinearLayout llChoiceA, llChoiceB, llChoiceC, llChoiceD, llChoiceE;

        public FiveViewHolder(View itemView) {
            super(itemView);
            tvTtitle = (TextView) itemView.findViewById(R.id.tv_cloze_title);
            tvADesc = (TextView) itemView.findViewById(R.id.tv_index_a_desc);
            tvBDesc = (TextView) itemView.findViewById(R.id.tv_index_b_desc);
            tvCDesc = (TextView) itemView.findViewById(R.id.tv_index_c_desc);
            tvDDesc = (TextView) itemView.findViewById(R.id.tv_index_d_desc);
            tvEDesc = (TextView) itemView.findViewById(R.id.tv_index_e_desc);

            listOptions.add(tvADesc);
            listOptions.add(tvBDesc);
            listOptions.add(tvCDesc);
            listOptions.add(tvDDesc);
            listOptions.add(tvEDesc);

            imgA = (ImageView) itemView.findViewById(R.id.img_index_a);
            imgB = (ImageView) itemView.findViewById(R.id.img_index_b);
            imgC = (ImageView) itemView.findViewById(R.id.img_index_c);
            imgD = (ImageView) itemView.findViewById(R.id.img_index_d);
            imgE = (ImageView) itemView.findViewById(R.id.img_index_e);

            listImg.add(imgA);
            listImg.add(imgB);
            listImg.add(imgC);
            listImg.add(imgD);
            listImg.add(imgE);

            llChoiceA = (LinearLayout) itemView.findViewById(R.id.ll_choice_A);
            llChoiceB = (LinearLayout) itemView.findViewById(R.id.ll_choice_B);
            llChoiceC = (LinearLayout) itemView.findViewById(R.id.ll_choice_C);
            llChoiceD = (LinearLayout) itemView.findViewById(R.id.ll_choice_D);
            llChoiceE = (LinearLayout) itemView.findViewById(R.id.ll_choice_E);

            listChoice.add(llChoiceA);
            listChoice.add(llChoiceB);
            listChoice.add(llChoiceC);
            listChoice.add(llChoiceD);
            listChoice.add(llChoiceE);
        }
    }
}
