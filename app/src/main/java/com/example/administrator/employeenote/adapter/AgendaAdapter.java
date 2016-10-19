package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.activity.AgendaInfoActivity;
import com.example.administrator.employeenote.entity.AgendaData;
import com.example.administrator.employeenote.entity.EmployeeData;

import java.util.List;

/**
 * Created by GE11522 on 2016-10-17.
 */

public class AgendaAdapter extends BaseAdapter {
    public List<AgendaData> data;
    private LayoutInflater mInflater;
    private Context context;

    private final String TAG = "AgendaAdapter";

    public AgendaAdapter(Context context, List<AgendaData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.list_agenda, null);
            hold.monthView = (TextView) convertView.findViewById(R.id.month);
            hold.dateView = (TextView) convertView.findViewById(R.id.date);
            hold.weekView = (TextView) convertView.findViewById(R.id.week);
            hold.deslsView = (ListView) convertView.findViewById(R.id.agendainfo_ls);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.monthView.setText(data.get(position).getMonth());
        hold.dateView.setText(data.get(position).getDate());
        hold.weekView.setText(data.get(position).getWeek());
        hold.deslsView.setAdapter(new EventAdapter(context, data.get(position).getEventDatas()));
        hold.deslsView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
                Intent it = new Intent(context, AgendaInfoActivity.class);
                it.putExtra("event_id", data.get(position).getEventDatas().get(position1).getEvent_id());
                context.startActivity(it);
            }
        });

        return convertView;
    }

    class ViewHold {
        public TextView monthView, dateView, weekView;
        public ListView deslsView;
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
