package com.anima.multiplefiltersearchbar.popupview.singlelistpopup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;

/**
 * Created by jianjianhong on 19-4-4
 */
public class StaticSingleListPopupView extends SingleListPopupView {
    private MenuItem menuItem;
    public StaticSingleListPopupView(@NonNull Context context) {
        super(context);
    }

    public StaticSingleListPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.menuItem = menuItem;
    }

    @Override
    protected void setData(OnDataChangeListener listener) {
        if(menuItem.getDataList() != null) {
            listener.dataCompletion(menuItem.getDataList());
        }else if(menuItem.getMapListDataListener() != null){
            menuItem.getMapListDataListener().requestData(listener);
        }
    }
}
