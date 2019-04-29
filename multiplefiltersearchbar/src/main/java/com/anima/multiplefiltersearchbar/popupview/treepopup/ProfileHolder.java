package com.anima.multiplefiltersearchbar.popupview.treepopup;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.R;
import com.unnamed.b.atv.model.TreeNode;

import static com.anima.multiplefiltersearchbar.popupview.treepopup.HeaderHolder.dpToPx;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class ProfileHolder extends TreeNode.BaseNodeViewHolder<TextTreeItemHolder.TextTreeItem> {

    private Context mContext;
    private TextView tvValue;
    public ProfileHolder(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    public View createNodeView(TreeNode node, TextTreeItemHolder.TextTreeItem value) {
        Log.i("ProfileHolder", "createNodeView");
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_profile_node, null, false);
        view.setPadding(dpToPx(16 * value.level.getLevel(), context), 0, 0, 0);
        tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.level.getName());
        if(node.isSelected()) {
            tvValue.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
        return view;
    }

    @Override
    public void toggle(boolean active) {
    }

    @Override
    public int getContainerStyle() {
        return R.style.TreeNodeStyleCustom;
    }

    public void setSelected() {
        if(tvValue != null) {
            tvValue.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
    }

    public void setUnselected() {
        if(tvValue != null) {
            tvValue.setTextColor(mContext.getResources().getColor(R.color.primary_text_dark_color));
        }
    }
}
