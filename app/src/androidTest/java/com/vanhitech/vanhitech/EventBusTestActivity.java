//package com.vanhitech.vanhitech;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//
//import de.greenrobot.event.EventBus;
//
///**
// *发到原来没有销毁的 activity 上面 ， 不是新创建的  ，适合
// */
//
//public class EventBusTestActivity extends AppCompatActivity {
//    Button button_one;
//    TextView textView_one,textView_two,textView_third;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.event_bus_test_layout);
//        EventBus.getDefault().register(this);
//        button_one=(Button)this.findViewById(R.id.button_one);
//        textView_one=(TextView)this.findViewById(R.id.textView_one);
//        textView_two=(TextView)this.findViewById(R.id.textView_two);
//        textView_third=(TextView)this.findViewById(R.id.textView_third);
//        button_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity(EventBusTestThirdActivity.class);
//            }
//        });
//    }
//    protected void openActivity(Class<?> pClass) {
//        Intent _Intent = new Intent();
//        _Intent.setClass(this, pClass);
//        startActivity(_Intent);
//    }
//    /**
//     * 收到消息 进行相关处理
//     * @param event
//     */
//    public void onEventMainThread(TestEventFirst event) {
//        Log.d("zttjiangqq","onEventMainThread收到消息:"+event.getMsg());
//        textView_one.setText(event.getMsg());
//        //showToastMsgShort(event.getMsg());
//    }
//    /**
//     * 收到消息 进行相关处理
//     * @param event
//     */
//    public void onEventMainThread(TestEventSecond event) {
//
//        Log.d("zttjiangqq","onEventMainThread收到消息:"+event.getMsg());
//        textView_two.setText(event.getMsg());
//        //showToastMsgShort(event.getMsg());
//    }
//    /**
//     * 收到消息 进行相关处理
//     * @param event
//     */
//    public void onEventMainThread(TestEventThird event) {
//
//        Log.d("zttjiangqq","onEventMainThread收到消息:"+event.getMsg());
//        textView_third.setText(event.getMsg());
//        //showToastMsgShort(event.getMsg());
//    }
//        @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//}
