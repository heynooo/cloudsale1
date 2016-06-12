package com.vanhitech.vanhitech.utils;

import android.text.method.ReplacementTransformationMethod;

/**
 * 创建者     heyn
 * 创建时间   2016/4/14 16:09
 * 描述	      //限制只输入大写，自动小写转大写
                editext.setTransformationMethod(new AllCapTransformationMethod ());
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AllCapTransformationMethod extends ReplacementTransformationMethod {
    @Override
    protected char[] getOriginal() {
        char[] aa = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z' };
        return aa;
    }

    @Override
    protected char[] getReplacement() {
        char[] cc = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z' };
        return cc;
    }
}
