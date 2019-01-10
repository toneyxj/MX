package com.dangdang.reader.dread.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.dangdang.reader.R;
import com.dangdang.reader.dread.config.ReadConfig;
import com.dangdang.reader.dread.core.epub.GlobalWindow.IFloatingOperation;
import com.dangdang.reader.dread.data.BookNote;
import com.dangdang.reader.view.MyPopupWindow;
import com.dangdang.zframework.utils.UiUtil;
import com.dangdang.zframework.view.DDTextView;
import com.mx.mxbase.constant.APPLog;

public class FloatingWindow {

    private Context mContext;
    private PopupWindow mWindow;
    private View mParent;
    private View mContentView;
    private int mCurrentX;
    private int mCurrentY;
    private int mShowAtPosX;
    private int mShowAtPosY;
    private boolean mIsPdf = false;
    private View mSeperatorView;
    private View mColorsView;
    private boolean mIsDirectionUp = false;
    private int mDrawlineColorViewHeight = 0;

    private IFloatingOperation mOperCallback;

    private int mDrawLineColor = BookNote.NOTE_DRAWLINE_COLOR_RED;
    private int mCurSelectDrawLineColorID = R.id.read_fw_drawline_color_red;

    public FloatingWindow(Context context, View parent) {
        mContext = context;
        mParent = parent;

        mContentView = View.inflate(mContext, R.layout.read_floatingwindow,
                null);
        mWindow = new MyPopupWindow(mContentView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        mSeperatorView = mContentView.findViewById(R.id.read_fw_seperator);
        mColorsView = mContentView.findViewById(R.id.read_fw_drawline_colors);
        mContentView.findViewById(R.id.read_fw_copy).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_check_dir).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_delete).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_share).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_note).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline_color_yellow).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline_color_green).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline_color_blue).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline_color_pink).setOnClickListener(
                mClickListener);
        mContentView.findViewById(R.id.read_fw_drawline_color_red).setOnClickListener(
                mClickListener);

        setCurSelectDrawLineColorID(mCurSelectDrawLineColorID);

        int colorsHeight = UiUtil.dip2px(mContext, 28);
        int seperatorHeight = UiUtil.dip2px(mContext, (float) 16.5);
        mDrawlineColorViewHeight = colorsHeight + seperatorHeight;
    }

    public void show(int currentX, int currentY, int x, int y, boolean direction) {
        setBackground(direction);
        mShowAtPosX = x;
        mShowAtPosY = y;
        mCurrentX = currentX;
        mCurrentY = currentY;
        if (mColorsView.getVisibility() == View.VISIBLE && !mIsDirectionUp) {
            y -= mDrawlineColorViewHeight;
        }

        mWindow.showAtLocation(mParent, Gravity.NO_GRAVITY, x, y);
    }

    private void setBackground(boolean direction) {
        View view = mContentView.findViewById(R.id.read_fw_bglayout);
        APPLog.e("修改了标记界面");
//		if (direction) {
//			view.setBackgroundResource(R.drawable.reader_note_arrow_up_empty);
//		} else {
//			view.setBackgroundResource(R.drawable.reader_note_arrow_down_empty);
//		}
        mIsDirectionUp = direction;
    }

    /**
     * @param isShowDelete false 显示删除按钮
     */
    public void setDrawLineOrDelete(boolean isShowDelete) {
        if (mIsPdf) {
            return;
        }

        DDTextView drawLineView = (DDTextView) mContentView.findViewById(R.id.read_fw_drawline);
        View seperatorView = mContentView.findViewById(R.id.read_fw_seperator);
        View drawlinecolorsView = mContentView.findViewById(R.id.read_fw_drawline_colors);
        if (isShowDelete) {
//            drawLineView.setTextColor(Color.WHITE);
            seperatorView.setVisibility(View.GONE);
            drawlinecolorsView.setVisibility(View.GONE);
            drawLineView.setEnabled(true);
        } else {
//            drawLineView.setTextColor(Color.RED);
            seperatorView.setVisibility(View.GONE);
            drawlinecolorsView.setVisibility(View.GONE);
            drawLineView.setEnabled(true);
        }
    }

    public void initIsPdf(boolean isPdf) {
        mIsPdf = isPdf;
        if (isPdf) {
            mContentView.findViewById(R.id.read_fw_drawline).setVisibility(View.GONE);
            mContentView.findViewById(R.id.read_fw_delete).setVisibility(View.GONE);
            mContentView.findViewById(R.id.read_fw_note).setVisibility(View.GONE);

            mContentView.findViewById(R.id.read_fw_firstdivide).setVisibility(View.GONE);

            mColorsView.setVisibility(View.GONE);
            mSeperatorView.setVisibility(View.GONE);
        }
    }

    public void hide() {
        if (mWindow != null) {
            mWindow.dismiss();
        }
    }

    public boolean isShowing() {
        return mWindow != null && mWindow.isShowing();
    }

    public boolean isShowDelete() {
        View view = mContentView.findViewById(R.id.read_fw_drawline_colors);
        return view.getVisibility() == View.VISIBLE;
    }

    public void setFloatingOperation(IFloatingOperation l) {
        mOperCallback = l;
        mOperCallback.onSetCurDrawLineColor(mDrawLineColor);
    }

    final OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOperCallback == null) {
                return;
            }
            int i = v.getId();
            if (i == R.id.read_fw_copy) {
                mOperCallback.onCopy();

            } else if (i == R.id.read_fw_drawline) {
//				setCurSelectDrawLineColorIDByColor(mDrawLineColor);
                APPLog.e("布局隐藏改造");
//				mSeperatorView.setVisibility(View.VISIBLE);
//				mColorsView.setVisibility(View.VISIBLE);
//				((TextView) mContentView.findViewById(R.id.read_fw_drawline)).setTextColor(Color.RED);
                /**替换画线代码*/
//				if (!mIsDirectionUp) {
//					mWindow.update(0, mShowAtPosY - mDrawlineColorViewHeight, -1, -1);
//				}
//
//				mOperCallback.onMarkSelected(true, "", mDrawLineColor, false);
                /**替换画线代码结束*/
                /**添加删除*/
                mOperCallback.onDelete();
            } else if (i == R.id.read_fw_delete) {
                mOperCallback.onDelete();
            }else if (i == R.id.read_check_dir) {//字典查询
                mOperCallback.chackDir();
            } else if (i == R.id.read_fw_note) {
                mOperCallback.onNote(!isShowDelete());

            } else if (i == R.id.read_fw_drawline_color_yellow) {
                setDrawLineColor(BookNote.NOTE_DRAWLINE_COLOR_YELLOW);
                mOperCallback.onMarkSelected(false, "", mDrawLineColor, true);

            } else if (i == R.id.read_fw_drawline_color_green) {
                setDrawLineColor(BookNote.NOTE_DRAWLINE_COLOR_GREEN);
                mOperCallback.onMarkSelected(false, "", mDrawLineColor, true);

            } else if (i == R.id.read_fw_drawline_color_blue) {
                setDrawLineColor(BookNote.NOTE_DRAWLINE_COLOR_BLUE);
                mOperCallback.onMarkSelected(false, "", mDrawLineColor, true);

            } else if (i == R.id.read_fw_drawline_color_pink) {
                setDrawLineColor(BookNote.NOTE_DRAWLINE_COLOR_PINK);
                mOperCallback.onMarkSelected(false, "", mDrawLineColor, true);

            } else if (i == R.id.read_fw_drawline_color_red) {
                setDrawLineColor(BookNote.NOTE_DRAWLINE_COLOR_RED);
                mOperCallback.onMarkSelected(false, "", mDrawLineColor, true);

            }else if (i==R.id.read_fw_share){//分享
                mOperCallback.onShare();
            }else {
            }
