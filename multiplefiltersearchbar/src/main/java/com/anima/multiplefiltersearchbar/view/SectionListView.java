package com.anima.multiplefiltersearchbar.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import org.zakariya.stickyheaders.StickyHeaderLayoutManager;

public class SectionListView extends FrameLayout implements UTSideBarView.OnLetterChangeListener {

    private StickyHeaderLayoutManager mStickyHeaderLayoutManager;
    private UTSectionAdapter mUTSectionAdapter;
    private RecyclerView mRecyclerView;
    private UTSectionAdapter.OnItemClickListener mOnItemSelectedListener;

    private String[] mDataArray;
    private RelativeLayout mRelativeLayout;

    /*public SectionListView(Context context, String[] dataArray) {
        this(context);
        this.mDataArray = dataArray;
        setupViews();
    }
*/
    public SectionListView(Context context) {
        this(context, null);
    }

    public SectionListView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SectionListView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public void setDataArray(String[] mDataArray) {
        this.mDataArray = mDataArray;
        setupViews();
    }

    protected void setupViews() {
        mRecyclerView = new RecyclerView(getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mStickyHeaderLayoutManager = new StickyHeaderLayoutManager();
        mUTSectionAdapter = new UTSectionAdapter(mDataArray);
        if (mOnItemSelectedListener != null) {
            mUTSectionAdapter.setOnClickListener(mOnItemSelectedListener);
        }

        mRecyclerView.setAdapter(mUTSectionAdapter);
        mRecyclerView.setLayoutManager(mStickyHeaderLayoutManager);

        mRelativeLayout = new RelativeLayout(getContext());
        mRelativeLayout.addView(mRecyclerView, layoutParams);
        UTSideBarView sideBarView = new UTSideBarView(getContext());
        RelativeLayout.LayoutParams sliderLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mRelativeLayout.addView(sideBarView, sliderLayoutParams);

        sideBarView.setOnLetterChangeListener(this);

        addView(mRelativeLayout);
    }

    @Override
    public void onLetterChange(String letter) {
        int position = mUTSectionAdapter.getLetterSectionPosition(letter);

        if (position > -1) {
            int adapterPosition = mUTSectionAdapter.getAdapterPositionForSectionHeader(position);
            mRecyclerView.scrollToPosition(adapterPosition);
        }
    }

    public void setOnClickListener(UTSectionAdapter.OnItemClickListener onClickListener) {
        mOnItemSelectedListener = onClickListener;
        mUTSectionAdapter.setOnClickListener(mOnItemSelectedListener);
    }

    public UTSectionAdapter getmUTSectionAdapter() {
        return mUTSectionAdapter;
    }
}
