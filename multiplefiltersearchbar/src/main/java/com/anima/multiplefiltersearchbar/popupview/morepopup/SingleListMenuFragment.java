package com.anima.multiplefiltersearchbar.popupview.morepopup;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;
import com.anima.multiplefiltersearchbar.popupview.singlelistpopup.SingleListAdapter;

import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by Admin on 2015/8/15.
 */
public class SingleListMenuFragment extends Fragment implements MenuFragmentAction {
    private View rootView;
    private MenuItem menuItem;
    private SingleListAdapter adapter;

    private View contentView;
    private View requestLoadingView;
    private View requestFailureView;

    private String selectedValue;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView = inflater.inflate(R.layout.popupview_singlelist, container, false);

            Bundle bundle = getArguments();
            this.menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");

            contentView = rootView.findViewById(R.id.popupview_singlelist_content);
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

    private void initListView() {
        RecyclerView recyclerView = rootView.findViewById(R.id.singleListPopup_recyclerview);
        adapter = new SingleListAdapter(getContext(), menuItem);
        adapter.setOnItemClickListener(new SingleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                clearValue();
                Map<String, String> data = menuItem.getDataList().get(position);
                selectedValue = data.keySet().iterator().next();
                adapter.setSelectedIndex(position);
            }
        });
        adapter.setSelectedListener(new SingleListAdapter.IsItemSelectedListener() {
            @Override
            public void selected(TextView itemView) {
                itemView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), layoutManager.getOrientation()));
        recyclerView.setLayoutManager(layoutManager);
    }

    protected void setData(OnDataChangeListener listener) {
        if(menuItem.getDataList() != null) {
            listener.dataCompletion(menuItem.getDataList());
        }else if(menuItem.getMapListDataListener() != null){
            menuItem.getMapListDataListener().requestData(listener);
        }
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
            if(adapter != null) {
                adapter.setSelectedIndex(-1);
            }
        }
    }
}
