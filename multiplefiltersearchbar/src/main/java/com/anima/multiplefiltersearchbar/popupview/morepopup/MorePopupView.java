package com.anima.multiplefiltersearchbar.popupview.morepopup;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jianjianhong on 19-4-11
 */
public class MorePopupView extends MenuPopupView implements View.OnClickListener {

    private Context context;
    private MenuItemAction action;
    private MenuItem menuItem;
    private List<MenuItem> childMenuItems;
    private Map<MenuItem, Fragment> menuFragmentMap = new HashMap<>();
    private List<MenuFragmentAction> fragmentList = new ArrayList<>();

    private RecyclerView menuListView;
    private MoreMenuAdapter adapter;
    private Button resetButton;
    private Button okButton;


    public MorePopupView(@NonNull Context context) {
        super(context);
    }

    public MorePopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.context = context;
        this.action = action;
        this.menuItem = menuItem;
        this.childMenuItems = menuItem.getMoreMenuList();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_more;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onCreate");

        menuListView = findViewById(R.id.morePopup_menu_layout);
        resetButton = findViewById(R.id.popupView_reset);
        okButton = findViewById(R.id.popupView_ok);
        resetButton.setOnClickListener(this);
        okButton.setOnClickListener(this);

        adapter = new MoreMenuAdapter(context, this, R.layout.listview_moremenu_item, childMenuItems);
        menuListView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        menuListView.setLayoutManager(layoutManager);

        adapter.setOnItemClickListener(new MoreMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                selectMenuItem(position);
            }
        });
        selectMenuItem(0);
    }

    /*private void initFragmentList() {
        for(MenuItem menuItem : childMenuItems) {
            Fragment fragment = MenuFragmentFactory.create(menuItem);
            menuFragmentMap.put(menuItem, fragment);
            fragmentList.add((MenuFragmentAction) fragment);
        }
    }*/

    private void selectMenuItem(int index) {
        adapter.setSelectedIndex(index);
        MenuItem menuItem = childMenuItems.get(index);
        if(menuFragmentMap.get(menuItem) == null) {
            Fragment fragment = MenuFragmentFactory.create(menuItem);
            menuFragmentMap.put(menuItem, fragment);
            fragmentList.add((MenuFragmentAction) fragment);
        }
        Fragment fragment = menuFragmentMap.get(menuItem);
        Bundle bundle = new Bundle();
        bundle.putSerializable("MENU_ITEM_DATA",menuItem);//这里的values就是我们要传的值
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.morePopup_content_frame, fragment).commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.popupView_ok) {
            for(MenuFragmentAction fragment : fragmentList) {
                if(fragment != null) {fragment.saveValue();}
            }
        } else if (i == R.id.popupView_reset) {
            for(MenuFragmentAction fragment : fragmentList) {
                if(fragment != null) {fragment.clearValue();}
            }
        }
        setText("");
        dismiss();
        action.onSearch();
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onShow");
        action.selectMenuItem(menuItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.i(this.getClass().getSimpleName(),"onDismiss");
        action.unSelectMenuItem(menuItem);
        for(MenuFragmentAction fragment : fragmentList) {
            if(fragment != null) {
                fragment.clearSelected();
            }
        }
    }

    boolean isMenuItemSelected(MenuItem menuItem) {
        MenuFragmentAction action = (MenuFragmentAction)menuFragmentMap.get(menuItem);
        if(action != null) {
            return action.isSelected();
        } else {
            return false;
        }
    }

    @Override
    protected void setText(String text) {
        menuItem.setText(menuItem.isSelected() ? "已筛选": "更多筛选");
    }

    @Override
    protected void setValue(String value) {

    }
}
