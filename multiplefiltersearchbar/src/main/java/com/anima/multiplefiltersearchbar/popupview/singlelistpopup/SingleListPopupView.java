package com.anima.multiplefiltersearchbar.popupview.singlelistpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;

import java.util.List;
import java.util.Map;

/**
 * Created by jianjianhong on 19-4-4
 */
public abstract class SingleListPopupView extends MenuPopupView {

    private Context context;
    private MenuItem menuItem;
    private MenuItemAction action;
    private SingleListAdapter adapter;

    private View contentView;
    private View requestLoadingView;
    private View requestFailureView;

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

    public SingleListPopupView(@NonNull Context context) {
        super(context);
    }

    public SingleListPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.context = context;
        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_singlelist;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onCreate");
        contentView = findViewById(R.id.popupview_singlelist_content);
        requestLoadingView = findViewById(R.id.popupview_request_loading_layout);
        requestFailureView = findViewById(R.id.popupview_loading_failure_layout);
        requestFailureView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(dataChangeListener);
            }
        });

        setData(dataChangeListener);

        Button resetButton = findViewById(R.id.singleListPopup_reset);
        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setValue("");
                setText("");
                dismiss();
                action.onSearch();
            }
        });
    }

    private void initListView() {
        RecyclerView recyclerView = findViewById(R.id.singleListPopup_recyclerview);
        adapter = new SingleListAdapter(context, menuItem);
        adapter.setOnItemClickListener(new SingleListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Map<String, String> data = menuItem.getDataList().get(position);
                String value = data.keySet().iterator().next();
                String text = data.get(value);
                setValue(value);
                setText(text);
                dismiss();
                action.onSearch();
            }
        });
        adapter.setSelectedListener(new SingleListAdapter.IsItemSelectedListener() {
            @Override
            public void selected(TextView itemView) {
                itemView.setTextColor(action.getColorTint());
            }
        });
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.addItemDecoration(new DividerItemDecoration(context, layoutManager.getOrientation()));
        recyclerView.setLayoutManager(layoutManager);
    }

    protected abstract void setData(OnDataChangeListener listener);

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onShow");
        action.selectMenuItem(menuItem);
        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.i(this.getClass().getSimpleName(),"onDismiss");
        action.unSelectMenuItem(menuItem);
    }

    @Override
    protected void setText(String text) {
        menuItem.setText(text);
    }

    @Override
    protected void setValue(String value) {
        menuItem.resetValueListByString(value);
    }
}
