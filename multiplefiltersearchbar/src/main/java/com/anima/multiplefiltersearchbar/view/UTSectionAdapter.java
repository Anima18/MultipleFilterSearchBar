package com.anima.multiplefiltersearchbar.view;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anima.multiplefiltersearchbar.R;
import com.github.promeg.pinyinhelper.Pinyin;

import org.zakariya.stickyheaders.SectioningAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class UTSectionAdapter extends SectioningAdapter {

    private List<Section> mSections;
    private OnItemClickListener mOnClickListener;

    private String[] mSourceDataArray; //保持原数据顺序的数组
    private String[] mSortDataArray; // 按拼音重新排序后的数组

    private int selectedIndex = -1;
    private int selectedTextColor, unSelectTextColor;

    public interface OnItemClickListener {
        void onClick(String[] dataArray, int position);
    }

    private class Section {
        int index;
        String header;
        ArrayList<String> items = new ArrayList<>();
    }

    public UTSectionAdapter(String[] dataArray) {
        // 处理数据
        mSourceDataArray = Arrays.copyOf(dataArray, dataArray.length);
        mSortDataArray = Arrays.copyOf(dataArray, dataArray.length);
        mSections = new ArrayList<>();

        Arrays.sort(mSortDataArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return Pinyin.toPinyin(o1, "").compareTo(Pinyin.toPinyin(o2, ""));
            }
        });

        //获得按拼音排好序的数据
        List<String> list = Arrays.asList(mSortDataArray);

        for (String letter : "#|A|B|C|D|E|F|G|H|I|J|K|L|M|N|O|P|Q|R|S|T|U|V|W|X|Y|Z".split("\\|")) {
            Section section = new Section();
            section.header = letter;
            for (String data : list) {
                String pinyin = Pinyin.toPinyin(data, "");
                if (isChar(pinyin)) {
                    //字母
                    if (pinyin.toUpperCase().startsWith(letter) && !data.equals("全部")) {
                        section.items.add(data);
                    }
                } else if (letter.equals("#")) {
                    //非字母
                    section.items.add(data);
                }
            }

            if (!section.items.isEmpty()) {
                mSections.add(section);
            }
        }

        for (String data : list) {
            if (data.equals("全部")) {
                Section section = new Section();
//                section.header = "全部";
                section.items.add("全部");
                mSections.add(0, section);
                break;
            }
        }


        System.out.println(list.toString());
    }

    private boolean isChar(String fstrData) {
        char c = fstrData.charAt(0);
        if(((c>='a'&&c<='z')   ||   (c>='A'&&c<='Z'))) {
            return   true;
        } else {
            return   false;
        }
    }

    @Override
    public int getNumberOfSections() {
        return mSections.size();
    }

    @Override
    public int getNumberOfItemsInSection(int sectionIndex) {
        return mSections.get(sectionIndex).items.size();
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerUserType) {
        TextView textView = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sectionlist_header, parent, false);
        return new HeaderViewHolder(textView);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder viewHolder, int sectionIndex, int headerUserType) {
        ((TextView)viewHolder.itemView).setText("  " + mSections.get(sectionIndex).header);
    }

    @Override
    public ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemUserType) {
        selectedTextColor = parent.getContext().getResources().getColor(R.color.colorPrimary);
        unSelectTextColor = parent.getContext().getResources().getColor(R.color.primary_text_dark_color);
        return new SingleChoiceItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_sectionlist_item, parent, false));
    }

    @Override
    public GhostHeaderViewHolder onCreateGhostHeaderViewHolder(ViewGroup parent) {
        final View ghostView = new View(parent.getContext());
        ghostView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return new GhostHeaderViewHolder(ghostView);
    }

    @Override
    public void onBindItemViewHolder(ItemViewHolder viewHolder, int sectionIndex, int itemIndex, int itemUserType) {
        SingleChoiceItemViewHolder itemViewHolder = (SingleChoiceItemViewHolder)viewHolder;
        String value = mSections.get(sectionIndex).items.get(itemIndex);
        itemViewHolder.textView.setText(value);

        if (selectedIndex != -1) {
            String selectedValue = mSourceDataArray[selectedIndex];
            if (value.equals(selectedValue)) {
                itemViewHolder.textView.setTextColor(selectedTextColor);
            } else {
                itemViewHolder.textView.setTextColor(unSelectTextColor);
            }
        } else {
            itemViewHolder.textView.setTextColor(unSelectTextColor);
        }
    }

    @Override
    public boolean doesSectionHaveHeader(int sectionIndex) {
        return !TextUtils.isEmpty(mSections.get(sectionIndex).header);
    }

    private class SingleChoiceItemViewHolder extends ItemViewHolder {

        TextView textView;

        public SingleChoiceItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.section_item_text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null) {
                        String data = textView.getText().toString();

                        mOnClickListener.onClick(mSourceDataArray, getDataIndexInSourceDataArray(data));
                    }
                }
            });
        }
    }

    private int getDataIndexInSourceDataArray(String data) {
        int index = -1;
        for (int i = 0; i < mSourceDataArray.length; i++) {
            if (data.equals(mSourceDataArray[i])) {
                index = i;
            }
        }

        return index;
    }

    public int getLetterSectionPosition(String letter) {
        int position = 0;
        boolean found = false;
        for (Section section : mSections) {
            if (letter.equals(section.header)) {
                found = true;
                position = mSections.indexOf(section);
                break;
            }
        }

        position = found ? position : -1;

        return position;
    }

    public void setOnClickListener(OnItemClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setSelectedIndex(int position) {
        this.selectedIndex = position;
        notifyDataSetChanged();
    }
}
