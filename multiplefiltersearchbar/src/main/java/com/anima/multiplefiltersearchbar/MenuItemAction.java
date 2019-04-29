package com.anima.multiplefiltersearchbar;

/**
 * Created by jianjianhong on 19-4-3
 */
public interface MenuItemAction {

    void unSelectMenuItem(MenuItem menuItem);
    void selectMenuItem(MenuItem menuItem);
    int getColorTint();
    void onSearch();
}
