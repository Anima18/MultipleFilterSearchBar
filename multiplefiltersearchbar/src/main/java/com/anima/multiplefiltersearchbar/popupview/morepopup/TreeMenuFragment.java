package com.anima.multiplefiltersearchbar.popupview.morepopup;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;
import com.anima.multiplefiltersearchbar.popupview.treepopup.HeaderHolder;
import com.anima.multiplefiltersearchbar.popupview.treepopup.ProfileHolder;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TextTreeItemHolder;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TreeDataLevel;
import com.anima.multiplefiltersearchbar.popupview.treepopup.TreePopupView;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by jianjianhong on 19-4-12
 */
public class TreeMenuFragment extends Fragment implements MenuFragmentAction {
    private View rootView;
    private MenuItem menuItem;

    private View contentView;
    private LinearLayout treeLayout;
    private View requestLoadingView;
    private View requestFailureView;

    private List<ProfileHolder> profileHolders = new ArrayList<>();
    private String selectedCode ;

    private TreePopupView.OnTreeDataChangeListener dataChangeListener = new TreePopupView.OnTreeDataChangeListener() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(rootView == null) {
            rootView =  inflater.inflate(R.layout.popupview_tree, container, false);

            Bundle bundle = getArguments();
            menuItem = (MenuItem) bundle.getSerializable("MENU_ITEM_DATA");

            contentView = rootView.findViewById(R.id.popupview_tree_content);
            treeLayout = contentView.findViewById(R.id.popupview_treeView);
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

    protected void setData(TreePopupView.OnTreeDataChangeListener listener) {
        if(menuItem.getTreeDataListener() != null){
            menuItem.getTreeDataListener().requestData(listener);
        }
    }

    public void initTreeView(TreeDataLevel level) {

        final TreeNode root = TreeNode.root();
        TreeNode firstNode = new TreeNode(new TextTreeItemHolder.TextTreeItem(level)).setViewHolder(new HeaderHolder(getContext()));
        firstNode.setExpanded(true);
        root.addChild(firstNode);
        initTreeNode(firstNode, level.getChilds());

        AndroidTreeView tView = new AndroidTreeView(getContext(), root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);

        treeLayout.addView(tView.getView());
    }

    private void initTreeNode(TreeNode parentNode, List<TreeDataLevel> levels) {
        for(TreeDataLevel level : levels) {
            TextTreeItemHolder.TextTreeItem textTreeItem = new TextTreeItemHolder.TextTreeItem(level);
            TreeNode node;
            if(level.getChilds().size() > 0) {
                node = new TreeNode(textTreeItem).setViewHolder(new HeaderHolder(getContext()));
                if(level.getChilds().size() > 0) {
                    initTreeNode(node, level.getChilds());
                }
            }else {
                ProfileHolder profileHolder = new ProfileHolder(getContext());
                profileHolders.add(profileHolder);
                node = new TreeNode(textTreeItem).setViewHolder(profileHolder);
                node.setClickListener(new TreeNode.TreeNodeClickListener() {
                    @Override
                    public void onClick(TreeNode node, Object value) {
                        TextTreeItemHolder.TextTreeItem item = (TextTreeItemHolder.TextTreeItem)value;
                        ProfileHolder adapter = (ProfileHolder) node.getViewHolder();
                        for(ProfileHolder holder : profileHolders) {
                            if(holder != adapter) {
                                holder.setUnselected();
                            }else  {
                                holder.setSelected();
                            }
                        }
                        selectedCode = item.level.getCode();
                    }
                });
            }
            parentNode.addChild(node);
        }
    }
    @Override
    public void saveValue() {
        menuItem.resetTreeValueList(selectedCode, "");
    }

    @Override
    public void clearValue() {
        menuItem.clearValueList();
        clearSelected();
    }

    @Override
    public boolean isSelected() {
        return !TextUtils.isEmpty(selectedCode);
    }

    @Override
    public void clearSelected() {
        if(!menuItem.isSelected()) {
            selectedCode = "";
            for(ProfileHolder holder : profileHolders) {
                holder.setUnselected();
            }
        }
    }
}
