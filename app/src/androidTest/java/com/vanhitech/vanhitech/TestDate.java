package com.vanhitech.vanhitech;

import android.test.AndroidTestCase;
import android.widget.Toast;

import com.vanhitech.vanhitech.utils.DateUtils;

/**
 * 创建者     heyn
 * 创建时间   2016/4/16 19:03
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class TestDate extends AndroidTestCase {
    //DateUtils

    //getCurTime 获取当前时间
    public void test1(){
        String date = DateUtils.getCurTime("yyyyMMddHHmmss");
        date.toString();

    }
    //getCurTimeAdd30M
    public void test2(){
        String date =DateUtils.getCurTimeAdd30M();
        date.toString();
    }
    //getCurTimeAddND 10天后
    public void test3(){
        String date =DateUtils.getCurTimeAddND(10,"yyyyMMddHHmmss");
        date.toString();

    }

    public void test4(){
        String date1 =DateUtils.getCurTimeAddXM(1);
        String date = DateUtils.getCurTime();
        Long in= Long.valueOf(date1)-Long.valueOf(date);
        Toast.makeText(mContext, "in:" + in, Toast.LENGTH_SHORT).show();

        Boolean b= DateUtils.isDeadLine(date1);
        b.toString();
    }
    //前面补0("%04d", 99)  str=String.format("Hi,%s", "王力");
    public void test5(){
//        String s=11;
        String s1 =String.format("%02d",1);
        s1.toString();
    }

}
