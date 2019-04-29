package com.anima.multiplefiltersearchbar.popupview.singlelistpopup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.MenuItem;
import com.anima.multiplefiltersearchbar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by chris on 2015/11/18
 */
public class SingleListAdapter extends RecyclerView.Adapter<SingleListAdapter.ViewHolder> {
    // Store the context for later use
    private Context context;
    private List<Map<String, String>> dList;
    private MenuItem menuItem;

    private OnItemClickListener listener;
    private IsItemSelectedListener selectedListener;

    private int selectedIndex = -1;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

    public interface IsItemSelectedListener {
        void selected(TextView itemView);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setSelectedListener(IsItemSelectedListener selectedListener) {
        this.selectedListener = selectedListener;
    }

    // Pass in the context and users array into the constructor
    public SingleListAdapter(Context context, MenuItem menuItem) {
        this.context = context;
        this.menuItem = menuItem;
        this.dList = menuItem.getDataList();
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the custom layout
        View itemView = LayoutInflater.from(context).inflate(R.layout.singlelist_popupview_item, parent, false);
        // Return a new holder instance
        return new ViewHolder(context, itemView);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        Map<String, String> map = dList.get(position);

        String key = map.keySet().iterator().next();
        holder.titleTv.setText(map.get(key));
        if((selectedIndex == position || isSeleted(key)) && selectedListener != null) {
            selectedListener.selected(holder.titleTv);
        }else {
            holder.titleTv.setTextColor(context.getResources().getColor(R.color.primary_text_dark_color));
        }
    }

    // Return the total count of items
    @Override
    public int getItemCount() {
        return dList.size();
    }

    private boolean isSeleted(String key) {
        if(menuItem.isSelected()) {
            return menuItem.getValueList().get(0).equals(key);
        }else {
            return false;
        }
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView titleTv;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(Context context, final View itemView) {
            super(itemView);

            titleTv = itemView.findViewById(R.id.singleListPopup_item_title);

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