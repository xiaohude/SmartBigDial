package com.smarttiger.bigdial;

import android.app.Activity;
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
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		WebView webview = (WebView) findViewById(R.id.webView1);
		webview.loadUrl("file:///android_asset/help.txt");
		
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setOnLongClickListener(new OnLongClickListener() {		
			@Override
			public boolean onLongClick(View arg0) {
				return false;
			}
		});
		
	}

	public void onBackToMain(View view) {
		this.finish();
	}
}