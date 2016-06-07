package com.smarttiger.bigdial;

import com.smarttiger.bigdial.DataControl.SettingData;

public interface MainInterface {
	
	public void setSettingData(SettingData data);
	
	/** 设置是否显示速度。  */
	public void setIsShowSpeed(boolean isShow);
	
	/** 设置是否隐藏选项列表。  */
	public void setIsHideItemList(boolean isHide);
	
	/** 设置按钮加速度。  */
	public void setAcceleration(double acceleration);
	/** 获取按钮加速度。 */
	public double getAcceleration();
	
	/** 设置转盘摩擦力。 */
	public void setFriction(double friction);
	/** 获取转盘摩擦力。 */
	public double getFriction();
	
	/** 设置手指摩擦力。 */
	public void setTouchFriction(double touchFriction);
	
	/** 设置固定速度。 */
	public void setSpeed(double speed);
	/** 获取固定速度。 */
	public double getSpeed();
	
	/** 设置外挂参数 */
	public void setCheatIndex(int index, boolean hasCheat);
	
	public void StartHelpActivity();
}
