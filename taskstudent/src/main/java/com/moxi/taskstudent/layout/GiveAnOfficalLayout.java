package com.moxi.taskstudent.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.moxi.taskstudent.R;
import com.moxi.taskstudent.config.Connector;
import com.moxi.taskstudent.model.WorkListModel;
import com.moxi.taskstudent.taskInterface.MainInterface;
import com.moxi.taskstudent.view.ImageViewProgressArc;

/**
 * 批复显示界面
 * Created by Administrator on 2016/12/21.
 */
public class GiveAnOfficalLayout extends BaseLayout{
private WorkListModel model;
    public GiveAnOfficalLayout(Context context, MainInterface mainInterface) {
        super(context, mainInterface);

    }
    private ImageViewProgressArc give_an_offical_image;
    private Button click_close;

    @Override
    int getLayout() {
        return R.layout.addview_give_an_offical;
    }

    @Override
    void initLayout(View view) {
        give_an_offical_image=(ImageViewProgressArc)view.findViewById(R.id.give_an_offical_image);
        click_close=(Button) view.findViewById(R.id.click_close);

        click_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mainInterface.closeWork();
            }
        });
    }
    public void initView(WorkListModel model){
        this.model=model;
        give_an_offical_image.loadImage(Connector.getInstance().getDownloadFileURL(2,model.replyFile));
    }
}
