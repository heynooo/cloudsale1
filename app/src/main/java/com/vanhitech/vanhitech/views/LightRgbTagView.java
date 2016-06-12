package com.vanhitech.vanhitech.views;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanhitech.vanhitech.R;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 9:39
 * 描述	     rgb 灯控制－彩色－暖色标签  接口回调
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LightRgbTagView extends RelativeLayout {


    private TextView mColor;
    private TextView mUncolor;
    private LinearLayout mRbglayout;

    public LightRgbTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        initEvent();

        initdata();

    }

    private void initdata() {//用于
        isLeftSelect=true;
        mColor.setSelected(false);//TODO
        mUncolor.setSelected(false);

    }


    public LightRgbTagView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }



    /**
     * 点击标签变化的事件回调
     */
    public interface OnTagChangeListener {//TODO

        public void tagChange(View view, boolean isLeftSelect);
    }

    private OnTagChangeListener listener;

    public void setOnTagChangeListener(OnTagChangeListener listener) {
        this.listener = listener;
    }

    private boolean isLeftSelect = true; //默认值


    /**
     * 事件处理
     */
    private void initEvent() {
        //TODO
        OnClickListener listener = new OnClickListener() {


            @Override
            public void onClick(View v) {

                switch (v.getId()) {
                    case R.id.tv_light_tag_color:
                        isLeftSelect = false;
                        mColor.setSelected(true);
                        mUncolor.setSelected(false);
                        mRbglayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.lightrgb_tab1));  //TODO
                        break;
                    case R.id.tv_light_tag_uncolor:
                        isLeftSelect = true;
                        mColor.setSelected(false);

                        mUncolor.setSelected(true);
                        mRbglayout.setBackgroundDrawable(getResources().getDrawable(R.mipmap.lightrgb_tab2));
                        break;
                    default:
                        break;
                }
                if (LightRgbTagView.this.listener != null) {
            LightRgbTagView.this.listener.tagChange(LightRgbTagView.this,isLeftSelect);

                }

            }
        };
            mColor.setOnClickListener(listener);
            mUncolor.setOnClickListener(listener);

    }
    private FragmentManager fragmentManager;
    private FrameLayout fl_content;
    /**
     * 视图
     */
    private void initView() {
        View view = View.inflate(getContext(), R.layout.light_rgb_tag_view, this);
        mColor = (TextView) view.findViewById(R.id.tv_light_tag_color);
        mUncolor = (TextView) view.findViewById(R.id.tv_light_tag_uncolor);
        mRbglayout = (LinearLayout) view.findViewById(R.id.layout_rbg_tag);
    }


}
