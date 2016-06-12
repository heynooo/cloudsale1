package com.vanhitech.vanhitech.activity.device;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanhitech.protocol.cmd.ClientCommand;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD06_QueryDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD07_ServerRespondDeviceStatus;
import com.vanhitech.protocol.cmd.server.CMD09_ServerControlResult;
import com.vanhitech.protocol.cmd.server.CMDFF_ServerException;
import com.vanhitech.protocol.object.device.AirTypeADevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.UIUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2016/5/11.
 */
public class AirDataSelectionActivity extends BaseActivityController {
    private RelativeLayout bottom_air_dataR,bottom_air_priceR;
    private RelativeLayout dataR,priceR;
    private Button air_dataB,air_priceB;
    private TextView tv_els, tv_pow, tv_ec, tv_ev;
    private AirTypeADevice airtypeADevice;
    private String mId;
    private Timer timer6s;
    private EditText et_one_cost;
    private float price;
    private TextView tv_cost;
    private LinearLayout ll_cost;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public void initView() {
        super.initView();
        findView();
        initData();
    }
    private void findView() {
        setContentView(R.layout.activity_air_data_selection);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("数据");
        ActivityTitleManager.getInstance().help.setVisibility(View.GONE);
        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bottom_air_dataR = (RelativeLayout) findViewById(R.id.air_dataR);
        bottom_air_priceR = (RelativeLayout) findViewById(R.id.air_priceR);
        dataR = (RelativeLayout) findViewById(R.id.select_data);
        priceR = (RelativeLayout) findViewById(R.id.select_price);
        air_dataB = (Button) findViewById(R.id.air_dataB);
        air_priceB = (Button) findViewById(R.id.air_priceB);
        air_dataB.setSelected(true);
        tv_ec = (TextView) findViewById(R.id.tv_ec);
        tv_pow = (TextView) findViewById(R.id.tv_pow);
        tv_els = (TextView)findViewById(R.id.tv_els);
        tv_ev = (TextView) findViewById(R.id.tv_ev);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        et_one_cost = (EditText) findViewById(R.id.et_one_cost);
        ll_cost = (LinearLayout) findViewById(R.id.ll_cost);

        bottom_air_priceR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                air_dataB.setSelected(false);
                air_priceB.setSelected(true);
                dataR.setVisibility(View.GONE);
                priceR.setVisibility(View.VISIBLE);
            }
        });
        air_priceB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                air_dataB.setSelected(false);
                air_priceB.setSelected(true);
                dataR.setVisibility(View.GONE);
                priceR.setVisibility(View.VISIBLE);
            }
        });
        bottom_air_dataR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                air_dataB.setSelected(true);
                air_priceB.setSelected(false);
                priceR.setVisibility(View.GONE);
                dataR.setVisibility(View.VISIBLE);
            }
        });
        air_dataB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                air_dataB.setSelected(true);
                air_priceB.setSelected(false);
                priceR.setVisibility(View.GONE);
                dataR.setVisibility(View.VISIBLE);
            }
        });
        et_one_cost.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                if (TextUtils.isEmpty(et_one_cost.getText().toString().trim())) {
                    tv_cost.setText("0");
                    return;
                }
                try {
                    price = Float.parseFloat(et_one_cost.getText().toString());
                    System.out.println("price:" + price);
                    float sump = (float) (price * airtypeADevice.current);
                    System.out.println("price:" + sump);
                    String str = String.format("%.2f", sump);
                    System.out.println("price:" + str);
                    tv_cost.setText(str);
                    save(""+price);
                    // sPreferenceUtil.setPrice(price);
                } catch (Exception e) {
//                    Util.showToast(getActivity(), "输入了非法数字或者是设备不在线！");
                }
            }
        });

    }
    @Override
    public void initData() {
        super.initData();

        Intent intent = getIntent();
        mId = intent.getStringExtra("id");
        CMD06_QueryDeviceStatus cmd06 = new CMD06_QueryDeviceStatus(mId);
        sendCMD(cmd06);
    }
    private void sendCMD(ClientCommand cmd) {
        CmdBaseActivity.getInstance().mHelper.setCommandListener(AirDataSelectionActivity.this);
        CmdBaseActivity.getInstance().sendCmd(cmd);
    }
    private void setData() {
        String str_els;
        if (airtypeADevice.current < 1000) {
            str_els = String.format("%.2f", airtypeADevice.current);
        } else if (airtypeADevice.current >= 1000 && airtypeADevice.current < 10000) {
            str_els = String.format("%.1f", airtypeADevice.current);
        } else {
            str_els = String.format("%.0f", airtypeADevice.current);
        }
            tv_els.setText(String.format("%.1f", airtypeADevice.ttpower) + "kwh");
            tv_pow.setText(String.format("%.3f", airtypeADevice.kw) + "w");
            tv_ec.setText(str_els + "A");
            tv_ev.setText( String.format("%.1f", airtypeADevice.voltage) + "V");
        tv_cost.setText(""+getPrice());
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
                CMD07_ServerRespondDeviceStatus cmd07 = (CMD07_ServerRespondDeviceStatus) cmd;
                Device d07 = cmd07.status;
                airtypeADevice = (AirTypeADevice)d07;
                setData();
                break;
            case CMD09_ServerControlResult.Command:
                CMD09_ServerControlResult cmd09 = (CMD09_ServerControlResult) cmd;
                Device device = cmd09.status;
                airtypeADevice = (AirTypeADevice)device;
                setData();

                break;
        }
    }

    private void startTimer6s() {
        if (timer6s == null) {
            timer6s = new Timer();
            TimerTask task = new TimerTask() {

                @Override
                public void run() {

//                    sendQueryData();

                }
            };
            timer6s.schedule(task, 6 * 1000, 6 * 1000);
        }
    }

    private void cancelTimer6s() {
        if (timer6s != null) {
            timer6s.cancel();
            timer6s = null;
        }
    }

    /**
     * 保存价格信息
     */

    public void save(String price) {
        File file = new File(FileUtils.getCachePath(), "price.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write(price.getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

    /**
     * 读取价格
     */
    public float getPrice() {
       float pricee = 0.0f;
        File file = new File(FileUtils.getCachePath(), "price.txt");
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(file);
                //把字节流转换成字符流
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                //读取txt文件里的用户名和密码
                String text = br.readLine();
                pricee = Float.parseFloat(text);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    return  pricee;

    }

}
