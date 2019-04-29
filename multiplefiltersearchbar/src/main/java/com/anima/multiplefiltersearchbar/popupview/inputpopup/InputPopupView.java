package com.anima.multiplefiltersearchbar.popupview.inputpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;

/**
 * Created by jianjianhong on 19-4-2
 */
public class InputPopupView extends MenuPopupView implements View.OnClickListener {

    private MenuItem menuItem;
    private EditText editText;
    private Button resetButton;
    private Button okButton;
    private MenuItemAction action;

    public InputPopupView(@NonNull Context context) {
        super(context);
    }

    public InputPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context);
        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_input;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onCreate");
        editText = findViewById(R.id.inputPopup_et);
        resetButton = findViewById(R.id.popupView_reset);
        okButton = findViewById(R.id.popupView_ok);

        editText.setHint(menuItem.getName());
        resetButton.setOnClickListener(this);
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.popupView_ok) {
            String text = editText.getText().toString();
            setValue(text);
            setValue(text);
        } else if (i == R.id.popupView_reset) {
            editText.setText("");
            setText("");
            setValue("");
        }
        dismiss();
        action.onSearch();
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onShow");
        action.selectMenuItem(menuItem);
        editText.setText(menuItem.getText());
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
