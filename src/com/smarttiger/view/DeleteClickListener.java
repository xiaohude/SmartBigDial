package com.smarttiger.view;


import com.smarttiger.bigdial.MainActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class DeleteClickListener implements OnClickListener{

	public int index = 0;
	private MainActivity mainActivity;
	public LinearLayout parentView;
	public View itemView;
	private LuckyPanView luckyPanView;
	
	public DeleteClickListener(int index, MainActivity mainActivity, LinearLayout parentView, View itemView, LuckyPanView luckyPanView)
	{
		this.index = index;
		this.mainActivity = mainActivity;
		this.parentView = parentView;
		this.itemView = itemView;
		this.luckyPanView = luckyPanView;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(parentView != null)
			parentView.removeView(itemView);
		luckyPanView.deleteItem(index);
		mainActivity.deleteItemView();
		
	}

}
