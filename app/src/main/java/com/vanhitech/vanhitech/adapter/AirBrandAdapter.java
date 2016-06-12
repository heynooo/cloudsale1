package com.vanhitech.vanhitech.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vanhitech.vanhitech.R;


public class AirBrandAdapter extends BaseAdapter {
	private String str[];
	private Context context;
	private boolean[] isChoose;

	public AirBrandAdapter(String str[], Context context, boolean[] isChoose) {
		this.str = str;
		this.context = context;
		this.isChoose = isChoose;
	}

	public void setList(boolean[] isChoose) {
		this.isChoose = isChoose;
		notifyDataSetChanged();
	}

	@Override
	public boolean areAllItemsEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean[] getList() {
		return isChoose;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return str == null ? 0 : str.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return str[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position1 = position;
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_airname_list, null);
			holder = new ViewHolder();
			holder.rl_air = (RelativeLayout) convertView
					.findViewById(R.id.rl_air);
			holder.tv_air_name = (TextView) convertView
					.findViewById(R.id.tv_air_name);
			holder.iv_air_brand_check = (ImageView) convertView
					.findViewById(R.id.iv_air_brand_check);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tv_air_name.setText(str[position1]);
		if (isChoose[position1]) {
			Log.e("swg", "iv_device_control_center_isChoosed"
					+ isChoose[position1]);
			holder.iv_air_brand_check.setVisibility(View.VISIBLE);
		} else {
			holder.iv_air_brand_check.setVisibility(View.INVISIBLE);
		}
		holder.rl_air.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int i = 0; i < isChoose.length; i++) {
					if (i == position1) {
						isChoose[i] = true;
					} else {
						isChoose[i] = false;
					}
				}
				setList(isChoose);
				Intent intent = new Intent();
				intent.putExtra("currentnum", position1);
				((Activity) context).setResult(1, intent);
				((Activity) context).finish();

			}
		});
		return convertView;
	}

	private static class ViewHolder {
		TextView tv_air_name;
		ImageView iv_air_brand_check;
		RelativeLayout rl_air;
	}

}
