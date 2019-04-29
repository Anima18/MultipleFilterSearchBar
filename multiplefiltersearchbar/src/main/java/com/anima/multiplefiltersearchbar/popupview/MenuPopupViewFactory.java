package com.anima.multiplefiltersearchbar.popupview;

import android.content.Context;
import android.view.View;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.popupview.dateintervalpopup.DateIntervalPopupView;
import com.anima.multiplefiltersearchbar.popupview.datepopup.DatePickerPopupView;
import com.anima.multiplefiltersearchbar.popupview.inputpopup.InputPopupView;
import com.anima.multiplefiltersearchbar.popupview.morepopup.MorePopupView;
import com.anima.multiplefiltersearchbar.popupview.singlelistpopup.StaticSingleListPopupView;
import com.anima.multiplefiltersearchbar.popupview.sectionlistpopup.StaticSectionListPopupView;
import com.anima.multiplefiltersearchbar.popupview.treepopup.StaticTreePopupView;
import com.lxj.xpopup.XPopup;

/**
 * Created by jianjianhong on 19-4-2
 */
public class MenuPopupViewFactory {
    public static MenuPopupView create(Context context, View anchorView, MenuItem menuItem) {
        switch (menuItem.getItemType()) {
            case INPUT:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new InputPopupView(context, menuItem, (MenuItemAction)anchorView));
            case SINGLE_CHOICE:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new StaticSingleListPopupView(context, menuItem, (MenuItemAction)anchorView));
            case DATE:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new DatePickerPopupView(context, menuItem, (MenuItemAction)anchorView));
            case SECTION_CHOICE:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new StaticSectionListPopupView(context, menuItem, (MenuItemAction)anchorView));
            case TREE:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new StaticTreePopupView(context, menuItem, (MenuItemAction)anchorView));
            case DATE_INTERVAL:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new DateIntervalPopupView(context, menuItem, (MenuItemAction)anchorView));
            case MORE:
                return (MenuPopupView) new XPopup.Builder(context)
                        .atView(anchorView)
                        .asCustom(new MorePopupView(context, menuItem, (MenuItemAction)anchorView));
        }
        return null;
    }
}
