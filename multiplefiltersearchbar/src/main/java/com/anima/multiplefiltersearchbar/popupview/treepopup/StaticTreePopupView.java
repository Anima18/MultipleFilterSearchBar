package com.anima.multiplefiltersearchbar.popupview.treepopup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;

/**
 * Created by jianjianhong on 19-4-8
 */
public class StaticTreePopupView extends TreePopupView {
    private MenuItem menuItem;

    public StaticTreePopupView(@NonNull Context context) {
        super(context);
    }

    public StaticTreePopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.menuItem = menuItem;
    }

    @Override
    protected void setData(OnTreeDataChangeListener listener) {
        if(menuItem.getTreeDataListener() != null){
            menuItem.getTreeDataListener().requestData(listener);
        }
    }
}
