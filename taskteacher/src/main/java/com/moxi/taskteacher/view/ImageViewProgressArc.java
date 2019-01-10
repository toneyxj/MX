package com.moxi.taskteacher.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.moxi.taskteacher.R;
import com.moxi.taskteacher.TaskApplication;
import com.moxi.taskteacher.teainterface.UploadImageStatus;
import com.mx.mxbase.utils.ImageLoadUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 圆形进度提示
 * Created by Administrator on 2016/10/21.
 */
public class ImageViewProgressArc extends ImageView implements View.OnClickListener {
    private UploadImageStatus uploadSucess;
    /**
     * 绘制外圆颜色
     */
    private int arcColor = getContext().getResources().getColor(R.color.colorgray50);
    /**
     * 绘制圆的直径
     */
    private int arcWidth;
    /**
     * 绘制圆本来控件的高度
     */
    private int kitHeight;
    /**
     * 绘制圆本来控件的宽度
     */
    private int kitWidth;
    /**
     * 加载总大小设置
     */
    private int endNumber = 0;
    /**
     * 当前加载大小
     */
    private int currentNumber = 0;
    /**
     * 当前进度显示文字100为加载完毕
     */
    private int progressTxt = 0;
    // 画圆所在的距形区域
    private RectF mRectF;
    private int mCircleLineStrokeWidth = 8;
    private String uploadPath;
    private String path;
    private DisplayImageOptions options;

    private void setCurrentNumber(int currentNumber) {
        this.currentNumber = currentNumber;
        setShowTxt(currentNumber / (endNumber * 1f));
    }

    private void setShowTxt(float progress) {
        this.progressTxt = (int) (progress * 100);
        invalidate();
    }

    public void setUploadSucess(UploadImageStatus uploadSucess) {
        this.uploadSucess = uploadSucess;
    }

    public ImageViewProgressArc(Context context) {
        super(context);
        init();
    }


    public ImageViewProgressArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageViewProgressArc(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mRectF = new RectF();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        kitWidth = getMeasuredWidth();
        kitHeight = getMeasuredHeight();
        arcWidth = (getMeasuredWidth() > getMeasuredHeight() ? getMeasuredHeight() : getMeasuredWidth()) / 5;
        if (arcWidth > TaskApplication.ScreenWidth / 10)
            arcWidth = TaskApplication.ScreenWidth / 10;
        mCircleLineStrokeWidth = arcWidth / 20;
    }

    /**
     * 加载图片
     *
     * @param path 加载图片网络路径
     */
    public void loadImage(String path, DisplayImageOptions options) {
        this.path = path;
        this.options = options;
        this.uploadPath = path;

        setOnClickListener(null);
        ImageLoader.getInstance().displayImage(path, this, options, simpleImageLoadingListener, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, int currentPosotion, int totalPosotion) {
                setCurrentNumber(currentPosotion);
                endNumber = totalPosotion;
            }
        });
    }

    SimpleImageLoadingListener simpleImageLoadingListener = new SimpleImageLoadingListener() {
        @Override
        public void onLoadingStarted(String imageUri, View view) {
            if (uploadSucess != null)
                uploadSucess.onUploadImage(ImageViewProgressArc.this, false, uploadPath, 1);
        }

        @Override
        public void onLoadingFailed(String imageUri, View view,
                                    FailReason failReason) {
            setOnClickListener(ImageViewProgressArc.this);
            if (uploadSucess != null)
                uploadSucess.onUploadImage(ImageViewProgressArc.this, false, uploadPath, 2);
        }

        @Override
        public void onLoadingComplete(String imageUri,
                                      View view, Bitmap loadedImage) {
            if (uploadSucess != null)
                uploadSucess.onUploadImage(ImageViewProgressArc.this, false, uploadPath, 3);
        }

        public void onLoadingCancelled(String imageUri, View view) {
            if (uploadSucess != null)
                uploadSucess.onUploadImage(ImageViewProgressArc.this, false, uploadPath, 2);
        }
    };

    public void loadImage(String path) {
        loadImage(path, ImageLoadUtils.getoptions());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (endNumber != 0 && endNumber > currentNumber) {
            initArc(canvas);
        }
    }

    /**
     * 绘制外圆
     *
     * @param canvas
     */
    private void initArc(Canvas canvas) {
        //绘制外圆
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(arcColor);
        paint.setStrokeWidth(mCircleLineStrokeWidth);

        mRectF.left = (kitWidth / 2) - arcWidth / 2 + mCircleLineStrokeWidth / 2; // 左上角x
        mRectF.top = (kitHeight / 2) - arcWidth / 2 + mCircleLineStrokeWidth / 2; // 左上角y
        mRectF.right = (kitWidth / 2) + arcWidth / 2 - mCircleLineStrokeWidth / 2; // 左下角x
        mRectF.bottom = (kitHeight / 2) + arcWidth / 2 - mCircleLineStrokeWidth / 2; // 右下角y
        canvas.drawArc(mRectF, -90, 360, false, paint);
        paint.setColor(arcColor);

        mRectF.left = (float) (mRectF.left + mCircleLineStrokeWidth * 1.3); // 左上角x
        mRectF.top = (float) (mRectF.top + mCircleLineStrokeWidth * 1.3); // 左上角y
        mRectF.right = (float) (mRectF.right - mCircleLineStrokeWidth * 1.3); // 左下角x
        mRectF.bottom = (float) (mRectF.bottom - mCircleLineStrokeWidth * 1.3); // 右下角y

        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(mRectF, -90, ((float) progressTxt / 100) * 360, true, paint);
    }

    @Override
    public void onClick(View v) {
        loadImage(path, options);
    }
}
