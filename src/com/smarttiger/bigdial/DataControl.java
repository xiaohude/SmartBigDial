package com.smarttiger.bigdial;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class DataControl {
	
	public static final String DATA_PREFERENCES = "data_preferences";
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	public static final String NAME_STRING_ARRAY = "name_array";
	public static final String WEIGHT_INT_ARRAY = "weight_array";
	public static final String SWEEP_ANGLE_DOUBLE_ARRAY = "sweep_angle_double";
	public static final String ITEMCOUNT_INT = "itemcount_int";
	public static final String ALLWEIGHT_INT = "allweight_int";

	public static final String ISSHOWSPEED_BOOLEAN = "isshowspeed_boolean";
	public static final String ACCELERATION_DOUBLE = "acceleration_double";
	public static final String FRICTION_DOUBLE = "friction_double";
	public static final String TOUCHFRICTION_DOUBLE = "touchfriction_double";
	public static final String SPEED_DOUBLE = "speed_double";
	
	public static final String HASCHEAT_BOOLEAN = "hascheat_boolean";
	public static final String CHEATINDEX_INT = "cheatindex_int";
	public static final String CHEATCOUNT_INT = "cheatcount_int";
	public static final String CHEATINDEXS_ARRAY= "cheatindexS_array";
	
	public static class LuckyData {
		public String[] names;
		public int[] weights;
		public double[] sweepAngles;
		public int itemCount;
		public int allWeight;
		
		public LuckyData(int itemCount)
		{
			names = new String[itemCount];
			Arrays.fill(names, ""); 
			weights = new int[itemCount];
			Arrays.fill(weights, 1); 
			sweepAngles = new double[itemCount];
			Arrays.fill(sweepAngles, 1); 
			this.itemCount = itemCount;
			allWeight = 0;
		}
		
		public LuckyData(String[] names, int[] weights, double[] sweepAngles, int itemCount, int allWeight) {
			// TODO Auto-generated constructor stub
			this.names = names;
			this.weights = weights;
			this.sweepAngles = sweepAngles;
			this.itemCount = itemCount;
			this.allWeight = allWeight;
		}
	}
	
	public static class SettingData {
		public boolean isShowSpeed = false;
		public double acceleration = 0.08;
		public double friction = 0.1;
		public double touchFriction = 0.3;
		public double speed = 0;
		
		public boolean hasCheat = false;
		public int cheatIndex = 0;
		public int cheatCount = -1;
		public boolean[] cheatIndexs;
		
		public SettingData()
		{
			
		}
		/**
		 * 设置参数类
		 * @param isShowSpeed 是否显示速度
		 * @param acceleration 按钮加速度
		 * @param friction 转盘摩擦力
		 * @param speed 固定速度
		 */
		public SettingData(boolean isShowSpeed, double acceleration, double friction, double speed)
		{
			this.isShowSpeed = isShowSpeed;
			this.acceleration = acceleration;
			this.friction = friction;
			this.speed = speed;
		}
		/**
		 * 设置参数类
		 * @param isShowSpeed 是否显示速度
		 * @param acceleration 按钮加速度
		 * @param friction 转盘摩擦力
		 * @param touchFriction 手指摩擦力
		 * @param speed 固定速度
		 */
		public SettingData(boolean isShowSpeed, double acceleration, double friction, double touchFriction, double speed)
		{
			this.isShowSpeed = isShowSpeed;
			this.acceleration = acceleration;
			this.friction = friction;
			this.touchFriction = touchFriction;
			this.speed = speed;
		}
	}
	
	public DataControl(Context context) {
		// TODO Auto-generated constructor stub
		
		prefs = context.getSharedPreferences(DATA_PREFERENCES, Context.MODE_PRIVATE);  
	    editor = prefs.edit();  
	}
	
	
	public void putItems(String[] names, int[] weights, double[] sweepAngles, int itemCount, int allWeight)
	{
    	try {
			JSONArray nameArray = new JSONArray();
			JSONArray weightArray = new JSONArray(); 
			JSONArray sweepAngleArray = new JSONArray(); 
			
		    for (int i = 0; i < itemCount; i++) {
		    	nameArray.put(names[i]); 
		    	weightArray.put(weights[i]);
				sweepAngleArray.put(sweepAngles[i]);
		    }    
		    editor.putString(NAME_STRING_ARRAY,nameArray.toString());
		    editor.putString(WEIGHT_INT_ARRAY,weightArray.toString());
		    editor.putString(SWEEP_ANGLE_DOUBLE_ARRAY,sweepAngleArray.toString()); 
		    editor.putInt(ITEMCOUNT_INT, itemCount);
		    editor.putInt(ALLWEIGHT_INT, allWeight);
		    editor.commit();  
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void putItems(LuckyData luckyData)
	{
		editor = prefs.edit(); 
    	try {
			JSONArray nameArray = new JSONArray();
			JSONArray weightArray = new JSONArray(); 
			JSONArray sweepAngleArray = new JSONArray(); 
			
		    for (int i = 0; i < luckyData.itemCount; i++) {
		    	nameArray.put(luckyData.names[i]); 
		    	weightArray.put(luckyData.weights[i]);
				sweepAngleArray.put(luckyData.sweepAngles[i]);
		    }    
		    System.out.println("putItems()-----nameArray.toString()==="+nameArray.toString());
		    System.out.println("putItems()-----weightArray.toString()==="+weightArray.toString());
		    System.out.println("putItems()-----luckyData.itemCount==="+luckyData.itemCount);
		    editor.putString(NAME_STRING_ARRAY,nameArray.toString());
		    editor.putString(WEIGHT_INT_ARRAY,weightArray.toString());
		    editor.putString(SWEEP_ANGLE_DOUBLE_ARRAY,sweepAngleArray.toString()); 
		    editor.putInt(ITEMCOUNT_INT, luckyData.itemCount);
		    editor.putInt(ALLWEIGHT_INT, luckyData.allWeight);
		    editor.commit();  
    	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LuckyData getLuckyData()
	{
		int itemCount = prefs.getInt(ITEMCOUNT_INT, 0);
		LuckyData luckyData = new LuckyData(itemCount);
		luckyData.allWeight = prefs.getInt(ALLWEIGHT_INT, 0);
		
	    try { 
			JSONArray nameArray = new JSONArray(prefs.getString(NAME_STRING_ARRAY, "[]"));
		    System.out.println("getItems()-----nameArray.toString()==="+nameArray.toString());
			JSONArray weightArray = new JSONArray(prefs.getString(WEIGHT_INT_ARRAY, "[]")); 
		    System.out.println("getItems()-----weightArray.toString()==="+weightArray.toString());
		    System.out.println("getItems()-----luckyData.itemCount==="+luckyData.itemCount);
			JSONArray sweepAngleArray = new JSONArray(prefs.getString(SWEEP_ANGLE_DOUBLE_ARRAY, "[]")); 
	        for (int i = 0; i < itemCount; i++) {  
	        	luckyData.names[i] = nameArray.getString(i);
	        	luckyData.weights[i] = weightArray.getInt(i);
	        	luckyData.sweepAngles[i] = sweepAngleArray.getDouble(i);
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    
	    return luckyData;
	}
	
	public void saveSetting(SettingData settingData)
	{
	    editor.putBoolean(ISSHOWSPEED_BOOLEAN,settingData.isShowSpeed);
	    putDouble(editor, ACCELERATION_DOUBLE, settingData.acceleration);
	    putDouble(editor, FRICTION_DOUBLE, settingData.friction);
	    putDouble(editor, TOUCHFRICTION_DOUBLE, settingData.touchFriction);
	    putDouble(editor, SPEED_DOUBLE, settingData.speed);
	    
	    editor.putBoolean(HASCHEAT_BOOLEAN, settingData.hasCheat);
	    editor.putInt(CHEATINDEX_INT, settingData.cheatIndex);
	    editor.putInt(CHEATCOUNT_INT, settingData.cheatCount);
	    JSONArray cheatArray = new JSONArray();
		for (int i = 0; i < settingData.cheatCount; i++) {
			cheatArray.put(settingData.cheatIndexs[i]); 
		}    
		editor.putString(CHEATINDEXS_ARRAY,cheatArray.toString());
	    
	    editor.commit();  
	}
	public SettingData getSettingData()
	{
		SettingData settingData = new SettingData();
		settingData.isShowSpeed = prefs.getBoolean(ISSHOWSPEED_BOOLEAN, false);
		settingData.acceleration = getDouble(prefs, ACCELERATION_DOUBLE, 0.08);
		settingData.friction = getDouble(prefs, FRICTION_DOUBLE, 0.1);
		settingData.touchFriction = getDouble(prefs, TOUCHFRICTION_DOUBLE, 0.3);
		settingData.speed = getDouble(prefs, SPEED_DOUBLE, 0);
		
		settingData.hasCheat = prefs.getBoolean(HASCHEAT_BOOLEAN, false);
		settingData.cheatIndex = prefs.getInt(CHEATINDEX_INT, 0);
		settingData.cheatCount = prefs.getInt(CHEATCOUNT_INT, 0);
		try { 
			JSONArray cheatArray = new JSONArray(prefs.getString(CHEATINDEXS_ARRAY, "[]")); 
			settingData.cheatIndexs = new boolean[settingData.cheatCount];
	        for (int i = 0; i < settingData.cheatCount; i++) {  
	        	settingData.cheatIndexs[i] = cheatArray.getBoolean(i);
	        } 
        } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
		
		return settingData;
	}
	
	private Editor putDouble(final Editor edit, final String key, final double value) {
		return edit.putLong(key, Double.doubleToRawLongBits(value));
	}

	private double getDouble(final SharedPreferences prefs, final String key, final double defaultValue) {
		return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));
	}

}
