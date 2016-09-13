package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.MyLocationData;
import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.entity.Employee.EmployeeData;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by GE11522 on 2016-9-5.
 */
public class EmployeeAdapter extends BaseAdapter{

    public List<EmployeeData> data;
    private LayoutInflater mInflater;
    private Context context;


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
            //hold.image = (ImageView) convertView.findViewById(R.id.hkavatar);
            hold.nameView = (TextView) convertView.findViewById(R.id.ename);
            hold.jobView = (TextView) convertView.findViewById(R.id.ejob);
            hold.departView = (TextView) convertView.findViewById(R.id.edepart);
            hold.localView = (TextView) convertView.findViewById(R.id.elocal);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }
        //hold.image.setImageResource(R.drawable.ic_launcher);
        hold.nameView.setText(data.get(position).getEname());
        hold.jobView.setText(data.get(position).getEjob());
        hold.departView.setText(data.get(position).getEdepart());
        //TODO 获取实时位置点
        return convertView;
    }

    class ViewHold {
        //public ImageView image;
        public TextView nameView, jobView, departView, localView;
    }


    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


}
