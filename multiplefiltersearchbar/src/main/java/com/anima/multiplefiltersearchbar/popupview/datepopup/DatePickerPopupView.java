package com.anima.multiplefiltersearchbar.popupview.datepopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;

import java.util.Calendar;

/**
 * Created by jianjianhong on 19-4-3
 */
public class DatePickerPopupView extends MenuPopupView {

    private MenuItem menuItem;
    private MenuItemAction action;
    private DatePicker datePicker;

    public DatePickerPopupView(@NonNull Context context) {
        super(context);
    }

    public DatePickerPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);

        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_date;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onDismiss");
        datePicker = findViewById(R.id.datePopup_date);
        Button resetButton = findViewById(R.id.datePopupView_reset);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue("");
                setText("");
                dismiss();
                action.onSearch();
            }
        });
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onDismiss");
        action.selectMenuItem(menuItem);

        int year, month, dayOfMonth;
        if(menuItem.isSelected()) {
            String[] strArray = menuItem.getText().split("-");
            year = Integer.parseInt(strArray[0]);
            month = Integer.parseInt(strArray[1]) -1;
            dayOfMonth = Integer.parseInt(strArray[2]);

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
                String value = String.format("%d-%d-%d", i, i1+1, i2);
                setValue(value);
                setText(value);
                dismiss();
                action.onSearch();
            }
        });
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
        menuItem.resetValueListByString(value);
    }
}
