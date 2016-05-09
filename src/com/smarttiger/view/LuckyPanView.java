package com.smarttiger.view;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.smarttiger.bigdial.DataControl.LuckyData;
import com.smarttiger.bigdial.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;


public class LuckyPanView extends SurfaceView implements Callback, Runnable
{

	private SurfaceHolder mHolder;
	/**
	 * 与SurfaceHolder绑定的Canvas
	 */
	private Canvas mCanvas;
	/**
	 * 用于绘制的线程
	 */
	private Thread t;
	/**
	 * 线程的控制开关
	 */
	private boolean isRunning;

	/**
	 * 抽奖的文字
	 */
	private String[] mStrs = new String[] { "选项一", "选项二", "选项三", "选项四" };
	/**
	 * 每个盘块的颜色
	 */
//	private int[] mColors = new int[] { 0xFFFFC300, 0xFFF17E01, 0xFFFFC300, 0xFFF17E01};
	private int[] Colors = new int[] { 0xFFEC3D4C, 0xFF3DB144, 0xFF01BEFE, 0xFFFFF000, 0xFFC336F4};
	private int[] mColors = new int[] { 0xFFEC3D4C, 0xFF3DB144, 0xFF01BEFE, 0xFFFFF000};
	/**
	 * 与文字对应的图片
	 */
	private int[] mImgs = new int[] { R.drawable.item1, R.drawable.smile2, R.drawable.item3
			, R.drawable.smile3, R.drawable.item2, R.drawable.smile4
			, R.drawable.item4, R.drawable.smile5, R.drawable.item5
			, R.drawable.smile1, R.drawable.item6, R.drawable.smile6 };

	/**
	 * 每个盘块所占比重，扫过的角度
	 */
	private double[] mSweepAngles = new double[]{60, 120, 30, 150};
	
	/**
	 * 与文字对应图片的bitmap数组
	 */
	private Bitmap[] mImgsBitmap;
	/**
	 * 盘块的个数
	 */
	private int mItemCount = 0;

	/**
	 * 绘制盘块的范围
	 */
	private RectF mRange = new RectF();
	/**
	 * 圆的直径
	 */
	private int mRadius;
	/**
	 * 绘制盘快的画笔
	 */
	private Paint mArcPaint;

	/**
	 * 绘制文字的画笔
	 */
	private Paint mTextPaint;
	private Paint mTextPaint2;

	/**
	 * 滚动的速度
	 */
	private double mSpeed;
	//是否显示速度
	private boolean isShowSpeed = false;
	private volatile float mStartAngle = 0;
	//转盘摩擦力
	private double friction = 0.1;
	//手指摩擦力
	private double touchFriction = 0;
	//按钮加速度
	private double acceleration = 0;
	
	/**
	 * 是否点击了停止
	 */
	private boolean isShouldEnd;

	/**
	 * 控件的中心位置
	 */
	private int mCenter;
	/**
	 * 控件的padding，这里我们认为4个padding的值一致，以paddingleft为标准
	 */
	private int mPadding;

