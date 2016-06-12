package com.vanhitech.vanhitech.zxing;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * 创建者     heyn
 * 创建时间   2016/4/14 13:49
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SnEdittext extends EditText {

    public boolean isTel=true;
    private String addString="-";
    private boolean isRun=false;

    public SnEdittext(Context context) {
        this(context,null);
    }

    public SnEdittext(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("tag", "onTextChanged()之前");
                if(isRun){//这几句要加，不然每输入一个值都会执行两次onTextChanged()，导致堆栈溢出，原因不明
                    isRun = false;
                    return;
                }
                isRun = true;
                Log.i("tag", "onTextChanged()");
                if (isTel) {
                    String finalString="";
                    int index=0;
                    String telString=s.toString().replace(" ", "");//TODO
                    if (telString.length()>(index+14)) {//3
                        finalString+=(telString.substring(index, index+14)+addString);
                        addString="";
                        index+=14;//
                    }
//                    while ((index+15)<telString.length()) {//4
//                        finalString+=(telString.substring(index, index+4)+addString);
//                        index+=15;
//                    }
                    finalString+=telString.substring(index,telString.length());
                    SnEdittext.this.setText(finalString);
                    //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                    SnEdittext.this.setSelection(finalString.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
            @Override
            public void afterTextChanged(Editable s) {



            }
        });
    }
}