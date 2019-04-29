package com.anima.multiplefiltersearchbar.popupview.morepopup;

import android.support.v4.app.Fragment;

import com.anima.multiplefiltersearchbar.MenuItem;

/**
 * Created by jianjianhong on 19-4-12
 */
public class MenuFragmentFactory {

    public static Fragment create(MenuItem menuItem) {
        switch (menuItem.getItemType()) {
            case INPUT:
                return new InputMenuFragment();
            case SINGLE_CHOICE:
                return new SingleListMenuFragment();
            case DATE:
                return new DateMenuFragment();
            case SECTION_CHOICE:
                return new SectionListMenuFragment();
            case TREE:
                return new TreeMenuFragment();
            case DATE_INTERVAL:
                return new DateIntervalFragment();
        }
        return null;
    }
}
