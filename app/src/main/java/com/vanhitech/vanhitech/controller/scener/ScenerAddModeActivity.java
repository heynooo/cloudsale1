package com.vanhitech.vanhitech.controller.scener;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD2E_AddScene;
import com.vanhitech.protocol.cmd.server.CMD2F_ServerAddSceneResult;
import com.vanhitech.protocol.cmd.server.CMDFC_ServerNotifiOnline;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.UIUtils;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${添加场景模式界面}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ScenerAddModeActivity extends BaseActivityController implements View.OnClickListener {
    private EditText mScenerSpaceName;
    private CheckBox mScenerSportsCb;
    private CheckBox mScenerGohomeCb;
    private CheckBox mScenerLeavehomeCb;
    private CheckBox mScenerLadyCb;
    private CheckBox mScenerMovieCb;
    private CheckBox mScenerSleepCb;
    private CheckBox mScenerEatCb;
    private CheckBox mScenerReceiveCb;
    private CheckBox mScenerCleanCb;
    private CheckBox mScenerMeetCb;
    private CheckBox mScenerReadCb;
    private CheckBox mScenerMusicCb;
    private Button mScemerBtnSave;
    boolean isSaveEdit = true;
    private int mImageno;
    private CMD2F_ServerAddSceneResult mCmd2F;

    private void assignViews() {
        mScenerSpaceName = (EditText) findViewById(R.id.scener_space_name);
        mScenerSportsCb = (CheckBox) findViewById(R.id.scener_sports_cb);
        mScenerGohomeCb = (CheckBox) findViewById(R.id.scener_gohome_cb);
        mScenerLeavehomeCb = (CheckBox) findViewById(R.id.scener_leavehome_cb);
        mScenerLadyCb = (CheckBox) findViewById(R.id.scener_lady_cb);
        mScenerMovieCb = (CheckBox) findViewById(R.id.scener_movie_cb);
        mScenerSleepCb = (CheckBox) findViewById(R.id.scener_sleep_cb);
        mScenerEatCb = (CheckBox) findViewById(R.id.scener_eat_cb);
        mScenerReceiveCb = (CheckBox) findViewById(R.id.scener_receive_cb);
        mScenerCleanCb = (CheckBox) findViewById(R.id.scener_clean_cb);
        mScenerMeetCb = (CheckBox) findViewById(R.id.scener_meet_cb);
        mScenerReadCb = (CheckBox) findViewById(R.id.scener_read_cb);
        mScenerMusicCb = (CheckBox) findViewById(R.id.scener_music_cb);
        mScemerBtnSave = (Button) findViewById(R.id.scemer_btn_save);
    }


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }


    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_scener_addspace);
        mActivity = this;
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("添加情景模式");
        ActivityTitleManager.getInstance().changehelpText("");
        assignViews();
    }

    @Override
    public void initData() {
        super.initData();
        getConterIntent();
        if (!StringUtils.isEmpty(mId)) {
            isSaveEdit = false;
            if (!StringUtils.isEmpty(mName)) {
                mScenerSpaceName.setText(mName);
            }
            if (!StringUtils.isEmpty(mSimageno + "")) {
                mImageno = Integer.valueOf(mSimageno);
                modeSetChecked(mImageno);
            }
        }

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
    public void initEvent() {
        super.initEvent();
        onClickListener();
    }

    private void onClickListener() {
        mScenerSpaceName.setOnClickListener(this);
        mScenerSportsCb.setOnClickListener(this);
        mScenerGohomeCb.setOnClickListener(this);
        mScenerLeavehomeCb.setOnClickListener(this);
        mScenerLadyCb.setOnClickListener(this);
        mScenerMovieCb.setOnClickListener(this);
        mScenerSleepCb.setOnClickListener(this);
        mScenerEatCb.setOnClickListener(this);
        mScenerReceiveCb.setOnClickListener(this);
        mScenerCleanCb.setOnClickListener(this);
        mScenerMeetCb.setOnClickListener(this);
        mScenerReadCb.setOnClickListener(this);
        mScenerMusicCb.setOnClickListener(this);
        mScemerBtnSave.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scener_space_name:    //名字
//                getName();
                break;
            case R.id.scener_sports_cb: //1_1
                modeSetChecked(0);
                break;
            case R.id.scener_gohome_cb: //1_2
                modeSetChecked(1);
                break;
            case R.id.scener_leavehome_cb: //1_3
                modeSetChecked(2);
                break;
            case R.id.scener_lady_cb:  //1_4
                modeSetChecked(3);
                break;
            case R.id.scener_movie_cb:  //2_1
                modeSetChecked(4);
                break;
            case R.id.scener_sleep_cb:  //2_2
                modeSetChecked(5);
                break;
            case R.id.scener_eat_cb:    //2_3
                modeSetChecked(6);
                break;
            case R.id.scener_receive_cb:    //2_4
                modeSetChecked(7);
                break;
            case R.id.scener_clean_cb:      //3_1
                modeSetChecked(8);
                break;
            case R.id.scener_meet_cb:   //3_2
                modeSetChecked(9);
                break;
            case R.id.scener_read_cb:   //3_3
                modeSetChecked(10);
                break;
            case R.id.scener_music_cb:  //3_4
                modeSetChecked(11);
                break;
            case R.id.scemer_btn_save:     //保存
                getName();
                break;
            default:
                break;
        }
    }


    private void getName() {
        mName = mScenerSpaceName.getText().toString().trim();
        if (TextUtils.isEmpty(mName)) {
            mScenerSpaceName.setError("情景模式名字不能为空");
            return;
        } else {//id =时间
            if (isSaveEdit) {
                sendCmd2E(DateUtils.getCurTime(), mImageno, mName);
            } else {
                sendCmd2E(mId, mImageno, mName);
            }
        }
    }

    private void sendCmd2E(String id, int imageno, String name) {
        SceneMode sceneMode = new SceneMode(id, imageno, name);
        CMD2E_AddScene mCMD2E_addScene = new CMD2E_AddScene(sceneMode);
        CmdBaseActivity.getInstance().mHelper.setCommandListener(ScenerAddModeActivity.this);
        CmdBaseActivity.getInstance().sendCmd(mCMD2E_addScene);
    }

    private void modeSetChecked(int i) {
        mImageno = i;
        mScenerSportsCb.setChecked(i == 0);
        mScenerGohomeCb.setChecked(i == 1);
        mScenerLeavehomeCb.setChecked(i == 2);
        mScenerLadyCb.setChecked(i == 3);
        mScenerMovieCb.setChecked(i == 4);
        mScenerSleepCb.setChecked(i == 5);
        mScenerEatCb.setChecked(i == 6);
        mScenerReceiveCb.setChecked(i == 7);
        mScenerCleanCb.setChecked(i == 8);
        mScenerMeetCb.setChecked(i == 9);
        mScenerReadCb.setChecked(i == 10);
        mScenerMusicCb.setChecked(i == 11);
    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        LogUtils.i("添加情景模式收到:" + cmd.toString());

        switch (cmd.CMDByte) {
            case CMDFC_ServerNotifiOnline.Command:
                break;

            case CMDFF_ServerException.Command:
                CMDFF_ServerException cmdff_serverException = (CMDFF_ServerException) cmd;
                final String info = cmdff_serverException.info;
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {

                    }
                });
                break;
            case CMD2F_ServerAddSceneResult.Command:
                mCmd2F = (CMD2F_ServerAddSceneResult) cmd;
                SceneMode resultdata = mCmd2F.scene;
                DataUtils.getInstance().save(resultdata);
                UIUtils.postTaskSafely(new Runnable() {
                    @Override
                    public void run() {
                        if (mCmd2F.result) {
                            T.showShort(mContext, "添加成功");
                            finish();
                        } else {
                            T.showShort(mContext, "添加失败");

                        }
                    }
                });
                break;
        }
    }
}
