package com.smarttiger.bigdial;

import com.smarttiger.bigdial.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.webkit.WebView;
import android.widget.TextView;

/**
 * 显示软件帮助和软件基本信息的
 * */
public class Help extends Activity {
	private TextView titleText;
	private Context context;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		context = this;
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.loadUrl("file:///android_asset/help.html");
		
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setOnLongClickListener(new OnLongClickListener() {		
			@Override
			public boolean onLongClick(View arg0) {
				startActivity(new Intent(context, CheatActivity.class));
				return false;
			}
		});
		
	}

	public void onBackToMain(View view) {
		this.finish();
	}
}