package com.anima.multiplefiltersearchbar.popupview.sectionlistpopup;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;

/**
 * Created by jianjianhong on 19-4-8
 */
public class StaticSectionListPopupView extends SectionListPopupView {
    private MenuItem menuItem;
    public StaticSectionListPopupView(@NonNull Context context) {
        super(context);
    }

    public StaticSectionListPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
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
