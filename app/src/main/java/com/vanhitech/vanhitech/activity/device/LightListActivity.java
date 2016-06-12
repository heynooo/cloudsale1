package com.vanhitech.vanhitech.activity.device;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD24_QueryTimer;
import com.vanhitech.protocol.cmd.client.CMD60_SetMatchStatus;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.views.SlipButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class LightListActivity extends BaseActivityController {

    private ListView mLvDeviceMsg;
    private CheckBox mCbIsRemoteControl;
    private CheckBox mCbIsTiming;
    private CheckBox mCbIsDeviceinfo;
    private String mName;
    private String mId;
    private List<com.vanhitech.protocol.object.Power> mListpower;
    private LightAdapter mLightAdapter;
    private Device mDevice;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_lightlist);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("多路灯开关");
        assignViews();
    }

    private void assignViews() {
        mLvDeviceMsg = (ListView) findViewById(R.id.lv_deviceMsg);
        mCbIsRemoteControl = (CheckBox) findViewById(R.id.cb_is_remote_control);
        mCbIsTiming = (CheckBox) findViewById(R.id.cb_is_timing);
        mCbIsDeviceinfo = (CheckBox) findViewById(R.id.cb_is_deviceinfo);
    }


    @Override
    public void initData() {
        super.initData();
        Intent intent = getIntent();
        //
        mName = intent.getStringExtra("name");
        //
        mId = intent.getStringExtra("id");


        Power power = DataUtils.getInstance().getdataPower(mId);
        mDevice = DataUtils.getInstance().getDevice(mId);

        mListpower = new ArrayList<>();
        if (power.power0 != null)
            mListpower.add(new com.vanhitech.protocol.object.Power(0, toTrueFalse(power.power0)));
        if (power.power1 != null)
            mListpower.add(new com.vanhitech.protocol.object.Power(1, toTrueFalse(power.power1)));
        if (power.power2 != null)
            mListpower.add(new com.vanhitech.protocol.object.Power(2, toTrueFalse(power.power2)));


        CmdBaseActivity.getInstance().mHelper.setCommandListener(LightListActivity.this);
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
        CmdBaseActivity.getInstance().sendCmd(cmd06);
    }

    @Override
    public void initEvent() {
        super.initEvent();
        mLightAdapter = new LightAdapter();
        mLvDeviceMsg.setAdapter(mLightAdapter);
        ActivityTitleManager.getInstance().changeTitle(mDevice.name);

        //设备信息
        mCbIsDeviceinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, DeviceInfoActivity.class);
                intent.putExtra("name",mDevice.name);
                intent.putExtra("id", mDevice.id);
                startActivity(intent);
                overridePendingTransition(R.animator.nextenteranim,R.animator.nextexitanim);
            }
        });
        //定时
        mCbIsTiming.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                CMD24_QueryTimer cmd24 = new CMD24_QueryTimer(mName);
                CmdBaseActivity.getInstance().sendCmd(cmd24);

                intent.setClass(mContext, TimeingDeviceActivity.class);
                intent.putExtra("mDeviceId", mId);
                startActivity(intent);

//                finish();
                overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
            }
        });
        //匹配
        mCbIsRemoteControl.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CMD60_SetMatchStatus cmd60 = new CMD60_SetMatchStatus(0x80,
                        mDevice.id);
                CmdBaseActivity.getInstance().sendCmd(cmd60);

            }
        });


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

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        switch (cmd.CMDByte) {

            case CMDFF_ServerException.Command:

                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
                break;
            case CMD07_ServerRespondDeviceStatus.Command:
                CMD07_ServerRespondDeviceStatus cmd07=(CMD07_ServerRespondDeviceStatus)cmd;
                Device d07 = cmd07.status;
                mListpower = d07.power;

                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;
                mListpower = device.power;
//                mLightAdapter.notifyDataSetChanged();
                break;


        }
    }
    //上适配器


    class LightAdapter extends BaseAdapter {


        /**
         * getCount 里面存放 比如说 客厅有多少设备 这里就返回多少个
         * 这里的device 是一个
         *
         * @return
         */
        @Override
        public int getCount() {

            return null != mListpower ? mListpower.size() : 0;
        }

        @Override
        public Object getItem(int position) {
//            if (mDeviceList != null) {
//                return mDeviceList.get(position);//TODO
//            }
            return null != mListpower ? mListpower.get(position) : 0;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;  //根视图
            if (convertView == null) {

                convertView = View.inflate(mContext, R.layout.item_list_lightlist, null);
                holder = new ViewHolder();
                //设置id
                holder.mLayoutV = (LinearLayout) convertView.findViewById(R.id.layout_v);
                holder.mTvLine = (TextView) convertView.findViewById(R.id.tv_line);
                holder.mSwitcher = (SlipButton) convertView.findViewById(R.id.switcher);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final com.vanhitech.protocol.object.Power power = mListpower.get(position);
            //数据绑定
            holder.mTvLine.setText(power.way + 1 + "路");

            if (power.on) {
                holder.mSwitcher.setCheck(true);
                                //setChecked 选中效果有个小bug
//                holder.mSwitcher.setClickable(true);
            } else {
                holder.mSwitcher.setCheck(false);
//                holder.mSwitcher.setClickable(false);
            }


            holder.mSwitcher.SetOnChangedListener(new SlipButton.OnChangedListener() {
                @Override
                public void OnChanged(boolean CheckState) {
                    //发送命令
                    Boolean flag = !power.on;
                    mListpower.set(power.way, new com.vanhitech.protocol.object.Power(power.way, flag));
                    mDevice.setPowers(mListpower);
                    CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(mDevice);
                    CmdBaseActivity.getInstance().sendCmd(cmd08);
                    mLightAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout mLayoutV;
            TextView mTvLine;
            SlipButton mSwitcher;

        }
    }

}
