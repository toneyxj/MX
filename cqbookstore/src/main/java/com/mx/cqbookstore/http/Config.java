package com.mx.cqbookstore.http;


import android.os.Environment;

import java.io.File;

import dalvik.annotation.TestTarget;

/**
 * Created by Administrator on 2016/12/20.
 */

public class Config {
    public final static String IMAGPATH="http://114.55.173.70:8015//Cover//";
    public final static String DOWNPATH= Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Ebook";
    public final static String HOST="http://114.55.173.70:8016/MxService.asmx";
    public final static String SPACENMAE="http://tempuri.org/";
    public final static String ACTION="Mx_SearchResource";
    public final static String ACTION_RESOUCEDETAIL="Mx_SelectExResourceDetail";
    public final static String ACTION_ADDLIKE="Mx_CollectResource";
    public final static String ACTION_CANCLELIKE="Mx_CancelCollect";
    public final static String ACTION_LOGIN="Mx_GetUser";
    public final static String ACTION_GETRESOUCE_URL="Mx_GetEpubById";
    public final static String ACTION_PAY="Mx_GetCode";
    public final static String ACTION_FAVORLIST="Mx_GetCollectionList";
    public final static String ACTION_RESOUCEKEY="Mx_GetKeyById";
    public final static String ACTION_BOUGHTRESOUCES="Mx_GetPurchasedByUserId";
    public final static String ACTION_PROBATION="Mx_GetEpubProbation";
}
