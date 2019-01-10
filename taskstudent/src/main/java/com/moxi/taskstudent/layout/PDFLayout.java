package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.taskInterface.MainInterface;

/**
 * Created by Administrator on 2016/12/23.
 */
public class PDFLayout extends BaseLayout{
    public PDFLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);
    }
//    private PdfView pdf_show;
    @Override
    int getLayout() {
        return R.layout.addview_pdf_layout;
    }

    public void openPdf(String path){
        if (path!=null) {
//            pdf_show.openPdf(path);
        }
    }
    @Override
    void initLayout(View view) {
//        pdf_show=(PdfView)view.findViewById(R.id.pdf_show);
    }
}
