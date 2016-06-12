package com.vanhitech.vanhitech.utils;

import java.util.Collection;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/4/20 19:30
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class YHDCollectionUtils  {

    public static final Collection NULL_COLLECTION = new NullCollection();

    public static final <T> Collection<T> nullCollection() {
        return (List<T>) NULL_COLLECTION;
    }
}