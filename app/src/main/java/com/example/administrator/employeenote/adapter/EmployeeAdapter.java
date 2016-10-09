package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.OnEntityListener;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.activity.EmployeeListActivity;
import com.example.administrator.employeenote.activity.HomePageActivity;
import com.example.administrator.employeenote.common.TrackApplication;
import com.example.administrator.employeenote.entity.EmployeeData;
import com.example.administrator.employeenote.entity.RealLocationData;
import com.example.administrator.employeenote.entity.VoiceData;
import com.example.administrator.employeenote.utils.GsonService;

import java.util.List;

/**
 * Created by GE11522 on 2016-9-5.
 */
public class EmployeeAdapter extends BaseAdapter {
    public List<EmployeeData> data;
    private LayoutInflater mInflater;
    private Context context;

    private final String TAG = "EmployeeAdapterE";

    public EmployeeAdapter(Context context, List<EmployeeData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new ViewHold();
            convertView = mInflater.inflate(R.layout.list_employee, null);
            hold.nameView = (TextView) convertView.findViewById(R.id.ename);
            hold.jobView = (TextView) convertView.findViewById(R.id.ejob);
            hold.departView = (TextView) convertView.findViewById(R.id.edepart);
            hold.localView = (TextView) convertView.findViewById(R.id.elocal);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.nameView.setText(data.get(position).getEname());
        hold.jobView.setText(data.get(position).getEjob());
        hold.departView.setText(data.get(position).getEdepart());

        //TODO 获取实时位置点
        hold.localView.setText(data.get(position).getElocal());
        return convertView;
    }

    class ViewHold {
        public TextView nameView, jobView, departView, localView;
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
