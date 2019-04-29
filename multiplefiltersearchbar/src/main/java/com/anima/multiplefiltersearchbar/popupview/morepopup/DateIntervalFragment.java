package com.anima.multiplefiltersearchbar.popupview.morepopup;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.singlelistpopup.SingleListAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anima.multiplefiltersearchbar.util.DateUtil.compareDate;
import static com.anima.multiplefiltersearchbar.util.DateUtil.formatDate;
import static com.anima.multiplefiltersearchbar.util.DateUtil.getTodayString;

/**
 * Created by jianjianhong on 19-4-19
 */
public class DateIntervalFragment extends Fragment implements MenuFragmentAction {

    private View rootView;
    private MenuItem menuItem;
    private SingleListAdapter adapter;
    private EditText beginDateET;
    private EditText endDateET;

    private String selectedValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView =  inflater.inflate(R.layout.popupview_date_interval, container, false);

            beginDateET = rootView.findViewById(R.id.dateIntervalPopup_custom_date_begin);
            endDateET = rootView.findViewById(R.id.dateIntervalPopup_custom_date_end);

            Bundle bundle = getArguments();
            menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");

            initCustomDate();
            initListData();
            initListView();
        }

        return rootView;
    }

    private void initCustomDate() {
        String todayString = getTodayString();
        beginDateET.setHint(todayString);
        endDateET.setHint(todayString);
        beginDateET.setKeyListener(null);
        endDateET.setKeyListener(null);

        beginDateET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(beginDateET, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String beginDate = formatDate( year, month, dayOfMonth);
                            String endDate = endDateET.getText().toString();
                            if(!TextUtils.isEmpty(endDate) && compareDate(beginDate, endDate) == 1) {
                                beginDateET.setError("开始时间不能大于结束时间");
                            }else {
                                beginDateET.setText(beginDate);
                                beginDateET.setError(null);
                            }
                            adapter.setSelectedIndex(-1);
                            selectedValue = "";
                        }
                    });
                }
                return false;
            }
        });

        endDateET.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickerDialog(endDateET, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String endDate = formatDate( year, month, dayOfMonth);
                            String beginDate = beginDateET.getText().toString();
                            if(!TextUtils.isEmpty(beginDate) && compareDate(beginDate, endDate) == 1) {
                                endDateET.setError("结束时间不能小于开始时间");
                            }else {
                                endDateET.setText(endDate);
                                endDateET.setError(null);
                            }
                            adapter.setSelectedIndex(-1);
                            selectedValue = "";
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), dateSetListener, year, month, dayOfMonth);
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
        RecyclerView recyclerView = rootView.findViewById(R.id.dateIntervalPopup_recyclerview);
        adapter = new SingleListAdapter(getContext(), menuItem);
        adapter.setOnItemClickListener(new SingleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Map<String, String> data = menuItem.getDataList().get(position);
                selectedValue = data.keySet().iterator().next();
                beginDateET.setText("");
                endDateET.setText("");
                adapter.setSelectedIndex(position);

            }
        });
        adapter.setSelectedListener(new SingleListAdapter.IsItemSelectedListener() {
            @Override
            public void selected(TextView itemView) {
                itemView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public void saveValue() {
        String beginDate = beginDateET.getText().toString();
        String endDate = endDateET.getText().toString();
        if(!TextUtils.isEmpty(beginDate) && !TextUtils.isEmpty(endDate)) {
            String date = beginDate+"至"+endDate;
            menuItem.clearValueList();
            menuItem.addValue(beginDate);
            menuItem.addValue(endDate);
            adapter.setSelectedIndex(-1);
        }else if(!TextUtils.isEmpty(beginDate) && TextUtils.isEmpty(endDate)) {
            String date = beginDate+"至今";
            menuItem.clearValueList();
            menuItem.addValue(beginDate);
            menuItem.addValue(getTodayString());
            adapter.setSelectedIndex(-1);
        }else if(TextUtils.isEmpty(beginDate) && !TextUtils.isEmpty(endDate)) {
            String date = endDate+"之前";
            menuItem.clearValueList();
            menuItem.addValue("1970-01-01");
            menuItem.addValue(endDate);
            adapter.setSelectedIndex(-1);
        }else if(!TextUtils.isEmpty(selectedValue)) {
            menuItem.clearValueList();
            String todayString = getTodayString();
            String endDateString = "";
            Calendar cal = Calendar.getInstance();
            if(selectedValue.equals("ONE_DAY")) {
                cal.add(Calendar.DATE, -1);
            }else if(selectedValue.equals("ONE_WEEK")) {
                cal.add(Calendar.DATE, -7);
            }else if(selectedValue.equals("ONE_MONTH")) {
                cal.add(Calendar.MONTH, -1);
            }else if(selectedValue.equals("THREE_MONTH")) {
                cal.add(Calendar.MONTH, -3);
            }else if(selectedValue.equals("ONE_YEAR")) {
                cal.add(Calendar.YEAR, -1);
            }
            endDateString = formatDate(cal);
            menuItem.addValue(endDateString);
            menuItem.addValue(todayString);
        }
    }

    @Override
    public void clearValue() {
        menuItem.clearValueList();
        clearSelected();
    }

    @Override
    public boolean isSelected() {
        if(beginDateET == null) {
            return false;
        }
        String beginDate = beginDateET.getText().toString();
        String endDate = endDateET.getText().toString();
        if(!TextUtils.isEmpty(selectedValue) || !TextUtils.isEmpty(beginDate) || !TextUtils.isEmpty(endDate)) {
            return true;
        }else {
            return false;
        }
    }

    @Override
    public void clearSelected() {
        if(!menuItem.isSelected()) {
            selectedValue = "";
            if(adapter != null) {
                adapter.setSelectedIndex(-1);
            }
            beginDateET.setText("");
            endDateET.setText("");
        }
    }

}