	/**
	 * 背景图的bitmap
	 */
	private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(),
			R.drawable.bg2);
	/**
	 * 文字的大小
	 */
	private float mTextSize = TypedValue.applyDimension(
			TypedValue.COMPLEX_UNIT_SP, 20, getResources().getDisplayMetrics());

	public LuckyPanView(Context context)
	{
		this(context, null);
	}

	public LuckyPanView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		System.out.println("------LuckyPanView()------");

		mHolder = getHolder();
		mHolder.addCallback(this);

		// setZOrderOnTop(true);// 设置画布 背景透明
		// mHolder.setFormat(PixelFormat.TRANSLUCENT);

		setFocusable(true);
		setFocusableInTouchMode(true);
		this.setKeepScreenOn(true);

	}

	/**
	 * 设置控件为正方形
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		System.out.println("------onMeasure()-----");
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
		// 获取圆形的直径
		mRadius = width - getPaddingLeft() - getPaddingRight();
		// padding值
		mPadding = getPaddingLeft();
		// 中心点
		mCenter = width / 2;
		setMeasuredDimension(width, width);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		System.out.println("------surfaceCreated()-----");
		// 初始化绘制圆弧的画笔
		mArcPaint = new Paint();
		mArcPaint.setAntiAlias(true);
		mArcPaint.setDither(true);
		// 初始化绘制文字的画笔
		mTextPaint = new Paint();
		mTextPaint.setColor(0xFFffffff);
		mTextPaint.setTextSize(mTextSize);
		mTextPaint2 = new Paint();
		mTextPaint2.setColor(0xFF000fff);
		mTextPaint2.setTextSize(mTextSize);
		// 圆弧的绘制范围
		mRange = new RectF(getPaddingLeft(), getPaddingLeft(), mRadius
				+ getPaddingLeft(), mRadius + getPaddingLeft());

		// 初始化图片
		mImgsBitmap = new Bitmap[12];
		for (int i = 0; i < 12; i++)
		{
			mImgsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImgs[i]);
		}

//		setYesOrNo();
//		resetItem();
		
		// 开启线程
		isRunning = true;
		t = new Thread(this);
		t.start();
		

		draw();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder)
	{
		// 通知关闭线程
		isRunning = false;
	}

	@Override
	public void run()
	{
		// 不断的进行draw
		while (isRunning)
		{
			long start = System.currentTimeMillis();
			if(mItemCount > 0)
				draw();
			long end = System.currentTimeMillis();
			try
			{
				if (end - start < 50)
				{
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}

		}

	}

	private void draw()
	{
		try
		{
			// 获得canvas
			mCanvas = mHolder.lockCanvas();
			if (mCanvas != null)
			{
				// 绘制背景图
				drawBg();

				/**
				 * 绘制每个块块，每个块块上的文本，每个块块上的图片
				 */
				float tmpAngle = mStartAngle;
				float sweepAngle = 0;
				for (int i = 0; i < mItemCount; i++)
				{
					sweepAngle = (float) mSweepAngles[i];
					if(sweepAngle == 0)
						continue;
					// 绘制快快
					mArcPaint.setColor(mColors[i]);
//					mArcPaint.setStyle(Style.STROKE);
					mCanvas.drawArc(mRange, tmpAngle, sweepAngle, true,
							mArcPaint);
					// 绘制文本
					drawText(tmpAngle, sweepAngle, mStrs[i]);
					drawSpeed(mSpeed);
					// 绘制Icon
					drawIcon(tmpAngle, sweepAngle, i);

					tmpAngle += sweepAngle;
				}

				// 如果mSpeed不等于0，则相当于在滚动
				mStartAngle += mSpeed;

				// 点击停止时，设置mSpeed为递减，为0值转盘停止
				if (isShouldEnd)
				{
					mSpeed -= friction;
					mSpeed -= touchFriction;
				}
				else
					mSpeed += acceleration;
				
				if (mSpeed <= 0)
				{
					mSpeed = 0;
					isShouldEnd = false;
				}
				// 根据当前旋转的mStartAngle计算当前滚动到的区域
