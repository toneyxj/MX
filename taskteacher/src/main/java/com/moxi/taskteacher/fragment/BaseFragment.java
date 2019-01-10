package com.moxi.taskteacher.fragment;

import android.support.v4.app.Fragment;

import com.moxi.taskteacher.R;
import com.mx.mxbase.dialog.HitnDialog;

/**
 * Created by Archer on 2016/12/21.
 */
public abstract class BaseFragment extends Fragment {
    //是否可见
    protected boolean isVisable;
    // 标志位，标志Fragment已经初始化完成。
    public boolean isPrepared = false;
    public HitnDialog dialog;

    /**
     * 实现Fragment数据的缓加载
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisable = true;
            onVisible();
        } else {
            isVisable = false;
            onInVisible();
        }
    }

    protected void onInVisible() {
    }

    protected void onVisible() {
        //加载数据
        loadData();
    }

    public HitnDialog dialogShowOrHide(boolean is, String hitn) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
        if (is) {
            dialog = new HitnDialog(getContext(), R.style.AlertDialogStyle, hitn);
            dialog.setCancelable(false);// 是否可以关闭dialog
            dialog.setCanceledOnTouchOutside(false);
            try {
                dialog.show();
            } catch (Exception e) {

            }
        }
        return dialog;
    }

    protected abstract void loadData();
}
