package com.example.test;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.artifex.mupdfdemo.PageFling;
import com.artifex.mupdfdemo.SearchTask;

public class PdfFragment extends Fragment implements PageFling  {
    private RelativeLayout mainLayout;
    private MuPDFCore core;
    private MuPDFReaderView mDocView;
    private Context mContext;
    private String mFilePath;
    Bundle args = new Bundle();
    private static final String TAG = "PdfFragment";

    public PdfFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        args = this.getArguments();
        mFilePath = args.getString("filepath");
        View rootView = inflater.inflate(R.layout.pdf, container, false);
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.pdflayout);
        mDocView=(MuPDFReaderView) rootView.findViewById(R.id.mupdf_rv);
        core = openFile(Uri.decode(mFilePath));

        if (core != null && core.countPages() == 0) {
            core = null;
        }
        if (core == null || core.countPages() == 0 || core.countPages() == -1) {
            Log.e(TAG, "Document Not Opening");
        }
        if (core != null) {
            /*mDocView = new MuPDFReaderView(getActivity()) {
                @Override
                protected void onMoveToChild(int i) {
                    if (core == null)
                        return;
                    super.onMoveToChild(i);
                }

            };*/
            mDocView.setVisibility(View.VISIBLE);
            mDocView.setPageFlyListener(this);
            mDocView.setAdapter(new MuPDFPageAdapter(mContext, core));
//            mainLayout.addView(mDocView);
            currentpage=1;
            Log.e("dofly:",currentpage+"/"+core.countPages());
        }

        /*mSearchTask = new SearchTask(mContext, core) {

            @Override
            protected void onTextFound(SearchTaskResult result) {
                SearchTaskResult.set(result);
                mDocView.setDisplayedViewIndex(result.pageNumber);
                mDocView.resetupChildren();
            }
        };*/

        return rootView;
    }
/*
    public void search(int direction, String text) {
        int displayPage = mDocView.getDisplayedViewIndex();
        SearchTaskResult r = SearchTaskResult.get();
        int searchPage = r != null ? r.pageNumber : -1;
        mSearchTask.go(text, direction, displayPage, searchPage);
    }*/


    private MuPDFCore openBuffer(byte buffer[]) {
        System.out.println("Trying to open byte buffer");
        try {
            core = new MuPDFCore(mContext, buffer);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }

    private MuPDFCore openFile(String path) {
        int lastSlashPos = path.lastIndexOf('/');
        mFilePath = new String(lastSlashPos == -1
                ? path
                : path.substring(lastSlashPos + 1));
        try {
            core = new MuPDFCore(mContext, path);
            Log.e("path:",path);
            Log.e("filePath:",mFilePath);
            // New file: drop the old outline data
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
        return core;
    }
    int currentpage;
    /*public void goLast(View v){
        if (currentpage>0) {
            currentpage--;
           // mDocView.setDisplayedViewIndex(corruntpage);
            core.mySetPage(currentpage);
        }
    }
    public void goNext(View v){
        if (currentpage<mDocView.getCount()) {
            currentpage++;
            //mDocView.setDisplayedViewIndex(corruntpage);
            core.mySetPage(currentpage);
        }
    }*/


    public void onDestroy() {
        if (core != null)
            core.onDestroy();
        core = null;
        super.onDestroy();
    }




    @Override
    public void doFly(int index,int flyterword) {
       if (0==flyterword) {
           if (currentpage>1)
           currentpage--;
       }else {
           if (currentpage<core.countPages())
           currentpage++;
       }
        Log.e("dofly:",currentpage+"/"+core.countPages());
    }
}


