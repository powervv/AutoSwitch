/**
* 文件名：AutoSwitchActivity.java
*
* 版本信息：
* 日期：2012-11-22
* Copyright powervv.com 2012
* 版权所有
*
*/
package com.powervv.autoswitch;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TableLayout;
//import android.widget.CompoundButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.TimePickerDialog;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
//import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import java.util.Calendar;
import android.util.Log;


/**
*
* 项目名称：com.powervv.autoswitch.AutoSwitchActivity
* 类名称：AutoSwitchActivity
* 类描述：
* 创建人：paul
* 创建时间：2012-12-1 下午6:50:22
* 修改人：paul
* 修改时间：2012-12-1 下午6:50:22
* 修改备注：
* @version
*
*/
public class AutoSwitchActivity extends Activity implements OnClickListener {
	private static final String	TAG	= "AutoSwitch";
	
	/* 数据库名 */
	private final static String	DATABASE_NAME	= "AutoSwitch.db";
	
	/* 表名 */
	private final static String	TABLE_NAME		= "EventTable";
	
	/* 版本 */
	private final static int 	DATABASE_VERSION = 1;
	
	/* 表中的字段 */
	private final static String	TABLE_ID		= "_id";
	private final static String	TABLE_HOUR		= "hour";
	private final static String	TABLE_MINUTE	= "minute";
	private final static String TABLE_WIFI		= "wifi";
	private final static String TABLE_MOBILE	= "mobile";
	private final static String TABLE_ACTIVE	= "active";
	
	/* 创建表的SQL语句 */
	private final static String	CREATE_TABLE	= "CREATE TABLE " + TABLE_NAME + 
		" (" + TABLE_ID + " INTEGER PRIMARY KEY," + TABLE_HOUR + " INTERGER,"+ TABLE_MINUTE + " INTEGER," +
			TABLE_WIFI + " INTEGER," + TABLE_MOBILE + " INTEGER," + TABLE_ACTIVE + " INTEGER)";
	
	/* 数据库对象 */
	private SQLiteDatabase		mSQLiteDatabase	= null;
	
	private Calendar mCalendars[];
	private int mHours[] 	= 	{7, 19, 23}; 
	private int mMinutes[] 	= 	{0, 0, 30}; 
	private boolean mWifiStates[] = {false, true, false};
	private boolean mMobileStates[] = {true, true, false};
	private boolean mActives[] = {true, true, true};
	
