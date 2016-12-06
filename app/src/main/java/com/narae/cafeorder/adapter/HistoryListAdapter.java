package com.narae.cafeorder.adapter;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.narae.cafeorder.R;
import com.narae.cafeorder.history.History;
import com.narae.cafeorder.history.HistoryItem;

public class HistoryListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<History> historyList;

    public HistoryListAdapter(Context context, List<History> historyList) {
        this._context = context;
        this.historyList = historyList;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return historyList.get(groupPosition).getHistoryItems().get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final HistoryItem child = (HistoryItem) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.history_item, null);
        }

        TextView lblMenuName = (TextView) convertView
                .findViewById(R.id.lblMenuName);

        lblMenuName.setText(child.getMenuname());

        TextView lblCount = (TextView) convertView
                .findViewById(R.id.lblCount);

        lblCount.setText(child.getCount()+"건");

        TextView lblPrice = (TextView) convertView
                .findViewById(R.id.lblPrice);

        lblPrice.setText(child.getPrice()+"원");

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.historyList.get(groupPosition).getHistoryItems().size();
        /*return this.listChild.get(this.historyList.get(groupPosition).getId()).size();*/
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.historyList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.historyList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        History history = (History) getGroup(groupPosition);
        //String headerTitleSub = (String) getGroupSub(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.history_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(history.getTitle());

        TextView lblListSub = (TextView) convertView
                .findViewById(R.id.lblListHeaderSub);
        lblListSub.setText(history.getSubtitle());

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}