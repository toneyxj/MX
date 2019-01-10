package com.moxi.mxwriter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private String packageName = "com.onyx.android.note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isAppInstalled(this,packageName)){
            try{
                Log.d("writer","installed");
                runApp(packageName);
                finish();
            }catch (Exception e){
                Log.d("writer","installed Exception:" + e.getMessage());
                finish();
            }

        }else{
            // TODO: 2016/10/19 未安装
            Log.d("writer","未安装...");
            finish();
        }

    }

    public boolean isAppInstalled(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        List<String> pName = new ArrayList<String>();
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                pName.add(pn);
            }
        }
        return pName.contains(packageName);
    }

    private void runApp(String packageName) throws Exception {
        PackageInfo pi;
        pi = getPackageManager().getPackageInfo(packageName, 0);
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.setPackage(pi.packageName);
        PackageManager pManager = getPackageManager();
        List apps = pManager.queryIntentActivities(
                resolveIntent, 0);

        ResolveInfo ri = (ResolveInfo)apps.iterator().next();
        if (ri != null) {
            packageName = ri.activityInfo.packageName;
            String className = ri.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            startActivity(intent);
        }

    }

}
