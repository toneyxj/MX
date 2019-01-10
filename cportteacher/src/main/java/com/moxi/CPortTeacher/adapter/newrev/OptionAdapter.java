package com.moxi.CPortTeacher.adapter.newrev;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.moxi.CPortTeacher.R;
import com.moxi.CPortTeacher.model.OptionsModel;
import com.moxi.CPortTeacher.utils.MxgsaTagHandler;
import com.mx.mxbase.utils.Base64Utils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by King on 2017/3/22.
 */

public class OptionAdapter extends BaseAdapter {

    private Context context;
    private List<OptionsModel.OptionsBean.ABCDSBean> listABCD;

    public OptionAdapter(Context context, List<OptionsModel.OptionsBean.ABCDSBean> listABCD) {
        this.context = context;
        this.listABCD = listABCD;
    }

    @Override
    public int getCount() {
        return listABCD == null ? 0 : listABCD.size();
    }

    @Override
    public Object getItem(int i) {
        return listABCD.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_options_item_layout, null);
            viewHolder.optionId = (TextView) convertView.findViewById(R.id.tv_options_id);
            viewHolder.optionDesc = (TextView) convertView.findViewById(R.id.tv_options_desc);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        setTitle(listABCD.get(i).getId() + "    " + listABCD.get(i).getDesc(), viewHolder.optionDesc);
        return convertView;
    }

    public class ViewHolder {
        TextView optionId;
        TextView optionDesc;
    }

    /**
     * @param title
     * @param view
     */
    private void setTitle(String title, TextView view) {
        Pattern p = Pattern.compile("<img[^>]+src\\s*=\\s*['\"](\\s*)(data:image/)\\S+(base64,)([^'\"]+)['\"][^>]*>");
        Matcher m = p.matcher(title);
        while (m.find()) {
            String str = m.group(4);
            title = title.replace(m.group(), "#@M#@X@" + str + "#@M#@X@");
        }
        String titleResult = title;
        if (titleResult.indexOf("#@M#@X@") > 0) {
            String[] s = titleResult.split("#@M#@X@");
            view.setText("");
            for (int j = 0; j < s.length; j++) {
                if (j % 2 == 0) {
                    view.append(Html.fromHtml(s[j]));
                } else {
                    Bitmap bitmap = Base64Utils.base64ToBitmap(s[j]);
                    ImageSpan imgSpan = new ImageSpan(context, bitmap);
                    SpannableString spanString = new SpannableString("icon");
                    spanString.setSpan(imgSpan, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    view.append(spanString);
                }
            }
        } else {
            view.setText(Html.fromHtml(title, null, new MxgsaTagHandler(context)));
        }
    }
}
