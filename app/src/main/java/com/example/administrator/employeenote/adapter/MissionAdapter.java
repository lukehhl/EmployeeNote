package com.example.administrator.employeenote.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.employeenote.R;
import com.example.administrator.employeenote.activity.DrawMapActivity;
import com.example.administrator.employeenote.entity.VoiceData;
import com.example.administrator.employeenote.utils.PlayerSingleton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

import static com.example.administrator.employeenote.common.TrackApplication.SERVERURL;

/**
 * Created by GE11522 on 2016/9/28.
 */

public class MissionAdapter extends BaseAdapter {

    private static final String TAG = "missionadapter";
    private String delSign = null;
    private String finSign = null;

    public List<VoiceData> data;
    private LayoutInflater mInflater;
    private Context context;

    public MissionAdapter(Context context, List<VoiceData> data) {
        this.context = context;
        this.data = data;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold hold = null;
        if (convertView == null) {
            hold = new MissionAdapter.ViewHold();
            convertView = mInflater.inflate(R.layout.list_mission, null);
            hold.yearView = (TextView) convertView.findViewById(R.id.year);
            hold.dateView = (TextView) convertView.findViewById(R.id.date);
            hold.playView = (Button) convertView.findViewById(R.id.btn_play);
            hold.delView = (Button) convertView.findViewById(R.id.btn_delete);
            hold.finishView = (Button) convertView.findViewById(R.id.btn_finish);
            hold.traceView = (Button) convertView.findViewById(R.id.btn_review);
            hold.stateView = (TextView) convertView.findViewById(R.id.state);
            convertView.setTag(hold);
        } else {
            hold = (ViewHold) convertView.getTag();
        }

        hold.yearView.setText(data.get(position).getVtime().toString().substring(0, 4) + "年");
        hold.dateView.setText(data.get(position).getVtime().toString().substring(5, 16).replace('-', '月').replace(' ', '日'));
        hold.playView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = data.get(position).getVsrc().replaceAll("voice/", "");
                if (!fileIsExists(fname))
                    initGetVoice(data.get(position).getVsrc(),
                            fname);
                else playVoice(fname);
            }
        });
        if (data.get(position).getVsign() == 0) { //进行中的任务

            hold.delView.setEnabled(true);
            hold.delView.setVisibility(View.VISIBLE);
            hold.finishView.setEnabled(true);
            hold.finishView.setVisibility(View.VISIBLE);
            hold.traceView.setEnabled(false);
            hold.traceView.setVisibility(View.INVISIBLE);

            hold.delView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDelMission(String.valueOf(data.get(position).getVid()), position);
                    //TODO 删除后处理

                }
            });

            hold.finishView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initFinMission(String.valueOf(data.get(position).getVid()), position);
                }
            });
            hold.stateView.setText("进行中");
            hold.stateView.setTextColor(convertView.getResources().getColor(R.color.red));
        } else if (data.get(position).getVsign() == 1) { //已完成的任务
            hold.delView.setEnabled(false);
            hold.delView.setVisibility(View.INVISIBLE);
            hold.finishView.setEnabled(false);
            hold.finishView.setVisibility(View.INVISIBLE);
            hold.traceView.setEnabled(true);
            hold.traceView.setVisibility(View.VISIBLE);

            hold.traceView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String startTime = null, endTime = null;
                    try {
//                    startTime = String.valueOf(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get(position).getVtime().toString()).getTime());
//                    endTime = String.valueOf(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(data.get(position).getEtime().toString()).getTime());
                        startTime = String.valueOf(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2016/08/22 08:40:00").getTime());
                        endTime = String.valueOf(new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("2016/08/22 08:55:00").getTime());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Intent it = new Intent(context, DrawMapActivity.class);
                    it.putExtra("startTime", startTime);
                    it.putExtra("endTime", endTime);
                    context.startActivity(it);
                }
            });
            hold.stateView.setText("已完成");
            hold.stateView.setTextColor(convertView.getResources().getColor(R.color.green));

        }

        return convertView;
    }

    private interface getVoiceIF {
        @GET
        Call<ResponseBody> getVoice(@Url String fileUrl);
    }

    private interface delVoiceIF {
        @GET("delMission.do")
        Call<ResponseBody> delMission(@Query("vid") String vid);
    }

    private interface finMissionIF {
        @GET("finMission.do")
        Call<ResponseBody> finMission(@Query("vid") String vid);
    }

    private void initGetVoice(String url, final String filename) {
        Log.d(TAG, url);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .build();


        getVoiceIF downloadService = retrofit.create(getVoiceIF.class);

        Call<ResponseBody> call = downloadService.getVoice(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), filename);
                    Log.d(TAG, "file download was a success? " + writtenToDisk);
                } else {
                    Log.d(TAG, "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    private void initDelMission(String vid, final int position) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        delVoiceIF delVoiceIF = retrofit.create(delVoiceIF.class);
        Call<ResponseBody> call = delVoiceIF.delMission(vid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        delSign = response.body().string();
                        Toast.makeText(context, delSign, Toast.LENGTH_SHORT).show();
                        data.remove(position);
                        notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "delete succeed");
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });
    }

    private void initFinMission(String vid, final int position) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVERURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        finMissionIF finmissionIF = retrofit.create(finMissionIF.class);
        Call<ResponseBody> call = finmissionIF.finMission(vid);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        finSign = response.body().string();
                        Toast.makeText(context, finSign, Toast.LENGTH_SHORT).show();
                        data.get(position).setVsign(1);
                        notifyDataSetChanged();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "finish succeed");
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, t.toString());
            }


        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body, String filename) {
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(context.getCacheDir() + File.separator + filename);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;

                    Log.d(TAG, "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                playVoice(filename);
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public boolean fileIsExists(String filename) {
        try {
            File f = new File(context.getCacheDir() + File.separator + filename);
            if (!f.exists()) {
                return false;
            }

        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }

    private void playVoice(String filename) {
        try {
            MediaPlayer mPlayer = PlayerSingleton.getInstance(context, Uri.parse(context.getCacheDir() + File.separator + filename));
            mPlayer.reset();
            mPlayer.setDataSource(context.getCacheDir() + File.separator + filename);
            mPlayer.prepare();
            mPlayer.start();
            Log.d(TAG, "开始播放");
        } catch (IOException e) {
            Log.d(TAG, "播放失败");
        }
    }

    class ViewHold {
        //public ImageView image;
        public TextView yearView, dateView, stateView;
        public Button playView, delView, traceView, finishView;
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
