package com.example.saveapp.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.saveapp.R;

import java.util.Calendar;
import java.util.Date;

public class BirthDayPicker {
    private static final String TAG = "BirthDayPicker";
    //回调接口
    private OnSelectListener listener;
    private TimePickerView mTimePickerView;
    private Context mContext;
    private boolean clickCancel = false;
    private String callbackDate;

    /**
     * 普通条件选择项方法
     */
    public BirthDayPicker(Context context, OnSelectListener listener) {
        this.listener = listener;
        mContext = context;
        init();
    }

    private void init() {
        //宝宝原来的生日，未填写可能为null
        Calendar selectCalendar = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();
        //月要设置成日期时间-1，年，日不需要
        startDate.set(2019, 0, 1);//日期下限
        endDate.set(2022, 11, 31);//日期上限
        mTimePickerView = new TimePickerBuilder(mContext, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {

            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
            @Override
            public void onTimeSelectChanged(Date date) {
                Calendar result = Calendar.getInstance();
                if (date != null) {
                    result.setTime(date);
                }
                int year = result.get(Calendar.YEAR);
                int month = result.get(Calendar.MONTH) + 1;
                String monthString = String.valueOf(month);
                if (month < 10) {
                    monthString = "0" + month;
                }
                int date1 = result.get(Calendar.DATE);
                String dateString = String.valueOf(date1);
                if (date1 < 10) {
                    dateString = "0" + date1;
                }
                callbackDate = year + "-" + monthString + "-" + dateString;
            }
        }).setLayoutRes(R.layout.view_day_picker, new CustomListener() {
            @Override
            public void customLayout(View v) {
                TextView cancel = v.findViewById(R.id.tv_cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCancel = true;
                        dismiss();
                    }
                });
                TextView confirm = v.findViewById(R.id.tv_confirm);
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickCancel = false;
                        dismiss();
                    }
                });

            }
        }).setDate(selectCalendar).setRangDate(startDate, endDate).build();
        mTimePickerView.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(Object o) {
                if (!clickCancel) {
                    listener.onDateSelect(callbackDate);
                }
                clickCancel = false;
            }
        });
    }

    /**
     * 显示选择器
     */
    public void show() {
        if (mTimePickerView != null && !mTimePickerView.isShowing()) {
            mTimePickerView.show();
        }
    }

    /**
     * 关闭选择器
     */
    public void dismiss() {
        if (mTimePickerView != null && mTimePickerView.isShowing()) {
            mTimePickerView.dismiss();
        }
    }

    /**
     * 选择项回调
     */
    public interface OnSelectListener {
        void onDateSelect(String date);
    }
}
