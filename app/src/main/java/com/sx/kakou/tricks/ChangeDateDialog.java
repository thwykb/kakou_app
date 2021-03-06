package com.sx.kakou.tricks;

import java.util.ArrayList;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sx_kakou.R;
import com.sx.kakou.wheel.widget.adapters.AbstractWheelTextAdapter;
import com.sx.kakou.wheel.widget.views.OnWheelChangedListener;
import com.sx.kakou.wheel.widget.views.OnWheelScrollListener;
import com.sx.kakou.wheel.widget.views.WheelView;

import org.w3c.dom.Text;

/**
 * 日期选择对话框
 * 
 * @author ywl
 *
 */
public class ChangeDateDialog extends Dialog implements View.OnClickListener {

	private Context context;
	private String title;
	private WheelView wvYear;
	private WheelView wvMonth;
	private WheelView wvDay;
	private WheelView wvHour;
	private WheelView wvMinute;

	private View vChangeDate;
	private View vChangeDateChild;
    private TextView vChangDateTitle;
	private TextView btnSure;
	private TextView btnCancel;

	private ArrayList<String> arry_years = new ArrayList<String>();
	private ArrayList<String> arry_months = new ArrayList<String>();
	private ArrayList<String> arry_days = new ArrayList<String>();
	private ArrayList<String> arry_Hours = new ArrayList<String>();
	private ArrayList<String> arry_Minutes = new ArrayList<String>();
	private CalendarTextAdapter mYearAdapter;
	private CalendarTextAdapter mMonthAdapter;
	private CalendarTextAdapter mDaydapter;
	private CalendarTextAdapter mHourdapter;
	private CalendarTextAdapter mMinutedapter;

	private int month;
	private int day;

	private int currentYear = getYear();
	private int currentMonth = 1;
	private int currentDay = 1;
	private int currentHour = 0;
	private int currentMinute = 0;

	private int maxTextSize = 24;
	private int minTextSize = 14;

	private boolean issetdata = false;

	private String selectYear;
	private String selectMonth;
	private String selectDay;
	private String selectHour;
	private String selectMinute;


	private OnDateListener onDateListener;

	public ChangeDateDialog(Context context,String title) {
		super(context, R.style.ShareDialog);
		this.context = context;
		this.title = title;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_myinfo_changedate);
		wvYear = (WheelView) findViewById(R.id.wv_date_year);
		wvMonth = (WheelView) findViewById(R.id.wv_date_month);
		wvDay = (WheelView) findViewById(R.id.wv_date_day);
		wvHour = (WheelView) findViewById(R.id.wv_date_hour);
		wvMinute = (WheelView) findViewById(R.id.wv_date_minute);

		vChangeDate = findViewById(R.id.ly_myinfo_changedate);
		vChangeDateChild = findViewById(R.id.ly_myinfo_changedate_child);
        vChangDateTitle =(TextView)findViewById(R.id.tv_share_title);
		btnSure = (TextView) findViewById(R.id.btn_myinfo_sure);
		btnCancel = (TextView) findViewById(R.id.btn_myinfo_cancel);

		vChangeDate.setOnClickListener(this);
		vChangeDateChild.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
        vChangDateTitle.setText(title);

		if (!issetdata) {
			initData();
		}
		initYears();
		mYearAdapter = new CalendarTextAdapter(context, arry_years, setYear(currentYear), maxTextSize, minTextSize);
		wvYear.setVisibleItems(5);
		wvYear.setViewAdapter(mYearAdapter);
		wvYear.setCurrentItem(setYear(currentYear));

		initMonths(month);
		mMonthAdapter = new CalendarTextAdapter(context, arry_months, setMonth(currentMonth), maxTextSize, minTextSize);
		wvMonth.setVisibleItems(5);
		wvMonth.setViewAdapter(mMonthAdapter);
		wvMonth.setCurrentItem(setMonth(currentMonth));