	private TableLayout mTable = null;
	private View mView = null;
	//6个多选项
/*	private CheckBox	mCheckBox1;
	private CheckBox	mCheckBox2;
	private CheckBox	mCheckBox3;
	private CheckBox	mCheckBox4;
	private CheckBox	mCheckBox5;
	private CheckBox	mCheckBox6;
	
	private TableRow	mTableRow2;
	private TableRow	mTableRow3;
	private TableRow	mTableRow4;	
	private TextView 	mTextView3;
	private TextView 	mTextView4;
	private TextView 	mTextView5;	*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tableview);
		
        // 读取配置文件，初始化。
		load();
        
		// 更新界面显示
		mTable = (TableLayout) findViewById(R.id.table);
		//-------------------
		for (int i = 0; i < 3; ++i) {
			TableRow row = new TableRow(this);
			//row.setId(0);
			//orders++;

			TextView timeView = new TextView(this);
			timeView.setId(i*3);
			timeView.setText(String.format("%02d", mHours[i]) + ":" + String.format("%02d", mMinutes[i]));	
			//timeView.setVisibility(View.GONE);
			timeView.setOnClickListener(this);

			CheckBox wifiBox = new CheckBox(this);

			wifiBox.setId(i*3 + 1);			
			wifiBox.setChecked(mWifiStates[i]);
			wifiBox.setOnClickListener(this); 			
			
			CheckBox mobileBox = new CheckBox(this);
			mobileBox.setId(i*3 + 2);
			mobileBox.setChecked(mMobileStates[i]);
			mobileBox.setOnClickListener(this); 			
			
			row.addView(timeView);
			row.addView(wifiBox);
			row.addView(mobileBox);

			mTable.addView(row);
			
			View cutLine = new View(this);
			if (i < 2) cutLine.setBackgroundColor(Color.parseColor("#FFE6E6E6"));
			else cutLine.setBackgroundColor(Color.parseColor("#FF909090"));
			cutLine.setMinimumHeight(2);
			//cutLine.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));  
			mTable.addView(cutLine);
		}

		
		//-------------------
        
/*		// 取得每个CheckBox对象 
		mCheckBox1 = (CheckBox) findViewById(R.id.checkBox1);
		mCheckBox2 = (CheckBox) findViewById(R.id.checkBox2);
		mCheckBox3 = (CheckBox) findViewById(R.id.checkBox3);
		mCheckBox4 = (CheckBox) findViewById(R.id.checkBox4);
		mCheckBox5 = (CheckBox) findViewById(R.id.checkBox5);
		mCheckBox6 = (CheckBox) findViewById(R.id.checkBox6);	
		
		mTableRow2 = (TableRow) findViewById(R.id.tableRow2);
		mTableRow3 = (TableRow) findViewById(R.id.tableRow3);
		mTableRow4 = (TableRow) findViewById(R.id.tableRow4);		
		mTextView3 = (TextView) findViewById(R.id.textView3);
		mTextView4 = (TextView) findViewById(R.id.textView4);
		mTextView5 = (TextView) findViewById(R.id.textView5);
		

		
		mCheckBox1.setChecked(mWifiStates[0]);
		mCheckBox2.setChecked(mMobileStates[0]);
		mCheckBox3.setChecked(mWifiStates[1]);
		mCheckBox4.setChecked(mMobileStates[1]);
		mCheckBox5.setChecked(mWifiStates[2]);
		mCheckBox6.setChecked(mMobileStates[2]);
		
		mCheckBox1.setOnClickListener(this);
		mCheckBox2.setOnClickListener(this);
		mCheckBox3.setOnClickListener(this);
		mCheckBox4.setOnClickListener(this);
		mCheckBox5.setOnClickListener(this);
		mCheckBox6.setOnClickListener(this);
		mTableRow2.setOnClickListener(this);
		mTableRow3.setOnClickListener(this);
		mTableRow4.setOnClickListener(this);
		
		
		// 更新 TextView显示
    	mTextView3.setText(String.format("%02d", mHours[0]) + ":" + String.format("%02d", mMinutes[0]));	
    	mTextView4.setText(String.format("%02d", mHours[1]) + ":" + String.format("%02d", mMinutes[1]));	
    	mTextView5.setText(String.format("%02d", mHours[2]) + ":" + String.format("%02d", mMinutes[2]));
    	*/	
		
		// 创建日历实例
        mCalendars = new Calendar[3];
        int len = mCalendars.length;
        for (int i = 0; i < len; ++i)
        {
        	mCalendars[i]=Calendar.getInstance();
        }
        setSwitchRules();
         	
    }

    @Override
	public void onSaveInstanceState(Bundle outState) {
		/* 这里我们在退出应用程序时保存数据 */
		save();
		super.onSaveInstanceState(outState);
	}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			/* 这里我们在退出应用程序时保存数据 */
			save();
			mSQLiteDatabase.close();
			this.finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_auto_switch, menu);
        return true;
    }
    
    @Override
    public void onClick(View v) {  
    	int id = v.getId();
    	int row = id / 3;
    	int colum = id % 3;
    	CheckBox box = null;
    	switch (colum)
    	{
    	case 0: // textview
			Calendar calendar = mCalendars[row];
			int mHour = calendar.get(Calendar.HOUR_OF_DAY);
			int mMinute = calendar.get(Calendar.MINUTE);
			mView = v;
			new TimePickerDialog(AutoSwitchActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view,
								int hourOfDay, int minute) {
					    	int row = mView.getId() / 3;
					    	TextView textView =  (TextView)mView;
							mHours[row] = hourOfDay;
							mMinutes[row] = minute;
							textView.setText(String.format("%02d",
									mHours[row])
									+ ":"
									+ String.format("%02d", mMinutes[row]));
							setSwitchRule(row);
						}
					}, mHour, mMinute, true).show();    		
    		break;
    	case 1: // wifi checkbox
    		box = (CheckBox)v;
    		mWifiStates[row] 	= box.isChecked();
    		break;
    	case 2: // mobile checkbox
    		box = (CheckBox)v;
    		mMobileStates[row] 	= box.isChecked();	
    		break;
    	default:
    		break;
    	}


    	
