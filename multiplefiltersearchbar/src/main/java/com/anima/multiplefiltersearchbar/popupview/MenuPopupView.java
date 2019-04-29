package com.anima.multiplefiltersearchbar.popupview;

import android.content.Context;
import android.support.annotation.NonNull;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.lxj.xpopup.impl.PartShadowPopupView;

/**
 * Created by jianjianhong on 19-4-2
 */
public abstract class MenuPopupView extends PartShadowPopupView {
    public MenuPopupView(@NonNull Context context) {
        super(context);
    }

    public MenuPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context);
    }

    protected abstract void setText(String text);

    protected abstract void setValue(String value);
}
