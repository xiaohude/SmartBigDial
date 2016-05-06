package com.smarttiger.bigdial;

import com.smarttiger.bigdial.DataControl.LuckyData;
import com.smarttiger.bigdial.DataControl.SettingData;
import com.smarttiger.guillotine.util.GuillotineAnimation;
import com.smarttiger.guillotine.util.GuillotineMenuInit;
import com.smarttiger.view.DeleteClickListener;
import com.smarttiger.view.LuckyPanView;
import com.smarttiger.view.NameTextWatcher;
import com.smarttiger.view.WeightTextWatcher;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;


/**
 * Created by Dmytro Denysenko on 5/4/15.
 */
public class MainActivity extends ActionBarActivity {
    private static final long RIPPLE_DURATION = 300;
    Toolbar toolbar;
    FrameLayout root;
    View contentHamburger;
    
	private Context context;
	private LuckyPanView mLuckyPanView;
	private ImageView mStartBtn;
	private boolean isLongClick = false;
	
	private LayoutInflater mInflater;
	private LinearLayout itemsLayout;
	
	//设置参数
	private SettingData settingData = new SettingData(false, 0.08, 0.1, 0);
	
	//viewIndex和LuckyPanView里的itemCount对应，只增不减，删除只是把weight改为0.只有重置的时候才清空。
	private int viewIndex = 0;
	private DataControl dataControl;
	
	private GuillotineMenuInit guillotineMenuInit;
	
