package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.AgendaData;
import com.example.administrator.employeenote.entity.EventData;
import com.example.administrator.employeenote.utils.DateUtils;

import java.util.List;

/**
 * Created by GE11522 on 2016-10-17.
 */

public class EventAdapter extends BaseAdapter {
    public List<EventData> data;
    private LayoutInflater mInflater;
    private Context context;

    private final String TAG = "EventAdapter";

    public EventAdapter(Context context, List<EventData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.list_event, null);
            hold.timeView = (TextView) convertView.findViewById(R.id.time);
            hold.titleView = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.timeView.setText(DateUtils.unixToStringhour(data.get(position).getDtstart()) + " - " + DateUtils.unixToStringhour(data.get(position).getDtend()));

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
