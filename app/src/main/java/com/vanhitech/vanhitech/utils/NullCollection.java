package com.vanhitech.vanhitech.utils;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.RandomAccess;

/**
 * 创建者     heyn
 * 创建时间   2016/4/20 19:30
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class NullCollection extends AbstractList<Object>
        implements RandomAccess, Serializable {

    private static final long serialVersionUID = 5206887786441397812L;

    @Override
    public Object get(int index) {
        return null;
    }

    @Override
    public int size() {
        return 1;
    }

    public boolean contains(Object obj) {
        return null == obj;
    }

    private Object readResolve() {
        return null;
    }
}