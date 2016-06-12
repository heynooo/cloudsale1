//package com.vanhitech.vanhitech;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//
//import de.greenrobot.event.EventBus;
//
///**
// * 数据发送
// */
//
//public class EventBusTestTwoActivity extends AppCompatActivity {
//    Button button_one,button_two,button_third,button_out;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.event_bus_test_two_layout);
//        button_one=(Button)this.findViewById(R.id.button_one);
//        button_two=(Button)this.findViewById(R.id.button_two);
//        button_third=(Button)this.findViewById(R.id.button_third);
//        button_out=(Button)this.findViewById(R.id.button_out);
//        button_one.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new TestEventFirst("One Event button"));
//                //EventBusTestTwoActivity.this.finish();
//            }
//        });
//        button_two.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new TestEventSecond("Two Event button"));
//                //EventBusTestTwoActivity.this.finish();
//            }
//        });
//        button_third.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EventBus.getDefault().post(new TestEventThird("Third Event button"));
//                //EventBusTestTwoActivity.this.finish();
//            }
//        });
//        button_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openActivity(EventBusTestActivity.class);
//            }
//        });
//    }
//    protected void openActivity(Class<?> pClass) {
//        Intent _Intent = new Intent();
//        _Intent.setClass(this, pClass);
//        startActivity(_Intent);
//    }
//}
