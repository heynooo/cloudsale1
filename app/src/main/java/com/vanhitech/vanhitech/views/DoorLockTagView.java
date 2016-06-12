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
 * 描述	     门锁状态标签
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class DoorLockTagView extends RelativeLayout {

    private LinearLayout mLayoutDoorlockTag;
    private TextView mTvCurrentstatus;
    private TextView mTvHistoricalstate;

    public DoorLockTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

        initEvent();

        initdata();

    }

    private void initdata() {//用于
        isLeftSelect=true;
        mTvCurrentstatus.setSelected(false);//TODO
        mTvHistoricalstate.setSelected(false);

    }

    public DoorLockTagView(Context context) {
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
                        mTvCurrentstatus.setSelected(true);
                        mTvHistoricalstate.setSelected(false);
                        mTvHistoricalstate.setBackgroundDrawable(getResources().getDrawable(R.mipmap.lightrgb_tab1));  //TODO
                        break;
                    case R.id.tv_light_tag_uncolor:
                        isLeftSelect = true;
                        mTvCurrentstatus.setSelected(false);
                        mTvHistoricalstate.setSelected(true);
                        mTvHistoricalstate.setBackgroundDrawable(getResources().getDrawable(R.mipmap.lightrgb_tab2));
                        break;
                    default:
                        break;
                }
                if (DoorLockTagView.this.listener != null) {
            DoorLockTagView.this.listener.tagChange(DoorLockTagView.this,isLeftSelect);

                }

            }
        };
            mTvCurrentstatus.setOnClickListener(listener);
            mTvHistoricalstate.setOnClickListener(listener);

    }
    private FragmentManager fragmentManager;
    private FrameLayout fl_content;
    /**
     * 视图
     */
    private void initView() {
        View view = View.inflate(getContext(), R.layout.doorlock_tag_view, this);
        //通过注入找孩子
//        ViewUtils.inject(this, view);
        //TODO 这里可能会有布局风险
        mLayoutDoorlockTag = (LinearLayout) view.findViewById(R.id.layout_doorlock_tag);
        mTvCurrentstatus = (TextView) view.findViewById(R.id.tv_currentstatus);
        mTvHistoricalstate = (TextView) view.findViewById(R.id.tv_historicalstate);
    }





}
