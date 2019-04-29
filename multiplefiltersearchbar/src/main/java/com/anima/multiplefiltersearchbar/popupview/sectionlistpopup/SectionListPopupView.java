package com.anima.multiplefiltersearchbar.popupview.sectionlistpopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;
import com.anima.multiplefiltersearchbar.popupview.OnDataChangeListener;
import com.anima.multiplefiltersearchbar.view.SectionListView;
import com.anima.multiplefiltersearchbar.view.UTSectionAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jianjianhong on 19-4-8
 */
public abstract class SectionListPopupView extends MenuPopupView implements View.OnClickListener {
    private MenuItem menuItem;
    private Button resetButton;
    private MenuItemAction action;

    private View contentView;
    private View requestLoadingView;
    private View requestFailureView;
    private SectionListView sectionListView;

    private List<String> dataValueList = new ArrayList<>();;

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

    public SectionListPopupView(@NonNull Context context) {
        super(context);
    }

    public SectionListPopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_sectionlist;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onCreate");
        contentView = findViewById(R.id.popupview_sectionlist_content);
        sectionListView = contentView.findViewById(R.id.popupview_sectionlistview);
        resetButton = contentView.findViewById(R.id.singleListPopup_reset);
        resetButton.setOnClickListener(this);

        requestLoadingView = findViewById(R.id.popupview_request_loading_layout);
        requestFailureView = findViewById(R.id.popupview_loading_failure_layout);
        requestFailureView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(dataChangeListener);
            }
        });

        setData(dataChangeListener);
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
                sectionListView.getmUTSectionAdapter().setSelectedIndex(position);
                String value = dataValueList.get(position);
                setValue(value);
                setText(menuItem.getDataList().get(position).get(value));
                dismiss();
                action.onSearch();
            }
        });
    }

    protected abstract void setData(OnDataChangeListener listener);

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.singleListPopup_reset) {
            sectionListView.getmUTSectionAdapter().setSelectedIndex(-1);
            setValue("");
            setText("");
            dismiss();
            action.onSearch();
        }
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.i(this.getClass().getSimpleName(),"onShow");
        action.selectMenuItem(menuItem);
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
