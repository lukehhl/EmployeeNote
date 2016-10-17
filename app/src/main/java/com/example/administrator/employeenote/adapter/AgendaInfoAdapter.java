package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.AgendaData;

import java.util.List;

/**
 * Created by GE11522 on 2016-10-17.
 */

public class AgendaInfoAdapter extends BaseAdapter {
    public List<AgendaData.AgendaInfoData> data;
    private LayoutInflater mInflater;
    private Context context;

    private final String TAG = "AgendaInfoAdapter";

    public AgendaInfoAdapter(Context context, List<AgendaData.AgendaInfoData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.list_agendainfo, null);
            hold.timeView = (TextView) convertView.findViewById(R.id.time);
            hold.titleView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.timeView.setText(data.get(position).getTime());
        hold.titleView.setText(data.get(position).getTitle());

        return convertView;
    }

    class ViewHold {
        public TextView timeView, titleView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
