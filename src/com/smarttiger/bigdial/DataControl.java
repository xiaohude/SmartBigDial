package com.smarttiger.bigdial;

import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.content.SharedPreferences;

public class DataControl {
	
	public static final String DATA_ARRAY = "data_array";
	public static final String NAME_STRING_ARRAY = "name_array";
	public static final String WEIGHT_INT_ARRAY = "weight_array";
	public static final String SWEEP_ANGLE_DOUBLE_ARRAY = "sweep_angle_double";
	public static final String ITEMCOUNT_INT = "itemcount_int";
	public static final String ALLWEIGHT_INT = "allweight_int";
	
	private SharedPreferences prefs;
	private SharedPreferences.Editor editor;
	
	public static class LuckyData{
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
	
	public DataControl(Context context) {
		// TODO Auto-generated constructor stub
		
		prefs = context.getSharedPreferences(DATA_ARRAY, Context.MODE_PRIVATE);  
	    editor = prefs.edit();  
	}
	
	
	public void putNames(String[] names)
	{
		JSONArray jsonArray = new JSONArray();  
	    for (String s : names) {  
	        jsonArray.put(s);  
	    }    
	    editor.putString(DATA_ARRAY,jsonArray.toString());  
	    editor.commit();  
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
	
	public LuckyData getItems()
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
	
	

}