//				calInExactArea(mStartAngle);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		} finally
		{
			if (mCanvas != null)
				mHolder.unlockCanvasAndPost(mCanvas);
		}

	}

	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域 绘制背景，不重要，完全为了美观
	 */
	private void drawBg()
	{
		mCanvas.drawColor(getResources().getColor(R.color.guillotine_background_dark));
		mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding / 2,
				mPadding / 2, getMeasuredWidth() - mPadding / 2,
				getMeasuredWidth() - mPadding / 2), null);
	}

	/**
	 * 根据当前旋转的mStartAngle计算当前滚动到的区域
	 * 
	 * @param startAngle
	 */
	public void calInExactArea(float startAngle)
	{
		// 让指针从水平向右开始计算
		float rotate = startAngle + 90;
		rotate %= 360.0;
		for (int i = 0; i < mItemCount; i++)
		{
			// 每个的中奖范围
			float from = 360 - (i + 1) * (360 / mItemCount);
			float to = from + 360 - (i) * (360 / mItemCount);

			if ((rotate > from) && (rotate < to))
			{
				Log.d("TAG", mStrs[i]);
				return;
			}
		}
	}

	/**
	 * 绘制图片
	 * 
	 * @param startAngle
	 * @param sweepAngle
	 * @param i
	 */
	private void drawIcon(float startAngle, float sweepAngle, int i)
	{
		// 设置图片的宽度为直径的1/8
		int imgWidth = mRadius / 8;

		float angle = (float) ((sweepAngle/2 + startAngle) * (Math.PI / 180));

		int x = (int) (mCenter + mRadius / 2 / 1.5 * Math.cos(angle));
		int y = (int) (mCenter + mRadius / 2 / 1.5 * Math.sin(angle));

		// 确定绘制图片的位置
		Rect rect = new Rect(x - imgWidth / 2, y - imgWidth / 2, x + imgWidth
				/ 2, y + imgWidth / 2);

		mCanvas.drawBitmap(mImgsBitmap[i], null, rect, null);

	}

	/**
	 * 绘制文本
	 * 
	 * @param rect
	 * @param startAngle
	 * @param sweepAngle
	 * @param string
	 */
	private void drawText(float startAngle, float sweepAngle, String string)
	{
		Path path = new Path();
		path.addArc(mRange, startAngle, sweepAngle);
		float textWidth = mTextPaint.measureText(string);
		// 利用水平偏移让文字居中
		float hOffset = (float) (mRadius * Math.PI / 360*sweepAngle / 2 - textWidth / 2);// 水平偏移
		float vOffset = mRadius / 2 / 6;// 垂直偏移
		mCanvas.drawTextOnPath(string, path, hOffset, vOffset, mTextPaint);
	}
	
	private void drawSpeed(double speed)
	{
		if(!isShowSpeed)
			return;
		float textWidth = mTextPaint.measureText(""+speed);
		BigDecimal bd = new BigDecimal(speed);
		bd = bd.setScale(2,BigDecimal.ROUND_HALF_UP);  
		mCanvas.drawText(""+bd+"度/帧", 20, 50, mTextPaint2);
		
		//也是保留两位小数显示，但是小数为0时就不显示了，上面方法是固定的，小数为0时，也显示。
//		java.text.DecimalFormat df=new java.text.DecimalFormat("#.##");
//		mCanvas.drawText(""+df.format(speed)+"度/秒", 20, 50, mTextPaint2);
	}
	
	/**
	 * 是否显示速度
	 */
	public void isShowSpeed(boolean isShow)
	{
		isShowSpeed = isShow;
	}

	/**
	 * 点击开始旋转
	 * 里面的v1和v2是为了设置速度在v1到v2之间，刚好可以停在预定的扇形(luckyIndex)里
	 * @param luckyIndex
	 */
	public void luckyStart(int luckyIndex)
	{
		// 每项角度大小
		float angle = (float) (360 / mItemCount);
		// 中奖角度范围（因为指针向上，所以水平第一项旋转到指针指向，需要旋转210-270；）
		float from = 270 - (luckyIndex + 1) * angle;
		float to = from + 6*angle;
		// 停下来时旋转的距离
		float targetFrom = (float) ((0.1) * 360 + from);
		/**
		 * <pre>
		 *  (v1 + 0) * (v1+1) / 2 = target ;
		 *  v1*v1 + v1 - 2target = 0 ;
		 *  v1=-1+(1*1 + 8 *1 * target)/2;
		 * </pre>
		 */
		float v1 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetFrom) - 1) / 2;
		float targetTo = (float) (0.1 * 360 + to);
		float v2 = (float) (Math.sqrt(1 * 1 + 8 * 1 * targetTo) - 1) / 2;
		mSpeed = (float) (v1 + Math.random() * (v2 - v1));
		
		
		mSpeed = 100;
		
		isShouldEnd = false;
	}

	/**
	 * 直接随机一个速度。
	 * @param speed
	 */
	public void luckyStartRandom()
	{
		mSpeed = (int)(Math.random()*27 + 27) ;
		isShouldEnd = false;
	}
	
	/**
	 * 可累加的速度。
	 * @param speed
	 */
	public void luckyStarting(double speed)
	{
		mSpeed += speed;
		if (mSpeed <= 0)
			mSpeed = 0;
		isShouldEnd = false;
	}

	
	public void luckyEnd()
	{
//		mStartAngle = 0;///这个是配合上面那个设置预定停止位置的，如果真要随机，可以不用设置为0；
		isShouldEnd = true;
	}

	public boolean isStart()
	{
		return mSpeed != 0;
	}

	public boolean isShouldEnd()
	{
		return isShouldEnd;
	}

	
	
	//一切操作都修改下列变量。修改完成后，再重置那几个数组即可。
	private ArrayList<String> nameList = new ArrayList<String>();
	private ArrayList<Integer> weightList = new ArrayList<Integer>();
	private int allWeight = 0;
	
	public void drawItem()
	{
		draw();
	}
	
	/**
	 * 重置转盘，设置mItemCount为0。
	 */
	public void resetItem()
	{
		mItemCount = 0;
		
		nameList.clear();
		weightList.clear();
		allWeight = 0;
		draw();
	}
	public void addItem(String name, int weight)
	{
//		if(mItemCount > 12)
//			return;
			
		mStartAngle = 0;
		mItemCount++;
		
		System.out.println("addItem--mItemCount=="+mItemCount);
		nameList.add(name);
		weightList.add(new Integer(weight));
		allWeight += weight;
		
		mStrs = new String [mItemCount];
		mSweepAngles = new double [mItemCount];
		mColors = new int[mItemCount];
		for (int i = 0; i < mItemCount; i++) {
			mStrs[i] = nameList.get(i);
			mSweepAngles[i] = (double)weightList.get(i) / allWeight * 360;
			mColors[i] = Colors[i%5];
		}
		
		if(mItemCount % 5 == 1)
			mColors[mItemCount-1] = Colors[2];
		

		draw();
	}
	
	/**
	 * 全盘设置转盘。
	 * @param strs 选项名
	 * @param weights 权重
	 * @param allWeight 总权重
	 * @param itemCount 选项个数
	 */
	public void setItems(String[] strs, int[] weights, int allWeight, int itemCount)
	{
		mItemCount = itemCount;
		this.allWeight = allWeight;
		mStrs = strs;
		mSweepAngles = new double [mItemCount];
		mColors = new int[mItemCount];
		for (int i = 0; i < mItemCount; i++) {
			mSweepAngles[i] = (double)weights[i] / allWeight * 360;
			mColors[i] = Colors[i%5];
		}
		if(mItemCount % 5 == 1)
			mColors[mItemCount-1] = Colors[2];
		
	}
	
	
	public void changeItemName(int index, String name)
	{
		System.out.println("changName--"+index);
		
		if(index >= mItemCount)
			return;
		
		nameList.set(index, name);
		mStrs[index] = name;
		
		draw();
	}
	public void changeItemWeight(int index, int weight)
	{
		System.out.println("changeWeight--"+index);
		if(index >= mItemCount)
			return;
		
		allWeight = (int) (allWeight - weightList.get(index) + weight);

		weightList.set(index, weight);
		
		for (int i = 0; i < mItemCount; i++) {
			mSweepAngles[i] = (double)weightList.get(i) / allWeight * 360 ;
		}

		draw();
	}
	
	
	public void deleteItem(int index)
	{
		System.out.println("deleteItem--"+index);
		changeItemWeight(index, 0);
	}
	
	
	//直接删除会导致再次添加的item更改时，错乱。所以直接设置权重为0即为删除。
	public void deleteItem0(int index)
	{

		System.out.println("deleteItem--"+index);
		
		if(index >= mItemCount)
			return;
		
		mItemCount--;
		
		allWeight -= weightList.get(index);
		nameList.remove(index);
		weightList.remove(index);
		
		mStrs = new String [mItemCount];
		mSweepAngles = new double [mItemCount];
		mColors = new int[mItemCount];
		for (int i = 0; i < mItemCount; i++) {
			mStrs[i] = nameList.get(i);
			mSweepAngles[i] = (double)weightList.get(i) / allWeight * 360;
			mColors[i] = Colors[i%5];
		}
		
		if(mItemCount % 5 == 1)
			mColors[mItemCount-1] = Colors[2];
		
		draw();
	}
	
	public void setYesOrNo()
	{
		mStrs = new String[] { "是", "否" };
		mSweepAngles = new double[]{180, 180};
		mStartAngle = 90;
		mItemCount = 2;
	}
	
	public int getItemCount()
	{
		return mItemCount;
	}
	
	public void setLuckyData(LuckyData luckyData)
	{
		mItemCount = luckyData.itemCount;
		mStrs = luckyData.names;
		mSweepAngles = luckyData.sweepAngles;
		allWeight = luckyData.allWeight;
		
		mColors = new int[mItemCount];
		
		nameList.clear();
		weightList.clear();
		for (int i = 0; i < mItemCount; i++) {
			nameList.add(luckyData.names[i]);
			weightList.add(luckyData.weights[i]);
			
			mColors[i] = Colors[i%5];
		}
		

		if(mItemCount % 5 == 1)
			mColors[mItemCount-1] = Colors[2];
		
		draw();
	}
	
	public LuckyData getLuckyData()
	{
		LuckyData luckyData = new LuckyData(mItemCount);
		for (int i = 0; i < mItemCount; i++) {
			luckyData.names[i] = nameList.get(i);
			luckyData.weights[i] = weightList.get(i);
		}
		luckyData.sweepAngles = mSweepAngles;
		luckyData.itemCount = mItemCount;
		luckyData.allWeight = allWeight;
		return luckyData;
	}
	
	public void setFriction(double friction)
	{
		this.friction = friction;
	}
	
	public void setTouchFriction(double touchFriction)
	{
		this.touchFriction = touchFriction;
	}
	
	public void setAcceleration(double acceleration)
	{
		this.acceleration = acceleration;
	}
}
