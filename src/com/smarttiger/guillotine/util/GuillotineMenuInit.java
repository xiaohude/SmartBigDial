package com.smarttiger.guillotine.util;

import com.smarttiger.bigdial.MainActivity;
import com.smarttiger.bigdial.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GuillotineMenuInit {
	
	private MainActivity mainActivity;
	private View guillotineView;
	
	public GuillotineMenuInit(MainActivity mainActivity, View view) {
		// TODO Auto-generated constructor stub
		this.mainActivity = mainActivity;
		guillotineView = view;
		
		setSetingAction();
	}
	
	private void setSetingAction()
	{
		View setTingView = guillotineView.findViewById(R.id.settings_group);
		setTingView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainActivity.showMenuDialog();
				
			}
		});
	}

}
