package com.anima.multiplefiltersearchbar.popupview.treepopup;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.R;
import com.unnamed.b.atv.model.TreeNode;


public class HeaderHolder extends TreeNode.BaseNodeViewHolder<TextTreeItemHolder.TextTreeItem> {

    private Context context;
    private ImageView arrowView;

    public HeaderHolder(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View createNodeView(TreeNode node, TextTreeItemHolder.TextTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_header_node, null, false);
        view.setPadding(dpToPx(16 * value.level.getLevel(), context), 0, 0, 0);
        TextView tvValue = view.findViewById(R.id.node_value);
        tvValue.setText(value.level.getName());

        arrowView = view.findViewById(R.id.arrow_icon);
        if (node.isLeaf()) {
            arrowView.setVisibility(View.INVISIBLE);
        }

        return view;
    }

    @Override
    public void toggle(boolean active) {
       if(active) {
            arrowView.animate().rotation(90);
        }else {
            arrowView.animate().rotation(0);
        }
    }

    public static int dpToPx(int dp, Context ctx){
        DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();
        return (int) (dp * metrics.density);
    }
}
