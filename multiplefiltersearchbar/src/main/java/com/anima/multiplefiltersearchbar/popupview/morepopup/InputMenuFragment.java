package com.anima.multiplefiltersearchbar.popupview.morepopup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;


/**
 * Created by Admin on 2015/8/15.
 */
public class InputMenuFragment extends Fragment implements MenuFragmentAction {

    private final static String TAG = "Chris";
    private View rootView;
    private EditText editText;
    private MenuItem menuItem;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView");
        if(rootView == null) {
            Log.i(TAG, "create rootView");
            rootView =  inflater.inflate(R.layout.fragment_input, container, false);
            editText = rootView.findViewById(R.id.inputFragment_et);
            editText.setSaveEnabled(false);
            Bundle bundle = getArguments();
            menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");
        }

        if(editText != null && menuItem != null) {
            editText.setText(menuItem.getText());
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(editText != null && menuItem != null) {
            editText.setText(menuItem.getText());
        }
    }

    @Override
    public void saveValue() {
        if(isSelected()) {
            menuItem.resetValueListByString(editText.getText().toString());
        }
    }

    @Override
    public void clearValue() {
        menuItem.clearValueList();
        clearSelected();
    }

    @Override
    public boolean isSelected() {
        if(editText == null) {
            return false;
        }
        return !TextUtils.isEmpty(editText.getText().toString());
    }

    @Override
    public void clearSelected() {
        if(!menuItem.isSelected()) {
            editText.setText("");
        }
    }
}
