package com.example.test;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import java.io.File;


public class MainActivity extends Activity {

    PdfFragment fragment,fragment1;
    private static final String SAMPLE_FILE = "sample.pdf";
    private static final String FILE_PATH = "filepath";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openPdfWithFragment();

    }

    public void openPdfWithFragment() {
        fragment = new PdfFragment();
        //fragment1 = new PdfFragment();
        Bundle args = new Bundle();
        args.putString(FILE_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + SAMPLE_FILE);
        fragment.setArguments(args);
        //fragment1.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
       // fragmentManager.beginTransaction().replace(R.id.content_frame2, fragment1).commit();
    }

    public void godemo(View v){
        startActivity(new Intent(this,DemoActivity.class));
    }
}
