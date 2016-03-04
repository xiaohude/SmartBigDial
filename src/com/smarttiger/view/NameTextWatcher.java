package com.smarttiger.view;

import com.smarttiger.bigdial.MainActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class NameTextWatcher implements TextWatcher{
	
	public int index = 0;
	private MainActivity mainActivity;
	private EditText weightEdit;
	private View deleteButton;
	private LuckyPanView luckyPanView;
	
	public NameTextWatcher(int index, MainActivity mainActivity, EditText weightEdit, View deleteButton, LuckyPanView luckyPanView)
	{
		this.index = index;
		this.mainActivity = mainActivity;
		this.weightEdit = weightEdit;
		this.deleteButton = deleteButton;
		this.luckyPanView = luckyPanView;
	}

	private boolean isEmpty = false;
	private boolean addOnce = true;
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		isEmpty = TextUtils.isEmpty(s);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(addOnce && isEmpty && !TextUtils.isEmpty(s))
		{
			weightEdit.setText("1");
			mainActivity.createItemView();
			addOnce = false;
			luckyPanView.addItem(s.toString(), 1);
		}
		else
		{
			addOnce = false;
			luckyPanView.changeItemName(index, s.toString());
		}
		if(addOnce && TextUtils.isEmpty(s))
			deleteButton.setVisibility(View.INVISIBLE);
		else
			deleteButton.setVisibility(View.VISIBLE);
			
	}

}
