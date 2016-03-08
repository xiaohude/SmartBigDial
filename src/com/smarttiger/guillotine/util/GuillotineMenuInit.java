package com.smarttiger.guillotine.util;

import com.smarttiger.bigdial.MainActivity;
import com.smarttiger.bigdial.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GuillotineMenuInit {
	
	private MainActivity main;
	private View guillotineView;
	
	public GuillotineMenuInit(MainActivity main, View view) {
		// TODO Auto-generated constructor stub
		this.main = main;
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
				showMenuDialog();
				
			}
		});
	}
	
	//按钮加速度
	private double acceleration = 0.08;
	//转盘摩擦力
	private double friction = 0.1;
	//自定义速度
	private double speed = 0;

	public void showMenuDialog()
	{
		LinearLayout linearLayout = new LinearLayout(main);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		acceleration = main.getAcceleration();
		friction = main.getFriction();
		speed = main.getSpeed();
		
		final EditText editText0 = new EditText(main);
		editText0.setText(""+acceleration);
		editText0.selectAll();
		editText0.setKeyListener(new DigitsKeyListener(false,true));
		linearLayout.addView(editText0);
		
		final EditText editText1 = new EditText(main);
		editText1.setText(""+friction);
		editText1.selectAll();
		editText1.setKeyListener(new DigitsKeyListener(false,true));
		linearLayout.addView(editText1);
		
		final EditText editText2 = new EditText(main);
		editText2.setText(""+speed);
		editText2.selectAll();
		editText2.setKeyListener(new DigitsKeyListener(false,true));
		linearLayout.addView(editText2);
		
		new AlertDialog.Builder(main)
			.setTitle("请输入加速度，摩擦力：")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(linearLayout)
			.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						acceleration = Double.valueOf(editText0.getText().toString());
						main.setAcceleration(acceleration);
						friction = Double.valueOf(editText1.getText().toString());
						main.setFriction(friction);
						speed = Double.valueOf(editText2.getText().toString());
						main.setSpeed(speed);
					}
				})
			.setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
					}
				})
			.create()
			.show();
	}

}
