package com.anima.multiplefiltersearchbar.popupview.treepopup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.R;
import com.unnamed.b.atv.model.TreeNode;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class TextTreeItemHolder extends TreeNode.BaseNodeViewHolder<TextTreeItemHolder.TextTreeItem> {
    private TextView tvValue;

    public TextTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, TextTreeItem value) {
        Log.i("TextTreeItemHolder", "TextTreeItemHolder createNodeView");
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_profile_node, null, false);
        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.level.getName());

        return view;
    }

    public static class TextTreeItem {
        public TreeDataLevel level;
        public TextTreeItem(TreeDataLevel level) {
            this.level = level;
        }
    }
}
