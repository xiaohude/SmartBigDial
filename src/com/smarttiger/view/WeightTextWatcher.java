package com.smarttiger.view;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

public class WeightTextWatcher implements TextWatcher{
	
	public int index = 0;
	private View deleteButton;
	private LuckyPanView luckyPanView;
	
	public WeightTextWatcher(int index, View deleteButton, LuckyPanView luckyPanView)
	{
		this.index = index;
		this.deleteButton = deleteButton;
		this.luckyPanView = luckyPanView;
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(s))
			luckyPanView.changeItemWeight(index, Integer.parseInt(s.toString()));
	}

}
