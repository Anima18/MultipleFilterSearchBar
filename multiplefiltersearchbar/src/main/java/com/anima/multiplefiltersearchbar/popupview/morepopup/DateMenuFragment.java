package com.anima.multiplefiltersearchbar.popupview.morepopup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;

import java.util.Calendar;


/**
 * Created by Admin on 2015/8/15.
 */
public class DateMenuFragment extends Fragment implements MenuFragmentAction {

    private View rootView;
    private DatePicker datePicker;
    private MenuItem menuItem;

    private String seletedValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_date, container, false);
            datePicker = rootView.findViewById(R.id.dateFragment_date);

            Bundle bundle = getArguments();
            menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");

            int year, month, dayOfMonth;
            if(menuItem.isSelected()) {
                String[] strArray = menuItem.getText().split("-");
                year = Integer.parseInt(strArray[0]);
                month = Integer.parseInt(strArray[1]) -1;
                dayOfMonth = Integer.parseInt(strArray[2]);
                seletedValue =  String.format("%d-%d-%d", year, month+1, dayOfMonth);
            }else  {
                Calendar calendar = Calendar.getInstance();
                //年
                year = calendar.get(Calendar.YEAR);
                //月
                month = calendar.get(Calendar.MONTH);
                //日
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }

            datePicker.init(year, month, dayOfMonth, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
                    seletedValue = String.format("%d-%d-%d", i, i1+1, i2);
                }
            });
        }

        return rootView;
    }

    @Override
    public void saveValue() {
        menuItem.resetValueListByString(seletedValue);
    }

    @Override
    public void clearValue() {
        menuItem.clearValueList();
        clearSelected();
    }

    @Override
    public boolean isSelected() {
        return !TextUtils.isEmpty(seletedValue);
    }

    @Override
    public void clearSelected() {
        if(!menuItem.isSelected()) {
            seletedValue = "";
        }
    }
}
