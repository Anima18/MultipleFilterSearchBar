package com.anima.multiplefiltersearchbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupViewFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianjianhong on 19-4-2
 */
public class MultipleFilterSearchBar extends LinearLayout implements MenuItemAction, View.OnClickListener {

    private Context context;
    private List<MenuItem> menuList;
    private List<View> menuItemViewList;
    private List<MenuPopupView> menuPopupViewList;
    private int textColorTint;
    private int textColor;
    private OnSearchListener listener;


    public MultipleFilterSearchBar(Context context) {
        this(context, null);
    }

    public MultipleFilterSearchBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleFilterSearchBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.MultipleFilterSearchBar);
        String menuStr = array.getString(R.styleable.MultipleFilterSearchBar_menus);
        textColor = array.getColor(R.styleable.MultipleFilterSearchBar_textColor, context.getResources().getColor(android.R.color.black));
        textColorTint = array.getColor(R.styleable.MultipleFilterSearchBar_textColorTint, context.getResources().getColor(R.color.design_default_color_primary));
        array.recycle();

        setBackground(context.getResources().getDrawable(R.drawable.menu_bar_background));
        setMenuList(menuStr);
    }

    private void initView() {

        if(menuList != null && menuList.size() > 0) {
            menuItemViewList = new ArrayList<>();
            menuPopupViewList = new ArrayList<>();

            List<MenuItem> filterList = null;
            if(menuList.size() > 4) {
                filterList = new ArrayList<>();
                filterList.add(menuList.get(0));
                filterList.add(menuList.get(1));
                filterList.add(menuList.get(2));

                List<MenuItem> moreList = new ArrayList<>();
                for(int i = 3; i < menuList.size(); i++) {
                    moreList.add(menuList.get(i));
                }
                filterList.add(MenuItem.getMoreMenuItem("more", "更多筛选", moreList));
            }else {
                filterList = menuList;
            }
            for (int i = 0; i < filterList.size(); i++) {
                MenuItem menuItem = filterList.get(i);


                View menuItemView = LayoutInflater.from(context).inflate(R.layout.view_menu_item, this, false);
                TextView menuTitleView = menuItemView.findViewById(R.id.menuItem_title);
                menuTitleView.setText(menuItem.getName());
                menuTitleView.setTextColor(textColor);
                ImageView indicator = menuItemView.findViewById(R.id.menuItem_indicator);
                indicator.setImageDrawable(getTintDrawable(indicator.getDrawable(), textColor));


                addView(menuItemView);
                menuItemViewList.add(menuItemView);
                menuPopupViewList.add(MenuPopupViewFactory.create(getContext(), this, menuItem));

                menuItemView.setTag(i);
                menuItemView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View view) {
        int tag = (int)view.getTag();
        MenuPopupView menuPopupView = menuPopupViewList.get(tag);
        if(menuPopupView != null) {
            menuPopupView.toggle();
        }

    }

    private View getSelectedMenuItemView(MenuItem menuItem) {
        View menuItemView = null;
        if(menuItem.getItemType() != MenuItemType.MORE) {
            int index = menuList.indexOf(menuItem);
            menuItemView = menuItemViewList.get(index);
        }else {
            menuItemView = menuItemViewList.get(menuItemViewList.size() -1);
        }
        return menuItemView;
    }

    @Override
    public void unSelectMenuItem(MenuItem menuItem) {

        View menuItemView = getSelectedMenuItemView(menuItem);
        TextView menuTitleView = menuItemView.findViewById(R.id.menuItem_title);
        ImageView indicator = menuItemView.findViewById(R.id.menuItem_indicator);

        if(menuItem.isSelected()) {
            menuTitleView.setTextColor(textColorTint);
            menuTitleView.setText(menuItem.getText());
        }else {
            menuTitleView.setTextColor(textColor);
            menuTitleView.setText(menuItem.getName());
        }

        indicator.animate().rotation(0);
        indicator.setImageDrawable(getTintDrawable(indicator.getDrawable(), textColor));
    }

    @Override
    public void selectMenuItem(MenuItem menuItem) {
        View menuItemView = getSelectedMenuItemView(menuItem);
        TextView menuTitleView = menuItemView.findViewById(R.id.menuItem_title);
        ImageView indicator = menuItemView.findViewById(R.id.menuItem_indicator);

        menuTitleView.setTextColor(textColorTint);
        indicator.animate().rotation(180);
        indicator.setImageDrawable(getTintDrawable(indicator.getDrawable(), textColorTint));
    }

    @Override
    public void onSearch() {
        if(listener == null) {
            Toast.makeText(context, "你没有设置查询事件!", Toast.LENGTH_SHORT).show();
            return;
        }

        listener.onSearch(getData());
    }

    public List<MenuItem> getData() {
        List<MenuItem> valueList = new ArrayList<>();
        for(MenuItem menuItem : menuList) {
            if(menuItem.isSelected()) {
                MenuItem value = new MenuItem();
                value.setCode(menuItem.getCode());
                value.setItemType(menuItem.getItemType());
                value.setValueList(menuItem.getValueList());
                valueList.add(value);
            }
        }
        return valueList;
    }

    @Override
    public int getColorTint() {
        return textColorTint;
    }

    private Drawable getTintDrawable(Drawable drawable, int accentColor) {
        if(drawable == null) {
            return null;
        }
        Drawable tintIcon = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(tintIcon, ColorStateList.valueOf(accentColor));
        return tintIcon;
    }

    public void setMenuList(List<MenuItem> menuList) {
        this.menuList = menuList;
        initView();
    }

    public void setMenuList(String menuStr) {
        if(!TextUtils.isEmpty(menuStr)) {
            List<MenuItem> datas = new Gson().fromJson(menuStr, new TypeToken<List<MenuItem>>(){}.getType());
            setMenuList(datas);
        }
    }

    public void setOnSearchListener(OnSearchListener listener) {
        this.listener = listener;
    }

    public interface OnSearchListener {
        void onSearch(List<MenuItem> menuItemList);
    }
}