		initDays(day);
		mDaydapter = new CalendarTextAdapter(context, arry_days, currentDay - 1, maxTextSize, minTextSize);
		wvDay.setVisibleItems(5);
		wvDay.setViewAdapter(mDaydapter);
		wvDay.setCurrentItem(currentDay - 1);

        initHours();
        mHourdapter = new CalendarTextAdapter(context, arry_Hours, currentHour, maxTextSize, minTextSize);
        wvHour.setVisibleItems(5);
        wvHour.setViewAdapter(mHourdapter);
        wvHour.setCurrentItem(currentHour);

        initMinutes();
        mMinutedapter = new CalendarTextAdapter(context, arry_Minutes, currentMinute, maxTextSize, minTextSize);
        wvMinute.setVisibleItems(5);
        wvMinute.setViewAdapter(mMinutedapter);
        wvMinute.setCurrentItem(currentMinute);


		wvYear.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                selectYear = currentText;
                setTextviewSize(currentText, mYearAdapter);
                currentYear = Integer.parseInt(currentText);
                setYear(currentYear);
                initMonths(month);
                mMonthAdapter = new CalendarTextAdapter(context, arry_months, 0, maxTextSize, minTextSize);
                wvMonth.setVisibleItems(5);
                wvMonth.setViewAdapter(mMonthAdapter);
                wvMonth.setCurrentItem(0);
            }
        });

		wvYear.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mYearAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mYearAdapter);
            }
        });

		wvMonth.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				selectMonth = currentText;
				setTextviewSize(currentText, mMonthAdapter);
				setMonth(Integer.parseInt(currentText));
				initDays(day);
				mDaydapter = new CalendarTextAdapter(context, arry_days, 0, maxTextSize, minTextSize);
				wvDay.setVisibleItems(5);
				wvDay.setViewAdapter(mDaydapter);
				wvDay.setCurrentItem(0);
			}
		});

		wvMonth.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mMonthAdapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mMonthAdapter);
			}
		});

		wvDay.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
				selectDay = currentText;
			}
		});

		wvDay.addScrollingListener(new OnWheelScrollListener() {

			@Override
			public void onScrollingStarted(WheelView wheel) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrollingFinished(WheelView wheel) {
				// TODO Auto-generated method stub
				String currentText = (String) mDaydapter.getItemText(wheel.getCurrentItem());
				setTextviewSize(currentText, mDaydapter);
			}
		});

        wvHour.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mHourdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mHourdapter);
                selectHour = currentText;
            }
        });

        wvHour.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mHourdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mHourdapter);
            }
        });

        wvMinute.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinutedapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMinutedapter);
                selectMinute = currentText;
            }
        });

        wvMinute.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                // TODO Auto-generated method stub
                String currentText = (String) mMinutedapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, mMinutedapter);
            }
        });




	}

	public void initYears() {
		for (int i = getYear(); i > 1950; i--) {
			arry_years.add(i + "");
		}
	}

	public void initMonths(int months) {
		arry_months.clear();
		for (int i = 1; i <= months; i++) {
			if (i<10){
				arry_months.add("0"+i + "");
			}else {
				arry_months.add(i + "");
			}

		}
	}

	public void initDays(int days) {
		arry_days.clear();
		for (int i = 1; i <= days; i++) {
            if (i<10){
                arry_days.add("0"+i + "");
            }else {
                arry_days.add(i + "");
            }

		}
	}

	public void initHours() {
		arry_Hours.clear();
		for (int i = 0; i <= 23; i++) {
            if (i<10){
                arry_Hours.add("0"+i + "");
            }else{
                arry_Hours.add(i + "");
            }
		}
	}

	public void initMinutes() {
		arry_Minutes.clear();
		for (int i = 0; i <= 59; i++) {
            if (i<10){
                arry_Minutes.add("0"+i + "");
            }else {
                arry_Minutes.add(i + "");
            }
		}
	}

	private class CalendarTextAdapter extends AbstractWheelTextAdapter {
		ArrayList<String> list;

		protected CalendarTextAdapter(Context context, ArrayList<String> list, int currentItem, int maxsize, int minsize) {
			super(context, R.layout.item_date_year, NO_RESOURCE, currentItem, maxsize, minsize);
			this.list = list;
			setItemTextResource(R.id.tempValue);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);
			return view;
		}

		@Override
		public int getItemsCount() {
			return list.size();
		}

		@Override
		protected CharSequence getItemText(int index) {
			return list.get(index) + "";
		}
	}

	public void setDateListener(OnDateListener onDateListener) {
		this.onDateListener = onDateListener;
	}

	@Override
	public void onClick(View v) {

		if (v == btnSure) {
			if (onDateListener != null) {
				onDateListener.onClick(selectYear, selectMonth, selectDay,selectHour,selectMinute);
			}
		} else if (v == btnSure) {

		} else if (v == vChangeDateChild) {
			return;
		} else {
			dismiss();
		}
		dismiss();

	}

	public interface OnDateListener {
		public void onClick(String year, String month, String day,String hour,String minute);
	}

	/**
	 * 设置字体大小
	 * 
	 * @param curriteItemText
	 * @param adapter
	 */
	public void setTextviewSize(String curriteItemText, CalendarTextAdapter adapter) {
		ArrayList<View> arrayList = adapter.getTestViews();
		int size = arrayList.size();
		String currentText;
		for (int i = 0; i < size; i++) {
			TextView textvew = (TextView) arrayList.get(i);
			currentText = textvew.getText().toString();
			if (curriteItemText.equals(currentText)) {
				textvew.setTextSize(maxTextSize);
			} else {
				textvew.setTextSize(minTextSize);
			}
		}
	}

	public int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

	public int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DATE);
	}

	public int getHour() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.HOUR_OF_DAY);
	}

	public int getMinute() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MINUTE);
	}

	public void initData() {
		setDate(getYear(), getMonth(), getDay(),getHour(),getMinute());
		this.currentDay = 1;
		this.currentMonth = 1;
	}

	/**
	 * 设置年月日
	 * 
	 * @param year
	 * @param month
	 * @param day
	 */
	public void setDate(int year, int month, int day,int hour,int minute) {
		selectYear = year + "";
		selectMonth = month + "";
		selectDay = day + "";
		selectHour = hour+"";
		selectMinute = minute+"";
		issetdata = true;
		this.currentYear = year;
		this.currentMonth = month;
		this.currentDay = day;
		this.currentHour = hour;
		this.currentMinute = minute;
		if (year == getYear()) {
			this.month = getMonth();
		} else {
			this.month = 12;
		}
		calDays(year, month);
	}

	/**
	 * 设置年份
	 * 
	 * @param year
	 */
	public int setYear(int year) {
		int yearIndex = 0;
		if (year != getYear()) {
			this.month = 12;
		} else {
			this.month = getMonth();
		}
		for (int i = getYear(); i > 1950; i--) {
			if (i == year) {
				return yearIndex;
			}
			yearIndex++;
		}
		return yearIndex;
	}

	/**
	 * 设置月份
	 *
	 * @param month
	 * @return
	 */
	public int setMonth(int month) {
		int monthIndex = 0;
		calDays(currentYear, month);
		for (int i = 1; i < this.month; i++) {
			if (month == i) {
				return monthIndex;
			} else {
				monthIndex++;
			}
		}
		return monthIndex;
	}

	/**
	 * 计算每月多少天
	 * 
	 * @param month
	 */
	public void calDays(int year, int month) {
		boolean leayyear = false;
		if (year % 4 == 0 && year % 100 != 0) {
			leayyear = true;
		} else {
			leayyear = false;
		}
		for (int i = 1; i <= 12; i++) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				this.day = 31;
				break;
			case 2:
				if (leayyear) {
					this.day = 29;
				} else {
					this.day = 28;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				this.day = 30;
				break;
			}
		}
		if (year == getYear() && month == getMonth()) {
			this.day = getDay();
		}
	}
}