/*    	
        if (v == mCheckBox1){  
			mWifiStates[0] 	= mCheckBox1.isChecked();
	        setSwitchRule(0);
        }else if (v == mCheckBox2){  
			mMobileStates[0]	= mCheckBox2.isChecked();
	        setSwitchRule(0);
        }else if (v == mCheckBox3){  
			mWifiStates[1] 	= mCheckBox3.isChecked();
	        setSwitchRule(1);
        }else if (v == mCheckBox4){  
			mMobileStates[1]	= mCheckBox4.isChecked();
	        setSwitchRule(1);
        }else if (v == mCheckBox5){  
			mWifiStates[2] 	= mCheckBox5.isChecked();
	        setSwitchRule(2);
        }else if (v == mCheckBox6){  
			mMobileStates[2]	= mCheckBox6.isChecked();
	        setSwitchRule(2);
        }else if (v == mTableRow2){  
			Calendar calendar = mCalendars[0];
			int mHour = calendar.get(Calendar.HOUR_OF_DAY);
			int mMinute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(AutoSwitchActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view,
								int hourOfDay, int minute) {
							mHours[0] = hourOfDay;
							mMinutes[0] = minute;
							mTextView3.setText(String.format("%02d",
									mHours[0])
									+ ":"
									+ String.format("%02d", mMinutes[0]));
							setSwitchRule(0);
						}
					}, mHour, mMinute, true).show();
        }else if (v == mTableRow3){  
			Calendar calendar = mCalendars[1];
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(AutoSwitchActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view,
								int hourOfDay, int minute) {
							mHours[1] = hourOfDay;
							mMinutes[1] = minute;
							mTextView4.setText(String.format("%02d",
									mHours[1])
									+ ":"
									+ String.format("%02d", mMinutes[1]));
							setSwitchRule(1);
						}
					}, hour, minute, true).show();
        }else if (v == mTableRow4){  
			Calendar calendar = mCalendars[2];
			int mHour = calendar.get(Calendar.HOUR_OF_DAY);
			int mMinute = calendar.get(Calendar.MINUTE);
			new TimePickerDialog(AutoSwitchActivity.this,
					new TimePickerDialog.OnTimeSetListener() {
						public void onTimeSet(TimePicker view,
								int hourOfDay, int minute) {
							mHours[2] = hourOfDay;
							mMinutes[2] = minute;
							mTextView5.setText(String.format("%02d",
									mHours[2])
									+ ":"
									+ String.format("%02d", mMinutes[2]));
							setSwitchRule(2);
						}
					}, mHour, mMinute, true).show();
		}
*/
    }  
    
    // 更新切换规则，生成相应的alarm
    private void setSwitchRules() {
        int len = mCalendars.length;
        for (int i = 0; i < len; ++i)
        {
           	setSwitchRule(i);
        }
    }
    
    private void setSwitchRule(int i) {
    	long curTime = System.currentTimeMillis();
        mCalendars[i].setTimeInMillis(curTime);
        //String time = aCalendar[i].get(Calendar.HOUR_OF_DAY) + ":" + aCalendar[i].get(Calendar.MINUTE);
    	//Log.e(TAG, time);
        mCalendars[i].set(Calendar.HOUR_OF_DAY, mHours[i]);
        mCalendars[i].set(Calendar.MINUTE, mMinutes[i]);
        mCalendars[i].set(Calendar.SECOND,0);
        mCalendars[i].set(Calendar.MILLISECOND,0);
        long setTime = mCalendars[i].getTimeInMillis();
        if (setTime < curTime) {
        	setTime += (24*60*60*1000);
        	mCalendars[i].setTimeInMillis(setTime);
        }
        	
        Intent intent = new Intent(AutoSwitchActivity.this, AlarmReceiver.class);
        intent.putExtra("wifi", mWifiStates[i]);
        intent.putExtra("mobile", mMobileStates[i]);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(AutoSwitchActivity.this, i, intent,  PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am;
        /* 获取闹钟管理的实例 */
        am = (AlarmManager)getSystemService(ALARM_SERVICE);
        /* 设置闹钟 周期闹 */
        am.setRepeating(AlarmManager.RTC_WAKEUP, mCalendars[i].getTimeInMillis(), (24*60*60*1000), pendingIntent);     	
    }
    
	// 装载、读取数据   
	void load() {
		if (null == mSQLiteDatabase) {
			DatabaseHelper dbHelper = new DatabaseHelper(this, DATABASE_NAME,
					null, DATABASE_VERSION);
			try {
				mSQLiteDatabase = dbHelper.getWritableDatabase();
			} catch (SQLiteException ex) {
				mSQLiteDatabase = dbHelper.getReadableDatabase();
			}
		}
	}
		
	// 保存数据 
	void save() {
		Log.d(TAG, "Update table item and quit!");
		ContentValues cv = new ContentValues();
		for (int i = 0; i < 3; ++i) {
			cv.put(TABLE_ID, i);
			cv.put(TABLE_HOUR, mHours[i]);
			cv.put(TABLE_MINUTE, mMinutes[i]);
			cv.put(TABLE_WIFI, mWifiStates[i]);
			cv.put(TABLE_MOBILE, mMobileStates[i]);
			cv.put(TABLE_ACTIVE, mActives[i]);
			mSQLiteDatabase.update(TABLE_NAME, cv, TABLE_ID + "=" + i, null);
		}
	}
	
	public class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context, String name, CursorFactory cursorFactory,
				int version) {
			super(context, name, cursorFactory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO 创建数据库后，对数据库的操作
			/* 在数据库mSQLiteDatabase中创建一个表 */
			Log.d(TAG, "Create Database and talbe and item!");
			db.execSQL(CREATE_TABLE);	
			ContentValues cv = new ContentValues();
			for (int i = 0; i < 3; ++i)
			{
				cv.put(TABLE_ID, i);
				cv.put(TABLE_HOUR, mHours[i]);
				cv.put(TABLE_MINUTE, mMinutes[i]);
				cv.put(TABLE_WIFI, mWifiStates[i]);
				cv.put(TABLE_MOBILE, mMobileStates[i]);
				cv.put(TABLE_ACTIVE, mActives[i]);
				/* 插入数据 */
				db.insert(TABLE_NAME, null, cv);				
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO 更改数据库版本的操作
		}

		@Override
		public void onOpen(SQLiteDatabase db) {
			super.onOpen(db);
			// TODO 每次成功打开数据库后首先被执行
			/* 读取表中的数据 */
			Log.d(TAG, "Open Database and read talbe!");
			Cursor cursor = db.rawQuery(
					"SELECT * FROM " + TABLE_NAME, null);
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				int id = cursor.getInt(0);
				mHours[id] = cursor.getInt(1);
				mMinutes[id] = cursor.getInt(2);
				mWifiStates[id] = (cursor.getInt(3) == 0)? false:true;
				mMobileStates[id] = (cursor.getInt(4) == 0)? false:true ;
				mActives[id] = (cursor.getInt(5) == 0)? false:true;
				cursor.moveToNext();
			}
			cursor.close();
		}
		
	}  	

}


