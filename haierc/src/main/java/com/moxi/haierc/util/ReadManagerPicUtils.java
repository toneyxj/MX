package com.moxi.haierc.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.TextView;

import com.mx.mxbase.constant.APPLog;
import com.mx.mxbase.constant.PhotoConfig;
import com.mx.mxbase.utils.StringUtils;
import com.onyx.android.sdk.data.cms.OnyxBookProgress;
import com.onyx.android.sdk.data.cms.OnyxCmsCenter;
import com.onyx.android.sdk.data.cms.OnyxMetadata;
import com.onyx.android.sdk.data.cms.OnyxThumbnail;
import com.onyx.android.sdk.data.util.RefValue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xj on 2017/12/25.
 */

public class ReadManagerPicUtils {
    private static ReadManagerPicUtils instatnce = null;
    private Map<String, OnyxMetadata> mapMetdatas = null;

    public static ReadManagerPicUtils getInstance() {
        if (instatnce == null) {
            synchronized (ReadManagerPicUtils.class) {
                if (instatnce == null) {
                    instatnce = new ReadManagerPicUtils();
                }
            }
        }
        return instatnce;
    }

    private synchronized Map<String, OnyxMetadata> getMapMetdatas() {
        if (mapMetdatas == null) {
            mapMetdatas = new HashMap<>();
        }
        return mapMetdatas;
    }

    public void clearMetdata() {
        if (mapMetdatas != null) {
            mapMetdatas.clear();
            mapMetdatas = null;
        }
    }

    private void addpic(final Context context, final String path) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                APPLog.e("addpic-path", path);
                OnlyXMetadataManager onlyXMetadataManager = new OnlyXMetadataManager();
                onlyXMetadataManager.initOnlyXMetadataManager(context, path);
            }
        }).start();
    }

    private void initReadManagerPicUtils(Context context) {
        final Cursor cursor = context.getContentResolver().query(OnyxMetadata.CONTENT_URI, null, null, null, null);
        if (cursor == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (cursor.moveToNext()) {
                    OnyxMetadata item = OnyxMetadata.Columns.readColumnData(cursor);
                    if (item == null) {
                        break;
                    }
                    APPLog.e("item.getName", item.getName());
                    getMapMetdatas().put(item.getName(), item);
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }).start();

    }

    private String getFileMD5(Context context, String filePath) {
        if (null == mapMetdatas && getMapMetdatas().size() == 0) {
            initReadManagerPicUtils(context);
        }
        File file = new File(filePath);
        OnyxMetadata data = mapMetdatas.get(file.getName());
        if (data != null) {
            return data.getMD5();
        }
        return null;
    }

    /**
     * 更新页面进度
     * @param context 当前上下文
     * @param filePath 文件路径
     * @param newprogress 设置的新的进度
     * @return
     */
    public boolean updateMetadata(Context context,String filePath, int newprogress){
        try {
            OnyxMetadata onyxMetadata = OnyxCmsCenter.getMetadata(context, filePath);
            if (onyxMetadata.getProgress().getTotal()>newprogress) {
                OnyxBookProgress progress= new OnyxBookProgress(newprogress,onyxMetadata.getProgress().getTotal());
                onyxMetadata.setProgress(progress);
                OnyxCmsCenter.updateMetadata(context, onyxMetadata);
            }
        }catch (Exception e){
            return false;
        }
        return true;

    }

    public void setLocationBookPic(Context context, ImageView view, String filePath, TextView read_progress) {
        try {
            read_progress.setText("");
            String md5 = getFileMD5(context, filePath);
            OnyxMetadata onyxMetadata = OnyxCmsCenter.getMetadata(context, filePath);
            //获取阅读进度
            try {
                OnyxBookProgress progress=onyxMetadata.getProgress();
                read_progress.setText("已读:"+progress.toString());
                StringUtils.setReadBookProgress(progress.getTotal(),progress.getCurrent(),read_progress);
            }catch (Exception e){
                read_progress.setText("未读");
            }

            RefValue<Bitmap> refValue = new RefValue<Bitmap>();
            if (md5 == null && OnyxCmsCenter.getThumbnail(context,
                    onyxMetadata, OnyxThumbnail.ThumbnailKind.Middle, refValue)) {
                setImage(refValue.getValue(), view);
            } else if (md5 != null && OnyxCmsCenter.getThumbnailByMD5(context, md5, OnyxThumbnail.ThumbnailKind.Middle, refValue)) {
                setImage(refValue.getValue(), view);
            } else {
                view.setImageResource(PhotoConfig.getSources(filePath));
            }
            if (md5 == null) {
                ReadManagerPicUtils.getInstance().addpic(context, filePath);
            }

        } catch (Exception e) {
            e.printStackTrace();
            view.setImageResource(PhotoConfig.getSources(filePath));
        }
    }

    private void setImage(Bitmap bitmap, ImageView view) {
        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            view.setImageBitmap(bitmap);
        }
    }
}
