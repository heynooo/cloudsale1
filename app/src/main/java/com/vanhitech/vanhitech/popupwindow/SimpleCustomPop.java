package com.vanhitech.vanhitech.popupwindow;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.flyco.dialog.widget.popup.base.BasePopup;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.utils.T;

/**
 * 创建者     heyn
 * 创建时间   2016/4/27 0:10
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SimpleCustomPop extends BasePopup<SimpleCustomPop> {
    private TextView mTvItem1;
    private TextView mTvItem2;
    private TextView mTvItem3;
    private TextView mTvItem4;

    public SimpleCustomPop(Context context) {
        super(context);
    }

    @Override
    public View onCreatePopupView() {
        View inflate = View.inflate(mContext, R.layout.popup_custom, null);
        mTvItem1 = (TextView) inflate.findViewById(R.id.tv_item_1);
        mTvItem2 = (TextView) inflate.findViewById(R.id.tv_item_2);
        mTvItem3 = (TextView) inflate.findViewById(R.id.tv_item_3);
        mTvItem4 = (TextView) inflate.findViewById(R.id.tv_item_4);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mTvItem1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, mTvItem1.getText());
            }
        });
        mTvItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, mTvItem2.getText());
            }
        });
        mTvItem3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, mTvItem3.getText());
            }
        });
        mTvItem4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                T.showShort(mContext, mTvItem4.getText());
            }
        });
    }
}
