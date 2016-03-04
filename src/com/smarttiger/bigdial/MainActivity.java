package com.smarttiger.bigdial;

import com.smarttiger.bigdial.DataControl.LuckyData;
import com.smarttiger.guillotine.util.GuillotineAnimation;
import com.smarttiger.guillotine.util.GuillotineMenuInit;
import com.smarttiger.view.DeleteClickListener;
import com.smarttiger.view.LuckyPanView;
import com.smarttiger.view.NameTextWatcher;
import com.smarttiger.view.WeightTextWatcher;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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
	
	//按钮加速度
	private double acceleration = 0.08;
	//转盘摩擦力
	private double friction = 0.1;
	//自定义速度
	private double speed = 0;
	
	//viewIndex和LuckyPanView里的itemCount对应，只增不减，删除只是把weight改为0.只有重置的时候才清空。
	private int viewIndex = 0;
	private DataControl dataControl;
	
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
				mLuckyPanView.luckyStarting(-0.3);
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

    //铡刀菜单旋转中心坐标。
    private float x = 0;
    private float y = 0;
    View guillotineMenu;
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
        
        GuillotineMenuInit guillotineMenuInit = new GuillotineMenuInit(this, guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, 
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
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		System.out.println("------onPause()-------");
		dataControl.putItems(mLuckyPanView.getLuckyData());
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO 所有View被绘制完成后立马调用,在这可以测量view的高宽。
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		System.out.println("-------onResume()------");
		LuckyData luckyData = dataControl.getItems();
		mLuckyPanView.setLuckyData(luckyData);
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
				
				
				if(speed == 0)
					mLuckyPanView.luckyStartRandom();
				else
					mLuckyPanView.luckyStarting(speed);
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
						mLuckyPanView.luckyStarting(acceleration);
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
	
	
	public void showMenuDialog()
	{
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		
		
		final EditText editText0 = new EditText(this);
		editText0.setText(""+acceleration);
		editText0.selectAll();
		editText0.setInputType(InputType.TYPE_CLASS_NUMBER);
		linearLayout.addView(editText0);
		
		final EditText editText1 = new EditText(this);
		editText1.setText(""+friction);
		editText1.selectAll();
		editText1.setInputType(InputType.TYPE_CLASS_NUMBER);
		linearLayout.addView(editText1);
		
		final EditText editText2 = new EditText(this);
		editText2.setText(""+speed);
		editText2.selectAll();
		editText2.setInputType(InputType.TYPE_CLASS_NUMBER);
		linearLayout.addView(editText2);
		
		new AlertDialog.Builder(this)
			.setTitle("请输入加速度，摩擦力：")
			.setIcon(android.R.drawable.ic_dialog_info)
			.setView(linearLayout)
			.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {

						friction = Double.valueOf(editText1.getText().toString());
						mLuckyPanView.setFriction(friction);
						
						acceleration = Double.valueOf(editText0.getText().toString());
						
						speed = Double.valueOf(editText2.getText().toString());
						
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
