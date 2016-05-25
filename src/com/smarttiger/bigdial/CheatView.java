package com.smarttiger.bigdial;

import com.smarttiger.bigdial.DataControl.SettingData;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;

public class CheatView extends Activity {

	private DataControl dataControl;
	private SettingData settingData;
	private CheckBox hasCheatBox;
	private EditText cheatIndexEdit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cheat_view);
		
		dataControl = new DataControl(this);
		settingData = dataControl.getSettingData();
		
		initView();
	}
	
	private void initView()
	{
		hasCheatBox = (CheckBox) findViewById(R.id.hasCheatBox);
		cheatIndexEdit = (EditText) findViewById(R.id.cheatIndexEdit);
		hasCheatBox.setChecked(settingData.hasCheat);
		cheatIndexEdit.setText(""+settingData.cheatIndex);
		hasCheatBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				settingData.hasCheat = isChecked;
			}
		});
		cheatIndexEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(s))
					settingData.cheatIndex = new Integer(s.toString());
			}
		});
	}

	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		dataControl.saveSetting(settingData);
	}

	public void onBackToMain(View view) {
		this.finish();
	}
}
