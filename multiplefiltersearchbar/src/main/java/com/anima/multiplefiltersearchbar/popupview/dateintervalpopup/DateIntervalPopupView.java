package com.anima.multiplefiltersearchbar.popupview.dateintervalpopup;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;
import com.anima.multiplefiltersearchbar.popupview.singlelistpopup.SingleListAdapter;
import com.anima.multiplefiltersearchbar.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anima.multiplefiltersearchbar.util.DateUtil.formatDate;
import static com.anima.multiplefiltersearchbar.util.DateUtil.getTodayString;

/**
 * Created by jianjianhong on 19-4-19
 */
public class DateIntervalPopupView extends MenuPopupView implements View.OnClickListener {
    private Context context;
    private MenuItem menuItem;
    private MenuItemAction action;
    private SingleListAdapter adapter;
    private EditText beginDateET;
    private EditText endDateET;
    private Button resetButton;
    private Button okButton;

    public DateIntervalPopupView(@NonNull Context context) {
        super(context);
    }

    public DateIntervalPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.context = context;
        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_date_interval;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onDismiss");

        beginDateET = findViewById(R.id.dateIntervalPopup_custom_date_begin);
        endDateET = findViewById(R.id.dateIntervalPopup_custom_date_end);
        okButton = findViewById(R.id.popupView_ok);
        resetButton = findViewById(R.id.popupView_reset);

        initCustomDate();
        initListData();
        initListView();
        okButton.setOnClickListener(this);
        resetButton.setOnClickListener(this);
    }

    private void initCustomDate() {
        String todayString = getTodayString();
        beginDateET.setHint(todayString);
        endDateET.setHint(todayString);
        beginDateET.setKeyListener(null);
        endDateET.setKeyListener(null);

        beginDateET.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(beginDateET, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String beginDate = formatDate(year, month, dayOfMonth);
                            String endDate = endDateET.getText().toString();
                            if(!TextUtils.isEmpty(endDate) && DateUtil.compareDate(beginDate, endDate) == 1) {
                                beginDateET.setError("开始时间不能大于结束时间");
                            }else {
                                beginDateET.setText(beginDate);
                                beginDateET.setError(null);
                            }
                        }
                    });
                }
                return false;
            }
        });

        endDateET.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(endDateET, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String endDate = formatDate(year, month, dayOfMonth);
                            String beginDate = beginDateET.getText().toString();
                            if(!TextUtils.isEmpty(beginDate) && DateUtil.compareDate(beginDate, endDate) == 1) {
                                endDateET.setError("结束时间不能小于开始时间");
                            }else {
                                endDateET.setText(endDate);
                                endDateET.setError(null);
                            }

                        }
                    });
                }
                return false;
            }
        });

    }

    private void showDatePickerDialog(EditText editText, DatePickerDialog.OnDateSetListener dateSetListener) {
        String value = editText.getText().toString();
        if(TextUtils.isEmpty(value)) {
            value = getTodayString();
        }
        String[] strArray = value.split("-");
        int year = Integer.parseInt(strArray[0]);
        int month = Integer.parseInt(strArray[1]) -1;
        int dayOfMonth = Integer.parseInt(strArray[2]);
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, dateSetListener, year, month, dayOfMonth);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void initListData() {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> oneDayMap = new HashMap<>();
        oneDayMap.put("ONE_DAY", "一天内");
        dataList.add(oneDayMap);

        Map<String, String> oneWeekMap = new HashMap<>();
        oneWeekMap.put("ONE_WEEK", "一周内");
        dataList.add(oneWeekMap);

        Map<String, String> oneMonthMap = new HashMap<>();
        oneMonthMap.put("ONE_MONTH", "一月内");
        dataList.add(oneMonthMap);

        Map<String, String> threeMonthMap = new HashMap<>();
        threeMonthMap.put("THREE_MONTH", "三月内");
        dataList.add(threeMonthMap);

        /*Map<String, String> sixMonthMap = new HashMap<>();
        sixMonthMap.put("SIX_MONTH", "半年内");
        dataList.add(sixMonthMap);*/

        Map<String, String> oneYearMap = new HashMap<>();
        oneYearMap.put("ONE_YEAR", "一年内");
        dataList.add(oneYearMap);

        menuItem.setDataList(dataList);
    }

    private void initListView() {
        RecyclerView recyclerView = findViewById(R.id.dateIntervalPopup_recyclerview);
        adapter = new SingleListAdapter(context, menuItem);
        adapter.setOnItemClickListener(new SingleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Map<String, String> data = menuItem.getDataList().get(position);
                String value = data.keySet().iterator().next();
                String text = data.get(value);
                setValue(value);
                setText(text);
                beginDateET.setText("");
                endDateET.setText("");
                adapter.setSelectedIndex(position);
                dismiss();
                action.onSearch();
            }
        });
        adapter.setSelectedListener(new SingleListAdapter.IsItemSelectedListener() {
            @Override
            public void selected(TextView itemView) {
                itemView.setTextColor(action.getColorTint());
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.popupView_ok) {
            String beginDate = beginDateET.getText().toString();
            String endDate = endDateET.getText().toString();
            if(!TextUtils.isEmpty(beginDate) && !TextUtils.isEmpty(endDate)) {
                String date = beginDate+"至"+endDate;
                setText(date);
                menuItem.clearValueList();
                menuItem.addValue(beginDate);
                menuItem.addValue(endDate);
                adapter.setSelectedIndex(-1);
            }else if(!TextUtils.isEmpty(beginDate) && TextUtils.isEmpty(endDate)) {
                String date = beginDate+"至今";
                setText(date);
                menuItem.clearValueList();
                menuItem.addValue(beginDate);
                menuItem.addValue(getTodayString());
                adapter.setSelectedIndex(-1);
            }else if(TextUtils.isEmpty(beginDate) && !TextUtils.isEmpty(endDate)) {
                String date = endDate+"之前";
                setText(date);
                menuItem.clearValueList();
                menuItem.addValue("1970-01-01");
                menuItem.addValue(endDate);
                adapter.setSelectedIndex(-1);
            }

        }else if(id == R.id.popupView_reset) {
            adapter.setSelectedIndex(-1);
            menuItem.clearValueList();
            setText("");
            beginDateET.setText("");
            endDateET.setText("");
        }
        dismiss();
        action.onSearch();
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onShow");
        action.selectMenuItem(menuItem);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.i(this.getClass().getSimpleName(),"onDismiss");
        action.unSelectMenuItem(menuItem);
    }

    @Override
    protected void setText(String text) {
        menuItem.setText(text);
    }

    @Override
    protected void setValue(String value) {
        menuItem.clearValueList();
        String beginDate = getTodayString();
        String endDate = "";
        Calendar cal = Calendar.getInstance();
        if(value.equals("ONE_DAY")) {
            cal.add(Calendar.DATE, -1);
        }else if(value.equals("ONE_WEEK")) {
            cal.add(Calendar.DATE, -7);
        }else if(value.equals("ONE_MONTH")) {
            cal.add(Calendar.MONTH, -1);
        }else if(value.equals("THREE_MONTH")) {
            cal.add(Calendar.MONTH, -3);
        }else if(value.equals("ONE_YEAR")) {
            cal.add(Calendar.YEAR, -1);
        }
        endDate = formatDate(cal);
        menuItem.addValue(endDate);
        menuItem.addValue(beginDate);
    }

}
