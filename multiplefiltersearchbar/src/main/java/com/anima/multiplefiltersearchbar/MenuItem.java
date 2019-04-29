package com.anima.multiplefiltersearchbar;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TreePopupView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jianjianhong on 19-4-2
 */
public class MenuItem implements Serializable {
    /**
     * 查询条件code
     */
    private String code;
    /**
     * 查询条件描述
     */
    private String name;
    /**
     * 查询条件类型
     */
    private MenuItemType itemType;

    private String text;

    /**
     * 查询条件值
     */
    private List<String> valueList;
    /**
     * 查询条件数据
     */
    private List<Map<String, String>> dataList;

   // private String treeSelectText;
    private List<MenuItem> moreMenuList;

    private OnRequestMapListDataListener mapListDataListener;

    private OnRequestTreeDataListener treeDataListener;


    MenuItem() {
        valueList = new ArrayList<>();
    }

    private MenuItem(String code, String name, MenuItemType itemType) {
        this.code = code;
        this.name = name;
        this.itemType = itemType;
        this.text = "";
        this.valueList = new ArrayList<>();
    }

    public static MenuItem getInputMenuItem(String code, String name) {
        return new MenuItem(code, name, MenuItemType.INPUT);
    }

    public static MenuItem getDateMenuItem(String code, String name) {
        return new MenuItem(code, name, MenuItemType.DATE);
    }

    public static MenuItem getDateIntervalMenuItem(String code, String name) {
        return new MenuItem(code, name, MenuItemType.DATE_INTERVAL);
    }

    static MenuItem getMoreMenuItem(String code, String name, List<MenuItem> moreMenuList) {
        MenuItem moreItem = new MenuItem(code, name, MenuItemType.MORE);
        moreItem.moreMenuList = moreMenuList;
        return moreItem;
    }


    public MenuItem(String code, String name, MenuItemType itemType, @NonNull List<Map<String, String>> dataList) {
        this.code = code;
        this.name = name;
        this.itemType = itemType;
        this.valueList = new ArrayList<>();
        this.dataList = dataList;
    }

    public MenuItem(String code, String name, MenuItemType itemType, @NonNull OnRequestMapListDataListener mapListDataListener) {
        this.code = code;
        this.name = name;
        this.itemType = itemType;
        this.valueList = new ArrayList<>();
        this.mapListDataListener = mapListDataListener;
    }

    public MenuItem(String code, String name, MenuItemType itemType, @NonNull OnRequestTreeDataListener treeDataListener) {
        this.code = code;
        this.name = name;
        this.itemType = itemType;
        this.valueList = new ArrayList<>();
        this.treeDataListener = treeDataListener;
    }

    public interface OnRequestMapListDataListener {
        void requestData(OnDataChangeListener listener);
    }

    public interface OnRequestTreeDataListener {
        void requestData(TreePopupView.OnTreeDataChangeListener listener);
    }

    public boolean isSelected() {
        if(itemType.equals(MenuItemType.MORE)) {
            for(MenuItem menuItem : moreMenuList) {
                if(menuItem.getValueList().size() > 0) {
                    return true;
                }
            }
            return false;
        }else {
            return valueList.size() > 0;
        }
    }

    /*public String getSelectText() {
        if(valueList.size() > 0) {
            String value = valueList.get(0);
            if(MenuItemType.INPUT.equals(itemType) || MenuItemType.DATE.equals(itemType)) {
                return value;
            }else if(MenuItemType.SINGLE_CHOICE.equals(itemType) || MenuItemType.SECTION_CHOICE.equals(itemType)) {
                for(Map<String, String> map : dataList) {
                    if(map.containsKey(value)) {
                        return map.get(value);
                    }
                }
            }else if(MenuItemType.TREE.equals(itemType)) {
                return treeSelectText;
            }
            return "";
        }else if(MenuItemType.MORE.equals(itemType)) {
            return isSelected() ? "已筛选": "更多筛选";
        }else {
            return "";
        }
    }*/

    public OnRequestMapListDataListener getMapListDataListener() {
        return mapListDataListener;
    }

    public OnRequestTreeDataListener getTreeDataListener() {
        return treeDataListener;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuItemType getItemType() {
        return itemType;
    }

    public void setItemType(MenuItemType itemType) {
        this.itemType = itemType;
    }

    public List<String> getValueList() {
        return valueList;
    }

    public void setValueList(List<String> valueList) {
        clearValueList();
        this.valueList.addAll(valueList);
    }

    public void resetValueListByString(String value) {
        clearValueList();
        if(!TextUtils.isEmpty(value)) {
            this.valueList.add(value);
        }
    }

    public void resetTreeValueList(String value, String text) {
        resetValueListByString(value);
        this.text = text;
    }

    public void addValue(String value) {
        this.valueList.add(value);
    }

    public void clearValueList() {
        this.valueList.clear();
    }

    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    public void setDataList(List<Map<String, String>> dataList) {
        this.dataList = dataList;
    }

    public List<MenuItem> getMoreMenuList() {
        return moreMenuList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
