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
    private int place = 3;


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

    private void findLocationAtTime(int position) {

        //entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = String.valueOf(data.get(position).getEid());
        // 检索条件（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间，UNIX时间戳（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        int activeTime = 0;
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;
        // Entity监听器
        OnEntityListener entityListener = new OnEntityListener() {
            // 查询失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                Log.i(TAG, "onRequestFailedCallback" + "arg0 = " + arg0);
            }

            // 查询entity回调接口，返回查询结果列表
            @Override
            public void onQueryEntityListCallback(String arg0) {
                place = point2geo(arg0);
                Log.i(TAG, "onQueryEntityListCallback" + " arg0 = " + arg0);
            }
        };
        // 查询实时轨迹
        HomePageActivity.client.queryEntityList(TrackApplication.serviceId, entityNames, columnKey, returnType, activeTime, pageSize, pageIndex, entityListener);
    }

    private int point2geo(String realLoc) {
        RealLocationData realData = GsonService.parseJson(realLoc,
                RealLocationData.class);
        LatLng point = new LatLng(realData.getEntities().get(0).getRealtime_point().getLocation().get(0), realData.getEntities().get(0).getRealtime_point().getLocation().get(1));
        if (TrackApplication.isInJM(point)) {
            return 1;
        } else if (TrackApplication.isInTA(point)) {
            return 2;
        } else {
            return 0;
        }
    }

    private void realLoc(final int position) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                place = 3;
                findLocationAtTime(position);
                while (true) {
                    if (place == 1) {
                        EmployeeListActivity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                data.get(position).setElocal("集美");
                                notifyDataSetChanged();

                            }
                        });
                        break;
                    }
                    if (place == 2) {
                        EmployeeListActivity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                data.get(position).setElocal("同安");
                                notifyDataSetChanged();

                            }
                        });
                        break;
                    }
                    if (place == 0) {
                        EmployeeListActivity.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                data.get(position).setElocal("外出");
                                notifyDataSetChanged();

                            }
                        });
                        break;
                    }
                }

            }
        }.start();
    }


}
