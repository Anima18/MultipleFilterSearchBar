package com.anima.multiplefiltersearchbar.popupview.treepopup;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.MenuItemAction;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.MenuPopupView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianjianhong on 19-4-8
 */
public abstract class TreePopupView extends MenuPopupView implements View.OnClickListener {
    private Context context;
    private MenuItem menuItem;
    private MenuItemAction action;

    private View contentView;
    private LinearLayout treeLayout;
    private Button resetButton;
    private View requestLoadingView;
    private View requestFailureView;

    private List<ProfileHolder> profileHolders = new ArrayList<>();
    private OnTreeDataChangeListener dataChangeListener = new OnTreeDataChangeListener() {
        @Override
        public void dataRequest() {
            contentView.setVisibility(GONE);
            requestFailureView.setVisibility(GONE);
            requestLoadingView.setVisibility(VISIBLE);
        }

        @Override
        public void dataCompletion(TreeDataLevel level) {
            contentView.setVisibility(VISIBLE);
            requestLoadingView.setVisibility(GONE);
            requestFailureView.setVisibility(GONE);
            initTreeView(level);
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

    public TreePopupView(@NonNull Context context) {
        super(context);
    }

    public TreePopupView(@NonNull Context context, @NonNull MenuItem menuItem, MenuItemAction action) {
        super(context, menuItem, action);
        this.context = context;
        this.menuItem = menuItem;
        this.action = action;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.popupview_tree;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        Log.i(this.getClass().getSimpleName(),"onCreate");
        contentView = findViewById(R.id.popupview_tree_content);
        treeLayout = contentView.findViewById(R.id.popupview_treeView);
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

    public void initTreeView(TreeDataLevel level) {

        final TreeNode root = TreeNode.root();
        TreeNode firstNode = new TreeNode(new TextTreeItemHolder.TextTreeItem(level)).setViewHolder(new HeaderHolder(context));
        firstNode.setExpanded(true);
        root.addChild(firstNode);
        initTreeNode(firstNode, level.getChilds());

        AndroidTreeView tView = new AndroidTreeView(context, root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);

        treeLayout.addView(tView.getView());
    }

    private void initTreeNode(TreeNode parentNode, List<TreeDataLevel> levels) {
        for(TreeDataLevel level : levels) {
            TextTreeItemHolder.TextTreeItem textTreeItem = new TextTreeItemHolder.TextTreeItem(level);
            TreeNode node;
            if(level.getChilds().size() > 0) {
                node = new TreeNode(textTreeItem).setViewHolder(new HeaderHolder(context));
                if(level.getChilds().size() > 0) {
                    initTreeNode(node, level.getChilds());
                }
            }else {
                ProfileHolder profileHolder = new ProfileHolder(context);
                profileHolders.add(profileHolder);
                node = new TreeNode(textTreeItem).setViewHolder(profileHolder);
                node.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        TextTreeItemHolder.TextTreeItem item = (TextTreeItemHolder.TextTreeItem)value;
                        setValue(item.level.getCode());
                        setText(item.level.getName());
                        dismiss();
                        action.onSearch();

                        ProfileHolder adapter = (ProfileHolder) node.getViewHolder();
                        for(ProfileHolder holder : profileHolders) {
                            if(holder != adapter) {
                                holder.setUnselected();
                            }else  {
                                holder.setSelected();
                            }
                        }
                    }
                });
            }
            //node.setExpanded(true);
            parentNode.addChild(node);
        }
    }

    protected abstract void setData(OnTreeDataChangeListener listener);

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.singleListPopup_reset) {
            setText("");
            setValue("");
            for(ProfileHolder holder : profileHolders) {
                holder.setUnselected();
            }
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

    public interface OnTreeDataChangeListener {
        void dataRequest();
        void dataCompletion(TreeDataLevel level);
        void dataError(String message);
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