//			if (v.getId() != R.id.read_fw_drawline) {
            hide();
//			}
        }
    };

    private void setSelectedColorViewAndCancelOther(int id) {
        mContentView.findViewById(mCurSelectDrawLineColorID).setSelected(false);
        mContentView.findViewById(id).setSelected(true);
        mCurSelectDrawLineColorID = id;
    }

    public int getDrawLineColor() {
        return mDrawLineColor;
    }

    public void setDrawLineColor(int drawLineColor) {
        APPLog.e("setDrawLineColor", drawLineColor);
        this.mDrawLineColor = drawLineColor;
        if (mOperCallback != null)
            mOperCallback.onSetCurDrawLineColor(drawLineColor);
        setCurSelectDrawLineColorIDByColor(mDrawLineColor);
        ReadConfig config = ReadConfig.getConfig();
        config.setNoteDrawlineColor(drawLineColor);
    }

    public void setCurSelectDrawLineColorID(int id) {
        setSelectedColorViewAndCancelOther(id);
    }

    public void setCurSelectDrawLineColorIDByColor(int color) {
        if (color == BookNote.NOTE_DRAWLINE_COLOR_YELLOW)
            setCurSelectDrawLineColorID(R.id.read_fw_drawline_color_yellow);
        else if (color == BookNote.NOTE_DRAWLINE_COLOR_GREEN)
            setCurSelectDrawLineColorID(R.id.read_fw_drawline_color_green);
        else if (color == BookNote.NOTE_DRAWLINE_COLOR_BLUE)
            setCurSelectDrawLineColorID(R.id.read_fw_drawline_color_blue);
        else if (color == BookNote.NOTE_DRAWLINE_COLOR_PINK)
            setCurSelectDrawLineColorID(R.id.read_fw_drawline_color_pink);
        else if (color == BookNote.NOTE_DRAWLINE_COLOR_RED)
            setCurSelectDrawLineColorID(R.id.read_fw_drawline_color_red);
    }

}
