package com.anima.multiplefiltersearchbar.popupview.morepopup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;

import java.util.List;

/**
 * Created by Admin on 2015/8/19.
 */
public class MoreMenuAdapter extends RecyclerView.Adapter<MoreMenuAdapter.ViewHolder> {

    private Context context;
    private int layoutId;
    private List<MenuItem> menuItemList;
    private int selectedIndex = 0;        //记录当前选中的条目索引
    private MorePopupView morePopupView;

    private static OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Pass in the context and users array into the constructor
    public MoreMenuAdapter(Context context, MorePopupView morePopupView, int layoutId, List<MenuItem> menuItemList) {
        this.context = context;
        this.layoutId = layoutId;
        this.morePopupView = morePopupView;
        this.menuItemList = menuItemList;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        // Return a new holder instance
        return new ViewHolder(context, itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MenuItem menuItem = menuItemList.get(position);
        holder.textView.setText(menuItem.getName());

        if (selectedIndex == position) {
            holder.textView.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.more_menuitem_selected));
        } else {
            holder.textView.setTextColor(context.getResources().getColor(R.color.primary_text_dark_color));
            holder.textView.setBackground(context.getResources().getDrawable(R.drawable.more_menuitem_unselect));
        }
        if(menuItem.isSelected() || morePopupView.isMenuItemSelected(menuItem)) {
            holder.indicatorView.setVisibility(View.VISIBLE);
        }else {
            holder.indicatorView.setVisibility(View.GONE);
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return menuItemList.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView indicatorView;
        public ViewHolder(Context context, final View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.moreMenuList_text);
            indicatorView = itemView.findViewById(R.id.moreMenuList_indeicator);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null)
                        listener.onItemClick(itemView, getLayoutPosition());
                }
            });
        }
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
        notifyDataSetChanged();
    }
}