package com.anima.multiplefiltersearchbar.popupview.morepopup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;
import com.anima.multiplefiltersearchbar.view.SectionListView;
import com.anima.multiplefiltersearchbar.view.UTSectionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by jianjianhong on 19-4-12
 */
public class SectionListMenuFragment extends Fragment implements MenuFragmentAction {
    private View rootView;
    private MenuItem menuItem;
    private View contentView;
    private View requestLoadingView;
    private View requestFailureView;
    private SectionListView sectionListView;

    private List<String> dataValueList = new ArrayList<>();
    private String selectedValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView =  inflater.inflate(R.layout.popupview_sectionlist, container, false);

            Bundle bundle = getArguments();
            menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");

            contentView = rootView.findViewById(R.id.popupview_sectionlist_content);
            sectionListView = contentView.findViewById(R.id.popupview_sectionlistview);
            requestLoadingView = rootView.findViewById(R.id.popupview_request_loading_layout);
            requestFailureView = rootView.findViewById(R.id.popupview_loading_failure_layout);
            requestFailureView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setData(dataChangeListener);
                }
            });

            setData(dataChangeListener);
        }

        return rootView;
    }

    private OnDataChangeListener dataChangeListener = new OnDataChangeListener() {
        @Override
        public void dataRequest() {
            contentView.setVisibility(GONE);
            requestFailureView.setVisibility(GONE);
            requestLoadingView.setVisibility(VISIBLE);
        }

        @Override
        public void dataCompletion(List<Map<String, String>> dataList) {
            contentView.setVisibility(VISIBLE);
            requestLoadingView.setVisibility(GONE);
            requestFailureView.setVisibility(GONE);
            menuItem.setDataList(dataList);
            initListView();
        }

        @Override
        public void dataError(String message) {
            contentView.setVisibility(GONE);
            requestLoadingView.setVisibility(GONE);
            requestFailureView.setVisibility(VISIBLE);

            /*if(!TextUtils.isEmpty(message)) {
                TextView textView = requestFailureView.findViewById(R.id.loading_failure_message);
                textView.setText(message);
            }*/
        }
    };

    protected void setData(OnDataChangeListener listener) {
        if(menuItem.getDataList() != null) {
            listener.dataCompletion(menuItem.getDataList());
        }else if(menuItem.getMapListDataListener() != null){
            menuItem.getMapListDataListener().requestData(listener);
        }
    }

    private void initListView() {
        List<Map<String, String>> mapList = menuItem.getDataList();
        final String[] textArray = new String[mapList.size()];
        for(int i = 0; i < mapList.size(); i++) {
            Map<String, String> map = mapList.get(i);
            String key = map.keySet().iterator().next();
            textArray[i] = map.get(key);
            dataValueList.add(key);
        }
        sectionListView.setDataArray(textArray);
        sectionListView.setOnClickListener(new UTSectionAdapter.OnItemClickListener() {
            @Override
            public void onClick(String[] dataArray, int position) {
                //menuItem.resetValueListByString(dataValueList.get(position));
                sectionListView.getmUTSectionAdapter().setSelectedIndex(position);
                selectedValue = dataValueList.get(position);
            }
        });
    }
    @Override
    public void saveValue() {
        menuItem.resetValueListByString(selectedValue);
    }

    @Override
    public void clearValue() {
        menuItem.clearValueList();
        clearSelected();
    }

    @Override
    public boolean isSelected() {
        return !TextUtils.isEmpty(selectedValue);
    }

    @Override
    public void clearSelected() {
        if(!menuItem.isSelected()) {
            selectedValue = "";
            if(sectionListView.getmUTSectionAdapter() != null) {
                sectionListView.getmUTSectionAdapter().setSelectedIndex(-1);
            }
        }
    }
}
