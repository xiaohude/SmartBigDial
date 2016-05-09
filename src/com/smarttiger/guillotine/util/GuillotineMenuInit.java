package com.smarttiger.guillotine.util;

import com.smarttiger.bigdial.Help;
import com.smarttiger.bigdial.MainActivity;
import com.smarttiger.bigdial.R;
import com.smarttiger.bigdial.DataControl.SettingData;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class GuillotineMenuInit {
	
	private MainActivity main;
	private View guillotineView;
	private SettingData settingData;
	
	
	public GuillotineMenuInit(MainActivity main, View view, SettingData data) {
		// TODO Auto-generated constructor stub
		this.main = main;
		guillotineView = view;
		settingData = data;
		
		initIsShowSpeed();
		initIsFastOrSlow();
		initAccelerationEdit();
		initFrictionEdit();
		initTouchFrictionEdit();
		initSpeedEdit();
		setSetingAction();
	}
	
	private void refresh()
	{
		isShowSwitch.setChecked(settingData.isShowSpeed);
		accelerationEditText.setText(""+settingData.acceleration);
		frictionEditText.setText(""+settingData.friction);
		touchFrictionEditText.setText(""+settingData.touchFriction);
		speedEditText.setText(""+settingData.speed);
	}
	
	Switch isShowSwitch;
	private void initIsShowSpeed()
	{
		isShowSwitch = (Switch) guillotineView.findViewById(R.id.isShowSpeed_Switch);
		isShowSwitch.setChecked(settingData.isShowSpeed);
		isShowSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				main.setIsShowSpeed(isChecked);
			}
		});
	}
	
	private RadioGroup radioGroup;
	private RadioButton isFastMode;
	private OnCheckedChangeListener fastCheckChangeListener;
	private RadioButton isSlowMode;
	private OnCheckedChangeListener slowCheckChangeListener;
	private void initIsFastOrSlow()
	{
		radioGroup = (RadioGroup) guillotineView.findViewById(R.id.radio_group);
		isFastMode = (RadioButton) guillotineView.findViewById(R.id.isFastMode_radio);
		isSlowMode = (RadioButton) guillotineView.findViewById(R.id.isSlowMode_radio);
		fastCheckChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					settingData = new SettingData(settingData.isShowSpeed, 1, 1, settingData.touchFriction, 0);
					main.setSettingData(settingData);
					refresh();
				}
			}
		};
		slowCheckChangeListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked) {
					settingData = new SettingData(settingData.isShowSpeed, 0.08, 0.1, settingData.touchFriction, 0);
					main.setSettingData(settingData);
					refresh();
				}
			}
		};
		isFastMode.setOnCheckedChangeListener(fastCheckChangeListener);
		isSlowMode.setOnCheckedChangeListener(slowCheckChangeListener);
	}
	public void cleanRadio()
	{
		radioGroup.clearCheck();
	}
	
	private EditText accelerationEditText;
	private void initAccelerationEdit()
	{
		accelerationEditText = (EditText) guillotineView.findViewById(R.id.acceleration_edit);
		accelerationEditText.setText(""+settingData.acceleration);
		accelerationEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s))
					settingData.acceleration = 0.08;
				else
					settingData.acceleration = Double.valueOf(s.toString());
				main.setAcceleration(settingData.acceleration);
				if(settingData.acceleration != 0.08 && settingData.acceleration != 1)
					cleanRadio();
			}
		});
	}
	
	private EditText frictionEditText;
	private void initFrictionEdit()
	{
		frictionEditText = (EditText) guillotineView.findViewById(R.id.friction_edit);
		frictionEditText.setText(""+settingData.friction);
		frictionEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s))
					settingData.friction = 0.1;
				else
					settingData.friction = Double.valueOf(s.toString());
				main.setFriction(settingData.friction);
				if(settingData.friction != 0.1 && settingData.friction != 1)
					cleanRadio();
			}
		});
	}
	
	private EditText touchFrictionEditText;
	private void initTouchFrictionEdit()
	{
		touchFrictionEditText = (EditText) guillotineView.findViewById(R.id.touchFriction_edit);
		touchFrictionEditText.setText(""+settingData.touchFriction);
		touchFrictionEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s))
					settingData.touchFriction = 0.3;
				else
					settingData.touchFriction = Double.valueOf(s.toString());
				main.setTouchFriction(settingData.touchFriction);
//				cleanRadio();
			}
		});
	}
	
	private EditText speedEditText;
	private void initSpeedEdit()
	{
		speedEditText = (EditText) guillotineView.findViewById(R.id.speed_edit);
		speedEditText.setText(""+settingData.speed);
		speedEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				if(TextUtils.isEmpty(s))
					settingData.speed = 0;
				else
					settingData.speed = Double.valueOf(s.toString());
				main.setSpeed(settingData.speed);
			}
		});
	}
	
	private void setSetingAction()
	{
		View setTingView = guillotineView.findViewById(R.id.settings_group);
		setTingView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				showMenuDialog();
				
				main.startActivity(new Intent(main, Help.class));
				
			}
		});
	}
	
	public void clearMenuParameter()
	{
		settingData = new SettingData(false, 0.08, 0.1, 0.3, 0);
		main.setSettingData(settingData);
		refresh();
	}
	
//	//按钮加速度
//	private double acceleration = 0.08;
//	//转盘摩擦力
//	private double friction = 0.1;
//	//手指摩擦力
//	private double touchFriction = 0.3;
//	//自定义速度
//	private double speed = 0;
//
//	public void showMenuDialog()
//	{
//		LinearLayout linearLayout = new LinearLayout(main);
//		linearLayout.setOrientation(LinearLayout.VERTICAL);
//		
//		acceleration = main.getAcceleration();
//		friction = main.getFriction();
//		speed = main.getSpeed();
//		
//		final EditText editText0 = new EditText(main);
//		editText0.setText(""+acceleration);
//		editText0.selectAll();
//		editText0.setKeyListener(new DigitsKeyListener(false,true));
//		linearLayout.addView(editText0);
//		
//		final EditText editText1 = new EditText(main);
//		editText1.setText(""+friction);
//		editText1.selectAll();
//		editText1.setKeyListener(new DigitsKeyListener(false,true));
//		linearLayout.addView(editText1);
//		
//		final EditText editText2 = new EditText(main);
//		editText2.setText(""+speed);
//		editText2.selectAll();
//		editText2.setKeyListener(new DigitsKeyListener(false,true));
//		linearLayout.addView(editText2);
//		
//		new AlertDialog.Builder(main)
//			.setTitle("请输入加速度，摩擦力：")
//			.setIcon(android.R.drawable.ic_dialog_info)
//			.setView(linearLayout)
//			.setPositiveButton("确认",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog,int which) {
//						acceleration = Double.valueOf(editText0.getText().toString());
//						main.setAcceleration(acceleration);
//						friction = Double.valueOf(editText1.getText().toString());
//						main.setFriction(friction);
//						speed = Double.valueOf(editText2.getText().toString());
//						main.setSpeed(speed);
//					}
//				})
//			.setNegativeButton("取消",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog,int which) {
//					}
//				})
//			.create()
//			.show();
//	}

}
