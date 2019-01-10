package com.moxi.taskteacher.view;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.PageFling;
import com.moxi.taskteacher.R;

/**
 * Created by Administrator on 2016/10/28 0028.
 */
public class PdfView extends LinearLayout implements PageFling {

    private Context context;

    private MuPDFReaderView mv;
    private LinearLayout mv_ll;
    private int currentpage;
    private MuPDFCore core;

    public PdfView(Context context) {
        super(context);
        initView(context);
    }

    public PdfView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public PdfView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.widght_pdf,
                this);
        mv = (MuPDFReaderView) view.findViewById(R.id.mupdf_rv);
        mv_ll = (LinearLayout) view.findViewById(R.id.mv_ll);

    }

    public void openPdf(String path) {

        Log.d("TAG",path);
        core = openFile(Uri.decode(path));

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e("TAG", "Document Not Opening");
        }
        if (core != null) {
            this.setVisibility(View.VISIBLE);
            mv.setPageFlyListener(this);
            mv.setAdapter(new MuPDFPageAdapter(context, core));
            currentpage = 1;
        } else {
            Log.e("TAG", "core is null");
            this.setVisibility(View.GONE);
        }
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        String mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(context, path);
            Log.e("TAG", path);
            Log.e("TAG", mFilePath);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e("TAG", e.getMessage());
            return null;
        }
        return core;
    }

    @Override
    public void doFly(int index, int flyterword) {
        if (0 == flyterword) {
            if (currentpage > 1)
                currentpage--;
        } else {
            if (currentpage < core.countPages())
                currentpage++;
        }
    }

    /**
     * 关闭View
     *
     * @return
     */
    public boolean finshView() {
        if (this.getVisibility() == View.VISIBLE) {
            this.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }
}
