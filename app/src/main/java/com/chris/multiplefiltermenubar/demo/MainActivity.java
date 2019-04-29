package com.chris.multiplefiltermenubar.demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemType;
import com.anima.multiplefiltersearchbar.MultipleFilterSearchBar;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TreeDataLevel;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TreePopupView;
import com.google.gson.reflect.TypeToken;
import com.ut.requsetmanager.callback.DataRequestCallback;
import com.ut.requsetmanager.entity.ResponseStatus;
import com.ut.requsetmanager.request.NetworkRequestImpl;
import com.ut.requsetmanager.util.jsonParse.UTJsonFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ut.requsetmanager.entity.platformservice.PlatformServiceType.DATA_CENTER_SERVICE;

public class MainActivity extends AppCompatActivity {

    private MultipleFilterSearchBar menuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        menuBar = findViewById(R.id.mainAct_menubar);
        initMenuBarData();

        menuBar.setOnSearchListener(new MultipleFilterSearchBar.OnSearchListener() {
            @Override
            public void onSearch(List<MenuItem> menuItemList) {
                StringBuilder builder = new StringBuilder();
                for(MenuItem item : menuItemList) {
                    builder.append(item.getCode() + ":" + item.getValueList());
                    builder.append("\n");
                }
                Toast.makeText(MainActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initMenuBarData() {
        List<MenuItem> menuItemList = new ArrayList<>();
        menuItemList.add(MenuItem.getDateIntervalMenuItem("ddd", "时间不限"));
        menuItemList.add(initSectionMenuItem());
        menuItemList.add(initSingleMenuItem());
        menuItemList.add(MenuItem.getInputMenuItem("guid", "问题描述"));
        menuItemList.add(MenuItem.getDateIntervalMenuItem("date", "开始日期"));
        menuItemList.add(initTreeMenuItem());
        menuItemList.add(initSectionMenuItem());
        menuItemList.add(initSingleMenuItem());

        menuBar.setMenuList(menuItemList);
    }

    private MenuItem initSingleMenuItem() {
        return new MenuItem("type", "工作票类型", MenuItemType.SINGLE_CHOICE, new MenuItem.OnRequestMapListDataListener() {
            @Override
            public void requestData(final OnDataChangeListener listener) {
                listener.dataRequest();
                String url = "http://192.168.53.193:44423/GetRDDataJson?a=田林&b=e10adc3949ba59abbe56e057f20f883e&c=getdtObjectdata&d=dtctsvr_objectcode=utgzptickettype;dtctsvr_projectversion=1;dtctsvr_projectcode=utdtpower;";
                NetworkRequestImpl.create(MainActivity.this)
                        .setUrl(url)
                        .setMethod("GET")
                        .setPlatformService(DATA_CENTER_SERVICE)
                        .send(new DataRequestCallback() {
                            @Override
                            public void onResult(Object data, int dataSize, ResponseStatus status) {

                                if(200 == status.getCode()) {
                                    List<Map<String, String>> mapList = (List<Map<String, String>>)data;
                                    List<Map<String, String>> dataList = new ArrayList<>();
                                    for(Map<String, String> map : mapList) {
                                        Map<String, String> m = new HashMap<>();
                                        m.put(map.get("utdtguid"), map.get("fname"));
                                        dataList.add(m);
                                    }
                                    if(dataList.size() != 0) {
                                        listener.dataCompletion(dataList);
                                    }else {
                                        listener.dataError("没有数据");
                                    }

                                }else {
                                    listener.dataError(status.message);
                                }
                            }
                        });
            }
        });
    }

    private MenuItem initSectionMenuItem() {
        return new MenuItem("user", "用户", MenuItemType.SECTION_CHOICE, new MenuItem.OnRequestMapListDataListener() {
            @Override
            public void requestData(final OnDataChangeListener listener) {
                listener.dataRequest();
                String url = "http://192.168.53.193:44401/GetRDDataJson?a=田林&b=e10adc3949ba59abbe56e057f20f883e&c=getUserByUserOrgaId&d=ursvr_appname=utjoyoj;ursvr_procname=utjoyoj;";
                NetworkRequestImpl.create(MainActivity.this)
                        .setUrl(url)
                        .setMethod("GET")
                        .setPlatformService(DATA_CENTER_SERVICE)
                        .send(new DataRequestCallback() {
                            @Override
                            public void onResult(Object data, int dataSize, ResponseStatus status) {

                                if(200 == status.getCode()) {
                                    List<Map<String, String>> mapList = (List<Map<String, String>>)data;
                                    List<Map<String, String>> dataList = new ArrayList<>();
                                    for(Map<String, String> map : mapList) {
                                        Map<String, String> m = new HashMap<>();
                                        m.put(map.get("utdtguid"), map.get("fname"));
                                        dataList.add(m);
                                    }
                                    if(dataList.size() != 0) {
                                        listener.dataCompletion(dataList);
                                    }else {
                                        listener.dataError("没有数据");
                                    }

                                }else {
                                    listener.dataError(status.message);
                                }
                            }
                        });
            }
        });
    }

    private MenuItem initTreeMenuItem() {
        return new MenuItem("guid", "部门", MenuItemType.TREE, new MenuItem.OnRequestTreeDataListener() {
            @Override
            public void requestData(final TreePopupView.OnTreeDataChangeListener listener) {
                String url = "http://192.168.53.193:44401/GetRDDataJson?a=田林&b=e10adc3949ba59abbe56e057f20f883e&c=getuserorga&d=ursvr_appname=utjoyoj;ursvr_classname=ut.f500d.dfuserirhgt.department;ursvr_procname=utjoyoj;";
                listener.dataRequest();

                NetworkRequestImpl.create(MainActivity.this)
                        .setUrl(url)
                        .setMethod("GET")
                        .setPlatformService(DATA_CENTER_SERVICE)
                        .setDataType(new TypeToken<Department>() {}.getType())
                        .send(new DataRequestCallback<List<Department>>() {
                            @Override
                            public void onResult(List<Department> data, int dataSize, ResponseStatus status) {

                                if(200 == status.getCode()) {
                                    if(data.size() != 0) {
                                        TreeDataLevel root = null;
                                        for(Department department : data) {
                                            if("0".equals(department.getFcode())) {
                                                DepartmentInfo departmentInfo = UTJsonFactory.createJson().fromJson(department.getUoData(), new TypeToken<DepartmentInfo>() {}.getType());
                                                root  = new TreeDataLevel(department.getFcode(), departmentInfo.getDep_name(), 1);
                                                break;
                                            }
                                        }
                                        initTreeData(root, data, 2);
                                        listener.dataCompletion(root);
                                    }else {
                                        listener.dataError("没有数据");
                                    }

                                }else {
                                    listener.dataError(status.message);
                                }
                            }
                        });
            }
        });
    }

    private void initTreeData(TreeDataLevel node, List<Department> data, int level) {
        if(level > 4) {
            return;
        }
        for(Department department : data) {
            if(node != null && department.getParentfcode().equals(node.getCode())) {
                DepartmentInfo departmentInfo = UTJsonFactory.createJson().fromJson(department.getUoData(), new TypeToken<DepartmentInfo>() {}.getType());
                TreeDataLevel child = new TreeDataLevel(department.getFcode(), departmentInfo.getDep_name(), level);
                node.addChild(child);
                initTreeData(child, data, level+1);
            }
        }
    }

    private MenuItem initStaticData() {
        List<Map<String, String>> mapList = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("1", "Aaaaaaaaaaa");
        Map<String, String> map1 = new HashMap<>();
        map1.put("2", "Bbbbbbbbbbb");
        Map<String, String> map2 = new HashMap<>();
        map2.put("3", "Ccccccccccc");
        Map<String, String> map3 = new HashMap<>();
        map3.put("4", "Dddddddddd");
        Map<String, String> map4 = new HashMap<>();
        map4.put("5", "22222");
        mapList.add(map);
        mapList.add(map1);
        mapList.add(map2);
        mapList.add(map3);
        mapList.add(map4);
        MenuItem menuItem = new MenuItem("name", "dddd", MenuItemType.SINGLE_CHOICE, mapList);
        return menuItem;
    }
}