	private OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				System.out.println("OnTouchListener----ACTION_DOWN");
				InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			    inputMethodManager.hideSoftInputFromWindow(itemsLayout.getWindowToken(), 0);
			}
			else if(event.getAction() == MotionEvent.ACTION_MOVE)
			{
				System.out.println("OnTouchListener----ACTION_MOVE");
				mLuckyPanView.luckyStarting(0-settingData.touchFriction);
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				System.out.println("OnTouchListener----ACTION_UP");
				mLuckyPanView.luckyEnd();
			}
			
			return false;
		}
	};
	

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity);
		context = this;
		
		dataControl = new DataControl(context);
		settingData = dataControl.getSettingData();
		mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		itemsLayout = (LinearLayout) findViewById(R.id.items_layout);
		mLuckyPanView = (LuckyPanView) findViewById(R.id.id_luckypan);
		
		itemsLayout.setOnTouchListener(touchListener);///研究一下为啥没效果。
		
		mLuckyPanView.setOnClickListener(null);//只有设置了setOnClickListener。才能在OnTouch里面监听到ACTION_MOVE
		mLuckyPanView.setOnTouchListener(touchListener);

        initGuillotineMenu();
        
		initLuckButton();
		
		initItemsLayout();
		
		createItemView();
		
    }
	
	
	public void onResetClick(View view)
	{
		mLuckyPanView.resetItem();
		viewIndex = 0;
		itemsLayout.removeAllViews();
		createItemView();
	}
	
	public void onResetMenuClick(View view)
	{
		guillotineMenuInit.clearMenuParameter();
		guillotineMenuInit.cleanRadio();
	}

    //铡刀菜单旋转中心坐标。
    private float x = 0;
    private float y = 0;
    View guillotineMenu;
    GuillotineAnimation guillotineAnimation;
	//初始化铡刀菜单
	private void initGuillotineMenu()
	{
		toolbar = (Toolbar) findViewById(R.id.toolbar);
        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = findViewById(R.id.content_hamburger);
        
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);
        
        guillotineMenuInit = new GuillotineMenuInit(this, guillotineMenu, settingData);

        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, 
        		guillotineMenu.findViewById(R.id.guillotine_hamburger), 
        		contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
	}
	
	
	private void initItemsLayout()
	{
		LuckyData luckyData = dataControl.getItems();
		
		for (int i = 0; i < luckyData.itemCount; i++) {
			if(luckyData.weights[i] == 0)
				viewIndex++;
			else
				createItemView(luckyData.names[i], luckyData.weights[i]);
		}
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		System.out.println("-------onResume()------");
		LuckyData luckyData = dataControl.getItems();
		mLuckyPanView.setLuckyData(luckyData);
		mLuckyPanView.isShowSpeed(settingData.isShowSpeed);
		mLuckyPanView.setFriction(settingData.friction);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 所有View被绘制完成后立马调用,在这可以测量view的高宽。
		// 窗口获得或失去焦点时被调用,在onResume之后或onPause之后
		super.onWindowFocusChanged(hasFocus);
		System.out.println("-------onWindowFocusChanged()------"+hasFocus);
		
		if(x == 0)
		{
			x = GuillotineAnimation.calculatePivotX(contentHamburger);
	        y = GuillotineAnimation.calculatePivotY(contentHamburger);
			guillotineMenu.setPivotX(x);
			guillotineMenu.setPivotY(y);
			toolbar.setPivotX(x);
			toolbar.setPivotY(y);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		System.out.println("------onPause()-------");
		dataControl.putItems(mLuckyPanView.getLuckyData());
		dataControl.saveSetting(settingData);
	}

	public void createItemView()
	{
		createItemView("", 0);
	}
	
	
	/**
	 * 新建一个选项编辑框的itemView
	 */
	public void createItemView(String name, int weight)
	{
		
		View itemView = mInflater.inflate(R.layout.item_view, itemsLayout, false);
		
		EditText nameEdit = (EditText) itemView.findViewById(R.id.item_name);
		EditText weightEdit = (EditText) itemView.findViewById(R.id.item_weight);
		ImageView deleteButton = (ImageView) itemView.findViewById(R.id.delete_button);
		
		nameEdit.setText(name);
		weightEdit.setText(""+weight);
		if(weight > 0)
			deleteButton.setVisibility(View.VISIBLE);
		
		nameEdit.addTextChangedListener(new NameTextWatcher(viewIndex, this, weightEdit, deleteButton, mLuckyPanView));
		
		weightEdit.addTextChangedListener(new WeightTextWatcher(viewIndex, deleteButton, mLuckyPanView));
		
		deleteButton.setOnClickListener(new DeleteClickListener(viewIndex, this, itemsLayout, itemView, mLuckyPanView));
		
		itemsLayout.addView(itemView);
		
		viewIndex++;

	}
	
	
	
	
	public void deleteItemView()
	{
		if(itemsLayout.getChildCount() == 1)
		{
			mLuckyPanView.resetItem();
			viewIndex = 0;
			itemsLayout.removeAllViews();
			createItemView();
		}
			
	}
	
	
	private void initLuckButton()
	{
		mStartBtn = (ImageView) findViewById(R.id.id_start_btn);

		mStartBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				System.out.println("onClick----------");
				
				
				if(settingData.speed == 0)
					mLuckyPanView.luckyStartRandom();
				else
					mLuckyPanView.luckyStarting(settingData.speed);
				mLuckyPanView.luckyEnd();
				
				
			}
		});
		
		
		mStartBtn.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("onLongClick----------");
				
				isLongClick = true;
				
				return true;
			}
		});
		
		mStartBtn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					System.out.println("000000000000000000000");
				}
				else if(event.getAction() == MotionEvent.ACTION_MOVE)
				{
					System.out.println("111111111111111111111");
					if(isLongClick)
						mLuckyPanView.luckyStarting(settingData.acceleration);
				}
				else if(event.getAction() == MotionEvent.ACTION_UP)
				{
					System.out.println("222222222222222222222");
					
					if(isLongClick)
						mLuckyPanView.luckyEnd();
					
					isLongClick = false;
					
					
//					mLuckyPanView.luckyStarting(100);
//					mLuckyPanView.luckyEnd();
				}
				
				return false;
			}
		});
	}
	
	public void setSettingData(SettingData data)
	{
		settingData = data;
		mLuckyPanView.setFriction(data.friction);
	}
	
	/** 设置是否显示速度。  */
	public void setIsShowSpeed(boolean isShow)
	{
		settingData.isShowSpeed = isShow;
		mLuckyPanView.isShowSpeed(isShow);
	}
	/** 设置按钮加速度。  */
	public void setAcceleration(double acceleration)
	{
		settingData.acceleration = acceleration;
	}
	/** 获取按钮加速度。 */
	public double getAcceleration()
	{
		return settingData.acceleration;
	}
	
	/** 设置转盘摩擦力。 */
	public void setFriction(double friction)
	{
		settingData.friction = friction;
		mLuckyPanView.setFriction(friction);
	}
	/** 获取转盘摩擦力。 */
	public double getFriction()
	{
		return settingData.friction;
	}
	
	/** 设置手指摩擦力。 */
	public void setTouchFriction(double touchFriction)
	{
		settingData.touchFriction = touchFriction;
	}
	
	/** 设置固定速度。 */
	public void setSpeed(double speed)
	{
		settingData.speed = speed;
	}
	/** 获取固定速度。 */
	public double getSpeed()
	{
		return settingData.speed;
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		//解决菜单界面点击返回直接退出问题。
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
        	if(guillotineMenu.getVisibility() == View.VISIBLE) {
        		guillotineAnimation.close();
                return true;
        	}
         }
         return super.onKeyDown(keyCode, event);
	}
}
