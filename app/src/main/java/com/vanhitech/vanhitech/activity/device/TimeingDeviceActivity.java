package com.vanhitech.vanhitech.activity.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD20_ModifyTimer;
import com.vanhitech.protocol.cmd.client.CMD22_DelTimer;
import com.vanhitech.protocol.cmd.client.CMD24_QueryTimer;
import com.vanhitech.protocol.cmd.server.CMD21_ServerModifyTimerResult;
import com.vanhitech.protocol.cmd.server.CMD23_ServerDelTimerResult;
import com.vanhitech.protocol.cmd.server.CMD25_ServerQueryTimerResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.TimerInfo;
import com.vanhitech.protocol.object.TimerTask;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenu;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuCreator;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.TimeTaskDay;
import com.vanhitech.vanhitech.bean.UpdateTime;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.pulltorefresh.RefreshTime;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.views.SlipButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	     显示设备的定时情况
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TimeingDeviceActivity extends BaseActivityController implements PullToRefreshSwipeMenuListView.IXListViewListener {
    private PullToRefreshSwipeMenuListView mListView;

    private List<TimeTaskDay> mLis;
    private String mDeviceId;
    private TimeingAdapter mTimeingAdapter;
    private Boolean sw = true;
    private BitmapUtils mBitmapUtils;
    private Context mContext = this;
    private ImageView mIvAdd;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:

                    if (MyApplication.getIsbrushTime()) {
                        MyApplication.setIsbrushTime(false);
                        start(false);
                        CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mDeviceId);
                        CmdBaseActivity.getInstance().sendCmd(cmd24);
                        //  延迟一会
                        new Handler().postDelayed(new Runnable() {//延迟1秒去更新ui
                            public void run() {
                                if (isNull(mLis)) return;
                                if (mLis.size() > 0)
                                    mLis.clear();
                                mLis = DataUtils.getInstance().getTimeTaskDayDeviceId(mDeviceId);
                                sort();
                                mTimeingAdapter.notifyDataSetChanged();
                            }
                        }, 200);
                    }

                    break;
            }
        }
    };

    public void start(Boolean flag) {
        if (flag) {
            java.util.TimerTask tt = new java.util.TimerTask() {
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            Timer timer = new Timer();
            timer.schedule(tt, 1000, 1000);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_timeingdevice);
        assignViews();
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("定时");
        ActivityTitleManager.getInstance().changehelpText("添加");
    }

    @Override
    public void initListener() {
        super.initListener();

        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
    }

    private void assignViews() {
        mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.timeingdevice_listview);
        mIvAdd = (ImageView) findViewById(R.id.sence_add);
    }


    @Override
    public void initData() {
        super.initData();
        //从数据库取数据
        MyApplication.setIsbrushTime(true);
        start(MyApplication.getIsbrushTime());
        LoginConstants.timgingfresh = true;
        Intent intent = getIntent();
        mDeviceId = intent.getStringExtra("mDeviceId");

        CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mDeviceId);
        CmdBaseActivity.getInstance().sendCmd(cmd24);

        mLis = new ArrayList<>();
        //  延迟一会
        new Handler().postDelayed(new Runnable() {//延迟3秒去更新发送指令
            public void run() {
                mLis = DataUtils.getInstance().getTimeTaskDayDeviceId(mDeviceId);
                sort();
            }
        }, 2);


        //需要listview
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mBitmapUtils = new BitmapUtils(this);

        mTimeingAdapter = new TimeingAdapter();
        mListView.setAdapter(mTimeingAdapter);

        mListView.setPullRefreshEnable(true);//下拉刷新
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new ListViewCilickListener());


        buttonClickListener();

        swipeRefresh();
        CmdBaseActivity.getInstance().mHelper.setCommandListener(this);

    }

    private void buttonClickListener() {
        ActivityTitleManager.getInstance().help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TimeingDeviceActivity.this, TimeingDeviceSettingActivity.class);
                intent.putExtra("mDeviceId", mDeviceId);
                startActivity(intent);
            }
        });

        //再添加一个按键 ,和这个功能一样
        mIvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(TimeingDeviceActivity.this, TimeingDeviceSettingActivity.class);
                intent.putExtra("mDeviceId", mDeviceId);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRefresh() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        RefreshTime.setRefreshTime(TimeingDeviceActivity.this, df.format(new Date()));
        onLoad();
        CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mDeviceId);
        CmdBaseActivity.getInstance().sendCmd(cmd24);
        //  延迟一会
        new Handler().postDelayed(new Runnable() {//延迟3秒去更新发送指令
            public void run() {
                if (mLis != null)
                    mLis.clear();
                mLis = DataUtils.getInstance().getTimeTaskDayDeviceId(mDeviceId);
                sort();
                mTimeingAdapter.notifyDataSetChanged();
            }
        }, 200);
        onLoad();
    }

    private void sort() {
        if (mLis != null)
            Collections.sort(mLis, new Comparator<TimeTaskDay>() {

                @Override
                public int compare(TimeTaskDay lhs, TimeTaskDay rhs) {
                    if (Integer.parseInt(lhs.hour + String.format("%02d", Integer.parseInt(lhs.minute))) > Integer.parseInt(rhs.hour + String.format("%02d", Integer.parseInt(rhs.minute)))) {
                        return 1;
                    } else {
                        return -1;
                    }

                }

            });
    }

    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(TimeingDeviceActivity.this));
        mListView.stopRefresh();
        mListView.stopLoadMore();

    }

    @Override
    public void onLoadMore() {
        CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mDeviceId);
        CmdBaseActivity.getInstance().sendCmd(cmd24);
        //  延迟一会
        new Handler().postDelayed(new Runnable() {//延迟1秒去更新ui
            public void run() {
                if (isNull(mLis)) return;
                if (mLis.size() > 0)
                    mLis.clear();
                mLis = DataUtils.getInstance().getTimeTaskDayDeviceId(mDeviceId);
                sort();
                mTimeingAdapter.notifyDataSetChanged();
            }
        }, 200);
        onLoad();
    }

    class TimeingAdapter extends BaseAdapter {


        /**
         * getCount 里面存放 比如说 客厅有多少设备 这里就返回多少个
         * 这里的device 是一个
         *
         * @return
         */
        @Override
        public int getCount() {

            return null != mLis ? mLis.size() : 0;
        }

        @Override
        public Object getItem(int position) {
//            if (mDeviceList != null) {
//                return mDeviceList.get(position);//TODO
//            }
            return null != mLis ? mLis.get(position) : 0;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;  //根视图
            if (convertView == null) {

                convertView = View.inflate(TimeingDeviceActivity.this, R.layout.item_list_timeingdevice, null);
                holder = new ViewHolder();
                //设置id
                holder.layoutV = (LinearLayout) convertView.findViewById(R.id.layout_v);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                holder.tvtimeonoff = (TextView) convertView.findViewById(R.id.tv_timeonoff);
                holder.tvPower1 = (TextView) convertView.findViewById(R.id.tv_power1);
                holder.tvPower2 = (TextView) convertView.findViewById(R.id.tv_power2);
                holder.tvPower3 = (TextView) convertView.findViewById(R.id.tv_power3);
                holder.tvDay1 = (TextView) convertView.findViewById(R.id.tv_day1);
                holder.tvDay2 = (TextView) convertView.findViewById(R.id.tv_day2);
                holder.tvDay3 = (TextView) convertView.findViewById(R.id.tv_day3);
                holder.tvDay4 = (TextView) convertView.findViewById(R.id.tv_day4);
                holder.tvDay5 = (TextView) convertView.findViewById(R.id.tv_day5);
                holder.tvDay6 = (TextView) convertView.findViewById(R.id.tv_day6);
                holder.tvDay7 = (TextView) convertView.findViewById(R.id.tv_day7);
                holder.btSplitbutton = (SlipButton) convertView.findViewById(R.id.bt_splitbutton);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

//            Device devicebean = mDeviceList.get(position);

            TimeTaskDay timeTaskDay = mLis.get(position);
            //数据绑定
            //这里只负责去数据库取数据,数据刷新由下拉刷新负责,
//            holder.mTvName.setText(mDeviceList.get(position).name);
//            holder.mTvStatus.setText("暂时不设置");

            holder.tvTime.setText(timeTaskDay.hour + ":" + timeTaskDay.minute);
            if (isTureFalse(timeTaskDay.enabled)) {
                holder.tvtimeonoff.setText("开");
            } else {
                holder.tvtimeonoff.setText("关");
            }
            powerDayIsColor(holder.tvPower1, timeTaskDay.power0);
            powerDayIsColor(holder.tvPower2, timeTaskDay.power1);
            powerDayIsColor(holder.tvPower3, timeTaskDay.power2);

            powerDayIsColor(holder.tvDay1, timeTaskDay.D1);
            powerDayIsColor(holder.tvDay2, timeTaskDay.D2);
            powerDayIsColor(holder.tvDay3, timeTaskDay.D3);
            powerDayIsColor(holder.tvDay4, timeTaskDay.D4);
            powerDayIsColor(holder.tvDay5, timeTaskDay.D5);
            powerDayIsColor(holder.tvDay6, timeTaskDay.D6);
            powerDayIsColor(holder.tvDay7, timeTaskDay.D7);
            int hout = stringToInt(timeTaskDay.hour);
            if (hout > 7 && hout < 14) {//"assets/img/login_bg.bmp"
                mBitmapUtils.display(holder.layoutV, "assets/img/morning.bmp");//TODO
            } else if (hout > 13 && hout < 19) {
                mBitmapUtils.display(holder.layoutV, "assets/img/noon.bmp");//TODO
            } else if (hout > 18 && hout < 25) {
                mBitmapUtils.display(holder.layoutV, "assets/img/evening.bmp");//TODO
            } else if (hout > -1 && hout < 8) {
                mBitmapUtils.display(holder.layoutV, "assets/img/evening.bmp");//TODO
            }

//            switch (hout){
//                case 1:
//                    break;
//            }


            if (timeTaskDay.enabled.equals("1")) {
                holder.btSplitbutton.setCheck(true);
//            holder.btSplitbutton.setSelected(true);
//            holder.btSplitbutton.setBackgroundDrawable(getResources().getDrawable(R.mipmap.slipbutton_on_left));
            } else {
                holder.btSplitbutton.setCheck(false);
//            holder.btSplitbutton.setSelected(false);
//                holder.btSplitbutton.setBackgroundDrawable(getResources().getDrawable(R.mipmap.slipbutton_off_right));
            }
            setListener(holder.btSplitbutton, timeTaskDay);
            return convertView;
        }

        class ViewHolder {
            LinearLayout layoutV;
            TextView tvTime;
            TextView tvtimeonoff;
            TextView tvPower1;
            TextView tvPower2;
            TextView tvPower3;
            TextView tvDay1;
            TextView tvDay2;
            TextView tvDay3;
            TextView tvDay4;
            TextView tvDay5;
            TextView tvDay6;
            TextView tvDay7;
            SlipButton btSplitbutton;
        }
    }

    /**
     * 滑动删除
     * 下拉刷新
     */
    private void swipeRefresh() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(TimeingDeviceActivity.this);
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(45));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
//                SwipeMenuItem deleteItem = new SwipeMenuItem(TimeingDeviceActivity.this);
//                // set item background
//                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
//                // set item width
//                deleteItem.setWidth(dp2px(66));
//                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete);
//                // add to menu
//                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);


        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                TimeTaskDay item = mLis.get(position);
                switch (index) {
                    case 0:

                        mLis.remove(position);
                        //删除设备
                        try {
                            CMD22_DelTimer cmd22 = new CMD22_DelTimer(item.id);
                            CmdBaseActivity.getInstance().sendCmd(cmd22);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //返回cmd11 删除结果
                        //数据库也应该删掉
                        try {
                            DataUtils.getInstance().mDb.delete(TimeTaskDay.class, WhereBuilder.b("id", "=", item.id));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                        break;
                    case 1:
                        // delete
                        // delete(item);

                        break;
                }
                mTimeingAdapter.notifyDataSetChanged();
            }
        });


    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, TimeingDeviceActivity.this.getResources().getDisplayMetrics());
    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("timeing:" + mContext.getClass().getName() + cmd.toString());
        switch (cmd.CMDByte) {
            case CMDFF_ServerException.Command:
                CMDFF_ServerException cmdff = (CMDFF_ServerException) cmd;
                break;
            case CMD21_ServerModifyTimerResult.Command:
                CMD21_ServerModifyTimerResult cmd21 = (CMD21_ServerModifyTimerResult) cmd;
                if (cmd21.result) {
                    LogUtils.s("修改成功");
                } else {
                    LogUtils.s("修改失败");
                }
                break;
            case CMD23_ServerDelTimerResult.Command:
                //服务器返回删除定时任务结果
                CMD23_ServerDelTimerResult cmd23 = (CMD23_ServerDelTimerResult) cmd;
                if (cmd23.result) {
                    LogUtils.s("刪除成功");
                } else {
                    LogUtils.s("刪除失败");
                }
                break;
            case CMD25_ServerQueryTimerResult.Command:
//              public CMD25_ServerQueryTimerResult(List<TimerInfo> timers) {
                DataUtils.getInstance().save(new UpdateTime(1 + "", DateUtils.getCurTimeAddXM(25)));
                CMD25_ServerQueryTimerResult cmd25 = (CMD25_ServerQueryTimerResult) cmd;
                List<TimerInfo> timerInfos = cmd25.timerList;
                if (timerInfos.size() > 0) {
                    //删除 mDeviceId
                    DataUtils.getInstance().deleteTimerTask(mDeviceId);
                    for (TimerInfo info : timerInfos) {
                        try {
                            DataUtils.getInstance().TimeTaskDaySaveUpdate(info.schedinfo, info.ctrlinfo);
                            //保存TimeTaskDay
                            //保存TimeTaskId
//                        DataUtils.getInstance().updateDevice();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //定时时间信息,只用来查看
                        try {
                            DataUtils.getInstance().mDb.saveOrUpdate(info.schedinfo);
                        } catch (DbException e) {
                            e.printStackTrace();
                        }

                    }
                }

                break;
        }

    }


    /**
     * if(timeTaskDay.power0==null&&timeTaskDay.equals("0")){
     * holder.tvPower1.setTextColor(Color.parseColor("#FFFFFF"));
     * }else{
     * holder.tvPower1.setTextColor(Color.parseColor("#BCBCBC"));
     * }
     *
     * @param tv
     * @param powerday
     */
    private void powerDayIsColor(TextView tv, String powerday) {
        if (powerday == null || powerday.equals("0")) {
//            tvPower1.setTextColor(Color.parseColor("#FFFFFF"));
            tv.setVisibility(View.INVISIBLE);//隐藏参与布局（还占着地方）
        } else {//显示
            tv.setVisibility(View.VISIBLE);
//            tvPower1.setTextColor(Color.parseColor("#BCBCBC"));
        }
    }

    /**
     * 设置监听
     *
     * @param btSplitbutton
     * @param ttd
     */
    private void setListener(SlipButton btSplitbutton, final TimeTaskDay ttd) {
        btSplitbutton.SetOnChangedListener(new SlipButton.OnChangedListener() {

            public void OnChanged(boolean CheckState) {
//				btn.setText(CheckState ? "True" : "False");
                //改：20
                List<Boolean> day = new ArrayList<Boolean>();
                day.add(0, isTureFalse(ttd.D1));
                day.add(1, isTureFalse(ttd.D2));
                day.add(2, isTureFalse(ttd.D3));
                day.add(3, isTureFalse(ttd.D4));
                day.add(4, isTureFalse(ttd.D5));
                day.add(5, isTureFalse(ttd.D6));
                day.add(6, isTureFalse(ttd.D7));

                List<com.vanhitech.protocol.object.Power> lisp = new ArrayList<com.vanhitech.protocol.object.Power>();
                Boolean online = isTureFalse(ttd.online);
                Device ctrlinfo = new Device(ttd.deviceId, ttd.pid, ttd.name, ttd.place, online, lisp);

                if (CheckState) {
                    ttd.setEnabled("1");
                    TimerTask schedinfo = new TimerTask(ttd.id, Integer.parseInt(ttd.hour), Integer.parseInt(ttd.minute),
                            day, isTureFalse(ttd.repeated), true);
                    CMD20_ModifyTimer cmd20 = new CMD20_ModifyTimer(DataUtils.getInstance().transform(schedinfo), ctrlinfo);
                    CmdBaseActivity.getInstance().sendCmd(cmd20);
                    mTimeingAdapter.notifyDataSetChanged();
                } else {
                    ttd.setEnabled("0");
                    TimerTask schedinfo = new TimerTask(ttd.id, Integer.parseInt(ttd.hour), Integer.parseInt(ttd.minute),
                            day, isTureFalse(ttd.repeated), false);
                    CMD20_ModifyTimer cmd20 = new CMD20_ModifyTimer(DataUtils.getInstance().transform(schedinfo), ctrlinfo);
                    CmdBaseActivity.getInstance().sendCmd(cmd20);
                    mTimeingAdapter.notifyDataSetChanged();

                }
            }
        });
    }

    @NonNull
    public Boolean isTureFalse(String ttd) {
        Boolean online = false;
        if (ttd == null || ttd.equals("0")) {
            online = false;
        } else {
            online = true;
        }
        return online;
    }

    //item点击事件
    class ListViewCilickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent();
            intent.setClass(TimeingDeviceActivity.this, TimeingDeviceSettingActivity.class);
            intent.putExtra("mId", mLis.get(position - 1).id);
            startActivity(intent);
            overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        }
    }

    private int stringToInt(String s) {

        return Integer.parseInt(s);
    }
}
