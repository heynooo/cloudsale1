package com.vanhitech.vanhitech.adapter;

import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/4/22 17:36
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ViewPagerAdapter  extends PagerAdapter {

    private SparseArray<View> mPageViews = new SparseArray<View>();

    public ViewPagerAdapter() {
        super();
    }

    @Override
    public int getCount() {
        return 0;
    }

    /**
     * 获取view方法，子类实现这个方法来获取渲染View
     *
     * @param convertView 如果是null则没有可复用的View，如果非null则是可复用的View
     * @param position
     * @return
     */
    protected View getView(View convertView, int position) {
        return null;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // 获取复用的view给子类去用，没有可复用的view时为null
        View view = getView(pullViewFromPool(), position);
        // 记录该view以在destroyItem中能找到
        mPageViews.put(position, view);
        // 添加到view pager
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = mPageViews.get(position);
        // 把要删除的view放到pool里以供复用
        pushViewToPool(view);
        // 从view pager中删除
        container.removeView(view);
    }

    /****************** 复用view start ******************************/
    private List<View> mPageViewPool = new ArrayList<View>();

    private View pullViewFromPool() {
        View view = null;
        for (View v : mPageViewPool) {
            view = v;
            break;
        }
        if (view != null) {
            mPageViewPool.remove(view);
        }
        return view;
    }

    private void pushViewToPool(View view) {
        if (!mPageViewPool.contains(view)) {
            mPageViewPool.add(view);
        }
    }
    /****************** 复用view end ******************************/
}