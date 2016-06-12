package com.vanhitech.vanhitech.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.bean.Place;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/4/22 17:17
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class ArticleAdapter extends PagerAdapter {
    List<Place> list;
    private ViewPager pager;
    LayoutInflater inflater = null;
    //用于存储回收掉的View
    private List<WeakReference<LinearLayout>> viewList;
    private Context context;

    private BitmapUtils mBitmapUtils;
    private int mPosition;

    public ArticleAdapter(Context context, List<Place> list) {
        this.list = list;
        this.context = context;
        viewList = new ArrayList<WeakReference<LinearLayout>>();
        inflater = LayoutInflater.from(context);
        mBitmapUtils = new BitmapUtils(context);//图片加载处理
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public Object instantiateItem(View container, int position) { // 这个方法用来实例化页卡

        if (pager == null) {
            pager = (ViewPager) container;
        }
        View view = null;
        // 从废弃的里去取 取到则使用 取不到则创建
        if (viewList.size() > 0) {
            if (viewList.get(0) != null) {
                view = initView(viewList.get(0).get(), position);
                viewList.remove(0);
            }
        }
        view = initView(null, position);
        pager.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        //存储离屏的View 等待复用
        LinearLayout view = (LinearLayout) object;
        container.removeView(view);
        viewList.add(new WeakReference<LinearLayout>(view));

    }



    private View initView(LinearLayout view, int position) {
        mPosition = position;
        ViewHolder viewHolder = null;
        if (view == null) {
            view = (LinearLayout) inflater.inflate(R.layout.item_viewpager_home, null);
            viewHolder = new ViewHolder();
            viewHolder.title_TV = (TextView) view
                    .findViewById(R.id.home_pager_title);
            viewHolder.mImageView = (ImageView) view
                    .findViewById(R.id.homelist_pager_imageview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        /**
         * 初始化数据
         */
        viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (list != null && position < list.size()) {
            Place article = list.get(position);
            viewHolder.title_TV.setText(article.place);
            mBitmapUtils.display(viewHolder.mImageView, article.url);

        }
        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    private class ViewHolder {
        TextView title_TV;
        ImageView mImageView;
    }


}